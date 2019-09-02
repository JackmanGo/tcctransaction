import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sample.dubbo.order.OrderApplication;
import org.sample.dubbo.order.service.PlaceOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import repository.TransactionRepository;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class TestTransaction {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PlaceOrderServiceImpl placeOrderService;

    @Test
    public void test() throws Exception {
        Assert.assertNotNull(transactionRepository);

    }
}
