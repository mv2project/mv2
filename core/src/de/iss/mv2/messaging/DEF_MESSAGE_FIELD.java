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
	 * A message field containing a Base64 encoded binary value.
	 * @deprecated Use {@link DEF_MESSAGE_FIELD#CONTENT_BINARY}.
	 */
	@Deprecated
	CONTENT_BASE64(3),
	/**
	 * A message field containing the symmetric encryption key and IV.
	 */
	ENCRYPTION_KEY(4, CONTENT_TYPE.BINARY),
	/**
	 * A message field containing the name of the symmetric algorithm.
	 */
	SYMETRIC_ALGORITHM(5),
	/**
	 * A message field containing the name of the asymmetric algorithm.
	 */
	ASYMETRIC_ALGORITHM(6),
	/**
	 * A message field containing the symmetric encryption key and IV.
	 * @deprecated Duplicate of {@link DEF_MESSAGE_FIELD#ENCRYPTION_KEY}.
	 */
	@Deprecated
	KEY(4),
	/**
	 * A message field containing pain text content.
	 */
	CONTENT_PLAIN(7),
	/**
	 * A message field containing a Base64 encoded hash value.
	 * @deprecated Use {@link DEF_MESSAGE_FIELD#HASH_BINARY}.
	 */
	@Deprecated
	HASH_BASE64(8),
	/**
	 * A message field containing the name of a hash algorithm.
	 */
	HASH_ALGORITHM(9),
	/**
	 * A message field containing binary content data.
	 */
	CONTENT_BINARY(10, CONTENT_TYPE.BINARY),
	/**
	 * A message field containing binary hash data.
	 */
	HASH_BINARY(11, CONTENT_TYPE.BINARY),
	/**
	 * A message field containing the receiving web space address.
	 */
	RECEIVER(12),
	/**
	 * A message field containing the subject of a {@link STD_MESSAGE#CONTENT_MESSAGE}.
	 */
	SUBJECT(13),
	/**
	 * A message field containing the senders certificate. The encoded certificate is stored <b>binary</b>.
	 */
	SENDER_CERTIFICATE(14, CONTENT_TYPE.BINARY),
	/**
	 * A message field containing the carbon copy (CC) addresses.
	 */
	CARBON_COPY(15),
	/**
	 * The type of a message field containing a signature.
	 */
	SIGNATURE(16, CONTENT_TYPE.BINARY),
	/**
	 * The type of a message field containing the lower bound of a time range.
	 */
	NOT_BEFORE(17);
	
	
	
	
	
	
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
