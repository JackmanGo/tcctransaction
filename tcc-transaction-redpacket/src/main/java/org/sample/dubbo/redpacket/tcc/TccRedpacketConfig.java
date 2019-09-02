package org.sample.dubbo.redpacket.tcc;

import config.TccTransactionConfigurator;
import interceptor.TccTransactionAspect;
import interceptor.TccTransactionExplorerAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.TransactionRepository;
import spring.SpringBeanFactory;
import spring.SpringTccTransactionAspect;
import spring.SpringTccTransactionExplorerAspect;
import spring.SpringTransactionConfigurator;

/**
 * @author wangxi
 * @date 2019-07-27 09:22
 */
@Configuration
public class TccRedpacketConfig {

    @Autowired
    private TransactionRepository transactionRepository;

    @Bean
    public SpringBeanFactory getSpringBeanFactory(){
        return new SpringBeanFactory();
    }

    @Bean
    public TccTransactionConfigurator getTransactionConfigurator(){

        SpringTransactionConfigurator springTransactionConfigurator = new SpringTransactionConfigurator();
        springTransactionConfigurator.setTransactionRepository(transactionRepository);
        springTransactionConfigurator.init();

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
