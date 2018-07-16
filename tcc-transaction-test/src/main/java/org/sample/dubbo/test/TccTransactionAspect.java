package org.sample.dubbo.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 拦截@TccTransaction
 *
 */
@Aspect
@Component
public class TccTransactionAspect implements Ordered {

    @Pointcut("@annotation(org.sample.dubbo.test.TccTransaction)")
    public void tccTransactionAspect(){

    }

    @Around("tccTransactionAspect()")
    //@Around("execution(* org.sample.dubbo.test.TestAop.*(..))")
    public Object interceptTccTransactionMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        //TODO
        System.out.println(1);
        //Object obj =  joinPoint.proceed(joinPoint.getArgs());
        Object obj = joinPoint.proceed(new Object[]{"322"});
        System.out.println(4);

        return obj;

    }

    //最高级(数值最小)和最低级(数值最大)
    @Override
    public int getOrder() {

        return  Ordered.HIGHEST_PRECEDENCE;
    }
}
