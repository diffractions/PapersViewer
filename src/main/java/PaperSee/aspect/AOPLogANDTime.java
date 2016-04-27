package aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;

import com.sun.javafx.binding.StringFormatter;

import static utility.LogPrinter.*;

public class AOPLogANDTime {

	public Object profile(ProceedingJoinPoint call) throws Throwable {
		println("LOG.IN");
		long start = System.nanoTime();
		try {
			return call.proceed();
		} finally {
			println(StringFormatter.format(
					"LOG.OUT.Method: " + call.getSignature().toLongString()
							+ ";" + " arguments: "
							+ Arrays.toString(call.getArgs())
							+ " work time: %,d ns.",
					(System.nanoTime() - start)).get());

		}
	}

}
