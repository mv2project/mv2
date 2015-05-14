package de.iss.mv2.server.data;

import java.util.Date;

/**
 * The default implementation of {@link ContentMessage}.
 * @author MARCEL
 *
 */
public class ContentMessageImpl implements ContentMessage{

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
	 * Holds the name of the symmetric algorithm used to encrypt the content message.
	 */
	private String algorithmName;
	
	/**
	 * Holds the asymmetric encrypted key used to encrypt the content message.
	 */
	private byte[] key;
	
	/**
	 * Creates a new instance of {@link ContentMessageImpl}.
	 * @param webSpace The web space this message is allocated to.
	 * @param timestamp The timestamp when this message was received.
	 * @param contentData The content of this message.
	 * @param key The symmetric encryption key of this message.
	 * @param algorithmName The symmetric algorithm name.
	 */
	public ContentMessageImpl(WebSpace webSpace, Date timestamp, byte[] contentData, byte[] key, String algorithmName) {
		this.webSpace = webSpace;
		this.timestamp = timestamp;
		this.key = key;
		this.algorithmName = algorithmName;
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
	public byte[] getKey() {
		return key;
	}

	@Override
	public String getSymmetricAlgorithm() {
		return algorithmName;
	}

}
