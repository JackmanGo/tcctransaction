package recoverJob;

import recover.RecoverConfig;

public class DefaultRecoverConfig implements RecoverConfig {

    private int maxRetryCount = 30;

    private int recoverDuration = 120; //120 seconds

    private String cronExpression = "0 */1 * * * ?";

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public void setRecoverDuration(int recoverDuration) {
        this.recoverDuration = recoverDuration;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Override
    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    @Override
    public int getRecoverDuration() {
        return recoverDuration;
    }

    @Override
    public String getCronExpression() {
        return cronExpression;
    }
}
