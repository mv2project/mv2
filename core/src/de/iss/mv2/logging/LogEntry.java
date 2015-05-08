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
	
}
