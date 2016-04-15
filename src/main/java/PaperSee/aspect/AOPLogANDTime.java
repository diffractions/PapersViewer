package aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;

public class AOPLogANDTime {

	public Object profile(ProceedingJoinPoint call) throws Throwable {
		 System.out.println("Aspect.LOG.IN");
		long start = System.nanoTime();
		try {
			return call.proceed();
		} finally {
			System.out.println("Aspect.LOG.OUT.Method: "
					+ call.getSignature().toLongString() + ";" + " arguments: "
					+ Arrays.toString(call.getArgs()) + " work time: "
					+ (System.nanoTime() - start));
		}
	}

}
