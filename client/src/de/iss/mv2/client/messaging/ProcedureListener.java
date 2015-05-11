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
public interface ProcedureListener<R> extends ProcedureResultListener<R> {

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

	

}
