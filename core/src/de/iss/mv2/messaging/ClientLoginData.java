package de.iss.mv2.messaging;

import java.util.Base64;

/**
 * A message containing the decrypted test phrase of a {@link ServerLoginResponse}.
 * @author Marcel Singer
 *
 */
public class ClientLoginData extends MV2Message {

	/**
	 * Creates a new instance of {@link ClientLoginData}.
	 */
	public ClientLoginData() {
		super(STD_MESSAGE.CLIENT_LOGIN_DATA);
	}
	
	/**
	 * Sets the decrypted test phrase.
	 * @param decryptedTestPhrase The test phrase to set.
	 * @throws IllegalArgumentException
	 */
	public void setDecryptedTestPhrase(byte[] decryptedTestPhrase) throws IllegalArgumentException{
		if(decryptedTestPhrase == null || decryptedTestPhrase.length == 0) throw new IllegalArgumentException("The decrpyted test phrase must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BASE64, Base64.getEncoder().encodeToString(decryptedTestPhrase)), true);
	}
	
	/**
	 * Returns the decrypted test phrase.
	 * @return The decrypted test phrase.
	 */
	public byte[] getDecryptedTestPhrase(){
		String value = getFieldValue(DEF_MESSAGE_FIELD.CONTENT_BASE64, null);
		if(value == null) return null;
		return Base64.getDecoder().decode(value);
	}
	
	

}
