package interceptor;

import api.*;
import bean.Transaction;
import exception.TccTransactionNOTFoundException;
import factory.FactoryBuilder;
import manager.TccTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import utils.TccTransactionMethodUtils;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class TccTransactionAspectInterceptor {

    static final Logger logger = Logger.getLogger(TccTransactionAspectInterceptor.class.getSimpleName());

    private TccTransactionManager transactionManager;

    public void setTransactionManager(TccTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object interceptTccTransactionMethod(ProceedingJoinPoint pjp) throws Throwable {

        Method method = TccTransactionMethodUtils.getTransactionMethod(pjp);

        TccTransaction transaction = method.getAnnotation(TccTransaction.class);
        Propagation propagation = transaction.propagation();

        //获取事务上下文
        TccTransactionContextEditor tccTransactionContextEditor = FactoryBuilder.factoryOf(transaction.transactionContextEditor()).getInstance();
        TccTransactionContext context = tccTransactionContextEditor.get(method,pjp.getArgs());

        logger.info("tranasctioncontext:"+context);

        //判断当前是否存在事务
        Boolean isExistTransaction = transactionManager.isExistTransaction();

        //判断该次发起事务的类型
        TccTransactionType type = TccTransactionMethodUtils.calculateTransactionType(context, propagation, isExistTransaction);

        logger.info("type:"+type);

        if(type == null){
            throw new TccTransactionNOTFoundException(method.getName()+"must run TccTracsactionContext");
        }

        switch (type){

            //发起根事务
            case ROOT:
                return this.rootTransactionProceed(pjp, transaction.asyncConfirm(), transaction.asyncCancel());
            //发起分支事务
            case BRANCH:
                return this.branchTransactionProceed(pjp, context, transaction.asyncConfirm(), transaction.asyncCancel());
            case NORMAL:
                //不需要事务
                return pjp.proceed();
        }

        return null;
    }

    /**
     * 开启根事务
     * @param pjp
     * @param asyncConfirm
     * @param asyncCancel
     */
    private Object rootTransactionProceed(ProceedingJoinPoint pjp, Boolean asyncConfirm, Boolean asyncCancel) throws Throwable {

        //开始根事务
        Transaction transaction = transactionManager.begin();

        Object returnValue = null;

        try {

            //执行业务方法
            try {
                returnValue = pjp.proceed();
            } catch (Throwable throwable) {

                //TODO 部分异常不适合立即回滚。例如：是否为延迟取消回滚
                //执行业务方法发送异常，此时TccTransaction状态处于trying状态
                //此时业务如果没有开启原子性操作，则需要执行rollback
                transactionManager.rollback(asyncCancel);
                throw throwable;
            }

            //提交事务
            transactionManager.commit(asyncConfirm);
        }finally {

            //无论业务代码执行成功与否，执行了rollback或 commit后，该事务已完结
            //cleanAfterCompletion方法一定被执行，即从当前线程队列中删除该数据
            transactionManager.cleanAfterCompletion(transaction);
        }

        return returnValue;

    }

    /**
     * 当前已存在事务，开启分支事务
     * @param pjp
     * @param asyncConfirm
     * @param asyncCancel
     * @return
     */
    private Object branchTransactionProceed(ProceedingJoinPoint pjp, TccTransactionContext context, Boolean asyncConfirm, Boolean asyncCancel) throws Throwable {

        Transaction transaction = null;

        switch (context.getStatus()) {

            case TRYING:
                //传播发起分支事务。
                //发起分支事务完成后，调用 ProceedingJoinPoint#proceed() 方法，执行方法原逻辑( 即 Try 逻辑 )。
                transaction = transactionManager.branchNewBegin(context);
                return pjp.proceed();
            case CONFIRMING:
                //TODO
                break;
            case CANCELLING:
                //TODO
                break;
        }

        //TODO
        return null;
    }
}
