package org.sample.dubbo.order.dubbo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author wangxi
 * @date 2019-07-27 09:31
 */
@Configuration
@PropertySource("classpath:dubbo-order.properties")
@EnableDubbo(scanBasePackages = "org.sample.dubbo.order.service")
@ComponentScan(value = {"org.sample.dubbo.order.service"})
public class DubboConsumerConfig {

}
