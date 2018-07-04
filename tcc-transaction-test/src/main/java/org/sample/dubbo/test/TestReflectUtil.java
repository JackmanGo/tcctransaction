package org.sample.dubbo.test;

import java.lang.reflect.Method;

public class TestReflectUtil{

    public static Class getDeclaringType(Class aClass, String methodName, Class<?>[] parameterTypes) {

        Method method = null;


        Class findClass = aClass;

        do {
            Class[] clazzes = findClass.getInterfaces();

            for (Class clazz : clazzes) {

                try {
                    method = clazz.getDeclaredMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                    method = null;
                }

                if (method != null) {
                    return clazz;
                }
            }

            findClass = findClass.getSuperclass();

        } while (!findClass.equals(Object.class));

        return aClass;
    }
}
