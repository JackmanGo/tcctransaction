package service;

import exception.MqRunTimeException;
import manager.RpcTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 业务实现
 *
 * @author wangxi
 * @date 2019-12-18 17:50
 */
@Component
public class StartMqTransactionService implements MqTransactionService {

    @Autowired
    private RpcTransactionManager rpcTransactionManager;


    /**
     * mq分布式事务的业务，当前请求不存在事务
     *
     * @param transactionId
     * @param pjp
     * @return
     */
    @Override
    public Object invoke(String transactionId, ProceedingJoinPoint pjp) {

        //开始事务
        rpcTransactionManager.begin(pjp);
        //继续执行业务方法，如RPC调用等
        try {
            final Object proceed = pjp.proceed();
            //提交事务
            rpcTransactionManager.commit();
            return proceed;
        } catch (Throwable throwable) {
            //TODO 发生异常，更新事务状态
            throw new MqRunTimeException(throwable.getMessage());
        } finally {
            rpcTransactionManager.sendMqMessage();
            rpcTransactionManager.cleanThreadLocal();
        }

    }
}
