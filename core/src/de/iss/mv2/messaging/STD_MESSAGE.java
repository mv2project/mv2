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
	HELLO(10);
	
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