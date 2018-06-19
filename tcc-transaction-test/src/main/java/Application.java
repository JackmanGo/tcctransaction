import org.sample.dubbo.test.TccTransaction;
import org.sample.dubbo.test.TestAop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.IOException;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan("org.sample.dubbo.test")
public class Application implements CommandLineRunner{

    @Autowired
    TestAop testAop;

    public static void main(String[] args) throws IOException {

        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        testAop.methodFirst();
    }

}
