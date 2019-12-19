package factory;

import bean.MqTransactionContext;
import bean.MqTransactionEntity;
import manager.RpcTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import service.ActorMqTransactionService;
import service.LocalMqTransactionService;
import service.MqTransactionService;
import service.StartMqTransactionService;

import java.util.Objects;

/**
 * @author wangxi
 * @date 2019-12-19 10:30
 */
@Component
public class MqTransactionHandleFactory {

    @Autowired
    private RpcTransactionManager rpcTransactionManager;

    public MqTransactionService factory(MqTransactionEntity entity){

        //上下文不为空，并且当前事务存在threadLocal里面
        if(Objects.isNull(entity) && rpcTransactionManager.isBegain()) {
            return new LocalMqTransactionService();
        }

        //当前事务不存在threadLocal里面，则开启一个事务
        if (!rpcTransactionManager.isBegain()) {
            return new StartMqTransactionService();
        }

        //事务参与者
        return new ActorMqTransactionService();
    }
}
