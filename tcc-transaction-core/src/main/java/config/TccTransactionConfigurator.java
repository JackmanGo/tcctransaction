package config;

import manager.TccTransactionManager;
import repository.TransactionRepository;

import java.util.Set;

public interface TccTransactionConfigurator {


    TccTransactionManager getTransactionManager();

    TransactionRepository getTransactionRepository();

    Set<Class<? extends Exception>> getDelayCancelExceptions();
}
