package repository;

import api.TccTransactionXid;
import bean.Transaction;

import java.util.Date;
import java.util.List;

public interface TransactionRepository {

    int create(Transaction transaction);

    int update(Transaction transaction);

    int delete(Transaction transaction);

    Transaction findByXid(TccTransactionXid xid);

    List<Transaction> findAllUnmodifiedSince(Date date);
}
