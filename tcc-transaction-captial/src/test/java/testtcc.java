import api.TccTransactionContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sample.dubbo.cap.CapApplication;
import org.sample.dubbo.cap.api.CapitalTradeOrderService;
import org.sample.dubbo.cap.apiImpl.CapitalTradeOrderServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CapApplication.class)
public class testtcc implements ApplicationContextAware {

    ApplicationContext applicationContext;

    //private CapitalTradeOrderService capitalTradeOrderService;
    @Autowired
    private CapitalTradeOrderServiceImpl testTcc;

    @Test
    public void test() throws NoSuchMethodException {
        //capitalTradeOrderService = applicationContext.getBean(CapitalTradeOrderService.class);
        testTcc.testTcc(null);
        //capitalTradeOrderService.testTcc(null);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
