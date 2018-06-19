package interceptor;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

/**
 * 资源分配器
 *
 */
@Aspect
public class TccTransactionExplorerAspect implements Ordered {

    TccTransactionExplorerInterceptor tccTransactionExplorerInterceptor;

    @Pointcut("@annotation(api.TccTransaction)")
    public void transactionContextCall() {

    }

    @Around("transactionContextCall()")
    public Object interceptTransactionContextMethod(ProceedingJoinPoint pjp) throws Throwable {

        return tccTransactionExplorerInterceptor.interceptTransactionContextMethod(pjp);
    }

    //最高级(数值最小)和最低级(数值最大)
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+1;
    }
}
