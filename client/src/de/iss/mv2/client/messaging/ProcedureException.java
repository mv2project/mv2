package de.iss.mv2.client.messaging;

/**
 * An exception that wraps an exception that is thrown during the execution of a procedure.
 * @author Marcel Singer
 *
 */
public class ProcedureException extends Exception {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -7189174550685557101L;

	
	/**
	 * Creates a new instance of {@link ProcedureException} with the given inner exception.
	 * @param cause The exception to wrap.
	 */
	public ProcedureException(Throwable cause) {
		super(cause);
	}

	

}
