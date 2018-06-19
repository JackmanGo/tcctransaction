package org.sample.dubbo.test;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启tcc事务的注解，仅用于方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TccTransaction {

    String confirmMethod() default "";

    String cancelMethod() default "";

    boolean asyncConfirm() default false;

    boolean asyncCancel() default false;
}
