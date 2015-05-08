package de.iss.mv2.server.io.sql;

import java.sql.SQLException;

/**
 * Hilfsklasse zur asynchronen Ausführung von Datenbank-Aufrufen.
 * 
 * @author singer
 *
 * @param <T>
 *            Der Ergebnistyp des Datenbank-Aufrufs.
 */
public class SQLScheduler<T> {

	/**
	 * Holds the context to communicate with a database.
	 */
	private final SQLContext context;

	/**
	 * A constant defining the time to wait before a request is executed.
	 */
	private final int SLEEP_LENGTH = 0000;

	/**
	 * Erstellt eine neue Instanz des {@link SQLScheduler}'s.
	 * 
	 * @param context
	 *            Gibt den Zugriffskontext an.
	 */
	public SQLScheduler(SQLContext context) {
		this.context = context;
	}

	/**
	 * Führt eine Anfrage asynchron aus.
	 * 
	 * @param request
	 *            Die asynchron auszuführende Anfrage.
	 */
	public void runAsync(SQLAction<T> request) {
		 runAsync(request, null);
	}

	/**
	 * Führt eine Anfrage asynchron aus und ruft bei auftretenden Ereignissen
	 * den angegebenen Callback auf.
	 * 
	 * @param request
	 *            Die asynchron auszuführende Anfrage.
	 * @param callback
	 *            Der Callback der bei auftretenden Ereignissen aufgerufen
	 *            werden soll. Der Methoden des Callacks werden innerhalb des
	 *            Event-Dispatcher-Thread aufgerufen.
	 */
	public void runAsync(final SQLAction<T> request,
			final SQLCallback<T> callback) {

		context.getRequestSynchronizer().execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(SLEEP_LENGTH);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				T result = null;
				try {
					result = request.perform(context.getConnection());
				} catch (SQLException e) {
					callException(e, callback);
					return;
				}
				callResult(result, callback);
			}
		});
	}

	/**
	 * Führt den Ergebnis-Aufruf des Callbacks durch.
	 * 
	 * @param result
	 *            Das zu übergebende Ergebnis,
	 * @param callback
	 *            Der aufzurufende Callback.
	 */
	private void callResult(final T result, final SQLCallback<T> callback) {
		System.out.println("SQL-Request completed with: " + result);
		if (callback != null) {
			context.getResponseSynchronizer().execute(new Runnable() {
				@Override
				public void run() {
					callback.requestCompleted(result);
				}
			});

		}
	}

	/**
	 * Führt den Fehler-Aufruf des Callbacks durch.
	 * 
	 * @param e
	 *            Die zu übergebende Exception.
	 * @param callback
	 *            Der aufzurufende Callback.
	 */
	private void callException(final SQLException e,
			final SQLCallback<T> callback) {
		if (callback != null) {
			context.getResponseSynchronizer().execute(new Runnable() {
				@Override
				public void run() {
					callback.requestFailed(e);
				}
			});

		}
	}

}
