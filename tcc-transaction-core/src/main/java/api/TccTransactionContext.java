package api;

import java.io.Serializable;

public class TccTransactionContext implements Serializable {
    private static final long serialVersionUID = -1L;

    private TccTransactionXid xid;

    private TccTransactionStatus status;

    public TccTransactionContext(){

    }

    public TccTransactionContext(TccTransactionXid xid, TccTransactionStatus status) {
       this.xid = xid;
       this.status = status;
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
}
