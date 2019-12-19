package interceptor;

import bean.MqTransactionEntity;
import factory.MqTransactionHandleFactory;
import manager.RpcTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import service.MqTransactionService;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * SpringCloud 实现mq事务的业务
 * @author wangxi
 * @date 2019-12-18 16:27
 */
@Component
public class SpringCloudMqTransactionInterceptor implements MqTransactionInterceptor {


    @Autowired
    private RpcTransactionManager rpcTransactionManager;
    @Autowired
    private MqTransactionHandleFactory factory;

    /**
     * SpringCloud 为HTTP调用，故
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Override
    public Object interceptor(ProceedingJoinPoint pjp){

        //需要一个工厂
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        //request::getHeader 即request.getHeader() 即 RpcAcquire接口的实现方式
        MqTransactionEntity entity = rpcTransactionManager.getContext(request::getHeader);

        //获取当前的请求handler
        MqTransactionService mqTransactionService = factory.factory(entity);

        return mqTransactionService.invoke(entity == null?"": entity.getTransactionId(), pjp);
    }
}
