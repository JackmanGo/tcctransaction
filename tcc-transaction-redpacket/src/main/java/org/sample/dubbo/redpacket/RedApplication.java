package org.sample.dubbo.redpacket;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:redpacket.dao/*.xml", "classpath:redpacket.tcc/tcc.xml"})
@EnableDubbo(scanBasePackages = "org.sample.dubbo.redpacket.resource")
public class RedApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedApplication.class);
    }


}
