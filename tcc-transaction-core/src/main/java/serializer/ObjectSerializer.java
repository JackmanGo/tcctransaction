package serializer;

/**
 * 事务存储序列化接口
 * @param <T>
 */
public interface ObjectSerializer<T> {

    byte[] serialize(T t);

    T deserialize(byte[] bytes);

    T clone(T object);

}
