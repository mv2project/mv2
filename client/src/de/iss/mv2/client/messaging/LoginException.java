package de.iss.mv2.client.messaging;

/**
 * An exception that is thrown to indicate the failure of a login procedure.
 * @author Marcel Singer
 *
 */
public class LoginException extends Exception {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -805596951069450081L;

	/**
	 * Creates a new instance of {@link LoginException} with the given message.
	 * @param message The message of this exception.
	 */
	public LoginException(String message) {
		super(message);
	}

}
