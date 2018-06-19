package factory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FactoryBuilder {

    private FactoryBuilder() {

    }

    private static List<BeanFactory> beanFactories = new ArrayList<BeanFactory>();

    private static ConcurrentHashMap<Class, SingeltonFactory> classFactoryMap = new ConcurrentHashMap<Class, SingeltonFactory>();

    public static void registerBeanFactory(BeanFactory beanFactory) {
        beanFactories.add(beanFactory);
    }

    //
    public static <T> SingeltonFactory<T> factoryOf(Class<T> clazz){

        if(!classFactoryMap.containsKey(clazz)){

            for (BeanFactory beanFactory : beanFactories) {
                if (beanFactory.isFactoryOf(clazz)) {

                    //putIfAbsent 与 put的区别是，当key已经有值，putIfAbsent不进行替换
                    classFactoryMap.putIfAbsent(clazz, new SingeltonFactory<T>(clazz, beanFactory.getBean(clazz)));
                }
            }

            //尚未注册与beanFactory，直接构建
            if (!classFactoryMap.containsKey(clazz)) {
                classFactoryMap.putIfAbsent(clazz, new SingeltonFactory<T>(clazz));
            }
        }

        return classFactoryMap.get(clazz);
    }
}
