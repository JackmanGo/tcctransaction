package serializer;

import org.springframework.util.SerializationUtils;

import java.io.Serializable;

/**
 * jdk序列号工具
 * @param <T>
 */
public class JdkSerializer <T extends Serializable> implements ObjectSerializer<T> {


    @Override
    public byte[] serialize(T t) {

        return SerializationUtils.serialize(t);
    }

    @Override
    public T deserialize(byte[] bytes) {

        if (bytes == null) {
            return null;
        } else {
            return (T) SerializationUtils.deserialize(bytes);
        }
    }

    @Override
    public T clone(T object) {
        return null;
    }
}

