package interceptor;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 资源分配器
 *
 */
@Aspect
public abstract class TccTransactionExplorerAspect{

    private TccTransactionExplorerInterceptor tccTransactionExplorerInterceptor;

    @Pointcut("@annotation(api.TccTransaction)")
    public void transactionContextCall() {

    }

    @Around("transactionContextCall()")
    public Object interceptTransactionContextMethod(ProceedingJoinPoint pjp) throws Throwable {

        return tccTransactionExplorerInterceptor.interceptTransactionContextMethod(pjp);
    }

}
