package de.iss.mv2.server.io.sql;

/**
 * Provides the settings used for the building SQL commands.
 * @author Marcel Singer
 *
 */
public interface CommandBuilderSettings {

	/**
	 * Returns the quotes for column, table and database names.
	 * @return The quotes for column, table and database names.
	 */
	public String getColumnQuote();
	/**
	 * Returns {@code true} if the table name should be preceded by the name of the database.
	 * @return {@code true} if the table name should be preceded by the name of the database. 
	 */
	public boolean useDatabasePrefix();
	/**
	 * Returns the name of the database.
	 * @return The name of the database.
	 */
	public String getDatabaseName();
	/**
	 * Returns the name of the table.
	 * @return The name of the table.
	 */
	public String getTableName();
	
}
