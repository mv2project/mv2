package de.iss.mv2.sql;

import javax.swing.SwingUtilities;

/**
 * A {@link ThreadSynchronizer} that runs the given {@link Runnable} on the event dispatcher thread.
 * @author Marcel Singer
 *
 */
public class SwingThreadSynchronizer implements ThreadSynchronizer {

	@Override
	public void execute(Runnable r) {
		SwingUtilities.invokeLater(r);
	}

}
