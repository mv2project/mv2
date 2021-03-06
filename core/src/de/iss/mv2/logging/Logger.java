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
	public void setMinimalLogLevel(LogEntryLevel minimalLevel);
	
	/**
	 * Returns all log entries with a level higher or equal to the given minimum level.
	 * @param minimumLevel The minimum level of the entries to return.
	 * @return All log entries with a level higher or equal to the given minimum level.
	 */
	public Iterable<LogEntry> filter(LogEntryLevel minimumLevel);
	
	/**
	 * Returns all log entries.
	 * @return All log entries.
	 */
	public Iterable<LogEntry> getAll();
	
	/**
	 * Returns all log entries with a timestamp later than the given one.
	 * @param minimumDate The minimum timestamp of the entries to return.
	 * @return All log entries with a timestamp later than the given one.
	 */
	public Iterable<LogEntry> filter(Date minimumDate);
	
	/**
	 * Adds an entry to this log.
	 * @param entry The entry to log.
	 */
	public void push(LogEntry entry);
	
	/**
	 * Creates a new log entry and adds it to this log.
	 * @param level The log level type of the entry to create.
	 * @param component The component affected by the log entry to create.
	 * @param message The message of the log entry to create.
	 * @return The created log entry.
	 */
	public LogEntry push(LogEntryLevel level, String component, String message);
	
	/**
	 * Creates a new log entry from the given exception an adds it to this log.
	 * @param ex The exception to log.
	 * @return The generated log entry.
	 */
	public LogEntry push(Exception ex);
	
	/**
	 * Adds all given entries to this log.
	 * @param entries The entries to add.
	 */
	public void pushAll(Iterable<LogEntry> entries);
	
	/**
	 * Removes all cached log entries from this log.
	 */
	public void clear();
	
	/**
	 * Removes a log entry from this log.
	 * @param entry The entry to remove.
	 * @return {@code true}, if the given log entry was present.
	 */
	public boolean delete(LogEntry entry);
	
	/**
	 * Sets the stack tracing level.
	 * @param level The stack tracing level to set.
	 * @return {@code true} if the logging to set is supported by this logger.
	 */
	public boolean setStackTracingLevel(StackTracingLevel level);
	
	/**
	 * Sets the stack tracing level
	 * @return The current stack tracing level of this logger.
	 */
	public StackTracingLevel getStackTracingLevel();
}