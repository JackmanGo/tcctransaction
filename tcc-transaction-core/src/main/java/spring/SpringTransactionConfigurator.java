package spring;

import config.TccTransactionConfigurator;
import exception.TccOptimisticLockException;
import manager.TccTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import repository.TransactionRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * 供Spring注入的入口
 */
public class SpringTransactionConfigurator implements TccTransactionConfigurator {

    //@Autowired
    private TransactionRepository transactionRepository;
    private TccTransactionManager tccTransactionManager;
    private Set<Class<? extends Exception>> delayCancelExceptions = new HashSet<Class<? extends Exception>>();


    public void init(){

        tccTransactionManager = new TccTransactionManager();
        tccTransactionManager.setTransactionRepository(transactionRepository);
        delayCancelExceptions.add(TccOptimisticLockException.class);
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

    @Override
    public Set<Class<? extends Exception>> getDelayCancelExceptions() {
        return this.delayCancelExceptions;
    }

}
