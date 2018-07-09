package spring;

import config.TccTransactionConfigurator;
import interceptor.TccTransactionExplorerAspect;
import interceptor.TccTransactionExplorerInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

@Aspect
public class SpringTccTransactionExplorerAspect extends TccTransactionExplorerAspect implements Ordered {

    private TccTransactionConfigurator tccTransactionConfigurator;


    public void setTccTransactionConfigurator(TccTransactionConfigurator tccTransactionConfigurator) {

        this.tccTransactionConfigurator = tccTransactionConfigurator;
    }

    public void init(){

        TccTransactionExplorerInterceptor interceptor = new TccTransactionExplorerInterceptor();
        interceptor.setTransactionManager(tccTransactionConfigurator.getTransactionManager());

    }

    //最高级(数值最小)和最低级(数值最大)
    @Override
    public int getOrder() {

        return Ordered.HIGHEST_PRECEDENCE+1;
    }
}
