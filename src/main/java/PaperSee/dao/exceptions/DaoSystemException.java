package dao.exceptions;

public class DaoSystemException extends DaoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DaoSystemException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public DaoSystemException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public DaoSystemException(String message) {
		super(message);
	}

	public DaoSystemException(Throwable cause) {
		super(cause);
		
	}

	public DaoSystemException() {
		
	}

}
