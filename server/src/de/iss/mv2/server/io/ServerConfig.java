package de.iss.mv2.server.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;

import de.iss.mv2.io.PathBuilder;
import de.iss.mv2.server.ServerConstants;
import de.iss.mv2.server.data.DatabaseContext;

/**
 * A configuration holding the settings used during the server runtime.
 * 
 * @author Marcel Singer
 *
 */
public class ServerConfig extends Properties {

	/**
	 * The key for the database host name property.
	 */
	public static final String DB_HOST = "DB_HOST";

	/**
	 * The key for the database port property.
	 */
	public static final String DB_PORT = "DB_PORT";

	/**
	 * The key for the database name property.
	 */
	public static final String DB_NAME = "DB_NAME";

	/**
	 * The key for the database user property.
	 */
	public static final String DB_USER = "DB_USER";

	/**
	 * The key for the database users password property.
	 */
	public static final String DB_PASSWORD = "DB_PASSWORD";
	
	/**
	 * The key for the private key password property.
	 */
	public static final String PK_PASSWORD = "PK_PASSWORD";
	
	/**
	 * The key for the database connection string property.
	 */
	public static final String DB_CONNECTION_STRING = "DB_CONNECTION";
	
	/**
	 * The key class name of the JDBC driver to use.
	 */
	public static final String DB_DRIVER = "DB_DRIVER";

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -2578822746539890746L;
	
	/**
	 * Holds the configuration of the web spaces that are read only.
	 */
	private final ReadOnlyAccountsConfig readOnlyConfig = new ReadOnlyAccountsConfig();

	/**
	 * Creates a new instance of {@link ServerConfig}.
	 */
	public ServerConfig() {

	}

	/**
	 * Sets the host name of the database.
	 * 
	 * @param host
	 *            The host name to set.
	 * @throws IllegalArgumentException
	 *             If the given host name is {@code null} or empty.
	 */
	public void setDatabaseHost(String host) throws IllegalArgumentException {
		if (host == null || host.isEmpty())
			throw new IllegalArgumentException(
					"The database host must not be null or empty.");
		setProperty(DB_HOST, host);
	}

	/**
	 * Returns the host name of the database.
	 * 
	 * @return The host name of the database or {@code null} if there is none.
	 */
	public String getDatabaseHost() {
		return getProperty(DB_HOST);
	}

	/**
	 * Sets the port of the database.
	 * 
	 * @param port
	 *            The port to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given port is negative.
	 */
	public void setDatabasePort(int port) throws IllegalArgumentException {
		if (port < 0)
			throw new IllegalArgumentException("The port must not be negative.");
		setProperty(DB_PORT, "" + port);
	}

	/**
	 * Returns the port of the database.
	 * 
	 * @return The port of the database.
	 * @throws NoSuchElementException
	 *             Is thrown if the port was not set.
	 */
	public int getDatabasePort() throws NoSuchElementException {
		String portVal = getProperty(DB_PORT);
		if (portVal == null)
			throw new NoSuchElementException("The port was not set.");
		return Integer.parseInt(portVal);
	}

	/**
	 * Sets the name of the database.
	 * 
	 * @param databaseName
	 *            The name to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given database name is {@code null} or
	 *             empty.
	 */
	public void setDatabaseName(String databaseName)
			throws IllegalArgumentException {
		if (databaseName == null || databaseName.isEmpty())
			throw new IllegalArgumentException(
					"The database name must not be null or empty.");
		setProperty(DB_NAME, databaseName);
	}

	/**
	 * Returns the name of the database.
	 * 
	 * @return The name of the database or {@code null} if there is none.
	 */
	public String getDatabaseName() {
		return getProperty(DB_NAME);
	}

	/**
	 * Sets the database user.
	 * 
	 * @param user
	 *            The user to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given user is {@code null} or empty.
	 */
	public void setDatabaseUser(String user) throws IllegalArgumentException {
		if (user == null || user.isEmpty())
			throw new IllegalArgumentException(
					"The user must not be null or empty.");
		setProperty(DB_USER, user);
	}

	/**
	 * Returns the database user.
	 * 
	 * @return The database user or {@code null} if there is none.
	 */
	public String getDatabaseUser() {
		return getProperty(DB_USER);
	}

