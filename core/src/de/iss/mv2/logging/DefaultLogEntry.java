package de.iss.mv2.logging;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The default log entry.
 * 
 * @author Marcel Singer
 *
 */
public class DefaultLogEntry implements LogEntry {

	/**
	 * The level of this entry.
	 */
	private final LogEntryLevel level;
	/**
	 * The component affected by this entry.
	 */
	private final String component;
	/**
	 * The message of this entry.
	 */
	private final String message;
	/**
	 * The timestamp of this entry.
	 */
	private final Date timestamp;
	/**
	 * The detailed stack trace of this entry.
	 */
	private final StackTraceElement[] detailedStackTrace;
	/**
	 * The reduced stack trace of this entry.
	 */
	private final String reducedStackTrace;

	/**
	 * Creates a new instance of {@link DefaultLogEntry}.
	 * 
	 * @param level
	 *            The level of this entry.
	 * @param component
	 *            The component affected by this entry.
	 * @param message
	 *            The message of this entry.
	 * @param timestamp
	 *            The timestamp of this entry.
	 * @param stackTracinglevel
	 *            The level of stack tracing.
	 * @param stackTrace
	 *            The stack trace of this entry.
	 */
	public DefaultLogEntry(LogEntryLevel level, String component,
			String message, Date timestamp,
			StackTracingLevel stackTracinglevel, StackTraceElement[] stackTrace) {
		this.level = level;
		this.component = component;
		this.message = message;
		this.timestamp = timestamp;
		String reducedStackTrace = null;
		StackTraceElement[] detailedStackTrace = null;
		switch (stackTracinglevel) {
		case NONE:
			detailedStackTrace = null;
			reducedStackTrace = null;
			break;
		case REDUCED:
			detailedStackTrace = null;
			StringBuilder sb = new StringBuilder();
			for (StackTraceElement e : stackTrace) {
				sb.append(e + "\n");
			}
			reducedStackTrace = sb.toString();
			break;
		case DETAILED:
			detailedStackTrace = stackTrace;
			StringBuilder sb2 = new StringBuilder();
			for (StackTraceElement e : stackTrace) {
				sb2.append(e + "\n");
			}
			reducedStackTrace = sb2.toString();
			break;
		}
		this.detailedStackTrace = detailedStackTrace;
		this.reducedStackTrace = reducedStackTrace;
	}

	/**
	 * Creates a new instance of {@link DefaultLogEntry} with the current
	 * timestamp.
	 * 
	 * @param level
	 *            The level of this entry.
	 * @param component
	 *            The component affected by this entry.
	 * @param message
	 *            The message of this entry.
	 * @param stackTracingLevel
	 *            The stack tracing level of this entry.
	 * @param stackTrace
	 *            The stack trace of this entry.
	 */
	public DefaultLogEntry(LogEntryLevel level, String component,
			String message, StackTracingLevel stackTracingLevel,
			StackTraceElement[] stackTrace) {
		this(level, component, message, new GregorianCalendar().getTime(),
				stackTracingLevel, stackTrace);
	}

	@Override
	public LogEntryLevel getLevel() {
		return level;
	}

	@Override
	public Date getTimestamp() {
		return timestamp;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getComponent() {
		return component;
	}

	@Override
	public StackTraceElement[] getDatailedStackTrace() {
		return detailedStackTrace;
	}

	@Override
	public String getStackTrace() {
		return reducedStackTrace;
	}

}
