import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sample.dubbo.order.OrderApplication;
import org.sample.dubbo.order.dao.LockTestDao;
import org.sample.dubbo.order.service.PlaceOrderServiceImpl;
import org.sample.dubbo.order.service.test.TransactionService;
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
    @Autowired
    private LockTestDao lockTestDao;
    @Autowired
    private TransactionService transactionService;

    @Test
    public void test() throws Exception {
        Assert.assertNotNull(transactionRepository);
    }

    @Test
    public void testLock() throws InterruptedException {

        for(int i=0;i<15;i++) {

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<100;i++) {
                        lockTestDao.updateOrderMoney(143);
                    }
                }
            });

            t.start();
        }

        Thread.currentThread().join();
        System.out.println("执行完成");
    }

    @Test
    public void testTransaction() throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                transactionService.selectMoney();
            }
        });

        t.start();

        transactionService.updateMoney();
        t.join();
    }
}
