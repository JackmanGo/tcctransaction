package api;

import javax.transaction.xa.Xid;
import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

import static utils.UuidByteUtils.uuidToByteArray;

/**
 *  X/Open 事务标识符 XID 结构的 Java 映射
 */
public class TccTransactionXid implements Xid, Serializable {

    private static final long serialVersionUID = -1L;

    private byte[] globalTransactionId;
    private byte[] branchQualifier;

    public TccTransactionXid(){

        this.globalTransactionId = uuidToByteArray(UUID.randomUUID());
        this.branchQualifier = uuidToByteArray(UUID.randomUUID());
    }

    public TccTransactionXid(byte[] globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
        branchQualifier = uuidToByteArray(UUID.randomUUID());
    }

    public TccTransactionXid(byte[] globalTransactionId, byte[] branchQualifier) {
        this.globalTransactionId = globalTransactionId;
        this.branchQualifier = branchQualifier;
    }

    public void setGlobalTransactionId(byte[] globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
    }

    public void setBranchQualifier(byte[] branchQualifier) {
        this.branchQualifier = branchQualifier;
    }

    /**

     * 获取 XID 的格式标识符部分。
     * @return
     */
    @Override
    public int getFormatId() {
        return 0;
    }

    /**
     * 获取 XID 的事务分支标识符部分作为字节数组。
     * @return
     */
    @Override
    public byte[] getGlobalTransactionId() {
        return this.globalTransactionId;
    }

    /**
     * 获取 XID 的全局事务标识符部分作为字节数组。
     * @return
     */
    @Override
    public byte[] getBranchQualifier() {
        return this.branchQualifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TccTransactionXid that = (TccTransactionXid) o;
        return Arrays.equals(globalTransactionId, that.globalTransactionId) &&
                Arrays.equals(branchQualifier, that.branchQualifier);
    }

    @Override
    public int hashCode() {

        int result = Arrays.hashCode(globalTransactionId);
        result = 31 * result + Arrays.hashCode(branchQualifier);
        return result;
    }

    public TccTransactionXid clone() {

        byte[] cloneGlobalTransactionId = null;
        byte[] cloneBranchQualifier = null;

        if (globalTransactionId != null) {
            cloneGlobalTransactionId = new byte[globalTransactionId.length];
            System.arraycopy(globalTransactionId, 0, cloneGlobalTransactionId, 0, globalTransactionId.length);
        }

        if (branchQualifier != null) {
            cloneBranchQualifier = new byte[branchQualifier.length];
            System.arraycopy(branchQualifier, 0, cloneBranchQualifier, 0, branchQualifier.length);
        }

        return new TccTransactionXid(cloneGlobalTransactionId, cloneBranchQualifier);
    }
}
