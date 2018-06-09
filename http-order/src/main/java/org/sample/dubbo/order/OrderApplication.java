package org.sample.dubbo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(value = {"classpath:config.order.dubbo/*.xml","classpath:config/spring/local/*.xml"})
public class OrderApplication extends SpringBootServletInitializer {
    //This SpringBootServletInitializer run a SpringApplication from a traditional WAR deployment

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OrderApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(OrderApplication.class, args);
    }

}
