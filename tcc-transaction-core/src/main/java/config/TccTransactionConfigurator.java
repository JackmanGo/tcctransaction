package config;

import manager.TccTransactionManager;
import repository.TransactionRepository;

public interface TccTransactionConfigurator {


    TccTransactionManager getTransactionManager();

    TransactionRepository getTransactionRepository();
}
