package de.iss.mv2.server.io.sql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Stellt ein Objekt dar, dessen Tabellen-Felder zustandssensitiev sind.
 * 
 * @author singer
 * 
 */
public abstract class ChangeSensetiveObject extends Observable implements
		Observer, CommandBuilderSettings {

	
/**
 * Holds the name of the database.
 */
	private final String databaseName;
	/**
	 * Holds the name of the table.
	 */
	private final String tableName;
	/**
	 * Hold a list of table fields.
	 */
	private final List<TableField<?>> registeredFields = new ArrayList<TableField<?>>();

	/**
	 * Erstellt eine neue Instanz von {@link ChangeSensetiveObject}.
	 * 
	 * @param database
	 *            Gibt den Namen der Datenbank an.
	 * @param table
	 *            Gibt den Namen der Tabelle an.
	 */
	public ChangeSensetiveObject(String database, String table) {
		this.databaseName = database;
		this.tableName = table;
	}

	/**
	 * Creates a new instance of {@link ChangeSensetiveObject}.
	 * @param table The name of the table.
	 */
	public ChangeSensetiveObject(String table){
		this.databaseName = "";
		this.tableName = table;
		useDatabaseName = false;
	}
	
	
	@Override
	public String getDatabaseName() {
		return databaseName;
	}


	@Override
	public String getTableName() {
		return tableName;
	}
	
	
	/**
	 * Ruft alle Tabellen-Felder der aktuellen Instanz ab.
	 * 
	 * @return Die akutellen Tabellen-Felder dieser Instanz.
	 */
	protected final List<TableField<?>> getFields() {
		ArrayList<TableField<?>> result = new ArrayList<>();
		Field[] fields = getAllFields(getClass(), TableField.class);
		for (Field f : fields) {
			f.setAccessible(true);

			try {
				TableField<?> tf = (TableField<?>) f.get(this);
				result.add(tf);
			} catch (Exception e) {

			}

		}
		return result;
	}

	/**
	 * Selektiert alle Tabellen-Felder, die sich verändert haben.
	 * 
	 * @param fields
	 *            Die zur Selektion zu verwendende Liste.
	 * @return Eine nue Liste mit den sich geänderten Tabellen-Feldern.
	 */
	protected final List<TableField<?>> getChanged(List<TableField<?>> fields) {
		ArrayList<TableField<?>> result = new ArrayList<>();
		for (TableField<?> f : fields) {
			if (f.hasChanged())
				result.add(f);
		}
		return result;
	}

	/**
	 * Selektiert alle Primärschlüssel.
	 * 
	 * @param fields
	 *            De zur Selektion zu verwendende Liste.
	 * @return Eine Liste mit den Tabellen-Feldern die den Primärschlüssel
	 *         bilden.
	 */
	protected final List<TableField<?>> getPKs(List<TableField<?>> fields) {
		ArrayList<TableField<?>> result = new ArrayList<>();
		for (TableField<?> f : fields) {
			if (f.isPK())
				result.add(f);
		}
		return result;
	}

	/**
	 * Übernimmt alle Änderungen.
	 */
	protected void setState() {
		Field[] fields = getAllFields(getClass(), TableField.class);
		for (Field f : fields) {
			f.setAccessible(true);
			try {
				TableField<?> tf = (TableField<?>) f.get(this);
				tf.saveState();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Macht alle Änderungen rückgängig.
	 */
	protected void revertChanges() {
		List<TableField<?>> fields = getFields();
		for (TableField<?> f : fields) {
			f.revertChanges();
		}
	}

	/**
	 * Ruft sich geänderte Tabellen-Felder ab.
	 * 
	 * @return Eine Liste mit den sich veränderten Tabellen-Feldern.
	 */
	protected final List<TableField<?>> getChangedFields() {
		ArrayList<TableField<?>> result = new ArrayList<>();
		Field[] fields = getAllFields(getClass(), TableField.class);
		for (Field f : fields) {
			f.setAccessible(true);
			try {
				TableField<?> tf = (TableField<?>) f.get(this);
				if (tf.hasChanged())
					result.add(tf);
			} catch (Exception e) {

			}
		}
		return result;
	}

	/**
	 * Ruft alle deklarierten Instanzvariablen einer angegebenen Klasse und
	 * deren Superklassen ab.
	 * 
	 * @param cl
	 *            Die Klasse deren Instanzvariablen abgerufen werden sollen.
	 * @param ofType
	 *            Gibt den Typ der zu selektierenden Instanzvariablen an.
	 * @return Eine Array mit den Instanzvariablen.
	 */
	private static Field[] getAllFields(Class<?> cl, Class<?> ofType) {
		ArrayList<Field> field = new ArrayList<>();
		Field[] current = cl.getDeclaredFields();
		for (Field f : current) {
			if (ofType.isAssignableFrom(f.getType())) {
				field.add(f);
			}
		}
		if (cl.getSuperclass() != null) {
			field.addAll(Arrays.asList(getAllFields(cl.getSuperclass(), ofType)));
		}
		return field.toArray(new Field[field.size()]);
	}

	/**
	 * Gibt an ob sich die Tabellen-Felder dieses Objekts geändert haben.
	 */
	@Override
	public boolean hasChanged() {
		if (super.hasChanged())
			return true;
		return getChangedFields().size() > 0;
	}

	/**
	 * Gibt ein Array mit zusätzlich zu berücksichtigenden Tabellen-Feldern
	 * zurück.
	 * 
	 * @return Ein Array mit zusätlich zu berücksichtigenden Tabellen-Feldern.
	 */
	protected TableField<?>[] getAdditionalUpdateFields() {
		return new TableField<?>[0];
	}

	/**
	 * Ertsellt einen SQL-DELETE-Command anhand des aktuellen Zustands des
	 * Objekts.
	 * 
	 * @param conn
	 *            Die zu verwendende SQL-Verbindung.
	 * @return Der vorbereitete SQL-DELETE-Command.
	 * @throws SQLException If an SQL error occurs.
	 */
	protected PreparedStatement getDeleteCommand(Connection conn) throws SQLException {
		List<TableField<?>> pks = getPKs(getFields());
		Map<Integer, Object> indexValueMap = new HashMap<>();
		List<TableField<?>> changed = getChanged(getFields());
		StringBuilder sb = new StringBuilder();
		String quote = getColumnQuote();
		sb.append("DELETE FROM " + quote);
		if(useDatabasePrefix()){
			sb.append(databaseName + quote + "." + quote);
		}
		sb.append(tableName + quote + " WHERE ");
		int indetC = 0;
		boolean firstPK = true;
		for (TableField<?> pk : pks) {
			if (!firstPK) {
				sb.append(" AND ");
			} else {
				firstPK = false;
			}
			indetC++;
			sb.append(quote + pk.getColumnName() + quote + "=?");
			if (changed.contains(pk)) {
				indexValueMap.put(indetC, pk.getSavedStateForInsert());
			} else {
				indexValueMap.put(indetC, pk.getForInsert());
			}
		}
		sb.append(";");
		PreparedStatement ps = conn.prepareStatement(sb.toString());
		for(Integer index : indexValueMap.keySet()){
			ps.setObject(index, indexValueMap.get(index));
		}
		return ps;
	}

	/**
	 * Erstellt einen SQL-UPDATE-Command anhand des aktuellen Zustands des
	 * Objekts.
	 * 
	 * @param conn
	 *            Die zu verwendende SQL-Verbindung.
	 * @return Der vorbereitete SQL-UPDATE-Command.
	 * @throws SQLException If an SQL error occurs.
	 */
	protected PreparedStatement getUpdateCommand(Connection conn)
			throws SQLException {
		List<TableField<?>> fields = getFields();
		List<TableField<?>> changed = getChanged(fields);
		changed.addAll(Arrays.asList(getAdditionalUpdateFields()));
		if (changed.size() == 0)
			return null;
		List<TableField<?>> pks = getPKs(fields);
		Map<Integer, Object> indexValueMap = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		int indetC = 0;
		String quote = getColumnQuote();
		sb.append("UPDATE " + quote);
		if(useDatabasePrefix()){
			sb.append(databaseName + quote + "." + quote);
		}
		sb.append(tableName + quote + " SET ");
		for (TableField<?> f : changed) {
			if (indetC != 0) {
				sb.append(", ");
			}
			indetC++;
			sb.append(quote + f.getColumnName() + quote +  "=?");
			indexValueMap.put(indetC, f.getForInsert());
		}
		sb.append(" WHERE ");
		boolean firstPK = true;
		for (TableField<?> pk : pks) {
			if (!firstPK) {
				sb.append(" AND ");
			} else {
				firstPK = false;
			}
			indetC++;
			sb.append(quote + pk.getColumnName() + quote + "=?");
			if (changed.contains(pk)) {
				indexValueMap.put(indetC, pk.getSavedStateForInsert());
			} else {
				indexValueMap.put(indetC, pk.getForInsert());
			}
		}
		sb.append(";");
		PreparedStatement ps = conn.prepareStatement(sb.toString());
		for (Integer index : indexValueMap.keySet()) {
			ps.setObject(index, indexValueMap.get(index));
		}
		return ps;
	}

	
	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
		List<TableField<?>> currentFields = getFields();
		List<TableField<?>> toDelete = new ArrayList<>();

		for (TableField<?> tf : registeredFields) {
			if (!currentFields.contains(tf)) {
				tf.deleteObserver(this);
				toDelete.add(tf);
			}
		}
		for (TableField<?> tf : toDelete) {
			registeredFields.remove(tf);
		}
		toDelete.clear();
		for (TableField<?> tf : currentFields) {
			if (!registeredFields.contains(tf)) {
				registeredFields.add(tf);
				tf.addObserver(this);
			}
		}
	}

	
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
		clearChanged();
	}

	/**
	 * Prüft ob die Tabellenspalten gleich sind.
	 * 
	 * @param other
	 *            Gibt das zweite Objekt an.
	 * @param ignorePK
	 *            Gibt an ob PrimaryKeys ignoriert werden sollen.
	 * @return {@code true}, wenn die Tabellenspalten dieses und des angegebenen
	 *         Objekts gleich sind.
	 */
	public boolean isEqualTo(ChangeSensetiveObject other, boolean ignorePK) {
		if (!getClass().isAssignableFrom(other.getClass()))
			return false;
		List<TableField<?>> myFields = getFields();
		List<TableField<?>> otherFields = other.getFields();
		for (TableField<?> tf : myFields) {
			if (!checkField(tf, otherFields, ignorePK))
				return false;
		}
		return true;
	}

	/**
	 * Checks if the given field is a duplicate.
	 * @param field The field to check.
	 * @param others A list with fields to check with.
	 * @param ignorePK {@code true} if field to check should be ignored if it is a primary key.
	 * @return {@code true} if a duplicate was found or the given field is a primary key and primary keys should be ignored.
	 */
	private boolean checkField(TableField<?> field, List<TableField<?>> others,
			boolean ignorePK) {
		if (field.isPK() && ignorePK)
			return true;
		for (TableField<?> oF : others) {
			if (field.equalsTo(oF))
				return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object other){
		if(ChangeSensetiveObject.class.isAssignableFrom(other.getClass())){
			if(isEqualTo((ChangeSensetiveObject) other, false)) return true;
		}
		return super.equals(other);
	}
	
	/**
	 * Defines if the table name should be preceeded by the database name.
	 */
	private boolean useDatabaseName = true;
	
	@Override
	public boolean useDatabasePrefix(){
		return useDatabaseName;
	}
	/**
	 * Defines the quotes to use for column, table and database names.
	 */
	private String columnQuote = "`";
	@Override
	public String getColumnQuote(){
		return columnQuote;
	}

}
