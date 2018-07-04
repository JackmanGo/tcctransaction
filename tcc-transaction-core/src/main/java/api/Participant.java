package api;

import factory.FactoryBuilder;
import reflect.ParticipantDetail;
import utils.TccTransactionMethodUtils;

import java.io.Serializable;

/**
 * 事务参与者：存储事务与其参与者(confirm cancel)的关系
 */
public class Participant implements Serializable {

    private static final long serialVersionUID = -1L;

    //tcc事务id
    private TccTransactionXid xid;

    //confirm详情
    private ParticipantDetail confimParticipant;

    //cancel详情
    private ParticipantDetail cancelParticipant;

    //transactionContextEditor,获取 TccTransactionContext
    Class<? extends TccTransactionContextEditor> tccTransactionContextEditorClass;


    public Participant(){

    }

    public Participant(TccTransactionXid xid, ParticipantDetail confimParticipant, ParticipantDetail cancelParticipant,
                       Class<? extends TccTransactionContextEditor> tccTransactionContextEditorClass) {

        this.xid = xid;
        this.confimParticipant = confimParticipant;
        this.cancelParticipant = cancelParticipant;
        this.tccTransactionContextEditorClass = tccTransactionContextEditorClass;
    }


    public void commit() {

        //生成TccTransactionContext
        TccTransactionContext context = new TccTransactionContext(xid, TccTransactionStatus.CONFIRMING);
        //更新invoke方法时的实参，即赋值TccTransactionContext
        FactoryBuilder.factoryOf(tccTransactionContextEditorClass).getInstance()
                .set(context, confimParticipant.getMethod(), confimParticipant.getArgs());

        TccTransactionMethodUtils.invokeParticipant(confimParticipant);
    }

    public void rollback() {

        //生成TccTransactionContext
        TccTransactionContext context = new TccTransactionContext(xid, TccTransactionStatus.CANCELLING);
        //更新invoke方法时的实参，即赋值TccTransactionContext
        FactoryBuilder.factoryOf(tccTransactionContextEditorClass).getInstance()
                .set(context, cancelParticipant.getMethod(), cancelParticipant.getArgs());

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
