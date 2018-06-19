package bean;

import api.*;
import reflect.ParticipantDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction implements Serializable {

    private static final long serialVersionUID = -1L;

    private TccTransactionXid xid;

    private TccTransactionStatus status;

    private TccTransactionType transactionType;

    private volatile int retriedCount = 0;

    private Date createTime = new Date();

    private Date lastUpdateTime = new Date();

    private ParticipantDetail confirmParticipantDetail;

    private List<Participant> participants = new ArrayList<Participant>();

    private long version = 1;

    public Transaction(){

    }

    public Transaction(TccTransactionType transactionType) {
        this.xid = new TccTransactionXid();
        this.status = TccTransactionStatus.TRYING;
        this.transactionType = transactionType;
    }

    public Transaction(TccTransactionContext context) {
        this.xid = context.;
        this.status = TccTransactionStatus.TRYING;
        this.transactionType = TccTransactionType.BRANCH;
    }

    //添加参与者
    public void addParticipant(Participant participant){
        participants.add(participant);
    }

    public TccTransactionXid getXid() {
        return xid;
    }

    public void setXid(TccTransactionXid xid) {
        this.xid = xid;
    }

    public TccTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TccTransactionStatus status) {
        this.status = status;
    }

    public int getRetriedCount() {
        return retriedCount;
    }

    public void setRetriedCount(int retriedCount) {
        this.retriedCount = retriedCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    //提交
    public void commit() {

    }

    //回滚
    public void rollback() {

    }
}
