package de.iss.mv2.client.messaging;

/**
 * An exception that occurs during a server request.
 * @author Marcel Singer
 *
 */
public class RequestException extends Exception {

	
	/**
	 * The serial
	 */
	private static final long serialVersionUID = 3631198789682128333L;

	/**
	 * Creates a new instance of {@link RequestException} with a given message.
	 * @param message The message of the exception.
	 */
	public RequestException(String message) {
		super(message);
	}

}
