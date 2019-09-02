package spring;

import factory.BeanFactory;
import factory.FactoryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class SpringBeanFactory implements BeanFactory, ApplicationContextAware {

    private static final Logger LOGGER = Logger.getLogger(SpringBeanFactory.class);

    private ApplicationContext applicationContext;

    @Override
    public <T> T getBean(Class<T> var1) {
        return this.applicationContext.getBean(var1);
    }

    @Override
    public <T> boolean isFactoryOf(Class<T> clazz) {
        Map map = this.applicationContext.getBeansOfType(clazz);
        return map.size() > 0;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LOGGER.info("配置SpringBeanFactory");
        this.applicationContext = applicationContext;
        FactoryBuilder.registerBeanFactory(this);
    }
}
