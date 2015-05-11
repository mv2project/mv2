package de.iss.mv2.messaging;

/**
 * A message to request the certificate allocated to a web space.
 * 
 * @author Marcel Singer
 *
 */
public class ClientCertificateRequest extends MV2Message {

	/**
	 * Creates a new instance of {@link ClientCertificateRequest}.
	 */
	public ClientCertificateRequest() {
		super(STD_MESSAGE.CLIENT_CERTIFICATE_REQUEST);
	}

	/**
	 * Sets the identifier of the web space thats certificate is requested.
	 * 
	 * @param identifier
	 *            The identifier to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given identifier is {@code null} or empty.
	 */
	public void setIdentifier(String identifier)
			throws IllegalArgumentException {
		if (identifier == null || identifier.isEmpty())
			throw new IllegalArgumentException(
					"The identifier must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN,
				identifier), true);
	}
	
	/**
	 * Returns the identifier of the web space thats certificate is requested.
	 * @return The identifier of the web space thats certificate is requested.
	 */
	public String getIdentifier(){
		return getFieldValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, "");
	}

}
