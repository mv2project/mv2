package de.iss.mv2.messaging;

import java.util.Base64;

/**
 * The servers response to a {@link ClientLoginRequest} containing the test
 * phrase and it's digest.
 * 
 * @author Marcel Singer
 *
 */
public class ServerLoginResponse extends MV2Message {

	/**
	 * Creates a new instance of {@link ServerLoginResponse}.
	 */
	public ServerLoginResponse() {
		super(STD_MESSAGE.SERVER_LOGIN_RESPONSE);
	}

	/**
	 * Sets the encrypted phrase to test the clients identity.
	 * 
	 * @param testPhrase
	 *            The data of the test phrase to set.
	 * @throws IllegalArgumentException
	 *             If the given data is {@code null} or empty (
	 *             {@code testPhrase.length == 0}).
	 */
	public void setTestPhrase(byte[] testPhrase)
			throws IllegalArgumentException {
		if (testPhrase == null || testPhrase.length == 0)
			throw new IllegalArgumentException(
					"The test phrase must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BASE64,
				Base64.getEncoder().encodeToString(testPhrase)), true);
	}

	/**
	 * Returns the encrypted phrase to test the clients identity.
	 * 
	 * @return The encrypted phrase to test the clients identity.
	 */
	public byte[] getTestPhrase() {
		String value = getFieldValue(DEF_MESSAGE_FIELD.CONTENT_BASE64, null);
		if (value == null)
			return null;
		return Base64.getDecoder().decode(value);
	}

	/**
	 * Sets the name of the hash algorithm used to compose the digest of the
	 * unencrypted test phrase.
	 * 
	 * @param algorithmName
	 *            The name of the hash algorithm to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given algorithm name is {@code null} or
	 *             empty.
	 */
	public void setHashAlgorithm(String algorithmName)
			throws IllegalArgumentException {
		if (algorithmName == null || algorithmName.isEmpty())
			throw new IllegalArgumentException(
					"The algorithm name must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.HASH_ALGORITHM,
				algorithmName), true);
	}
	
	/**
	 * Returns the name of the hash algorithm used to compose the digest of the unencrypted test phrase.
	 * @return The name of the hash algorithm used to compose the digest of the unencrypted test phrase.
	 */
	public String getHashAlgorithm(){
		return getFieldValue(DEF_MESSAGE_FIELD.HASH_ALGORITHM, "");
	}
	
	/**
	 * Sets the digest of the unencrypted test phrase.
	 * @param testPhraseHash The digest to set.
	 * @throws IllegalArgumentException Is thrown if the given digest is {@code null} or empty.
	 */
	public void setTestPhraseHash(byte[] testPhraseHash) throws IllegalArgumentException{
		if(testPhraseHash == null || testPhraseHash.length == 0) throw new IllegalArgumentException("The test phrase hash must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.HASH_BASE64, Base64.getEncoder().encodeToString(testPhraseHash)), true);
	}
	
	/**
	 * Returns the digest of the unencrypted test phrase.
	 * @return The digest of the unencrypted test phrase.
	 */
	public byte[] getTestPhraseHash(){
		String value = getFieldValue(DEF_MESSAGE_FIELD.HASH_BASE64, null);
		if(value == null) return null;
		return Base64.getDecoder().decode(value);
	}
	
	

}
