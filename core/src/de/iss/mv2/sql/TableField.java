package de.iss.mv2.sql;

import java.util.Observable;
import java.util.Observer;

/**
 * Kapselt einen Wert zusammen mit Spalten und Zustandsinformationen.
 *
 * @param <T> Gibt den Typ des zu kapselnden Werts an.
 */
public class TableField<T> extends Observable implements Observer {
	/**
	 * Holds the value of this field.
	 */
	private T value;
	/**
	 * Holds the last committed state of this field.
	 */
	private T savedState;
	/**
	 * Holds the name of the corresponding column.
	 */
	private final String columnName;
	/**
	 * Indicates if this field is a primary key.
	 */
	private boolean isPK = false;
	/**
	 * Holds an object to resolve the value.
	 */
	private ReferenceValueResolver<T> resolver;

	/**
	 * Erstellt eine neue Instanz von {@link TableField} unter Angabe des Spaltennamens.
	 * @param columnName Der Name der zugehörigen Spalte.
	 */
	public TableField(String columnName) {
		this.columnName = columnName;
		this.resolver = new ReferenceValueResolver<T>() {
			@Override
			public Object getResolvedValue(T origignal) {
				return origignal;
			}
		};
	}

	/**
	 * Erstellt eine neue Instanz von {@link TableField} unter Angabe des Spaltennamens und einem Initialwert.
	 * @param columnName Der Name der zugehörigen Spalte.
	 * @param value Gibt den Initialwert an.
	 */
	public TableField(String columnName, T value) {
		this(columnName);
		this.value = value;
		observeCurrentValue();
		this.savedState = value;
	}
	
	/**
	 * Erstellt eine neue Instanz von {@link TableField} unter Angabe des Spaltennamens, Initialwerts und {@link ReferenceValueResolver}.
	 * @param columnName Der Name der zugehörigen Spalte.
	 * @param value Gibt den Initialwert an.
	 * @param resolver Die Instanz einer Klasse, die das {@link ReferenceValueResolver}-Interface implementiert und das gekapselte Original-Objekt zum Zeitpunkt der SQL-Generierung in ein entsprechendes Datenbank-Objekt umwandelt.
	 */
	public TableField(String columnName, T value, ReferenceValueResolver<T> resolver){
		this(columnName, value);
		this.resolver = resolver;
	}

	/**
	 * Erstellt eine neue Instanz von {@link TableField} unter Angabe des Spaltennamens, Initialwertes und der Funktion als Primärschlüssel.
	 * @param columnName Der Name der zugehörigen Spalte.
	 * @param value Gibt den Initialwert an.
	 * @param isPK {@code true}, wenn es sich um einen (Teil des) Primärschlüssels handelt.
	 */
	public TableField(String columnName, T value, boolean isPK) {
		this(columnName, value);
		this.isPK = isPK;
	}

	/**
	 * Erstellt eine neue Instanz von {@link TableField} unter Angabe des Spaltennamens, Initialwertes, Funktion als Primärschlüssel und {@link ReferenceValueResolver}.
	 * @param columnName Der Name der zugehörigen Spalte.
	 * @param value Gibt den Initialwert an.
	 * @param isPK {@code true}, wenn es sich um einen (Teil des) Primärschlüssels handelt.
	 * @param resolver Eine Instanz einer Klasse, die das {@link ReferenceValueResolver}-Interface implementiert und das gekapselte Original-Objekt zum Zeitpunkt der SQL-Generierung in ein entsprechendes Datenbank-Objekt umwandelt.
	 */
	public TableField(String columnName, T value, boolean isPK,
			ReferenceValueResolver<T> resolver) {
		this(columnName, value, isPK);
		this.resolver = resolver;
	}

	/**
	 * Setzt den zur Generierung von SQL-Befehlen zu verwendenden {@link ReferenceValueResolver}.
	 * @param resolver Eine Instanz einer Klasse, die das {@link ReferenceValueResolver}-Interface implementiert und das gekapselte Original-Objekt zum Zeitpunkt der SQL-Generierung in ein entsprechendes Datenbank-Objekt umwandelt.
	 */
	public void setResolver(ReferenceValueResolver<T> resolver) {
		if (resolver == null) {
			this.resolver = new ReferenceValueResolver<T>() {
				@Override
				public Object getResolvedValue(T origignal) {
					return origignal;
				}
			};
		} else {
			this.resolver = resolver;
		}
	}

