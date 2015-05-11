package de.iss.mv2.sql;

/**
 * Stellt eine Möglichkeit zur Auflösung von Datenbank-Referenzen zur Verfügung.
 * @author singer
 *
 * @param <O> Der Typ des aufzulösenden Objekts.
 */
public interface ReferenceValueResolver<O> {

	/**
	 * Löst das Original in ein anderes Objekt auf, um es in der Datenbank referenzieren zu können (PK-Verarbeitung).
	 * @param origignal Das aufzulösende Original.
	 * @return Das aufgelöste Original.
	 */
	Object getResolvedValue(O origignal);
	
}
