package aspect;

import org.aspectj.lang.JoinPoint;

import static utility.LogPrinter.println;

public class AOPExceptionPrint {
	public void printException(JoinPoint joinPoint, Throwable throwable) {

		println("PRINT EXCPTION FIND BY ASPECT", throwable.getStackTrace()[0],
				throwable);

	}
}
