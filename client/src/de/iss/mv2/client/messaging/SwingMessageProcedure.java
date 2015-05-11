package de.iss.mv2.client.messaging;

import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.sql.SwingThreadSynchronizer;
import de.iss.mv2.sql.ThreadSynchronizer;

/**
 * A procedure to execute and manage subsequent server calls. The execution of this procedure is
 * done in a single thread while the listeners will be called on Swings Event Dispatcher Thread.
 * 
 * @author Marcel Singer
 * @param <R>
 *            The type of the result.
 * @param <E>
 *            The type of exception that can occur during this procedure.
 */
public abstract class SwingMessageProcedure<E extends Throwable, R> extends MessageProcedure<E, R> {

	/**
	 * Creates a new instance of {@link SwingMessageProcedure}.
	 * @param client The client performing this procedure.
	 */
	public SwingMessageProcedure(MV2Client client) {
		super(client);
		setEventThreadSynchronizer(new SwingThreadSynchronizer());
	}
	
	
	@Override
	protected void setEventThreadSynchronizer(ThreadSynchronizer synchronizer) {
		if(synchronizer == null) synchronizer = new SwingThreadSynchronizer();
		super.setEventThreadSynchronizer(synchronizer);
	}

}
