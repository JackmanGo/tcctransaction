package interceptor;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * mq事务拦截器的业务实现接口
 * 不同的rpc方案实现方法不同
 * @author wangxi
 * @date 2019-12-18 16:08
 */
public interface MqTransactionInterceptor {


    /**
     * Interceptor object.
     * @param pjp
     * @return
     * @throws Throwable
     */
    Object interceptor(ProceedingJoinPoint pjp) throws Throwable;
}
