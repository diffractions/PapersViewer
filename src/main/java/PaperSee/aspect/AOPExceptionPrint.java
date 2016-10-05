package aspect;

import org.aspectj.lang.JoinPoint;

import org.apache.log4j.Logger;

public class AOPExceptionPrint {
	public void printException(JoinPoint joinPoint, Throwable throwable) {

		Logger.getLogger("LOG").error("PRINT EXCPTION FIND BY ASPECT",
				throwable);

	}
}
