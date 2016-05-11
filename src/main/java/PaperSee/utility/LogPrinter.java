package utility;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class LogPrinter {

	private final static int MAIN_TEXT_SPACE_POSITION = 11;
	private final static byte SPAECE = 32;
	private final static byte NEW_LINE = 10;
	private final static String NEW_LINE_S = new String(new byte[] { NEW_LINE });

	private final static String TAB_IN_EXCEPTION = new String(new byte[] {
			NEW_LINE, SPAECE, SPAECE, SPAECE });
	private final static String EMPTY_STR = new String();

	public static void printlnEnd(CharSequence str) {
		printers(str);
		System.out
				.println("------------------------------------------------------------");
	}

	public static void println(CharSequence str) {
		printers(str);
	}

	public static void println(Throwable throwable) {
		printers(getExceptionString(EMPTY_STR, throwable));
	}

	public static void println(CharSequence str, Throwable e) {
		printers(getExceptionString(
				str.length() != 0 ? str = new StringBuilder(TAB_IN_EXCEPTION)
						.append(str).append(";") : str, e));
	}

	public static void println(StackTraceElement element, Throwable throwable) {
		printers(getExceptionString(EMPTY_STR, throwable), element);
	}

	public static void println(CharSequence str, StackTraceElement element,
			Throwable throwable) {
		printers(getExceptionString(str, throwable), element);
	}

	private static void printers(CharSequence str) {
		printers(str, Thread.currentThread().getStackTrace()[3]);
	}

	private static void printers(CharSequence str, StackTraceElement element) {

		StringBuilder resultString = new StringBuilder();

		String className = element.getClassName();
		int length = className.indexOf(".");
		int rows = (int) Math.ceil(length / (double) MAIN_TEXT_SPACE_POSITION);
		GregorianCalendar inTime = new GregorianCalendar();

		StringBuilder headCaption = new StringBuilder(className.substring(0,
				length).toUpperCase())
				.append(fillHeadSpacesEmpty((rows * MAIN_TEXT_SPACE_POSITION)
						- length)).append("in:")
				.append(inTime.get(GregorianCalendar.SECOND)).append(".")
				.append(inTime.get(GregorianCalendar.MILLISECOND)).append("s");

		length = headCaption.length();
		rows = (int) Math.ceil(length / (double) MAIN_TEXT_SPACE_POSITION);

		String emptys = fillHeadSpaces(MAIN_TEXT_SPACE_POSITION);
		StringTokenizer stc = new StringTokenizer(new StringBuilder(
				element.toString()).append(NEW_LINE_S).append(str).toString(),
				NEW_LINE_S);
		int stcLength = stc.countTokens();
		int toi = Math.max(stcLength, rows);

		for (int i = 0; i < toi; i++) {
			if (i < rows) {
				int from = i * MAIN_TEXT_SPACE_POSITION;
				int to = (i + 1) * ((length - from) / MAIN_TEXT_SPACE_POSITION) > 0 ? from
						+ MAIN_TEXT_SPACE_POSITION
						: length;
				resultString.append(headCaption.substring(from, to)).append(
						fillHeadSpaces(MAIN_TEXT_SPACE_POSITION + from - to));
			} else {
				resultString.append(emptys);
			}
			if (i < stcLength) {
				resultString.append(stc.nextToken());
			}
			resultString.append(NEW_LINE_S);
		}
		System.out.print(resultString.toString() + "\n");
	}

	private static String fillHeadSpaces(int i) {
		byte[] b = new byte[i + 1];
		Arrays.fill(b, 0, i, SPAECE);
		b[i] = '|';
		return new String(b);
	}

	private static String fillHeadSpacesEmpty(int i) {
		if (i > 0) {
			byte[] b = new byte[i];
			Arrays.fill(b, SPAECE);
			return new String(b);
		}
		return EMPTY_STR;
	}

	private static StringBuilder getExceptionString(CharSequence str,
			Throwable e) {

		// e.printStackTrace();

		return new StringBuilder().append("EXCEPTION > ").append(str)
				.append("\nEception type:").append(TAB_IN_EXCEPTION)
				.append(e.getClass().getCanonicalName())
				.append(";\nCause stack:").append(getCaused(e))
				.append("\nMessage:").append(TAB_IN_EXCEPTION)
				.append(e.getMessage())
				.append(";\nLaast stack trace element is:")
				.append(TAB_IN_EXCEPTION).append(e.getStackTrace()[0]);

	}

	private static StringBuilder getCaused(Throwable e) {
		return e.getCause() != null ? new StringBuilder(TAB_IN_EXCEPTION)
				.append(e.getCause().getClass()).append(";   ")
				.append(getCaused(e.getCause())) : new StringBuilder();
	}

}
