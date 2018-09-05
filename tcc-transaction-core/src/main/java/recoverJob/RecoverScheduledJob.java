package recoverJob;

import exception.TccSystemException;
import org.quartz.Scheduler;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import recover.TransactionRecovery;

public class RecoverScheduledJob {

    private TransactionRecovery transactionRecovery;
    private Scheduler scheduler;


    public void init(){

        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        jobDetail.setTargetObject(transactionRecovery);
        jobDetail.setTargetMethod("startRecover");
        jobDetail.setName("transactionRecoveryJob");
        // 禁止并发
        jobDetail.setConcurrent(false);

        try {

            jobDetail.afterPropertiesSet();

            CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
            cronTrigger.setBeanName("transactionRecoveryCronTrigger");
            cronTrigger.setCronExpression(transactionRecovery.getRecoverConfig().getCronExpression());
            cronTrigger.setJobDetail(jobDetail.getObject());
            cronTrigger.afterPropertiesSet();

            scheduler.scheduleJob(jobDetail.getObject(), cronTrigger.getObject());

            scheduler.start();
        }catch (Exception e){
            throw new TccSystemException(e);
        }
    }

    public TransactionRecovery getTransactionRecovery() {
        return transactionRecovery;
    }

    public void setTransactionRecovery(TransactionRecovery transactionRecovery) {
        this.transactionRecovery = transactionRecovery;
    }

}
