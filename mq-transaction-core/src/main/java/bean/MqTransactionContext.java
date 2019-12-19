package bean;

import java.io.Serializable;

/**
 *
 * 事务上下文，在RPC中传播
 * @author wangxi
 * @date 2019-12-18 18:15
 */
public class MqTransactionContext implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 事务id
     */
    private String transactionId;

    public MqTransactionContext(){}

    public MqTransactionContext(String transactionId){

        this.transactionId = transactionId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}

