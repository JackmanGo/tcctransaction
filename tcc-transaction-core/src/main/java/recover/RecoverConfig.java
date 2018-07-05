package recover;

/**
 * 事务恢复器配置
 */
public interface RecoverConfig {

    //获取最大的重试次数
    int getMaxRetryCount();

    //获取恢复间隔时间，单位：毫秒
    int getRecoverDuration();

    //定时任务cron表达式： 0 */1 * * *
    //
    String getCronExpression();

    /*
    Cron表达式是一个由7个子表达式组成的字符串。每个子表达式都描述了一个单独的日程细节。这些子表达式用空格分隔，分别表示:
    1. Seconds 秒
    2. Minutes 分钟
    3. Hours 小时
    4. Day-of-Month 月中的天
    5. Month 月
    6. Day-of-Week 周中的天
    7. Year (optional field) 年(可选的域)

    通配符('*')可以被用来表示域中“每个”可能的值
    '/'字符用来表示值的增量，例如, 如果分钟域中放入'0/15'，它表示“每隔 15 分钟， 从 0 开始”，如果在份中域中使用'3/20'，则表示“小时中每隔 20 分钟，从第 3 分钟开 始”或者另外相同的形式就是'3,23,43'。
    */
}
