package utility.exception;

public class ReadWriteCodeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReadWriteCodeException() {
	}

	public ReadWriteCodeException(String message) {
		super(message);
	}

	public ReadWriteCodeException(Throwable cause) {
		super(cause);
	}

	public ReadWriteCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReadWriteCodeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
