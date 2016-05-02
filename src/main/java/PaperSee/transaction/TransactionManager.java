package transaction;

import java.util.concurrent.Callable;

import transaction.exception.TransactionException;

public interface TransactionManager {
	public <T> T doInTransaction(Callable<T> unitOfWork) throws TransactionException;
}
