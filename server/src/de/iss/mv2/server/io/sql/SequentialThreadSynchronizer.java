package de.iss.mv2.server.io.sql;

/**
 * A {@link ThreadSynchronizer} that invokes the given {@link Runnable} instantly on the calling thread.
 * @author Marcel Singer
 *
 */
public class SequentialThreadSynchronizer implements ThreadSynchronizer {

	
	@Override
	public void execute(Runnable r) {
		r.run();
	}

}
