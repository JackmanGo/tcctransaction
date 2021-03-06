package interceptor;

import api.*;
import bean.Transaction;
import factory.FactoryBuilder;
import manager.TccTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import reflect.ParticipantDetail;
import utils.ReflectionUtils;
import utils.TccTransactionMethodUtils;

import java.lang.reflect.Method;

public class TccTransactionExplorerInterceptor {

    private TccTransactionManager transactionManager;


    public void setTransactionManager(TccTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object interceptTransactionContextMethod(ProceedingJoinPoint pjp) throws Throwable {

        Transaction transaction = transactionManager.getCurrentTransaction();

        if (transaction != null) {

            switch (transaction.getStatus()) {
                case TRYING:
                    enlistParticipant(pjp);
                    break;
                case CONFIRMING:
                    break;
                case CANCELLING:
                    break;
            }
        }

        return pjp.proceed(pjp.getArgs());
    }

    /**
     * 分布式事务的参与者存储，即反射confirm方法和cancel方法
     * @param pjp
     */
    public void enlistParticipant(ProceedingJoinPoint pjp) {

        Method method = TccTransactionMethodUtils.getTransactionMethod(pjp);
        TccTransaction transactionComment = method.getAnnotation(TccTransaction.class);

        String confirmMethodName = transactionComment.confirmMethod();
        String cancelMethodName = transactionComment.cancelMethod();

        Transaction transaction = transactionManager.getCurrentTransaction();
        //xid
        TccTransactionXid xid = new TccTransactionXid(transaction.getXid().getGlobalTransactionId());

        TccTransactionContextEditor editor = FactoryBuilder.factoryOf(transactionComment.transactionContextEditor()).getInstance();

        //从实参中获取Context
        if(editor.get(method, pjp.getArgs()) == null){

            //Context注入到实参
            TccTransactionContext context = new TccTransactionContext(xid, TccTransactionStatus.TRYING);
            editor.set(context, method, pjp.getArgs());
        }

        //反射confirm 和 cancel的信息
        //此次不能直接pjp.getTarget().getClass(); 这个类有可能是实现类，在Spring容器中我们需要获取到继承方法的父类
        Class targetClassO = pjp.getTarget().getClass();
        System.out.println(targetClassO);
        Class targetClass = ReflectionUtils.getDeclaringType(pjp.getTarget().getClass(), method.getName(), method.getParameterTypes());
        System.out.println(targetClass);
        ParticipantDetail confirmParticipant = new ParticipantDetail(targetClass, confirmMethodName, method.getParameterTypes(), pjp.getArgs());
        ParticipantDetail cancelParticipant = new ParticipantDetail(targetClass, cancelMethodName, method.getParameterTypes(), pjp.getArgs());

        Participant participant = new Participant(transaction.getXid(), confirmParticipant,
                cancelParticipant, transactionComment.transactionContextEditor());

        //添加事务参与者
        transactionManager.enlistParticipant(participant);
    }
}
