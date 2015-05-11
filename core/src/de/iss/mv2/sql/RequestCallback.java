package de.iss.mv2.sql;

/**
 * Stellt einen Callback für asynchron durchgeführte Anfragen zur Verfügung.
 * @author singer
 *
 * @param <T> Der Typ des regulären Ergebnis.
 * @param <E> Der Typ einer evtl. auftretenden Exception.
 */
public interface RequestCallback<T, E extends Exception> {

	/**
	 * Wird aufgerufen, wenn die Aktion erfolgreich abgeschlossen wurde.
	 * @param result Das Ergebnis der durchgeführten Aktion.
	 */
	void requestCompleted(T result);
	/**
	 * Wird aufgerufen, wenn während der Durchführung ein Fehler aufgetreten ist.
	 * @param ex Die aufgetretene Exception.
	 */
	void requestFailed(E ex);
	
}
