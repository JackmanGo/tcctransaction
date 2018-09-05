package repository;

import api.TccTransactionXid;
import bean.Transaction;
import com.google.common.cache.Cache;
import exception.TccOptimisticLockException;

import javax.transaction.xa.Xid;
import java.util.Date;
import java.util.List;

public abstract class CachableTransactionRepository implements TransactionRepository {

    private int expireDuration = 120;

    private Cache<Xid, Transaction> transactionXidCompensableTransactionCache;

    protected void putToCache(Transaction transaction) {
        transactionXidCompensableTransactionCache.put(transaction.getXid(), transaction);
    }

    protected void removeFromCache(Transaction transaction) {
        transactionXidCompensableTransactionCache.invalidate(transaction.getXid());
    }

    protected Transaction findFromCache(TccTransactionXid transactionXid) {
        return transactionXidCompensableTransactionCache.getIfPresent(transactionXid);
    }

    @Override
    public int create(Transaction transaction) {
        int effectRows = doCreate(transaction);

        if(effectRows > 0){
            putToCache(transaction);
        }
        return 0;
    }

    @Override
    public int update(Transaction transaction) {

        int effectRows = 0;

        try{

            effectRows = doUpdate(transaction);
            if (effectRows > 0) {
                putToCache(transaction);
            } else {
                throw new TccOptimisticLockException();
            }
        }finally {

            if (effectRows <= 0) {
                removeFromCache(transaction);
            }
        }

        return effectRows;
    }

    @Override
    public int delete(Transaction transaction) {

        int result = 0;

        try {
            result = doDelete(transaction);

        } finally {
            removeFromCache(transaction);
        }
        return result;
    }

    @Override
    public Transaction findByXid(TccTransactionXid xid) {

        Transaction transaction = findFromCache(xid);

        if (transaction == null) {
            transaction = doFindByXid(xid);

            if (transaction != null) {
                putToCache(transaction);
            }
        }

        return transaction;
    }

    @Override
    public List<Transaction> findAllUnfinished(Date date) {

        List<Transaction> transactions = doFindAllUnfinished(date);

        for (Transaction transaction : transactions) {
            putToCache(transaction);
        }

        return transactions;
    }

    abstract int doCreate(Transaction transaction);

    abstract int doUpdate(Transaction transaction);

    abstract int doDelete(Transaction transaction);

    abstract Transaction doFindByXid(TccTransactionXid xid);

    abstract List<Transaction> doFindAllUnfinished(Date date);
}
