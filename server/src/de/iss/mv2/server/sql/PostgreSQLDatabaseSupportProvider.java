package de.iss.mv2.server.sql;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A {@link DatabaseSupportProvider} for PostgreSQL-databases.
 * @author Marcel Singer
 *
 */
public class PostgreSQLDatabaseSupportProvider implements DatabaseSupportProvider {
	
	/**
	 * The constant containing the identifier of this provider.
	 */
	private static final String DB_TYPE = "postgreSQL";

	/**
	 * Creates a new instance of this provider.
	 */
	public PostgreSQLDatabaseSupportProvider() {
	}

	@Override
	public String getDatabaseTypeIdentifier() {
		return DB_TYPE;
	}

	@Override
	public InputStreamReader getDDLReader() {
		return new InputStreamReader(getClass().getClassLoader().getResourceAsStream("ddls/postgresql.sql"), StandardCharsets.UTF_8);
	}

	@Override
	public String createConnectionString(String host, int port,
			String database, String user, String password) {
		return "jdbc:postgresql://" + host + ":" + port
			+ "/" + database + "?user=" + user + "&password=" + password;
	}

	@Override
	public void registerRequiredDrivers() {
		try {
			DriverManager.registerDriver(new org.postgresql.Driver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
