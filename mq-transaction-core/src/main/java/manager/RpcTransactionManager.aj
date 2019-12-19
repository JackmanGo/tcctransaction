package manager;

import bean.MqTransactionContext;
import bean.MqTransactionEntity;
import mq.MqSendService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

/**
 * Rpc请求过程中的事务管理
 * @author wangxi
 * @date 2019-12-18 17:37
 */
@Component
public class RpcTransactionManager {

    /**
     * save MythTransaction in threadLocal.
     */
    private static final ThreadLocal<MqTransactionEntity> CURRENT = new ThreadLocal<>();
    private static final ThreadLocal<MqTransactionContext> CURRENT_CONTEXT = new ThreadLocal<>();

    @Autowired
    private MqSendService mqSendService;

    /**
     * 获取当前事务的上下文
     * @return
     */
    public MqTransactionContext getNowTransaction(){

        return CURRENT_CONTEXT.get();
    }

    /**
     * 获取当前请求的事务上下文
     * @param rpcAcquire
     * @return
     */
    public MqTransactionEntity getContext(RpcAcquire rpcAcquire){

        String transactionId = rpcAcquire.acquire("MQ_TRANSACTION_CONTEXT");

        if(transactionId == null){
            return null;
        }

        //TODO DB 获取
        MqTransactionEntity mqTransactionContext = new MqTransactionEntity();
        mqTransactionContext.setTransactionId(transactionId);

        return mqTransactionContext;
    }

    /**
     * 开启事务
     * @param pjp
     */
    public void begin(ProceedingJoinPoint pjp) {

        String transactionId = UUID.randomUUID().toString();

        MqTransactionEntity mqTransactionContext = buildMqTransactionEntity(transactionId, pjp);

        //TODO 异步保存事务入库
        CURRENT.set(mqTransactionContext);
        //用于事务的传播
        CURRENT_CONTEXT.set(new MqTransactionContext(transactionId));
    }

    public void actor(String transactionId, ProceedingJoinPoint pjp){

        MqTransactionEntity mqTransactionContext = buildMqTransactionEntity(transactionId, pjp);

        //TODO 异步保存事务入库
        CURRENT.set(mqTransactionContext);
        //用于事务的传播
        CURRENT_CONTEXT.set(new MqTransactionContext(transactionId));
    }

    private MqTransactionEntity buildMqTransactionEntity(String transactionId, ProceedingJoinPoint pjp){

        MqTransactionEntity mqTransactionContext = new MqTransactionEntity();
        mqTransactionContext.setTransactionId(transactionId);
        mqTransactionContext.setClassName(pjp.getClass().getName());
        //调用方法源数据
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        mqTransactionContext.setTargetMethod(method.getName());
        mqTransactionContext.setIsFinished(0);

        return mqTransactionContext;
    }

    /**
     * 提交事务。更新事务状态，更新库
     */
    public void commit() {

        MqTransactionEntity mqTransactionContext  = CURRENT.get();

        if(mqTransactionContext!=null){

            //TODO 更新状态，入库

            CURRENT.remove();
        }
    }

    /**
     * 事务是否开启
     * @return
     */
    public Boolean isBegain() {

        return CURRENT.get()!=null;
    }

    /**
     * 事务终止
     */
    public void cleanThreadLocal() {

        CURRENT.remove();
        CURRENT_CONTEXT.remove();
    }

    public void sendMqMessage() {

        Optional.ofNullable(CURRENT.get()).ifPresent(mqSendService::sendMessage);
    }

}
