package de.iss.mv2.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Stellt eine Datenbank-Anfrage dar.
 * @author singer
 *
 * @param <T> Gibt den Ergebnistyp der Datenbank-Anfrage an.
 */
public interface SQLAction<T> {
	
	/**
	 * Führt die Datenbank-Anfrage durch.
	 * @param con Gibt die zu verwendende Datenbank-Verbindung an.
	 * @return Das Ergebnis der Datenbank-Anfrage.
	 * @throws SQLException Tritt bei Fehlern während der Datenbank-Anfrage auf.
	 */
	T perform(Connection con) throws SQLException;

}
