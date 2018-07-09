package org.sample.dubbo.order;

import interceptor.TccTransactionAspect;
import interceptor.TccTransactionExplorerAspect;
import org.sample.dubbo.order.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ImportResource(value = {"classpath:config.order.dubbo/*.xml","classpath:config/spring/local/*.xml", "classpath:order.tcc/tcc.xml"})
@EnableAspectJAutoProxy
public class OrderApplication extends SpringBootServletInitializer {
    //This SpringBootServletInitializer run a SpringApplication from a traditional WAR deployment

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OrderApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public TccTransactionAspect getTccTransactionAspect(){

        TccTransactionAspect tccTransactionAspect = new TccTransactionAspect();
        tccTransactionAspect.init();
        return tccTransactionAspect;
    }
    @Bean
    public TccTransactionExplorerAspect getTccTransactionExplorerAspect(){

        TccTransactionExplorerAspect tccTransactionExplorerAspect = new TccTransactionExplorerAspect();
        tccTransactionExplorerAspect.init();
        return tccTransactionExplorerAspect;
    }
}
