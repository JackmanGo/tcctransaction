package service;

import exception.MqRunTimeException;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 不执行RPC分布式事务
 * @author wangxi
 * @date 2019-12-19 10:36
 */
public class LocalMqTransactionService implements MqTransactionService {

    @Override
    public Object invoke(String transactionId, ProceedingJoinPoint pjp){
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
           throw new MqRunTimeException(throwable.getMessage());
        }
    }
}
