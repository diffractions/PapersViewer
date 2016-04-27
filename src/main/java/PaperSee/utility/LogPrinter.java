package utility;

import java.util.Arrays;
import java.util.Date;

public class LogPrinter {

	public static void println(String str) {
		String className = Thread.currentThread().getStackTrace()[2]
				.getClassName();
		String head = className.substring(0, className.indexOf("."))
				.toUpperCase();
		char[] l = new char[head.length()];
		Arrays.fill(l, ' ');
		String empty = new String(l);

		System.out.println(head + ": " + new Date(System.currentTimeMillis())
				+ ": " + className + " > "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "\n  " + empty + str);
	}

}
