package de.iss.mv2.client.messaging;

/**
 * An adaptor that passes occurring events to the given {@link ProcedureResultListener}.
 * @author Marcel Singer
 *
 * @param <R> The type of the result.
 */
public class ProcedureListenerAdapter<R> implements ProcedureListener<R> {

	/**
	 * Holds the result listener.
	 */
	private final ProcedureResultListener<R> resultListener;
	
	/**
	 * Creates a new instance of {@link ProcedureListenerAdapter} that passes occurring events to the given {@link ProcedureResultListener}.
	 * @param listener The listener to be notifier.
	 */
	public ProcedureListenerAdapter(ProcedureResultListener<R> listener) {
		this.resultListener = listener;
	}

	@Override
	public void procedureStateChanged(
			MessageProcedure<? extends Throwable, R> procedure, String state,
			int progress) {
		
		
	}

	@Override
	public void handleProcedureException(
			MessageProcedure<? extends Throwable, R> procedure,
			ProcedureException ex) {
		resultListener.handleProcedureException(procedure, ex);
	}

	@Override
	public void procedureCompleted(
			MessageProcedure<? extends Throwable, R> procedure, R result) {
		resultListener.procedureCompleted(procedure, result);
	}

}
