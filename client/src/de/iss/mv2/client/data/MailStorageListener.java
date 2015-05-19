package de.iss.mv2.client.data;


/**
 * A listener to handle changes of a {@link MailStorage} object.
 * @author MARCEL
 *
 */
public interface MailStorageListener {
	
	/**
	 * Is called when a {@link MailStorage} changed.
	 * @param storage The storage that caused this event.
	 */
	public void storeChanged(MailStorage storage);

}