	/**
	 * Macht alle Änderungen dieser Zelle rückgängig. Der Inhalt wird auf den Wert zum Zeitpunkt des letzten Aufrufs von {@link TableField#saveState()} bzw. den Initialwert zurückgesetzt.
	 */
	public void revertChanges() {
		value = savedState;
		setChanged();
		notifyObservers(this);
		clearChanged();
	}

	/**
	 * Gibt den aktuellen Wert zurück.
	 * @return Der akutelle Wert der Zelle.
	 */
	public T get() {
		return value;
	}

	/**
	 * Gibt den für einen SQL-Befehl zu verwendenden Wert zurück.
	 * @return Der in ein für SQL tauglichen Typ umgewandelte Wert dieser Zelle.
	 */
	public Object getForInsert(){
		return resolver.getResolvedValue(get());
	}

	/**
	 * Gibt den zuletzt gesicherten Zustand zurück.
	 * @return Der zuletzt gesicherte Zustand.
	 */
	public T getSavedState() {
		return savedState;
	}
	
	/**
	 * Gibt den für einen SQL-Befehl zu verwendenden zuletzt gesicherten Zustand zurück.
	 * @return Der in ein für SQL tauglichen Typ umgewandelte und zuletzt gesicherte Wert dieser Zelle.
	 */
	public Object getSavedStateForInsert(){
		return resolver.getResolvedValue(getSavedState());
	}

	/**
	 * Setzt den Wert dieser Zelle ohne die Änderung permanent zu übernehmen.
	 * @param value Der zu setzende Wert.
	 */
	public void set(T value) {
		releaseCurrentValue();
		set(value, false);
		observeCurrentValue();
		if(hasChanged()){
			setChanged();
			notifyObservers();
			clearChanged();
		}
	}
	
	/**
	 * Macht eine Beobachtung des temporären Wertes rückgängig.
	 */
	private void releaseCurrentValue(){
		if(get() != null){
			if(Observable.class.isAssignableFrom(get().getClass())){
				((Observable) get()).deleteObserver(this);
			}
		}
	}
	
	/**
	 * Veranlasst die Beobachtung des aktuellen Wertes.
	 */
	private void observeCurrentValue(){
		if(get() != null){
			if(Observable.class.isAssignableFrom(get().getClass())){
				((Observable) get()).addObserver(this);
			}
		}
	}

	/**
	 * Setzt den Wert diese Zelle.
	 * @param value Gibt den zu setzenden Wert an.
	 * @param saveState {@code true}, wenn der zu setzende Wert permanent übernommen werden soll.
	 */
	public void set(T value, boolean saveState) {
		if (saveState)
			savedState = value;
		releaseCurrentValue();
		this.value = value;
		observeCurrentValue();
		setChanged();
		notifyObservers(this);
		clearChanged();
	}

	/**
	 * Übernimmt alle temporären Änderungen.
	 */
	public void saveState() {
		savedState = value;
		setChanged();
		notifyObservers(this);
		clearChanged();
	}

	/**
	 * Gibt an, ob sich der Inhalt der Zelle verändert hat.
	 * @return {@code true}, wenn eine temporäre Änderung vorliegt.
	 */
	@Override
	public boolean hasChanged() {
		if(super.hasChanged()) return true;
		if(value == null || savedState == null){
			return value != savedState;
		}
		return !value.equals(savedState);
	}

	/**
	 * Gibt den Namen der zugehörigen Spalte an.
	 * @return Der Name der zugehörigen Spalte.
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Gibt an, ob es sich um einen (Teil des) Primärschlüssels handelt.
	 * @return {@code true}, wenn es sich um einen Teil des Primärschlüssels handelt.
	 */
	public boolean isPK() {
		return isPK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (value == null)
			return "";
		return value.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
	/**
	 * Prüft ob der Inhalt und die Definition dieses Tabellenfelds mit der eines zweiten übereinstimmt.
	 * @param tf Gibt das zu prüfende Tabellenfeld an.
	 * @return {@code true}, wenn die zwei Tabellenfelder gleich sind.
	 */
	public boolean equalsTo(TableField<?> tf){
		if(value == null || tf.value == null){
			return value == tf.value && columnName.equals(tf.columnName) && isPK == tf.isPK;
		}
		return value.equals(tf.value) && columnName.equals(tf.columnName) && isPK == tf.isPK;
	}

}
