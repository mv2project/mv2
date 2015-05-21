package de.iss.mv2.messaging;

/**
 * An enumeration containing all types and identifiers of all well known messages.
 * @author Marcel Singer
 *
 */
public enum STD_MESSAGE {
	/**
	 * The type for an unknown message.
	 */
	UNKNOWN(-1),
	/**
	 * The type of an OK-message.
	 */
	OK(1),
	/**
	 * The type of an encrypted message.
	 */
	ENCRYPTED_MESSAGE(2),
	/**
	 * The type of a unable to process message.
	 */
	UNABLE_TO_PROCESS(3),
	/**
	 * The type of a certificate request message.
	 */
	CERT_REQUEST(4),
	/**
	 * The type of a certificate response message.
	 */
	CERT_RESPONSE(5),
	/**
	 * The type of a space creation request message.
	 */
	SPACE_CREATION_REQUEST(6),
	/**
	 * The type of a space creation response message.
	 */
	SPACE_CREATION_RESPONSE(7),
	/**
	 * The type of a domain names request. 
	 */
	DOMAIN_NAMES_REQUEST(8),
	/**
	 * The type of a domain names response.
	 */
	DOMAIN_NAMES_RESPONSE(9),
	/**
	 * The type of a hello message.
	 */
	HELLO(10),
	/**
	 * The type of a clients login request.
	 */
	CLIENT_LOGIN_REQUEST(11),
	/**
	 * The servers answer to client login request.
	 */
	SERVER_LOGIN_RESPONSE(12),
	/**
	 * A message containing the clients login data.
	 */
	CLIENT_LOGIN_DATA(13),
	/**
	 * The servers answer containing the answer to a {@link STD_MESSAGE#CLIENT_LOGIN_DATA}.
	 */
	SERVER_LOGIN_RESULT(14),
	/**
	 * The clients request for a foreign client certificate. 
	 */
	CLIENT_CERTIFICATE_REQUEST(15),
	/**
	 * The message type of servers answer to a {@link ClientCertificateRequest}.
	 */
	CLIENT_CERTIFICATE_RESPONSE(16),
	/**
	 * The message type of a clients outgoing mail message.
	 */
	MESSAGE_DELIVERY_REQUEST(17),
	/**
	 * The message type of a servers response to an incoming {@link STD_MESSAGE#MESSAGE_DELIVERY_REQUEST}.
	 */
	MESSAGE_DELIVERY_RESPONSE(18),
	/**
	 * The message type of a client message.
	 * <p><b>Remarks:</b> This is not a transport message, but the encoded form of a plain mail message.</p> 
	 */
	CONTENT_MESSAGE(19),
	/**
	 * The type of a message that request the clients message.
	 */
	MESSAGE_QUERY_REQUEST(20),
	/**
	 * The type of a message containing the response to a {@link STD_MESSAGE#MESSAGE_DELIVERY_REQUEST} message.
	 */
	MESSAGE_QUERY_RESPONSE(21),
	/**
	 * The type of a message to request a mail message with a given identifier.
	 */
	MESSAGE_FETCH_REQUEST(22),
	/**
	 * The type of a response to a {@link STD_MESSAGE#MESSAGE_FETCH_REQUEST} containing the requested mail message.
	 */
	MESSAGE_FETCH_RESPONSE(23),
	/**
	 * The type of a message thats request to store the clients private key on the server.
	 */
	KEY_PUT_REQUEST(24),
	/**
	 * The type of a response to a {@link STD_MESSAGE#KEY_PUT_REQUEST} message.
	 */
	KEY_PUT_RESPONSE(25);
	
	/**
	 * Holds the identifier of a message type.
	 */
	private final int identifier;
	
	/**
	 * Creates a new {@link STD_MESSAGE}.
	 * @param identifier The identifier of the message.
	 */
	private STD_MESSAGE(int identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * Returns the identifier of this message type.
	 * @return The identifier of this message type.
	 */
	public int getIdentifier(){ return identifier; }
	
	/**
	 * Searches all well known message types for the given identifier.
	 * @param identifier The identifier to search for.
	 * @return A message type with the given identifier or {@link STD_MESSAGE#UNKNOWN} if no type was found.
	 */
	public static STD_MESSAGE find(int identifier){
		for (STD_MESSAGE m : STD_MESSAGE.values()) {
			if(m.getIdentifier() == identifier) return m;
		}
		return STD_MESSAGE.UNKNOWN;
	}
	
}
