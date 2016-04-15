package aspect;

//import java.util.Arrays;

import org.aspectj.lang.JoinPoint;

public class AOPExceptionPrint {

	public void printException(JoinPoint joinPoint, Throwable throwable) {
		System.out.println("Aspect.ERR: " + throwable.getMessage());
//		System.out
//				.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"
//						+ throwable.getMessage()
//						+ "\n-----------------------------Stack trace----------------------------------");
//		Arrays.asList(throwable.getStackTrace()).forEach(System.err::println);
//		System.out
//				.println("\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}

}
