package de.iss.mv2.client.data;

import java.util.List;

/**
 * A container to store incoming mails.
 * @author Marcel Singer
 *
 */
public interface MailStorage extends List<MailMessage> {
	
	/**
	 * Returns the newest mail of this storage.
	 * @return The newest mail of this storage or {@code null} if there is none.
	 */
	public MailMessage getNewest();

}
