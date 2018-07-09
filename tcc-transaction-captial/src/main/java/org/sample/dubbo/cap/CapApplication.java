package org.sample.dubbo.cap;
import com.alibaba.dubbo.common.json.JSON;
import org.junit.Assert;
import org.mybatis.spring.annotation.MapperScan;
import org.sample.dubbo.cap.dao.CapTradeOrderDao;
import org.sample.dubbo.cap.repository.CapTradeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Method;

@SpringBootApplication
@ImportResource(locations = {"classpath:capital.dao/*.xml","classpath:capital.dubbo/*.xml","classpath:capital.tcc/tcc.xml"})
@RestController
public class CapApplication {

    @Autowired
    CapTradeOrderDao dao;

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
}
