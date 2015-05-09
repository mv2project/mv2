package de.iss.mv2.logging;

/**
 * The type of a log entry declaring its relevance.
 * @author Marcel Singer
 *
 */
public enum LogEntryLevel {

	/**
	 * The type for a trivial log entry.
	 */
	TRIVIAL(0),
	/**
	 * The type for an informational log entry.
	 */
	INFORMATION(1),
	/**
	 * The type for a warning entry.
	 */
	WARNING(2),
	/**
	 * The type of an exception entry.
	 */
	EXCEPTION(3),
	/**
	 * The type of an critical log entry.
	 */
	CRITICAL(4);
	
	/**
	 * Holds the level identifier.
	 */
	private final int level;
	
	/**
	 * Creates a new {@link LogEntryLevel}.
	 * @param level The identifier of the level.
	 */
	private LogEntryLevel(int level){
		this.level = level;
	}
	
	/**
	 * Tests if this {@link LogEntryLevel} is equally or more important than the given one.
	 * @param level The {@link LogEntryLevel} to compare to.
	 * @return {@code true} if this level is equally or more important than the given one.
	 */
	public boolean matches(LogEntryLevel level){
		return this.level >= level.level;
	}
	
	
}
