package de.iss.mv2.messaging;


/**
 * A message to open a connection to a server.
 * 
 * @author Marcel Singer
 *
 */
public class HelloMessage extends MV2Message {

	/**
	 * Creates a new instance of {@link HelloMessage}.
	 */
	public HelloMessage() {
		super(STD_MESSAGE.HELLO);
		
	}
	
	/**
	 * Sets the host name of the server.
	 * @param hostName The host name of the server.
	 * @throws IllegalArgumentException Is thrown if the given host name is {@code null} or empty.
	 */
	public void setHostName(String hostName) throws IllegalArgumentException{
		if(hostName == null || hostName.isEmpty()) throw new IllegalArgumentException("The host name may not be null.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN, hostName), true);
	}
	
	/**
	 * Returns the host name of the server.
	 * @return The host name of the server.
	 */
	public String getHostName(){
		return getFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, "");
	}

}
