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
	 */
	CONTENT_BASE64(3),
	/**
	 * A message field containing the symmetric encryption key and IV.
	 */
	ENCRYPTION_KEY(4),
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
	CONTENT_PLAIN(7);
	
	
	
	/**
	 * Contains the identifier of the message field.
	 */
	private final int identifier;
	
	/**
	 * Creates a new field type.
	 * @param identifier The identifier of the field type to create.
	 */
	private DEF_MESSAGE_FIELD(int identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * Returns the identifier of this field.
	 * @return The identifier of this field.
	 */
	public int getIdentifier(){ return identifier; }
	
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
