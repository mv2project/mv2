package de.iss.mv2.client.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.sql.SequentialThreadSynchronizer;
import de.iss.mv2.sql.SingleThreadSynchronizer;
import de.iss.mv2.sql.ThreadSynchronizer;

/**
 * A procedure to execute and manage subsequent server calls.
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
	 * Holds the listeners to notify during the execution of this procedure.
	 */
	private List<ProcedureListener<R>> listeners = new ArrayList<ProcedureListener<R>>();

	/**
	 * Holds the thread synchronizer that is used to invoke the registered listeners.
	 */
	private ThreadSynchronizer eventThreadSynchronizer = new SequentialThreadSynchronizer();
	
	/**
	 * Holds the thread synchronizer that is used to invoke the procedure.
	 */
	private ThreadSynchronizer procedureThreadSynchronizer = new SingleThreadSynchronizer();

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
	 * 
	 * @param state
	 *            The new state to set.
	 */
	protected void update(final String state) {
		this.state = state;
		final int progress = this.progress;
		eventThreadSynchronizer.execute(new Runnable() {
			
			@Override
			public void run() {
				for(ProcedureListener<R> l : listeners) l.procedureStateChanged(MessageProcedure.this, state, progress);
			}
		});
	}

	/**
	 * Updates the progress of this procedure.
	 * 
	 * @param progress
	 *            The new progress to set.
	 */
	protected void update(final int progress) {
		this.progress = progress;
		final String state = this.state;
		eventThreadSynchronizer.execute(new Runnable() {
			
			@Override
			public void run() {
				for(ProcedureListener<R> l : listeners) l.procedureStateChanged(MessageProcedure.this, state, progress);
			}
		});
	}
	
	

	/**
	 * Updates the state and progress of this procedure.
	 * 
	 * @param state
	 *            The new state of this procedure.
	 * @param progress
	 *            The new progress of this procedure.
	 */
	protected void update(String state, int progress) {
		update(progress);
		update(state);
	}

	/**
	 * Returns the current state of this procedure.
	 * 
	 * @return The current state of this procedure.
	 */
	public String getState() {
		return state;
	}

	/**
	 * Returns the progress of this procedure.
	 * 
	 * @return The progress of this procedure.
	 */
	public int getProgress() {
		return progress;
	}
	
	

	/**
	 * Runs this procedure immediately on the calling thread an returns the
	 * procedure result.
	 * <p><b>Remarks:</b>&nbsp;If the procedure is executed by this method only the {@link ProcedureListener#procedureStateChanged(MessageProcedure, String, int)} method might be called.</p>
	 * @return The result of this procedure.
	 * @throws ProcedureException
	 *             Is thrown if there is any exception during the execution of
	 *             this procedure. See {@link ProcedureException#getCause()} to
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
	
	/**
	 * Runs this procedure.
	 */
	public void run(){
		procedureThreadSynchronizer.execute(new Runnable() {
			
			@Override
			public void run() {
				try{
					final R result = runImmediate();
					eventThreadSynchronizer.execute(new Runnable() {
						@Override
						public void run() {
							for(ProcedureListener<R> l : listeners) l.procedureCompleted(MessageProcedure.this, result);
						}
					});
				}catch(final ProcedureException ex){
					eventThreadSynchronizer.execute(new Runnable() {
						@Override
						public void run() {
							for(ProcedureListener<R> l : listeners) l.handleProcedureException(MessageProcedure.this, ex);
						}
					});
				}
			}
		});
	}

	/**
	 * Registers the given listener to be notified when the state of this procedure changes.
	 * @param listener The listener to add.
	 */
	public void addProcedureListener(ProcedureListener<R> listener){
		if(!this.listeners.contains(listener)) listeners.add(listener);
	}
	
	/**
	 * Removes the given listener.
	 * @param listener The listener to remove.
	 */
	public void removeProcedureListener(ProcedureListener<R> listener){
		while(this.listeners.contains(listener)){
			listeners.remove(listener);
		}
	}
	
	/**
	 * Sets the synchronizer that is used to invoke the event methods of the registered listeners.
	 * <p><b>Remarks:</b>&nbsp;Invoke this method during the construction or initialization of a new procedure. Changing the synchronizer during the execution of this procedure can cause
	 * unexpected behavior.</p>
	 * @param synchronizer The synchronizer to set. If the given synchronizer is {@code null} it will be reset to its default.
	 */
	protected void setEventThreadSynchronizer(ThreadSynchronizer synchronizer){
		if(synchronizer == null) synchronizer = new SequentialThreadSynchronizer();
		this.eventThreadSynchronizer = synchronizer;
	}
	
	/**
	 * Sets the synchronizer that is used to invoke the execution of this procedure.
	 * <p><b>Remarks:</b>&nbsp;Invoke this method during the construction or initialization of a new procedure. Changing the synchronizer during the execution of this procedure can cause
	 * unexpected behavior.</p>
	 * @param synchronizer The synchronizer to set. If the given synchronizer is {@code null} it will be reset to its default.
	 */
	protected void setProcedureThreadSynchronizer(ThreadSynchronizer synchronizer){
		if(synchronizer == null) synchronizer = new SingleThreadSynchronizer();
		this.procedureThreadSynchronizer = synchronizer;
	}
	
}
