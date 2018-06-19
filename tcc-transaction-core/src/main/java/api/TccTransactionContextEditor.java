package api;

import java.lang.reflect.Method;

public interface TccTransactionContextEditor {

    TccTransactionContext get(Method method, Object[] args);

    void set(TccTransactionContext transactionContext, Method method, Object[] args);

}
