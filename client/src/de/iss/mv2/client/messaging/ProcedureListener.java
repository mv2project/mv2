package de.iss.mv2.client.messaging;


/**
 * A listener for events that occur during the execution if a
 * {@link MessageProcedure}.
 * 
 * @author Marcel Singer
 *
 * @param <R>
 *            The type of the procedure result.
 */
public interface ProcedureListener<R> {

	/**
	 * Handles the state change of a message procedure.
	 * 
	 * @param procedure
	 *            The procedure thats state changed.
	 * @param state
	 *            The new state message of the given procedure.
	 * @param progress
	 *            The new progress of the given procedure.
	 */
	public void procedureStateChanged(
			MessageProcedure<? extends Throwable, R> procedure, String state,
			int progress);

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
