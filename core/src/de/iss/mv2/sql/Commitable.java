package de.iss.mv2.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


/**
 * Stellt eine Klasse mit Methoden zur automatisierten Sicherung von geänderten
 * Attributen zur Verfügung. Näheres dazu findet sich in der Dokumentation von
 * {@link Commitable#commitChanges(SQLContext, SQLCallback)}.
 * 
 * @author Patrick Kellermannn, Oliver Schadrin, Marcel Singer
 * 
 */
public abstract class Commitable extends ChangeSensetiveObject {

	/**
	 * Legt eine neue Instanz von {@link Commitable} an.
	 * 
	 * @param database
	 *            Gibt den Namen der zur Sicherung zu verwendenden Datenbank an.
	 * @param table
	 *            Gibt den Namen der zur Sicherung zu verwendenden
	 *            Datenbank-Tabelle an.
	 */
	public Commitable(String database, String table) {
		super(database, table);
	}

	/**
	 * Creates a new instance of {@link Commitable}.
	 * @param table The name of the table.
	 */
	public Commitable(String table){
		super(table);
	}
	
	/**
	 * Diese Methode wird vor der Erzeugung des UPDATE-Befehls (jedoch nach der
	 * Prüfung auf Änderungen) aufgerufen. Sie kann von Kind-Klassen
	 * überschrieben werden, um z.B. Validitätsprüfungen zu realisieren.
	 * */
	protected void preCommit() {

	}

	/**
	 * Diese Methode wird vor der Prüfung auf Änderungen aufgerufen. Sie kann
	 * von Kind-Klassen überschrieben werden, um z.B. externe Änderungen zu
	 * übernehmen.
	 */
	protected void preChecks() {

	}

	/**
	 * Diese Methode wird nach der erfolgreichen Ausführung aufgerufen und kann
	 * von Kind-Klassen überschrieben werden um weitere Aktionen im Zuge des
	 * Commits zu realisieren.
	 */
	protected void postCommit() {

	}

	/**
	 * Prüft das aktuelle Objekt auf Änderungen und führt einen
	 * dementsprechenden SQL-Update-Befehl aus.<br />
	 * <br />
	 * Der Ablauf zur Ermittlung des SQL-Update-Befehls ist folgendermaßen
	 * skizziert:
	 * <table>
	 * <tr>
	 * <td><b>Schritt</b></td>
	 * <td><b>Beschreibung</b></td>
	 * <td><b>Notizen</b></td>
	 * </tr>
	 * <tr>
	 * <td>1.</td>
	 * <td>Aufruf von {@link Commitable#preChecks()}</td>
	 * <td>Kind-Klassen haben mit dem Überschreiben dieser Methode z.B. die
	 * Möglichkeit externe Abhängigkeiten zu übernehmen.</td>
	 * </tr>
	 * <tr>
	 * <td>2.</td>
	 * <td>Aufruf von {@link Commitable#hasChanged()}</td>
	 * <td>Es wird geprüft ob sich das Objekt seit dem letzten Abruf/Commit
	 * verändert hat. Ist dies nicht der Fall, wird sofort die
	 * {@link SQLCallback#requestCompleted(Object)}-Methode aufgerufen und das
	 * Commit abgebrochen.</td>
	 * </tr>
	 * <tr>
	 * <td>3.</td>
	 * <td>Aufruf von {@link Commitable#preCommit()}</td>
	 * <td>Kind-Klassen haben mit dem Überschreiben dieser Methode z.B. die
	 * Möglichkeit Validitätsprüfungen durchzuführen.</td>
	 * </tr>
	 * <tr>
	 * <td>4.</td>
	 * <td>Generierung des SQL-Update-Befehls<br/>
	 * ({@link Commitable#getUpdateCommand(Connection)})</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>5.</td>
	 * <td>Asynchrones ausführen des Updates</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>6.</td>
	 * <td>Aktuellen Status des Objekts als gesichert übernehmen</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>7.</td>
	 * <td>Aufruf von {@link Commitable#postCommit()}</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * 
	 * @param context
	 *            Gibt den zum Datenbankzugriff zu verwendenden Zugriffskontext
	 *            an.
	 * @param callback
	 *            Gibt den nach Abschluss aufzurufenden Callback an. Bei einem
	 *            erfolgreichen Abschluss wird als Ergebnis-Parameter der
	 *            {@link SQLCallback#requestCompleted(Object)}-Methode die
	 *            aktuelle Instanz übergeben.
	 */
	public void commitChanges(final SQLContext context,
			final SQLCallback<Object> callback) {
		preChecks();
		if (!hasChanged()) {
			callback.requestCompleted(this);
			return;
		}
		preCommit();
		SQLScheduler<Object> scheduler = new SQLScheduler<>(context);
		scheduler.runAsync(new SQLAction<Object>() {

			@Override
			public Object perform(Connection con) throws SQLException {

				PreparedStatement ps = getUpdateCommand(con);
				ps.execute();
				setState();
				postCommit();
				return getInstance();
			}
		}, callback);
	}

	/**
	 * Prüft die angegebenen Objekte auf Änderungen und führt jedes einen entsprechenden SQL-Update-Befehl aus. Siehe {@link Commitable#commitChanges(SQLContext, SQLCallback)}.
	 * @param context Gibt den zum Datenbankaufruf zu verwendenden Zugriffskontext an.
	 * @param elements Eine Liste mit Elementen für die das Commit durchgeführt werden soll.
	 * @param callback Gibt den Callback an, der bei Abschluss aufgerufen werden soll.
	 */
	public static void commitChanges(final SQLContext context,
			final List<Commitable> elements, final SQLCallback<Object> callback) {
		final Connection con = context.getConnection();
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				for (Commitable c : elements) {
					try {
						c.preChecks();
						if (!c.hasChanged())
							continue;
						c.preCommit();
						PreparedStatement ps = c.getUpdateCommand(con);
						ps.execute();
						c.setState();
						c.postCommit();
					} catch (final SQLException ex) {
						context.getResponseSynchronizer().execute(new Runnable() {

							@Override
							public void run() {
								callback.requestFailed(ex);
							}
						});
						return;
					}
				}
				context.getResponseSynchronizer().execute(new Runnable() {

					@Override
					public void run() {
						callback.requestCompleted(new Object());
					}
				});
			}
		});
		th.start();
	}
	
	/**
	 * Entfernt das Objekt aus der Datenbank.
	 * @param context Gibt den für den Datenbankzugriff zu verwendenden Zugriffskontext an.
	 * @param callback Gibt den bei Abschluss aufzurufenden Callback an.
	 */
	public void delete(SQLContext context, SQLCallback<Boolean> callback){
		SQLScheduler<Boolean> scheduler = new SQLScheduler<>(context);
		scheduler.runAsync(new SQLAction<Boolean>() {

			@Override
			public Boolean perform(Connection con) throws SQLException {
				PreparedStatement ps = getDeleteCommand(con);
				ps.execute();
				return true;
			}
		}, callback);
	}

	/**
	 * Returns this instance.
	 * @return This instance.
	 */
	private Object getInstance() {
		return this;
	}

}
