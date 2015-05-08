package de.iss.mv2.server.io.sql;

import java.sql.Connection;

/**
 * Provides methods to communicate with a database and manage requests.
 * @author Marcel Singer
 *
 */
public interface SQLContext {

	/**
	 * Returns an open connection to a database.
	 * @return An open connection to a database.
	 */
	public Connection getConnection();
	/**
	 * Returns the synchronizer to use for dispatching responses.
	 * @return The synchronizer to use for dispatching responses.
	 */
	public ThreadSynchronizer getResponseSynchronizer();
	/**
	 * Returns the synchronizer to use for dispatching requests.
	 * @return The synchronizer to use for dispatching requests.
	 */
	public ThreadSynchronizer getRequestSynchronizer();
	
}
