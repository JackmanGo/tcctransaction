package utils;

import api.Propagation;
import api.TccTransaction;
import api.TccTransactionContext;
import api.TccTransactionType;
import exception.TccSystemException;
import factory.FactoryBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TccTransactionMethodUtils {

    /**
     * 获取注解了TccTransaction的方法
     * @param pjp
     * @return
     */
    public static Method getTransactionMethod(ProceedingJoinPoint pjp) {

        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();

        //上述方案有可能获取到接口方法
        if (method.getAnnotation(TccTransaction.class) == null) {
            try {
                method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        return method;
    }

    /**
     * 判断当前应该开始根事务或是分支事务
     * @param context
     * @param propagation
     * @param isExistingTransaction
     * @return
     */
    public static TccTransactionType calculateTransactionType(TccTransactionContext context, Propagation propagation, Boolean isExistingTransaction){

        //如果当前事务传播机制是REQUIRED，并且不存在事务，则开启一个根事务
        if(propagation.equals(Propagation.REQUIRED) && !isExistingTransaction && context == null){
            return TccTransactionType.ROOT;
        }
        //如果事务传播机制为REQUIRED，并且已经存在事务，则开启分支事务
        else if(propagation.equals(Propagation.REQUIRED)){
            return TccTransactionType.BRANCH;
        }
        //如果事务的传播机制为REQUIRES_NEW，无论当前有没有事务都会开启一个新事务
        else if(propagation.equals(Propagation.REQUIRES_NEW)){
            return TccTransactionType.ROOT;
        }
        //如果事务的传播机制为SUPPORTS，并且当前没有事务，则按普通方法执行
        else if(propagation.equals(Propagation.SUPPORTS) && !isExistingTransaction && context == null){
            return TccTransactionType.NORMAL;
        }
        //如果事务的传播机制为SUPPORTS，并且当前存在事务，则开启分支事务
        else if(propagation.equals(Propagation.SUPPORTS)){
            return TccTransactionType.BRANCH;
        }
        //如果事务的传播机制为MANDATORY，并且当前存在事务，则开启分支事务
        else if(propagation.equals(Propagation.MANDATORY) && isExistingTransaction && context != null){
            return TccTransactionType.NORMAL;
        }

        //如果事务的传播机制为MANDATORY，并且没有事务，则抛出异常
        return null;
    }

    /**
     * 反射调用confirm或cancel
     * @param
     * @return
     */
    public static Object invokeParticipant(Object target, Method method, Object[] args){

        Object returnValue = null;
        try {

            returnValue = method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TccSystemException(e);
        }

        return returnValue;

    }
}
