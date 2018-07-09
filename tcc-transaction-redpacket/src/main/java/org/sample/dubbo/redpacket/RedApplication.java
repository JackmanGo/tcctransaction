package org.sample.dubbo.redpacket;
import org.sample.dubbo.redpacket.dao.RedTradeOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@ImportResource(locations = {"classpath:redpacket.dao/*.xml","classpath:redpacket.dubbo/*.xml", "classpath:redpacket.tcc/tcc.xml"})
@RestController
public class RedApplication {

    @Autowired
    private RedTradeOrderDao dao;

    public static void main(String[] args) {
        SpringApplication.run(RedApplication.class);
    }

    @GetMapping
    public String get() throws IOException {
         return dao.toString();
    }

}
