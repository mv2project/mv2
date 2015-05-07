package de.iss.mv2.io.sql;

import java.sql.Connection;

/**
 * A {@link SQLContext} that sequentially executes requests and responses.
 * 
 * @author Marcel Singer
 *
 */
public class SequentialSQLContext extends DefaultSQLContext {

	/**
	 * Creates a new instance of {@link SequentialSQLContext}.
	 * 
	 * @param connection
	 *            The connection to use.
	 */
	public SequentialSQLContext(Connection connection) {
		super(connection, new SequentialThreadSynchronizer(),
				new SequentialThreadSynchronizer());
	}

}
