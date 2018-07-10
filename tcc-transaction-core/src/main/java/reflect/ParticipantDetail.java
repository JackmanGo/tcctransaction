package reflect;

import java.io.Serializable;

/**
 * 封装反射调用的方法的类
 *
 */
public class ParticipantDetail implements Serializable {

    private static final long serialVersionUID = -1L;

    private Class targetClass;

    //这个类需要序列化，不能使用Method类，该类无法序列号
    //private Method method;
    private String methodName;

    private Class[] parameterTypes;

    private Object[] args;

    public ParticipantDetail(){

    }

    public ParticipantDetail(Class targetClass, String methodName, Class[] parameterTypes, Object[] args){

        this.targetClass = targetClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
