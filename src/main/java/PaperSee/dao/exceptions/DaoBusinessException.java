package dao.exceptions;

public class DaoBusinessException extends DaoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DaoBusinessException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public DaoBusinessException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public DaoBusinessException(String message) {
		super(message);
		
	}

	public DaoBusinessException(Throwable cause) {
		super(cause);
		
	}

	public DaoBusinessException() {
		
	}

}
