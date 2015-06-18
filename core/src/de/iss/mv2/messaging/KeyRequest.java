package de.iss.mv2.messaging;

/**
 * A message to request the clients private key.
 * 
 * @author Marcel Singer
 *
 */
public class KeyRequest extends MV2Message {

	/**
	 * Creates a new instance of {@link KeyRequest}.
	 */
	public KeyRequest() {
		super(STD_MESSAGE.KEY_REQUEST);
	}

	/**
	 * Sets the passphrase.
	 * 
	 * @param passphrase
	 *            The passphrase needed to request the private key.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given passphrase is {@code null} or empty.
	 */
	public void setPassphrase(byte[] passphrase)
			throws IllegalArgumentException {
		if (passphrase == null || passphrase.length == 0)
			throw new IllegalArgumentException(
					"The passphrase must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.HASH_BINARY,
				passphrase), true);
	}

	/**
	 * Returns the passphrase.
	 * @return The passphrase needed to request the clients private key or {@code null} if there is none.
	 */
	public byte[] getPassphrase() {
		return getFieldArrayValue(DEF_MESSAGE_FIELD.HASH_BINARY, null);
	}
	
	/**
	 * Sets the identifier of the web space thats key should be returned.
	 * @param identifier The identifier to set.
	 * @throws IllegalArgumentException If the given identifier is {@code null} or empty.
	 */
	public void setIdentifier(String identifier) throws IllegalArgumentException{
		if(identifier == null || identifier.isEmpty()) throw new IllegalArgumentException("The identifier must not be empty or null.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN), true);
	}
	
	/**
	 * Returns the identifier of the web space thats key should be returned.
	 * @return The identifier or {@code null} if there is none.
	 */
	public String getIdentifier(){
		return getFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, null);
	}
}
