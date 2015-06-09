package de.iss.mv2.server.sql;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * This is a helper-class to install the basic database structure.
 * 
 * @author Marcel Singer
 *
 */
public class DatabaseInstaller {

	/**
	 * The database support provider to use.
	 */
	private final DatabaseSupportProvider dsp;
	/**
	 * The host of the server to connect to.
	 */
	private final String host;
	/**
	 * The port of the server to connect to.
	 */
	private final int port;
	/**
	 * The name of the database to connect to.
	 */
	private final String database;
	/**
	 * The name of the user to be used.
	 */
	private final String user;
	/**
	 * The password to be used.
	 */
	private final String password;

	/**
	 * The output.
	 */
	private PrintWriter output = new PrintWriter(new ByteArrayOutputStream());

	/**
	 * Creates a new instance of {@link DatabaseInstaller}.
	 * 
	 * @param provider
	 *            The provider to be used.
	 * @param host
	 *            The name of the host.
	 * @param port
	 *            The port of the server.
	 * @param database
	 *            The database to connect to.
	 * @param user
	 *            The name of the user.
	 * @param password
	 *            The password.
	 */
	public DatabaseInstaller(DatabaseSupportProvider provider, String host,
			int port, String database, String user, String password) {
		this.dsp = provider;
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
	}

	/**
	 * Sets the writer which is used to printout log information.
	 * 
	 * @param writer
	 *            The writer to set.
	 * @throws IllegalArgumentException
	 *             If the given writer is {@code null}.
	 */
	public void setOutputLogg(PrintWriter writer)
			throws IllegalArgumentException {
		if (writer == null)
			throw new IllegalArgumentException("The writer must not be null.");
		this.output = writer;
	}

	/**
	 * The database placeholder.
	 */
	private static final String DB_NAME_PLACEHOLDER = "@databaseName";

	/**
	 * Tries to setup the initial database structure.
	 */
	public void install() {
		String lineSeperator = System.getProperty("line.separator");
		output.println("Reading the DDL-script...");
		output.flush();
		StringBuilder sb = new StringBuilder();
		Scanner sc = new Scanner(dsp.getDDLReader());
		String line;
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			if (line.isEmpty() || line.startsWith("#"))
				continue;
			sb.append(line);
			sb.append(lineSeperator);
		}
		sc.close();
		String allCommands = sb.toString()
				.replace(DB_NAME_PLACEHOLDER, database)
				.replace(lineSeperator, "");
		String[] commands = allCommands.split(";");
		for (int i = 0; i < commands.length; i++) {
			commands[i] = commands[i].trim();
		}
		output.println("Connecting to the database...");
		output.flush();
		Connection con = null;
		try {
			con = DriverManager.getConnection(dsp.createConnectionString(host,
					port, database, user, password));
			PreparedStatement ps;
			output.println("Executing the DDL-scipt...");
			output.flush();
			for (String command : commands) {
				output.println(command + ";");
				output.flush();
				ps = con.prepareStatement(command + ";");
				ps.execute();
			}
		} catch (SQLException ex) {
			ex.printStackTrace(output);
			output.flush();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e2) {
					e2.printStackTrace(output);
				}
			}
		}

	}

}
