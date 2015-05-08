package de.iss.mv2.server.io.sql;

/**
 * A synchronizer that is used to run a given runnable in a certain manner.
 * @author Marcel Singer
 *
 */
public interface ThreadSynchronizer {

	/**
	 * Executes the given {@link Runnable}.
	 * @param r The runnable to execute.
	 */
	public void execute(Runnable r);
	
}
