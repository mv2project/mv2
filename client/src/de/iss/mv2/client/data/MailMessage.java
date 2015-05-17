package de.iss.mv2.client.data;

/**
 * Represents a message that was sent by or to this client.
 * 
 * @author Marcel Singer
 *
 */
public class MailMessage {

	/**
	 * Holds the subject of this message.
	 */
	private String subject;
	/**
	 * Holds the content of this message.
	 */
	private String content;
	/**
	 * Holds the receivers of this message.
	 */
	private String[] receivers;
	/**
	 * Holds the sender of this message.
	 */
	private String sender;

	/**
	 * Creates a new instance of {@link MailMessage}.
	 */
	public MailMessage() {

	}

	/**
	 * Returns the subject of this message.
	 * 
	 * @return The subject of this message.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject of this message.
	 * 
	 * @param subject
	 *            The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Returns the content of this message.
	 * 
	 * @return The content of this message.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content of this message.
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Returns an array containing the receivers of this message.
	 * @return An array containing the receivers of this message.
	 */
	public String[] getReceivers() {
		return receivers;
	}

	/**
	 * Sets the receivers of this message.
	 * @param receivers An array with the receivers to set.
	 */
	public void setReceivers(String[] receivers) {
		this.receivers = receivers;
	}

	/**
	 * Returns the sender of this message.
	 * @return The sender of this message.
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Sets the sender oft his message.
	 * @param sender The sender to set.
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

}
