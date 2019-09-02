package api;

public enum  TccTransactionStatus {

    //开启Tcc事务后的初始状态
    TRYING(1),
    //尝试完成阶段
    CONFIRMING(2),
    //回滚阶段
    CANCELLING(3);

    private int id;

    TccTransactionStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TccTransactionStatus valueOf(int id) {

        switch (id) {
            case 1:
                return TRYING;
            case 2:
                return CONFIRMING;
            default:
                return CANCELLING;
        }
    }
}
