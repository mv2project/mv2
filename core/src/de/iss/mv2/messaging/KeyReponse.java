package de.iss.mv2.messaging;

/**
 * A message containing the clients private key.
 * @author Marcel Singer
 *
 */
public class KeyReponse extends MV2Message {

	/**
	 * Creates a new instance of {@link KeyReponse}.
	 */
	public KeyReponse() {
		super(STD_MESSAGE.KEY_RESPONSE);
		
	}
	
	/**
	 * Sets the clients private key.
	 * @param key The clients private key.
	 * @throws IllegalArgumentException Is thrown if the given key is {@code null} or empty.
	 */
	public void setKey(byte[] key) throws IllegalArgumentException{
		if(key == null || key.length == 0) throw new IllegalArgumentException("The key must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, key), true);
	}
	
	/**
	 * Returns the clients private key.
	 * @return The clients private key or {@code null} if there is none.
	 */
	public byte[] getKey(){
		return getFieldDataArrayValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
	}

}