	/**
	 * Sets the password of the database user.
	 * 
	 * @param password
	 *            The password to set.
	 * @throws IllegalArgumentException
	 *             If thrown if the given password is {@code null}.
	 */
	public void setDatabaseUsersPassword(String password)
			throws IllegalArgumentException {
		setProperty(DB_PASSWORD, password);
	}

	/**
	 * Returns the password of the database user.
	 * 
	 * @return The password of the database user or {@code null} if there is
	 *         none.
	 */
	public String getDatabaseUsersPassword() {
		return getProperty(DB_PASSWORD);
	}
	
	/**
	 * Sets the password that was used to encrypt the private keys.
	 * @param password The password that was used to encrypt the private keys.
	 * @throws IllegalArgumentException Is thrown if the given password is {@code null}.
	 */
	public void setPrivateKeyPassword(String password){
		if(password == null) throw new IllegalArgumentException("The password must not be null.");
		setProperty(PK_PASSWORD, password);
	}
	
	/**
	 * Returns the password that was used to encrypt the private keys.
	 * @return The password that was used to encrypt the private keys or {@code null} if there is none. 
	 */
	public String getPrivateKeyPassword(){
		return getProperty(PK_PASSWORD);
	}

	/**
	 * Loads the configuration from the specified file.
	 * 
	 * @param f
	 *            The file to load.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public void load(File f) throws IOException {
		FileInputStream in = new FileInputStream(f);
		load(in);
		in.close();
		readOnlyConfig.loadOrIrgnore(new PathBuilder(f).getChildFile(ServerConstants.READ_ONLY_CONFIGURATION_FILE_NAME));
	}
	
	

	/**
	 * Writes this configuration to the specified file
	 * 
	 * @param f
	 *            The file to write.
	 * @throws IOException
	 */
	public void store(File f) throws IOException {
		FileOutputStream out = new FileOutputStream(f);
		store(out, null);
		out.flush();
		out.close();
	}
	
	/**
	 * Returns a new database context based on the settings of this configuration.
	 * @return A new database context.
	 */
	public DatabaseContext toDatabaseContext(){
		if(definesConnectionString()){
			return new DatabaseContext(getJDBCDriverName(), getConnectionString());
		}else{
			return new DatabaseContext(getDatabaseHost(), getDatabasePort(), getDatabaseName(), getDatabaseUser(), getDatabaseUsersPassword());
		}
	}
	
	/**
	 * Returns {@code true} if there is a value for the given settings key.
	 * @param key The key to test for a value.
	 * @return {@code true} if there is a value for the given settings key.
	 */
	public boolean defines(String key){
		return (getProperty(key, null) != null);
	}

	/**
	 * Returns {@code true} if this settings define a database connection string.
	 * @return {@code true} if this settings define a database connection string.
	 */
	public boolean definesConnectionString(){
		return defines(DB_CONNECTION_STRING);
	}
	
	/**
	 * Returns the database connection string.
	 * @return The database connection string.
	 */
	public String getConnectionString(){
		return getProperty(DB_CONNECTION_STRING, null);
	}
	
	/**
	 * Returns the name of the JDBC driver to use.
	 * @return The name of the JDBC driver to use.
	 */
	public String getJDBCDriverName(){
		return getProperty(DB_DRIVER, "org.postgresql.Driver");
	}
	
	/**
	 * Returns the configuration of the web spaces that are read only.
	 * @return The configuration of the web spaces that are read only.
	 */
	public ReadOnlyAccountsConfig getReadOnlyConfig(){
		return readOnlyConfig;
	}
	
	/**
	 * Returns an exemplary configuration.
	 * 
	 * @return An exemplary server configuration.
	 */
	public static ServerConfig createExample() {
		ServerConfig sc = new ServerConfig();
		sc.setDatabaseHost("localhost");
		sc.setDatabaseName("maildb");
		sc.setDatabasePort(815);
		sc.setDatabaseUser("testUser");
		sc.setDatabaseUsersPassword("testUsersPW");
		sc.setPrivateKeyPassword(ServerConstants.LOCALHOST_KEY_PASSWORD);
		return sc;
	}

}
