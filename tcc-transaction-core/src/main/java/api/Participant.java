package api;

import exception.TccSystemException;
import factory.FactoryBuilder;
import reflect.ParticipantDetail;
import utils.TccTransactionMethodUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

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

        Method method = null;
        try {
            method = confimParticipant.getTargetClass().getMethod(confimParticipant.getMethodName(), confimParticipant.getParameterTypes());
        } catch (NoSuchMethodException e) {

            throw new TccSystemException(e.getMessage());
        }

        //更新invoke方法时的实参，即赋值TccTransactionContext
        FactoryBuilder.factoryOf(tccTransactionContextEditorClass).getInstance()
                .set(context, method, confimParticipant.getArgs());

        TccTransactionMethodUtils.invokeParticipant(FactoryBuilder.factoryOf(confimParticipant.getTargetClass()).getInstance(), method, confimParticipant.getArgs());
    }

    public void rollback() {

        //生成TccTransactionContext
        TccTransactionContext context = new TccTransactionContext(xid, TccTransactionStatus.CANCELLING);

        Method method = null;
        try {
            method = cancelParticipant.getTargetClass().getMethod(cancelParticipant.getMethodName(), cancelParticipant.getParameterTypes());
        } catch (NoSuchMethodException e) {

            throw new TccSystemException(e);
        }

        //更新invoke方法时的实参，即赋值TccTransactionContext
        FactoryBuilder.factoryOf(tccTransactionContextEditorClass).getInstance()
                .set(context, method, cancelParticipant.getArgs());

        TccTransactionMethodUtils.invokeParticipant(FactoryBuilder.factoryOf(confimParticipant.getTargetClass()).getInstance(), method, cancelParticipant.getArgs());
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
