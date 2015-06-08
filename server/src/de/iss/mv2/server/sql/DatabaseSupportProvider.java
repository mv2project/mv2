package de.iss.mv2.server.sql;

import java.io.InputStreamReader;

/**
 * A provider to connect to a specific database system.
 * 
 * @author Marcel Singer
 *
 */
public interface DatabaseSupportProvider {

	/**
	 * Returns the name of the database system (e.g. PostgreSQL, MySQL).
	 * 
	 * @return The name of the database system.
	 */
	public String getDatabaseTypeIdentifier();

	/**
	 * Returns a initialized reader to read the DDL-script.
	 * 
	 * @return A initialized reader to read the DDL-script.
	 */
	public InputStreamReader getDDLReader();

	/**
	 * Creates the connection string to be used to connect to the specified
	 * server instance.
	 * 
	 * @param host
	 *            The host.
	 * @param port
	 *            The port.
	 * @param database
	 *            The name of the database to connect to.
	 * @param user
	 *            The user to be used for the connection.
	 * @param password
	 *            The password to be used for the connection.
	 * @return The connection string to be used to connect to the specified
	 *         server instance.
	 */
	public String createConnectionString(String host, int port,
			String database, String user, String password);

	/**
	 * Registers required drivers.
	 */
	public void registerRequiredDrivers();

}
