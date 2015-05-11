package de.iss.mv2.messaging;

import java.util.Base64;


/**
 * A message containing an encrypted mail.
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
	 * @param contentData The content message to set.
	 */
	public void setContent(byte[] contentData){
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BASE64, Base64.getEncoder().encodeToString(contentData)), true);
	}
	
	/**
	 * Returns the content message.
	 * @return The content message.
	 */
	public byte[] getContent(){
		String value = getFieldValue(DEF_MESSAGE_FIELD.CONTENT_BASE64, null);
		if(value == null || value.isEmpty()) return null;
		return Base64.getDecoder().decode(value);
	}
	
	/**
	 * Sets the identifier of the used symmetric algorithm.
	 * @param algorithmName The name of the symmetric algorithm used to encrypt the content message.
	 */
	public void setSymmetricAlgorithm(String algorithmName){
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.SYMETRIC_ALGORITHM, algorithmName), true);
	}
	
	/**
	 * Returns the name of the symmetric algorithm used to encrypt the content message.
	 * @return The name of the symmetric algorithm.
	 */
	public String getSymmetricAlgorithmName(){
		return getFieldValue(DEF_MESSAGE_FIELD.SYMETRIC_ALGORITHM, "");
	}
	
	/**
	 * Sets the asymmetric encrypted key used to encrypt the content message.
	 * @param key The encrypted key to set.
	 */
	public void setEncrpytedSymmetricKey(byte[] key){
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.ENCRYPTION_KEY, Base64.getEncoder().encodeToString(key)), true);
	}
	
	/**
	 * Returns the asymmetric encrypted key used to encrypt the content message.
	 * @return The encrypted key.
	 */
	public byte[] getEncryptedSymmetricKey(){
		String value = getFieldValue(DEF_MESSAGE_FIELD.ENCRYPTION_KEY, null);
		if(value == null) return null;
		return Base64.getDecoder().decode(value);
	}

}
