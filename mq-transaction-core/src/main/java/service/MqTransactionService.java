package service;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 事务实现的接口
 * @author wangxi
 * @date 2019-12-19 09:55
 */
public interface MqTransactionService {

    /**
     * 事务的实现。不同的周期实现不同
     * @param transactionId
     * @param pjp
     * @return
     */
    Object invoke(String transactionId, ProceedingJoinPoint pjp);
}
