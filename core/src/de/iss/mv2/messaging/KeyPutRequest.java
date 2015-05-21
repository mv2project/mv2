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
	 */
	public void setPassphrase(byte[] passphrase){
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.HASH_BINARY, passphrase), true);
	}
	
	/**
	 * Returns the hashed passphrase.
	 * @return The hashed passphrase that will be needed to request the clients private key.
	 */
	public byte[] getPassphrase(){
		return getFieldDataArrayValue(DEF_MESSAGE_FIELD.HASH_BINARY, null);
	}
	
	/*
	public void setPrivateKey(byte[] key){
		
	}*/
	
}
