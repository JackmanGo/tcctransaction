

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sample.dubbo.order.OrderApplication;
import org.sample.dubbo.order.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import repository.TransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class TestTransaction {

    @Autowired
    private ShopRepository studentService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void test() throws Exception {
        Assert.assertNotNull(transactionRepository);

    }
}
