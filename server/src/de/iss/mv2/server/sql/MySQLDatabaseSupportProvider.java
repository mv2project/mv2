package de.iss.mv2.server.sql;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * A {@link DatabaseSupportProvider} for MySQL servers.
 * @author Marcel Singer
 *
 */
public class MySQLDatabaseSupportProvider implements DatabaseSupportProvider {

	/**
	 * The identifier of this provider.
	 */
	private static final String IDENTIFIER = "mysql";
	
	/**
	 * Creates a new instance of {@link MySQLDatabaseSupportProvider}.
	 */
	public MySQLDatabaseSupportProvider() {
		
	}

	@Override
	public String getDatabaseTypeIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public InputStreamReader getDDLReader() {
		return new InputStreamReader(getClass().getClassLoader().getResourceAsStream("ddls/mysql.sql"), StandardCharsets.UTF_8);
	}

	@Override
	public String createConnectionString(String host, int port,
			String database, String user, String password) {
		return "jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password;
 	}

	@Override
	public void registerRequiredDrivers() {
		
		
	}

}
