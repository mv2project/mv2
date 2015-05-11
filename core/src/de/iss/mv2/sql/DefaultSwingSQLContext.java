package de.iss.mv2.sql;

import java.sql.Connection;

/**
 * A default {@link SQLContext} thats calls are invoked on the event dispatcher thread.
 * @author Marcel Singer
 *
 */
public class DefaultSwingSQLContext extends DefaultSQLContext {

	/**
	 * Creates a new instance of {@link DefaultSwingSQLContext}.
	 * @param connection The connection to use.
	 */
	public DefaultSwingSQLContext(Connection connection) {
		super(connection, new SingleThreadSynchronizer(),  new SwingThreadSynchronizer());	
	}
	
	/**
	 * Creates a new instance of {@link DefaultSwingSQLContext}.
	 * @param connection The connection to be used.
	 * @param requestSynchronizer A synchronized to dispatch requests.
	 */
	public DefaultSwingSQLContext(Connection connection, ThreadSynchronizer requestSynchronizer){
		super(connection, requestSynchronizer, new SwingThreadSynchronizer());
	}

}
