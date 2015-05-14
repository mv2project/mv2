package de.iss.mv2.messaging;


/**
 * A message containing an encrypted mail.
 * 
 * @author Marcel Singer
 *
 */
public class MessageDeliveryRequest extends MV2Message {

	/**
	 * Creates a new instance of {@link MessageDeliveryRequest}.
	 */
	public MessageDeliveryRequest() {
		super(STD_MESSAGE.MESSAGE_DELIVERY_REQUEST);
	}

	/**
	 * Sets the content message.
	 * 
	 * @param contentData
	 *            The content message to set.
	 */
	public void setContent(byte[] contentData) {
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY,
				contentData), true);
	}

	/**
	 * Returns the content message.
	 * 
	 * @return The content message.
	 */
	public byte[] getContent() {
		return getFieldDataArrayValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
	}

	/**
	 * Sets the identifier of the used symmetric algorithm.
	 * 
	 * @param algorithmName
	 *            The name of the symmetric algorithm used to encrypt the
	 *            content message.
	 */
	public void setSymmetricAlgorithm(String algorithmName) {
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.SYMETRIC_ALGORITHM,
				algorithmName), true);
	}

	/**
	 * Returns the name of the symmetric algorithm used to encrypt the content
	 * message.
	 * 
	 * @return The name of the symmetric algorithm.
	 */
	public String getSymmetricAlgorithmName() {
		return getFieldStringValue(DEF_MESSAGE_FIELD.SYMETRIC_ALGORITHM, "");
	}

	/**
	 * Sets the asymmetric encrypted key used to encrypt the content message.
	 * 
	 * @param key
	 *            The encrypted key to set.
	 */
	public void setEncrpytedSymmetricKey(byte[] key) {
		setMessageField(
				new MessageField(DEF_MESSAGE_FIELD.ENCRYPTION_KEY, key), true);
	}

	/**
	 * Returns the asymmetric encrypted key used to encrypt the content message.
	 * 
	 * @return The encrypted key.
	 */
	public byte[] getEncryptedSymmetricKey() {
		return getFieldDataArrayValue(DEF_MESSAGE_FIELD.ENCRYPTION_KEY, null);
	}
	
	/**
	 * Sets the receiver of this message.
	 * @param receiver The receiver of this message.
	 */
	public void setReceiver(String receiver){
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.RECEIVER, receiver), true); 
	}
	
	/**
	 * Returns the receiver of this message.
	 * @return The receiver of this message.
	 */
	public String getReceiver(){
		return getFieldStringValue(DEF_MESSAGE_FIELD.RECEIVER, null);
	}

}
