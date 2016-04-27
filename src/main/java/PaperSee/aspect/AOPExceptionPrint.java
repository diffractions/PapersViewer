package aspect;

//import java.util.Arrays;

import org.aspectj.lang.JoinPoint;

import static utility.LogPrinter.*;

public class AOPExceptionPrint {

	public void printException(JoinPoint joinPoint, Throwable throwable) {
		 println("ERR: " + throwable.getMessage()
				+ "; Laast StackTraceElement is in : class - "
				+ throwable.getStackTrace()[0].getClassName() + ", method - "
				+ throwable.getStackTrace()[0].getMethodName() + ", line - "
				+ throwable.getStackTrace()[0].getLineNumber());
//		  println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"
//		 + throwable.getMessage()
//		 +
//		 "\n-----------------------------Stack trace----------------------------------");
//		 Arrays.asList(throwable.getStackTrace()).forEach(System.err::println);
//		 println("\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}

}
