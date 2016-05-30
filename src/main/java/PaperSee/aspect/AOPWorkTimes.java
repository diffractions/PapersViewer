package aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;

import org.apache.log4j.Logger;

public class AOPWorkTimes {

	public Object profile(ProceedingJoinPoint call) throws Throwable {
		Logger.getLogger("LOG").trace(
				new StringBuilder().append("LOG.IN.Method: ")
						.append(call.getSignature().toLongString())
						.append(";\nArguments: ")
						.append(Arrays.toString(call.getArgs())).append(";"));
		long start = System.nanoTime();
		try {
			return call.proceed();
		} finally {
			Logger.getLogger("LOG").trace(
					new StringBuilder().append("LOG.OUT.Method: ")
							.append(call.getSignature().toShortString())
							.append(";\nWork time: ")
							.append((System.nanoTime() - start)));

		}
	}

}
