package de.iss.mv2.server.data;

import java.util.Date;

/**
 * The default implementation of {@link Message}.
 * @author MARCEL
 *
 */
public class MessageImpl implements Message{

	/**
	 * Holds the receiving web space.
	 */
	private WebSpace webSpace;
	
	/**
	 * Holds the date when this message was received.
	 */
	private Date timestamp;
	
	/**
	 * Holds the data of the encrypted message.
	 */
	private byte[] contentData;
	
	/**
	 * Holds the identifier of this message.
	 */
	private long identifier;
	
	/**
	 * Creates a new instance of {@link MessageImpl}.
	 * @param identifier The identifier of this message.
	 * @param webSpace The web space this message is allocated to.
	 * @param timestamp The timestamp when this message was received.
	 * @param contentData The content of this message.
	 */
	public MessageImpl(long identifier, WebSpace webSpace, Date timestamp, byte[] contentData) {
		this.identifier = identifier;
		this.webSpace = webSpace;
		this.timestamp = timestamp;
		this.contentData = contentData;
	}
	
	@Override
	public WebSpace getReceiver() {
		return webSpace;
	}

	@Override
	public Date getTimestamp() {
		return timestamp;
	}

	@Override
	public byte[] getContent() {
		return contentData;
	}

	@Override
	public long getIdentifier() {
		return identifier;
	}

}
