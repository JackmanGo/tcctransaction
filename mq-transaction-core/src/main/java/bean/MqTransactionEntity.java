package bean;

import java.io.Serializable;

/**
 * 事务实体
 * @author wangxi
 * @date 2019-12-18 16:43
 */
public class MqTransactionEntity implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 事务id
     */
    private String transactionId;

    /**
     * 开启分布式事务的类名
     */
    private String className;

    /**
     * 开启分布式事务的方法名
     */
    private String targetMethod;

    /**
     * 当前重试次数
     */
    private Integer retriedCount;

    /**
     * 是否完成，0未成功，1成功
     */
    private Integer isFinished;


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Integer getRetriedCount() {
        return retriedCount;
    }

    public void setRetriedCount(Integer retriedCount) {
        this.retriedCount = retriedCount;
    }

    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }
}
