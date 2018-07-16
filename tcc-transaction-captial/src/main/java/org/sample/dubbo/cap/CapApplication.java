package org.sample.dubbo.cap;
import com.alibaba.dubbo.common.json.JSON;
import config.TccTransactionConfigurator;
import interceptor.TccTransactionAspect;
import interceptor.TccTransactionExplorerAspect;
import org.junit.Assert;
import org.mybatis.spring.annotation.MapperScan;
import org.sample.dubbo.cap.dao.CapTradeOrderDao;
import org.sample.dubbo.cap.repository.CapTradeOrderRepository;
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
import java.lang.reflect.Method;
import java.util.logging.Logger;

@SpringBootApplication
@ImportResource(locations = {"classpath:capital.dao/*.xml","classpath:capital.dubbo/*.xml","classpath:capital.tcc/tcc.xml"})
@RestController
public class CapApplication {

    @Autowired
    CapTradeOrderDao dao;
    @Autowired
    private TransactionRepository transactionRepository;
    static final Logger logger = Logger.getLogger(CapApplication.class.getSimpleName());


    public static void main(String[] args) {
        SpringApplication.run(CapApplication.class);
    }

    @GetMapping("/")
    public String get() throws IOException {
        Method[] methods = dao.getClass().getMethods();
        String s = "";
        for(Method method: methods){
            s+=method.getName()+";";
        }
        System.out.print(s);

        return JSON.json(dao.findByMerchantOrderNo("cb49afa1-e171-4a6d-b360-b4cae4157bf1"));
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
