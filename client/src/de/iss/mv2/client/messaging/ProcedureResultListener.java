package de.iss.mv2.client.messaging;

/**
 * A listener to be notified when a procedure either completes or fails.
 * @author Marcel Singer
 * @param <R> The type of the result.
 *
 */
public interface ProcedureResultListener<R> {

	/**
	 * Handles an exception that occurred during the execution of the given
	 * message procedure.
	 * 
	 * @param procedure
	 *            The procedure in which the given exception occurred.
	 * @param ex
	 *            The thrown exception.
	 */
	public void handleProcedureException(
			MessageProcedure<? extends Throwable, R> procedure,
			ProcedureException ex);

	/**
	 * Handles the completion of the given {@link MessageProcedure}.
	 * 
	 * @param procedure
	 *            The completed procedure.
	 * @param result
	 *            The result of the given completed procedure.
	 */
	public void procedureCompleted(
			MessageProcedure<? extends Throwable, R> procedure, R result);
	
}
