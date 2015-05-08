package de.iss.mv2.server.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.iss.mv2.server.io.sql.SQLContext;
import de.iss.mv2.server.io.sql.SequentialSQLContext;

/**
 * Provides a {@link SQLContext} to connect to the servers database.
 * @author Marcel Singer
 *
 */
public class DatabaseContext extends SequentialSQLContext {

	/**
	 * The host of the database intended to use for testing purposes.
	 */
	private static final String TEST_HOST = "shome1.selfhost.eu";
	
	/**
	 * Test user to connect to the database sever intended for testing purposes.
	 */
	private static final String TEST_USER = "mv2user";
	/**
	 * Password of the test user.
	 */
	private static final String TEST_PW = "test123kss!";
	
	/**
	 * The port of the test host.
	 */
	private static final int TEST_PORT = 5432;

	/**
	 * Holds the database context to use during the current execution.
	 */
	private static final DatabaseContext instance = new DatabaseContext();

	/**
	 * Creates a new instance of {@link DatabaseContext} using the test database.
	 */
	private DatabaseContext() {
		super(createConnection());

	}

	/**
	 * Returns the database context to use during the current execution.
	 * @return The database context to use during the current execution.
	 */
	public static DatabaseContext getContext() {
		return instance;
	}

	/**
	 * Creates a SQL connection to the specified PostgreSQL host.
	 * @param host The address of the host.
	 * @param port The port of the host.
	 * @param databse The database to connect to.
	 * @param username The userame to use.
	 * @param password The password to use.
	 * @return An open connection to the specified host.
	 * @throws RuntimeException If the connection can not be created.
	 */
	private static Connection createConnection(String host, int port,
			String databse, String username, String password) throws RuntimeException {
		try {
			Class.forName("org.postgresql.Driver");
			String connectionString = "jdbc:postgresql://" + host + ":" + port
					+ "/" + databse + "?user=" + username + "&password="
					+ password;
			Connection con = DriverManager.getConnection(connectionString);
			return con;
		} catch (Throwable th) {
			throw new RuntimeException(th);
		}

	}

	/**
	 * Creates a SQL connection to the host intended for testing purposes.
	 * @return An open connection to the test host.
	 * @throws RuntimeException If the connection can not be created.
	 */
	private static Connection createConnection() throws RuntimeException{
		return createConnection(TEST_HOST, TEST_PORT, DynamicSQLSettings
				.getInstance().getDatabaseName(), TEST_USER, TEST_PW);
	}

	@Override
	public Connection getConnection() {
		Connection con = super.getConnection();
		try {
			if (con.isClosed()) {
				con = createConnection();
				setConnection(con);
			}
		} catch (SQLException e) {

		}
		return con;
	}

}
