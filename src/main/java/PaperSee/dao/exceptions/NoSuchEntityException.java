package dao.exceptions;

public class NoSuchEntityException extends DaoBusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchEntityException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public NoSuchEntityException(String message, Throwable cause) {
		super(message, cause);
		// 
	}

	public NoSuchEntityException(String message) {
		super(message);
		// 
	}

	public NoSuchEntityException(Throwable cause) {
		super(cause);
		// 
	}

	public NoSuchEntityException() {
		// 
	}

}
