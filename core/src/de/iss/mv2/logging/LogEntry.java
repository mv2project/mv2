package de.iss.mv2.logging;

import java.util.Date;

/**
 * A log entry containing information to a runtime message.
 * @author Marcel Singer
 *
 */
public interface LogEntry {

	/**
	 * Returns the log level of this entry.
	 * @return The log level of this entry.
	 */
	public LogEntryType getLevel();
	
	/**
	 * Returns the timestamp of this entry.
	 * @return The timestamp of this entry.
	 */
	public Date getTimestamp();
	
	/**
	 * Returns the message of this entry.
	 * @return The informational message of this entry.
	 */
	public String getMessage();
	
	/**
	 * Returns the component affected by this log entry.
	 * @return The component affected by this log entry.
	 */
	public String getComponent();
	
	/**
	 * Returns the detailed stack trace of this log entry.
	 * @return The detailed stack trace of this entry or {@code null} if there is none.
	 */
	public StackTraceElement[] getDatailedStackTrace();
	
	/**
	 * Returns the reduced stack trace information.
	 * @return The reduced stack trace information or {@code null} if there is none.
	 */
	public String getStackTrace();
	
}
