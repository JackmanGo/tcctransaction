package interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 拦截@TccTransaction，开启事务
 *
 */
@Aspect
@Component
public class TccTransactionAspect{

    /**
     * 不同的实现rpc方式，该接口的实现方案不同
     */
    private TccTransactionAspectInterceptor interceptor;

    public void setInterceptor(TccTransactionAspectInterceptor interceptor){
        this.interceptor = interceptor;
    }

    @Pointcut("@annotation(api.TccTransaction)")
    public void tccTransactionAspect(){

    }

    @Around("tccTransactionAspect()")
    public Object interceptTccTransactionMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        return interceptor.interceptTccTransactionMethod(joinPoint);

    }
}
