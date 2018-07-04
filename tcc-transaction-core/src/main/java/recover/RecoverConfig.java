package recover;

public interface RecoverConfig {

    //获取最大的重试次数
    int getMaxRetryCount();

    //获取恢复间隔时间，单位：毫秒
    int getRecoverDuration();

    //定时任务表达式： 0 */1 * * *
    String getCronExpression();
}
