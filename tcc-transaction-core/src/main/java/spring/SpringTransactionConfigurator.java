package spring;

import config.TccTransactionConfigurator;
import manager.TccTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import repository.TransactionRepository;

/**
 * 供Spring注入的入口
 */
public class SpringTransactionConfigurator implements TccTransactionConfigurator {

    //@Autowired
    private TransactionRepository transactionRepository;
    private TccTransactionManager tccTransactionManager;

    public void init(){

        System.out.println(transactionRepository);
        tccTransactionManager = new TccTransactionManager();
        tccTransactionManager.setTransactionRepository(transactionRepository);
    }

    public void setTransactionRepository(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TccTransactionManager getTransactionManager() {

        return tccTransactionManager;
    }

    @Override
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }
}
