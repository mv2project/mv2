package de.iss.mv2.io.sql;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * A database class that to load a database entry directly.
 * @author Marcel Singer
 * @deprecated This class comes from another project of mine and is not fully implemented. At the moment there is no reason to complete this class but it is still included in case there will be.
 */
@Deprecated
public class AutoLoadable extends Commitable {

	/**
	 * Creates a new instance of {@link AutoLoadable}.
	 * @param database The name of the database.
	 * @param table The name of the table.
	 */
	public AutoLoadable(String database, String table) {
		super(database, table);
		checkRequirements();
	}

	/**
	 * Crates a new instance of {@link AutoLoadable}.
	 * @param table The name of the table.
	 */
	public AutoLoadable(String table) {
		super(table);
		checkRequirements();
	}

	/**
	 * Creates a new instance of {@link AutoLoadable}.
	 * @param table The name of the table.
	 * @param databaseResult The database result to parse.
	 */
	public AutoLoadable(String table, ResultSet databaseResult) {
		super(table);
		checkRequirements();
		autoLoad(databaseResult);
	}

	/**
	 * Creates a new instance of {@link AutoLoadable}.
	 * @param database The name of the database.
	 * @param table The name of the table.
	 * @param databaseResult The database result to parse.
	 */
	public AutoLoadable(String database, String table, ResultSet databaseResult) {
		super(database, table);
		checkRequirements();
		autoLoad(databaseResult);
	}

	/**
	 * Loads the fields of this instance with the values of the given database response.
	 * @param rs The database response to parse.
	 */
	@SuppressWarnings("unchecked")
	protected void autoLoad(ResultSet rs) {
		List<TableField<?>> fields = getFields();
		Object currVal;
		for (TableField<?> f : fields) {
			try {
				currVal = rs.getObject(f.getColumnName());
				((TableField<Object>) f).set(currVal, true);
			} catch (SQLException ex) {

			}
		}
		setState();
	}

	/**
	 * Checks if all requirements to auto load from a database are met.
	 * @throws AutoLoadableConstructorException If there is no default load constructor.
	 */
	private final void checkRequirements() {
		if (!definesLoadConstructor())
			throw new AutoLoadableConstructorException(getClass());
	}

	/**
	 * Checks if the default load constructor is defined.
	 * @return {@code true} if the default load constructors.
	 */
	private final boolean definesLoadConstructor() {
		return getLoadConstructor(getClass()) != null;
	}

	/**
	 * Returns the default load constructor of the given class.
	 * A load constructor must be accessible and accept only a {@link ResultSet}. <br/>
	 * e.g. <plain>public Foo(ResultSet result){...</plain>
	 * @param loadClass The class thats default load constructor should be returned.
	 * @return The default load constructor of {@code null} if there is none.
	 */
	private static Constructor<?> getLoadConstructor(Class<? extends AutoLoadable> loadClass) {
		try {
			Constructor<?> constructor = loadClass.getConstructor(
					ResultSet.class);
			if (!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}
			return constructor;
		} catch (Throwable th) {
			return null;
		}

	}
	

	/**
	 * Loads a database entry and returns the created object.
	 * @param context The database context to connect to the database.
	 * @param classType The class of the object to create.
	 * @param settings The settings to use for creating the SELECT-command.
	 * @param args The select commands like: <br/>
	 * <plain>"columnName1", value1, "columnName2", value2, ... "columnNameN", valueN</plain>
	 * @return The loaded object.
	 */
	public static AutoLoadable load(SQLContext context, Class<? extends AutoLoadable> classType, CommandBuilderSettings settings, Object... args){
		Constructor<?> constructor = getLoadConstructor(classType);
		if(constructor == null) throw new AutoLoadableConstructorException(classType);
		StringBuilder sb = new StringBuilder();
		String qu = settings.getColumnQuote();
		sb.append("SELECT FROM " + qu);
		if(settings.useDatabasePrefix()){
			sb.append(settings.getDatabaseName() + qu + "." + qu);
		}
		return null;
	}
	
	

}
