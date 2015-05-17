package de.iss.mv2.server.data;

import java.util.Date;

/**
 * Represents a message that was sent to a user.
 * @author Marcel Singer
 *
 */
public interface Message {
	
	/**
	 * Returns the receiving web space.
	 * @return The web space that received this message.
	 */
	public WebSpace getReceiver();
	
	/**
	 * Returns the date and time when this message was received <b>by the server</b>. 
	 * @return The date when this message was received. 
	 */
	public Date getTimestamp();
	
	/**
	 * Returns the encrypted content of this message.
	 * @return The encrypted content of this message.
	 */
	public byte[] getContent();
	
	

}
