package de.iss.mv2.server.io.sql;

/**
 * A {@link ThreadSynchronizer} that instantly runs the given {@link Runnable} on a own thread.
 * @author Marcel Singer
 *
 */
public class SingleThreadSynchronizer implements ThreadSynchronizer {

	@Override
	public void execute(final Runnable r) {
		Thread th = new Thread(r);
		th.start();
	}

}
