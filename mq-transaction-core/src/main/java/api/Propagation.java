package api;

/**
 * 事务隔离级别，参考Spring Transactional注解的propagation
 * see: Spring.Transactional.PROPAGATION_*
 * @author wangxi
 * @date 2019-12-18 15:46
 */
public enum Propagation {

    /**
     *
     * @Description 默认级别。支持当前已经存在的事务，如果还没有事务，就创建一个新事务
     */
    REQUIRED(0),

    /**
     *
     * @Description 支持当前事务，如果没有事务那么就不在事务中运行。
     */
    SUPPORTS(1),

    /**
     *
     * @Description 必须在一个事务中运行，如果没有事务，将抛出异常。
     */
    MANDATORY(2),

    /**
     *
     * @Description 需要运行在一个新的事务中,当前如果有事务，则挂起当前事务
     */
    REQUIRES_NEW(3);

    private final int value;

    private Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
