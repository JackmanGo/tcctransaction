package api;

import reflect.ParticipantDetail;
import utils.TccTransactionMethodUtils;

import java.io.Serializable;

/**
 * 存储事务与其参与者(confirm cancel)的关系
 */
public class Participant implements Serializable {

    private static final long serialVersionUID = -1L;

    //tcc事务id
    private TccTransactionXid xid;

    //confirm详情
    private ParticipantDetail confimParticipant;

    //cancel详情
    private ParticipantDetail cancelParticipant;

    public Participant(){

    }

    public Participant(TccTransactionXid xid, ParticipantDetail confimParticipant, ParticipantDetail cancelParticipant) {
        this.xid = xid;
        this.confimParticipant = confimParticipant;
        this.cancelParticipant = cancelParticipant;
    }


    public void commit() {
        TccTransactionMethodUtils.invokeParticipant(confimParticipant);
    }

    public void rollback() {
        TccTransactionMethodUtils.invokeParticipant(cancelParticipant);
    }

    public TccTransactionXid getXid() {
        return xid;
    }

    public void setXid(TccTransactionXid xid) {
        this.xid = xid;
    }

    public ParticipantDetail getConfimParticipant() {
        return confimParticipant;
    }

    public void setConfimParticipant(ParticipantDetail confimParticipant) {
        this.confimParticipant = confimParticipant;
    }

    public ParticipantDetail getCancelParticipant() {
        return cancelParticipant;
    }

    public void setCancelParticipant(ParticipantDetail cancelParticipant) {
        this.cancelParticipant = cancelParticipant;
    }
}
