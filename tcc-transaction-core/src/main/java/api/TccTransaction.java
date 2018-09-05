package api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * 开启tcc事务的注解，仅用于方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TccTransaction {

    //事务传播机制
    Propagation propagation() default Propagation.REQUIRED;

    Class<? extends TccTransactionContextEditor> transactionContextEditor() default DefaultTransactionContextEditor.class;

    //confirm方法
    String confirmMethod() default "";

    //cancel方法
    String cancelMethod() default "";

    //是否异步处理
    boolean asyncConfirm() default false;

    boolean asyncCancel() default false;

    /**
     * 默认的上下文传递方法，为参数传递
     */
    class DefaultTransactionContextEditor implements TccTransactionContextEditor{

        @Override
        public TccTransactionContext get(Method method, Object[] args) {


            int position = getTccTransactionContextParamPosition(method);

            if(position < 0 || position >= args.length){
                throw new RuntimeException("Param TccTransactionContext Not Found");
            }

            return (TccTransactionContext) args[position];
        }

        @Override
        public void set(TccTransactionContext transactionContext, Method method, Object[] args) {

            int position = getTccTransactionContextParamPosition(method);

            if (position >= 0) {
                args[position] = transactionContext;
            }
        }

        //获取方法中上下文参数的位置
        private int getTccTransactionContextParamPosition(Method method){

            if(method == null){
                return -1;
            }

            Class[] parameterTypes = method.getParameterTypes();

            int position = 0;
            for (Class parameterType: parameterTypes){

                //a.class.isAssignableFrom(b.class)  当a与b是同一个类或b是a当子类，返回true
                if(parameterType != null && TccTransactionContext.class.isAssignableFrom(parameterType)){
                   return position;
                }
                position++;
            }

            return -1;
        }
    }
}
