package spring;

import interceptor.MqTransactionInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

/**
 * @author wangxi
 * @date 2019-12-18 15:54
 */
@Aspect
public class SpringMqTransactionAspect implements Ordered {


    @Autowired
    private MqTransactionInterceptor interceptor;

    public void setInterceptor(MqTransactionInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Pointcut("@annotation(api.MqTransaction)")
    public void mqTransactionAspect(){

    }

    @Around("mqTransactionAspect()")
    public Object interceptMqTransactionMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        return interceptor.interceptor(joinPoint);

    }
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
