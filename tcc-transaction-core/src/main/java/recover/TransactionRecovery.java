package recover;

import api.TccTransactionStatus;
import bean.Transaction;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import repository.TransactionRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 事务恢复器实现
 * 获取失败的事务并根据状态进行恢复
 */
public class TransactionRecovery {

    static final Logger logger = Logger.getLogger(TransactionRecovery.class);

    private TransactionRepository transactionRepository;

    private RecoverConfig recoverConfig;


    /**
     * 开始尝试恢复事务
     */
    public void startRecover() {

        List<Transaction> transactions = loadErrorTransactions();

        recoverErrorTransactions(transactions);
    }

    /**
     * 加载所有异常事务
     * @return
     */
    private List<Transaction> loadErrorTransactions() {

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

        return transactionRepository.findAllUnfinished(new Date());
    }

    /**
     * 尝试恢复事务
     * @param transactions
     */
    private void recoverErrorTransactions(List<Transaction> transactions) {

        for (Transaction transaction : transactions) {

            //如果尝试次数大于 config配置的次数，记入日志需要手动处理
            if (transaction.getRetriedCount() > recoverConfig.getMaxRetryCount()) {

                logger.error(String.format("recover failed with max retry count,will not try again. txid:%s, " +
                                "status:%s,retried count:%d,transaction content:%s",
                        transaction.getXid(), transaction.getStatus().getId(),
                        transaction.getRetriedCount(), JSON.toJSONString(transaction)));
                continue;
            }

            //confirm or cancel
            transaction.addRetriedCount();

            try {
                if (transaction.getStatus().equals(TccTransactionStatus.CONFIRMING)) {

                    //该更新的目的是更新字段LAST_UPDATE_TIME，控制频率
                    transaction.setStatus(TccTransactionStatus.CONFIRMING);
                    transactionRepository.update(transaction);
                    transaction.commit();
                    transactionRepository.delete(transaction);

                } else if (transaction.getStatus().equals(TccTransactionStatus.CANCELLING)) {

                    //该更新的目的是更新字段LAST_UPDATE_TIME，控制频率
                    transaction.setStatus(TccTransactionStatus.CANCELLING);
                    transactionRepository.update(transaction);
                    transaction.rollback();
                    transactionRepository.delete(transaction);

                    //TODO
                } else if (false) {

                }
            } catch (Exception e) {
                //TODO
            }

        }
    }

}
