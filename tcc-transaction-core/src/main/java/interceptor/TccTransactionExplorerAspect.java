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

    private TccTransactionExplorerInterceptor interceptor;

    public void  setInterceptor(TccTransactionExplorerInterceptor interceptor){
        this.interceptor = interceptor;
    }

    @Pointcut("@annotation(api.TccTransaction)")
    public void transactionContextCall() {

    }

    @Around("transactionContextCall()")
    public Object interceptTransactionContextMethod(ProceedingJoinPoint pjp) throws Throwable {

        return interceptor.interceptTransactionContextMethod(pjp);
    }

}
