package de.iss.mv2.server.io.sql;

import java.sql.SQLException;

/**
 * Stellt ein SQL-Skript dar.
 * 
 * @author Marcel Singer
 *
 */
public abstract class SQLScript {
	/**
	 * Holds the connection to the database.
	 */
	private SQLContext context;

	/**
	 * Erstellt eine neue Instanz von {@link SQLScript}.
	 * 
	 * @param context
	 *            Das für den Datenbankzugriff zu verwendende Zugriffskontext.
	 */
	public SQLScript(SQLContext context) {
		this.context = context;
	}

	/**
	 * Führt das Skript aus.
	 * 
	 * @throws SQLException
	 *             Tritt bei SQL-Problemen während der Ausführung auf.
	 */
	public abstract void run() throws SQLException;

	/**
	 * Gibt den zu verwendenden Zugriffskontext zurück.
	 * 
	 * @return Der zu verwendende Zugriffskontext.
	 */
	protected SQLContext getAccessContext() {
		return context;
	}

}
