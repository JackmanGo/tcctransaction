package interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

/**
 * 拦截@TccTransaction，开启事务
 *
 */
@Aspect
public class TccTransactionAspect implements Ordered {

    private TccTransactionAspectInterceptor interceptor;

    public void init(){
        interceptor = new TccTransactionAspectInterceptor();
    }

    @Pointcut("@annotation(api.TccTransaction)")
    public void tccTransactionAspect(){

    }

    @Around("tccTransactionAspect()")
    public Object interceptTccTransactionMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        return interceptor.interceptTccTransactionMethod(joinPoint);

    }

    //最高级(数值最小)和最低级(数值最大)
    @Override
    public int getOrder() {

        return  Ordered.HIGHEST_PRECEDENCE;
    }
}
