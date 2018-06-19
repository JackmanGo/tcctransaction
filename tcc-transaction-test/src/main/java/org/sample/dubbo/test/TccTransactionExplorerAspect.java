package org.sample.dubbo.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TccTransactionExplorerAspect implements Ordered {


    @Pointcut("@annotation(org.sample.dubbo.test.TccTransaction)")
    public void transactionContextCall() {

    }

    @Around("transactionContextCall()")
    //@Around("execution(* org.sample.dubbo.test.TestAop.*(..))")
    public Object interceptTransactionContextMethod(ProceedingJoinPoint pjp) throws Throwable {

        System.out.println(2);
        //Object obj = pjp.proceed(pjp.getArgs());
        Object obj = pjp.proceed();
        System.out.println(3);

        return obj;
    }

    //最高级(数值最小)和最低级(数值最大)
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+1;
    }
}
