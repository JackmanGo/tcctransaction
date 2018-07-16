package org.sample.dubbo.redpacket;
import config.TccTransactionConfigurator;
import interceptor.TccTransactionAspect;
import interceptor.TccTransactionExplorerAspect;
import org.sample.dubbo.redpacket.dao.RedTradeOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.TransactionRepository;
import spring.SpringBeanFactory;
import spring.SpringTccTransactionAspect;
import spring.SpringTccTransactionExplorerAspect;
import spring.SpringTransactionConfigurator;

import java.io.IOException;

@SpringBootApplication
@ImportResource(locations = {"classpath:redpacket.dao/*.xml","classpath:redpacket.dubbo/*.xml", "classpath:redpacket.tcc/tcc.xml"})
@RestController
public class RedApplication {

    @Autowired
    TransactionRepository transactionRepository;

    public static void main(String[] args) {
        SpringApplication.run(RedApplication.class);
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
