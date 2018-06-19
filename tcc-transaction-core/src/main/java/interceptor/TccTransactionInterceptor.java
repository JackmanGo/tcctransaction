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

public class TccTransactionInterceptor {

    static final Logger logger = Logger.getLogger(TccTransactionInterceptor.class.getSimpleName());

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

        //判断当前是否存在事务
        Boolean isExistTransaction = transactionManager.isExistTransaction();

        //判断该次发起事务的类型
        TccTransactionType type = TccTransactionMethodUtils.calculateTransactionType(context, propagation, isExistTransaction);

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

                //TODO rollback 回滚事务
                transactionManager.rollback(asyncCancel);
                throw throwable;
            }

            //TODO commit 提交事务
            transactionManager.commit(asyncConfirm);
        }finally {
            //无论上述执行rollback 还是 commit 均保证 cleanAfterCompletion方法一定被执行
            transactionManager.cleanAfterCompletion(transaction);
        }

        return returnValue;

    }

    /**
     * 开启分支事务
     * @param pjp
     * @param asyncConfirm
     * @param asyncCancel
     * @return
     */
    private Object branchTransactionProceed(ProceedingJoinPoint pjp, TccTransactionContext context, Boolean asyncConfirm, Boolean asyncCancel) throws Throwable {
        Transaction transaction = null;

        switch (context.getStatus()) {

            case TRYING:
                transaction = transactionManager.branchNewBegin(context);
                return pjp.proceed();
            case CONFIRMING:
                break;
            case CANCELLING:
                break;
        }


        return null;
    }
}
