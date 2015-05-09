package de.iss.mv2.logging;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * A logger to log all events on the standard output streams.
 * 
 * @author Marcel Singer
 *
 */
public class ConsoleLogger implements Logger {

	/**
	 * An empty iterable to return.
	 */
	private static final Iterable<LogEntry> EMPTY_ITERABLE = new ArrayList<LogEntry>();

	/**
	 * Holds the minimum log level.
	 */
	private LogEntryLevel entryLevel = LogEntryLevel.INFORMATION;

	/**
	 * Holds the log entry printer to use.
	 */
	private LogEntryPrinter entryPrinter = new LogEntryPrinter();
	
	/**
	 * Stack tracing level.
	 */
	private StackTracingLevel stackTracingLevel;

	/**
	 * Creates a new instance of {@link ConsoleLogger}.
	 */
	public ConsoleLogger() {

	}

	@Override
	public void setMinimalLogLevel(LogEntryLevel minimalLevel) {
		if (minimalLevel == null)
			minimalLevel = LogEntryLevel.INFORMATION;
		this.entryLevel = minimalLevel;
	}

	@Override
	public Iterable<LogEntry> filter(LogEntryLevel minimumLevel) {
		return EMPTY_ITERABLE;
	}

	@Override
	public Iterable<LogEntry> getAll() {
		return EMPTY_ITERABLE;
	}

	@Override
	public Iterable<LogEntry> filter(Date minimumDate) {
		return EMPTY_ITERABLE;
	}

	@Override
	public void push(LogEntry entry) {
		if (!entry.getLevel().matches(entryLevel))
			return;
		LogEntryLevel el = entry.getLevel();
		PrintStream out;
		if (el == LogEntryLevel.CRITICAL || el == LogEntryLevel.EXCEPTION
				|| el == LogEntryLevel.WARNING) {
			out = System.err;
		} else {
			out = System.out;
		}
		out.print(entryPrinter.getStringRepresentation(entry));
	}

	@Override
	public LogEntry push(LogEntryLevel type, String component, String message) {
		StackTraceElement[] stackTrace = StackTracer.getStackTrace(1);
		DefaultLogEntry dle = new DefaultLogEntry(type, component, message, stackTracingLevel, stackTrace);
		push(dle);
		return dle;
	}

	@Override
	public void pushAll(Iterable<LogEntry> entries) {
		for(LogEntry le : entries){
			push(le);
		}
	}

	@Override
	public void clear() {
		
	}

	@Override
	public boolean delete(LogEntry entry) {
		return false;
	}

	@Override
	public boolean setStackTracingLevel(StackTracingLevel level) {
		if(level == StackTracingLevel.DETAILED) return false;
		stackTracingLevel = level;
		return true;
	}

	@Override
	public StackTracingLevel getStackTracingLevel() {
		return stackTracingLevel;
	}

}
