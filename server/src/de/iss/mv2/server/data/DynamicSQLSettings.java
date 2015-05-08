package de.iss.mv2.server.data;

/**
 * A class that hold information about the SQL-command style to be used by the
 * command builders.
 * 
 * @author Marcel Singer
 *
 */
public class DynamicSQLSettings {

	/**
	 * Hold the default settings.
	 */
	private static final DynamicSQLSettings instance = new DynamicSQLSettings();
	/**
	 * Holds the style of the column quotation.
	 */
	private String sqlQuotes = "";
	/**
	 * {@code true} if the name of a table should be preceded by the database name.
	 */
	private boolean useDatabasePrefix = false;
	
	/**
	 * Holds the name of the database.
	 */
	private String databaseName = "mv2";

	/**
	 * Creates a new instance of {@link DynamicSQLSettings}.
	 */
	private DynamicSQLSettings() {

	}

	/**
	 * Returns the style of the column quotation.
	 * @return The style of the column quotation.
	 */
	public String getSQLQuotes() {
		return sqlQuotes;
	}

	/**
	 * Returns if the name of a table should be preceded by the database name.
	 * @return {@code true} is the name of a table should be preceded by the database name.
	 */
	public boolean useDatabasePrefix() {
		return useDatabasePrefix;
	}

	/**
	 * Returns the settings to be used by this runtime.
	 * @return The settings to be used by this runtime.
	 */
	public static DynamicSQLSettings getInstance() {
		return instance;
	}

	/**
	 * Returns the current database name.
	 * @return The current database name.
	 */
	public String getDatabaseName() {
		return databaseName;
	}

}
