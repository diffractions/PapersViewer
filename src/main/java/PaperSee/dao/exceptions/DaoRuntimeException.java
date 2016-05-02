package dao.exceptions;

public class DaoRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DaoRuntimeException() {
		super(); 
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DaoRuntimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace); 
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DaoRuntimeException(String message, Throwable cause) {
		super(message, cause); 
	}

	/**
	 * @param message
	 */
	public DaoRuntimeException(String message) {
		super(message); 
	}

	/**
	 * @param cause
	 */
	public DaoRuntimeException(Throwable cause) {
		super(cause); 
	}

}
