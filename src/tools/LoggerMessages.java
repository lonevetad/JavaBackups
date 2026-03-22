package tools;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.function.Supplier;

public interface LoggerMessages extends Serializable {

	public static final String NEW_LINE = "\n", CONSOLE_BASE_PATH =
			// since interfaces cannot have a static initializer,
			// a workaround is required
			((Supplier<String>) () -> {
				try {
					String basePath = FileSystems.getDefault().getPath(".").toAbsolutePath().toString();
					return basePath.substring(0, basePath.length() - 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "." + File.separatorChar;
			}).get();
	public static final LoggerMessages LOGGER_DEFAULT = (LoggerMessages & Serializable) ((t, n) -> {
		if (n)
			System.out.println(t);
		else
			System.out.print(t);
		return true;
	});

	//

	public boolean log(String text, boolean newLineRequired);

	public default boolean log(String text) { return log(text, true); }

	public default boolean logNoNewLine(String text) { return log(text, false); }

	public default boolean logAndPrint(String text) { return logAndPrint(text, System.out); }

	public default boolean logAndPrintError(String text) { return logAndPrint(text, System.err); }

	public default boolean logAndPrint(String text, PrintStream ps) {
		if (ps != null)
			ps.print(text);
		return log(text, false);
	}

	public default boolean logException(Exception e) {
		e.printStackTrace();
		return logAndPrintError("ERROR: exception raised:\n\t" + e.getMessage());
	}

	public default void clearLog() { System.out.println("DEFAULT LOG CANNOT BE CLEARED"); }

	public default List<String> getEntireLog() { return null; }

	public static void requireLogger(LoggerMessages log) {
		if (log == null)
			throw new RuntimeException("Logger's null when required.");
	}

	public static LoggerMessages loggerOrDefault(LoggerMessages log) { return log == null ? LOGGER_DEFAULT : log; }
}