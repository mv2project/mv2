package de.iss.mv2.logging;

import java.util.Date;

/**
 * A logger to track runtime messages.
 * @author Marcel Singer
 *
 */
public interface Logger {

	/**
	 * Sets the minimal logging level. Already cached log entries with a lower log level will be remove.
	 * @param minimalLevel The minimal logging level to set.
	 */
	public void setMinimalLogLevel(LogEntryType minimalLevel);
	
	/**
	 * Returns all log entries with a level higher or equal to the given minimum level.
	 * @param minimumLevel The minimum level of the entries to return.
	 * @return All log entries with a level higher or equal to the given minimum level.
	 */
	public Iterable<LogEntry> filter(LogEntryType minimumLevel);
	
	/**
	 * Returns all log entries.
	 * @return All log entries.
	 */
	public Iterable<LogEntry> getAll();
	
	/**
	 * Returns all log entries with a timestamp later than the given one.
	 * @param minimumDate The minimum timestamp of the entries to return.
	 * @return All log entries with a timestamp laten than the given one.
	 */
	public Iterable<LogEntry> filter(Date minimumDate);
	
	
	
}
