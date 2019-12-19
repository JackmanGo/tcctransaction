package api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mq消息事务注解，仅用于方法
 * @author wangxi
 * @date 2019-12-18 15:35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MqTransaction {


    /**
     * 消息队列的唯一标识，用于接受或发送MQ消息
     * @return
     */
    String topic() default "";

    /**
     * mq 消息模式.
     *
     * @return MessageTypeEnum message type enum
     */
    MessageTypeEnum pattern() default MessageTypeEnum.P2P;

    /**
     * 事务传播机制
     */
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * 目标接口类
     * 如果是springcloud用户，需要指定目标的接口服务
     * （因为springcloud是http的请求，通过反射序列化方式没办法调用，所有加了这个属性）
     * 如果是dubbo用户 则不需要指定
     * 如果是motan用户 则不需要指定.
     *
     * @return Class class
     */
    Class target() default Object.class;


    /**
     * 目标接口方法名称
     * 如果是springcloud用户，需要指定目标的方法名称
     * （因为springcloud是http的请求，通过反射序列化方式没办法调用，所有加了这个属性）
     * 如果是dubbo用户 则不需要指定
     * 如果是motan用户 则不需要指定.
     *
     * @return String string
     */
    String targetMethod() default "";

}
