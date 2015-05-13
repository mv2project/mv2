package de.iss.mv2.messaging;

/**
 * A message containing a client login request. It contains the identifier of the clients web space to connect to.
 * @author Marcel Singer
 *
 */
public class ClientLoginRequest extends MV2Message {

	/**
	 * Creates a new instance of {@link ClientLoginRequest}.
	 */
	public ClientLoginRequest() {
		super(STD_MESSAGE.CLIENT_LOGIN_REQUEST);
	}
	
	
	/**
	 * Sets the identifier of the web space to connect to.
	 * @param identifier The identifier of the web space to connect to.
	 * @throws IllegalArgumentException Is thrown if the given identifier is {@code null} or empty.
	 */
	public void setIdentifier(String identifier) throws IllegalArgumentException{
		if(identifier == null || identifier.isEmpty()) throw new IllegalArgumentException("The identifier must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN, identifier), true);
	}
	
	/**
	 * Returns the identifier of the web space to connect to.
	 * @return The identifier of the web space to connect to.
	 */
	public String getIdentifier(){
		return getFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, "");
	}
	
	

}
