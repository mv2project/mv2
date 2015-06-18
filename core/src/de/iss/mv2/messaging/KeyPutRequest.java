package de.iss.mv2.messaging;

/**
 * The request message to store the clients private key on the server. 
 * @author Marcel Singer
 *
 */
public class KeyPutRequest extends MV2Message {
	
	/**
	 * Creates a new instance of {@link KeyPutRequest}.
	 */
	public KeyPutRequest(){
		super(STD_MESSAGE.KEY_PUT_REQUEST);
	}
	
	/**
	 * Sets the hashed passphrase. This passphrase will be needed to request the clients private key.
	 * @param passphrase The passphrase to set.
	 * @throws IllegalArgumentException If the given passphrase is {@code null} or empty.
	 */
	public void setPassphrase(byte[] passphrase) throws IllegalArgumentException{
		if(passphrase == null || passphrase.length == 0) throw new IllegalArgumentException("The passphrase must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.HASH_BINARY, passphrase), true);
	}
	
	/**
	 * Returns the hashed passphrase.
	 * @return The hashed passphrase that will be needed to request the clients private key. If there is none this method will return {@code null}.
	 */
	public byte[] getPassphrase(){
		return getFieldArrayValue(DEF_MESSAGE_FIELD.HASH_BINARY, null);
	}
	
	/**
	 * Sets the clients encrypted private key.
	 * @param key The clients encrypted private key.
	 * @throws IllegalArgumentException Is thrown if the given key is {@code null} or empty.
	 */
	public void setPrivateKey(byte[] key) throws IllegalArgumentException{
		if(key == null || key.length == 0) throw new IllegalArgumentException("The key must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, key), true);
	}
	
	/**
	 * Returns the clients encrypted private key.
	 * @return The clients encrypted private key or {@code null} if there is none.
	 */
	public byte[] getPrivateKey(){
		return getFieldArrayValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
	}
	
}
