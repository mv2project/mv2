package de.iss.mv2.io.sql;

import java.sql.Connection;

/**
 * Represents a default SQL context.
 * @author Marcel Singer
 *
 */
public class DefaultSQLContext implements SQLContext {

	/**
	 * Holds the connection.
	 */
	private Connection connection;
	/**
	 * Holds the response synchronizer.
	 */
	private final ThreadSynchronizer responseSynchronizer;
	/**
	 * Holds the request synchronizer.
	 */
	private final ThreadSynchronizer requestSynchronizer;
	
	/**
	 * Creates a new instance of {@link DefaultSQLContext}.
	 * @param connection The connection to use.
	 * @param requestSynchronizer The request synchronizer to use.
	 * @param responseSynchronizer The response synchronizer to use.
	 */
	 public DefaultSQLContext(Connection connection, ThreadSynchronizer requestSynchronizer, ThreadSynchronizer responseSynchronizer) {
		this.connection = connection;
		this.responseSynchronizer = responseSynchronizer;
		this.requestSynchronizer = requestSynchronizer;
	 }
	
	
	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public ThreadSynchronizer getResponseSynchronizer() {
		return responseSynchronizer;
	}


	@Override
	public ThreadSynchronizer getRequestSynchronizer() {
		return requestSynchronizer;
	}
	
	/**
	 * Sets the connection of this context.
	 * @param connection The database connection to set.
	 */
	protected void setConnection(Connection connection){
		this.connection = connection;
	}

}
