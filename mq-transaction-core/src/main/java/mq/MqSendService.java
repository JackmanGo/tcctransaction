package mq;

import bean.MqTransactionEntity;

/**
 * @author wangxi
 * @date 2019-12-18 23:24
 */
public interface MqSendService {


    /**
     * send message.
     *
     * @param mqTransactionEntity
     * @return true
     */
    Boolean sendMessage(MqTransactionEntity mqTransactionEntity);
}
