package manager;

/**
 *
 * @author wangxi
 * @date 2019-12-18 17:40
 */
@FunctionalInterface
public interface RpcAcquire {

    /**
     * Acquire string.
     *
     * @param key the key
     * @return the string
     */
    String acquire(String key);
}
