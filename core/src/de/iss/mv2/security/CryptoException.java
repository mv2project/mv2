package de.iss.mv2.security;

import java.io.IOException;

/**
 * Stellte eine Ver-/Entschlüsselungs zugeordnete Exception zur Verfügung.
 */

@SuppressWarnings("serial")
public class CryptoException extends IOException {

	/**
	 * 
	 */
	public CryptoException() {
		super();
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public CryptoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message
	 */
	public CryptoException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public CryptoException(Throwable cause) {
		super(cause);
	}

}
