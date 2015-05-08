package de.iss.mv2.server.data;

import de.iss.mv2.data.Certificate;


/**
 * Represents a users web space.
 * @author Marcel Singer
 *
 */
public interface WebSpace {
	
	/**
	 * Returns the certificate of this web space.
	 * @return The certificate of this web space.
	 */
	public Certificate getCertificate();
	
	/**
	 * Sets the certificate for this web space.
	 * @param certificate The certificate to set.
	 */
	public void setCertificate(Certificate certificate);
	
	/**
	 * Returns the identifier for this web space. For example: "tina.test", "max.mustermann".
	 * @return The identifier for this web space.
	 */
	public String getIdentifier();

}
