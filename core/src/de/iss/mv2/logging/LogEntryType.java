package de.iss.mv2.logging;

/**
 * The type of a log entry declaring its relevance.
 * @author Marcel Singer
 *
 */
public enum LogEntryType {

	/**
	 * The type for a trivial log entry.
	 */
	TRIVIAL,
	/**
	 * The type for an informational log entry.
	 */
	INFORMATION,
	/**
	 * The type for a warning entry.
	 */
	WARNING,
	/**
	 * The type of an exception entry.
	 */
	EXCEPTION,
	/**
	 * The type of an critical log entry.
	 */
	CRITICAL;
	
}
