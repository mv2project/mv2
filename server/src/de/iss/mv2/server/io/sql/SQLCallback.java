package de.iss.mv2.server.io.sql;

import java.sql.SQLException;

/**
 * Ein Callback für asynchron ausgeführte Datenbankanfragen.
 * @author singer
 *
 * @param <T> Gibt den Ergebnistyp des Aufrufs an.
 */
public interface SQLCallback<T> extends RequestCallback<T, SQLException> {
}
