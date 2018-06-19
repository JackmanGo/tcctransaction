package api;

public enum  TccTransactionType {

    //开启根事务
    ROOT(1),
    //开启分支事务
    BRANCH(2),
    //不执行事务
    NORMAL(3);

    int id;

    TccTransactionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TccTransactionType valueOf(int id) {
        switch (id) {
            case 1:
                return ROOT;
            case 2:
                return BRANCH;
            default:
                return null;
        }
    }
}
