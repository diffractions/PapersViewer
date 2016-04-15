package inject.exeptions;

import javax.servlet.ServletException;

public class InjectInitialException extends ServletException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1872757237029397519L;

	public InjectInitialException() {
	}

	public InjectInitialException(String message) {
		super(message);
	}

	public InjectInitialException(Throwable cause) {
		super(cause);
	}

	public InjectInitialException(String message, Throwable cause) {
		super(message, cause);
	}



}
