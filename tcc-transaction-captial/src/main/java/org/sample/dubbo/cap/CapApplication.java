package org.sample.dubbo.cap;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@SpringBootApplication(scanBasePackages = "org.sample.dubbo.cap")
@ImportResource(locations = {"classpath:capital.dao/*.xml","classpath:capital.tcc/tcc.xml"})
@EnableDubbo(scanBasePackages = "org.sample.dubbo.cap.resource")
public class CapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapApplication.class);
    }
}

