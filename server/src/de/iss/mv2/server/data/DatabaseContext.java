package de.iss.mv2.server.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.iss.mv2.sql.SQLContext;
import de.iss.mv2.sql.SequentialSQLContext;

/**
 * Provides a {@link SQLContext} to connect to the servers database.
 * 
 * @author Marcel Singer
 *
 */
public class DatabaseContext extends SequentialSQLContext {

	/**
	 * Holds the address of the database host.
	 */
	private final String host;

	/**
	 * Holds the name of the database.
	 */
	private final String database;

	/**
	 * Holds the user of the database.
	 */
	private final String user;

	/**
	 * Holds the password of the database.
	 */
	private final String password;

	/**
	 * Holds the port of the database.
	 */
	private final int port;

	/**
	 * Holds the database context to use during the current execution.
	 */
	private static volatile DatabaseContext instance;

	/**
	 * Creates a new database context with the given connection parameters.
	 * 
	 * @param host
	 *            The address of the database host.
	 * @param port
	 *            The port of database.
	 * @param database
	 *            The database name to connect to.
	 * @param user
	 *            The user to use for the connection.
	 * @param password
	 *            The users password.
	 */
	public DatabaseContext(String host, int port, String database, String user,
			String password) {
		super(createConnection(host, port, database, user, password));
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
	}

	/**
	 * Returns the database context to use during the current execution.
	 * 
	 * @return The database context to use during the current execution.
	 * @throws IllegalStateException
	 *             Is thrown if the default context was not set before.
	 */
	public static synchronized DatabaseContext getContext()
			throws IllegalStateException {
		if (instance == null)
			throw new IllegalStateException("The default context was not set.");
		return instance;
	}

	/**
	 * Sets the default context to use.
	 * 
	 * @param context
	 *            The context to set.
	 */
	public static synchronized void setContext(DatabaseContext context) {
		if (instance != null) {
			try {
				instance.getConnection().close();
			} catch (SQLException ex) {

			}
		}
		instance = context;
	}

	/**
	 * Creates a SQL connection to the specified PostgreSQL host.
	 * 
	 * @param host
	 *            The address of the host.
	 * @param port
	 *            The port of the host.
	 * @param database
	 *            The database to connect to.
	 * @param username
	 *            The username to use.
	 * @param password
	 *            The password to use.
	 * @return An open connection to the specified host.
	 * @throws RuntimeException
	 *             If the connection can not be created.
	 */
	private static Connection createConnection(String host, int port,
			String database, String username, String password)
			throws RuntimeException {
		try {
			Class.forName("org.postgresql.Driver");
			String connectionString = "jdbc:postgresql://" + host + ":" + port
					+ "/" + database + "?user=" + username + "&password="
					+ password;
			Connection con = DriverManager.getConnection(connectionString);
			return con;
		} catch (Throwable th) {
			throw new RuntimeException(th);
		}

	}

	/**
	 * Creates a SQL connection to the host intended for testing purposes.
	 * 
	 * @return An open connection to the test host.
	 * @throws RuntimeException
	 *             If the connection can not be created.
	 */
	private Connection createConnection() throws RuntimeException {
		return createConnection(host, port, database, user, password);
	}

	@Override
	public Connection getConnection() {
		Connection con = super.getConnection();
		try {
			if (con == null || con.isClosed() || !con.isValid(3)) {
				try {
					if (con != null)
						con.close();
				} catch (SQLException e) {

				}
				con = createConnection();
				setConnection(con);
			}
		} catch (SQLException e) {

		}
		return con;
	}

}
