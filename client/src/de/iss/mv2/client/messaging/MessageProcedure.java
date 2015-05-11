package de.iss.mv2.client.messaging;

import java.io.IOException;

import de.iss.mv2.client.io.MV2Client;

/**
 * A procedure containing subsequent server calls.
 * 
 * @author Marcel Singer
 * @param <R>
 *            The type of the result.
 * @param <E>
 *            The type of exception that can occur during this procedure.
 */
public abstract class MessageProcedure<E extends Throwable, R> {

	/**
	 * The client performing this procedure.
	 */
	private final MV2Client client;
	
	/** 
	 * Holds the state of this procedure.
	 */
	private volatile String state = "";
	
	/**
	 * Holds the progress of this procedure.
	 */
	private volatile int progress = 0;

	/**
	 * Creates a new instance of {@link MessageProcedure} with the given client
	 * instance.
	 * 
	 * @param client
	 *            The client performing this procedure.
	 */
	public MessageProcedure(MV2Client client) {
		this.client = client;
	}

	/**
	 * Performs the needed server requests.
	 * 
	 * @param client
	 *            The client that performs the server calls.
	 * @return The result of the procedure.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws E
	 *             An exception that occurs during this procedure.
	 * 
	 */
	protected abstract R doPerform(MV2Client client) throws IOException, E;

	/**
	 * Handles a thrown communication exception.
	 * 
	 * @param exception
	 *            The thrown communication exception.
	 */
	protected abstract void handleCommunicationException(IOException exception);

	/**
	 * Handles the given exception that was thrown during the execution of this
	 * procedure.
	 * 
	 * @param exception
	 *            The thrown exception to handle.
	 */
	protected abstract void handleProcedureException(Throwable exception);
	
	/**
	 * Updates the state of this procedure.
	 * @param state The new state to set.
	 */
	protected void update(String state){
		this.state = state;
	}
	
	/**
	 * Updates the progress of this procedure.
	 * @param progress The new progress to set.
	 */
	protected void update(int progress){
		this.progress = progress;
	}
	
	/**
	 * Updates the state and progress of this procedure.
	 * @param state The new state of this procedure.
	 * @param progress The new progress of this procedure.
	 */
	protected void update(String state, int progress){
		update(progress);
		update(state);
	}
	
	/**
	 * Returns the current state of this procedure.
	 * @return The current state of this procedure.
	 */
	public String getState(){
		return state;
	}
	
	/**
	 * Returns the progress of this procedure.
	 * @return The progress of this procedure.
	 */
	public int getProgress(){
		return progress;
	}
	

	/**
	 * Runs this procedure immediately on the calling thread an returns the
	 * procedure result.
	 * 
	 * @return The result of this procedure.
	 * @throws ProcedureException
	 *             Is thrown if there is any exception during the execution of
	 *             this procedure. See {@code ProcedureException#getCause()} to
	 *             get more informations about the thrown exception.
	 */
	public R runImmediate() throws ProcedureException {
		try {
			return doPerform(client);
			
			
		} catch (IOException communicationException) {
			handleCommunicationException(communicationException);
			throw new ProcedureException(communicationException);
		} catch (Throwable th) {
			handleProcedureException(th);
			throw new ProcedureException(th);
		}
	}

}
