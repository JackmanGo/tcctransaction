package org.sample.dubbo.order;

import config.TccTransactionConfigurator;
import interceptor.TccTransactionAspect;
import interceptor.TccTransactionExplorerAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import repository.TransactionRepository;
import spring.SpringBeanFactory;
import spring.SpringTccTransactionAspect;
import spring.SpringTccTransactionExplorerAspect;
import spring.SpringTransactionConfigurator;

import java.util.logging.Logger;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ImportResource(value = {"classpath:config/spring/local/*.xml", "classpath:order.tcc/tcc.xml"})
@EnableAspectJAutoProxy
public class OrderApplication{
    //This SpringBootServletInitializer run a SpringApplication from a traditional WAR deployment

    static final Logger logger = Logger.getLogger(OrderApplication.class.getSimpleName());

    @Autowired
    private TransactionRepository transactionRepository;


    public static void main(String[] args) {

        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public SpringBeanFactory getSpringBeanFactory(){
        return new SpringBeanFactory();
    }

    @Bean
    public TccTransactionConfigurator getTransactionConfigurator(){

        SpringTransactionConfigurator springTransactionConfigurator = new SpringTransactionConfigurator();
        springTransactionConfigurator.setTransactionRepository(transactionRepository);
        springTransactionConfigurator.init();

        logger.info("jdbc===>{}" + springTransactionConfigurator.getTransactionRepository().toString());
        return springTransactionConfigurator;
    }

    @Bean
    public TccTransactionAspect getTccTransactionAspect(TccTransactionConfigurator configurator){

        SpringTccTransactionAspect tccTransactionAspect = new SpringTccTransactionAspect();
        tccTransactionAspect.setTccTransactionConfigurator(configurator);

        tccTransactionAspect.init();
        return tccTransactionAspect;
    }

    @Bean
    public TccTransactionExplorerAspect getTccTransactionExplorerAspect(TccTransactionConfigurator configurator){

        SpringTccTransactionExplorerAspect tccTransactionExplorerAspect = new SpringTccTransactionExplorerAspect();
        tccTransactionExplorerAspect.setTccTransactionConfigurator(configurator);
        tccTransactionExplorerAspect.init();

        return tccTransactionExplorerAspect;
    }
}
