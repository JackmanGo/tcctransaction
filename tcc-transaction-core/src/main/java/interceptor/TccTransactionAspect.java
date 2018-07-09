package interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 拦截@TccTransaction，开启事务
 *
 */
@Aspect
public abstract class TccTransactionAspect{

    private TccTransactionAspectInterceptor interceptor;

    @Pointcut("@annotation(api.TccTransaction)")
    public void tccTransactionAspect(){

    }

    @Around("tccTransactionAspect()")
    public Object interceptTccTransactionMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        return interceptor.interceptTccTransactionMethod(joinPoint);

    }
}
