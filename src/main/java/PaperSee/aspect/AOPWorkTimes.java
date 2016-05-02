package aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;

import static utility.LogPrinter.*;

public class AOPWorkTimes {

	public Object profile(ProceedingJoinPoint call) throws Throwable {
		println(new StringBuilder().append("LOG.IN.Method: ")
				.append(call.getSignature().toLongString())
				.append(";\nArguments: ")
				.append(Arrays.toString(call.getArgs())).append(";"));
		long start = System.nanoTime();
		try {
			return call.proceed();
		} finally {
			println(new StringBuilder().append("LOG.OUT.Method: ")
					.append(call.getSignature().toShortString())
					.append(";\nWork time: ")
					.append((System.nanoTime() - start)) );

		}
	}

}
