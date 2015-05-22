package de.iss.mv2.messaging;


/**
 * An enumeration containing all well known message fields.
 * @author Marcel Singer
 *
 */
public enum DEF_MESSAGE_FIELD {
	/**
	 * An unknown message field.
	 */
	UNKNOWN(-1),
	/**
	 * A message field containing the content encoding of a message.
	 */
	CONTENT_ENCODING(1),
	/**
	 * A message field declaring the cause for a message.
	 */
	CAUSE(2),
	/**
	 * A message field containing the symmetric encryption key and IV.
	 */
	ENCRYPTION_KEY(3, CONTENT_TYPE.BINARY),
	/**
	 * A message field containing the name of the symmetric algorithm.
	 */
	SYMMETRIC_ALGORITHM(4),
	/**
	 * A message field containing the name of the asymmetric algorithm.
	 */
	ASYMMETRIC_ALGORITHM(5),
	/**
	 * A message field containing pain text content.
	 */
	CONTENT_PLAIN(6),
	/**
	 * A message field containing the name of a hash algorithm.
	 */
	HASH_ALGORITHM(7),
	/**
	 * A message field containing binary content data.
	 */
	CONTENT_BINARY(8, CONTENT_TYPE.BINARY),
	/**
	 * A message field containing binary hash data.
	 */
	HASH_BINARY(9, CONTENT_TYPE.BINARY),
	/**
	 * A message field containing the receiving web space address.
	 */
	RECEIVER(10),
	/**
	 * A message field containing the subject of a {@link STD_MESSAGE#CONTENT_MESSAGE}.
	 */
	SUBJECT(11),
	/**
	 * A message field containing the senders certificate. The encoded certificate is stored <b>binary</b>.
	 */
	SENDER_CERTIFICATE(12, CONTENT_TYPE.BINARY),
	/**
	 * A message field containing the carbon copy (CC) addresses.
	 */
	CARBON_COPY(13),
	/**
	 * The type of a message field containing a signature.
	 */
	SIGNATURE(14, CONTENT_TYPE.BINARY),
	/**
	 * The type of a message field containing the lower bound of a time range.
	 */
	NOT_BEFORE(15),
	/**
	 * The type of a message field containing a timestamp.
	 */
	TIMESTAMP(16, CONTENT_TYPE.BINARY);
	
	
	/**
	 * Contains the identifier of the message field.
	 */
	private final int identifier;
	
	/**
	 * The type of the field content.
	 */
	private final CONTENT_TYPE contentType;
	
	/**
	 * Creates a new field type.
	 * @param identifier The identifier of the field type to create.
	 */
	private DEF_MESSAGE_FIELD(int identifier) {
		this(identifier, CONTENT_TYPE.STRING_LITERAL);
	}
	
	/**
	 * Creates a new field type.
	 * @param identifier The identifier 
	 * @param contentType
	 */
	private DEF_MESSAGE_FIELD(int identifier, CONTENT_TYPE contentType){
		this.identifier = identifier;
		this.contentType = contentType;
	}
	
	/**
	 * Returns the identifier of this field.
	 * @return The identifier of this field.
	 */
	public int getIdentifier(){ return identifier; }
	
	/**
	 * Returns the content type of the message field.
	 * @return The content type of the message field.
	 */
	public CONTENT_TYPE getContentType(){ return contentType; }
	
	/**
	 * Searches all well known message fields for the given identifier.
	 * @param identifier The identifier to search for.
	 * @return The field type with the given identifier or {@link DEF_MESSAGE_FIELD#UNKNOWN} if no type was found.
	 */
	public static DEF_MESSAGE_FIELD find(int identifier){
		for(DEF_MESSAGE_FIELD f : DEF_MESSAGE_FIELD.values()){
			if(f.getIdentifier() == identifier) return f;
		}
		return UNKNOWN;
	}
	
}
