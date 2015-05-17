package de.iss.mv2.security;

import java.security.Signature;

/**
 * A class that can be used to create an instance of {@link Signature}.
 * @author Marcel Singer
 *
 */
public interface SignatureProvider {
	
	/**
	 * Creates a new signature algorithm an returns it.
	 * @return The created signature object.
	 */
	public Signature createSignature();

}
