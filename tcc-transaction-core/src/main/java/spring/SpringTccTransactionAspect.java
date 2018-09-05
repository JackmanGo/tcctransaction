package spring;

import config.TccTransactionConfigurator;
import interceptor.TccTransactionAspect;
import interceptor.TccTransactionAspectInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

import java.util.Set;

@Aspect
public class SpringTccTransactionAspect extends TccTransactionAspect implements Ordered {

    private TccTransactionConfigurator tccTransactionConfigurator;

    public void setTccTransactionConfigurator(TccTransactionConfigurator tccTransactionConfigurator) {

        this.tccTransactionConfigurator = tccTransactionConfigurator;
    }

    public void init(){

        TccTransactionAspectInterceptor interceptor = new TccTransactionAspectInterceptor();
        interceptor.setTransactionManager(tccTransactionConfigurator.getTransactionManager());
        interceptor.setDelayCancelExceptions(tccTransactionConfigurator.getDelayCancelExceptions());

        this.setInterceptor(interceptor);
    }

    //最高级(数值最小)和最低级(数值最大)
    @Override
    public int getOrder() {

        return  Ordered.HIGHEST_PRECEDENCE;
    }
}
