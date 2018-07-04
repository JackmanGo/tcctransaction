package reflect;

import java.lang.reflect.Method;

/**
 * 封装反射调用的方法的类
 *
 */
public class ParticipantDetail {

    private Class targetClass;
    private Method method;
    private Object[] args;

    public ParticipantDetail(){

    }

    public ParticipantDetail(Class targetClass, Method method, Object[] args){

        this.targetClass = targetClass;
        this.method = method;
        this.args = args;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
