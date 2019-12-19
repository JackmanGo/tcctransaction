package service;

import bean.MqTransactionEntity;
import exception.MqRunTimeException;
import manager.RpcTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 事务参与者
 * @author wangxi
 * @date 2019-12-18 23:09
 */
public class ActorMqTransactionService implements MqTransactionService{

    @Autowired
    private RpcTransactionManager rpcTransactionManager;


    /**
     * mq分布式事务的业务，当前请求已经包含了事务
     *
     * @param transactionId
     * @param pjp
     * @return
     */
    @Override
    public Object invoke(String transactionId, ProceedingJoinPoint pjp) {

        rpcTransactionManager.actor(transactionId, pjp);
        try {
            Object proceed = pjp.proceed();
            return proceed;
        } catch (Throwable throwable) {

            throw new MqRunTimeException(throwable.getMessage());
        }finally {
            rpcTransactionManager.cleanThreadLocal();
        }
    }


}
