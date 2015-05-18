package de.iss.mv2.messaging;

/**
 * A message containing the identifiers of the queried mails.
 * @author Marcel Singer
 *
 */
public class MessageQueryResponseMessage extends MV2Message {

	/**
	 * Creates a new instance of {@link MessageQueryResponseMessage}.
	 */
	public MessageQueryResponseMessage() {
		super(STD_MESSAGE.MESSAGE_QUERY_RESPONSE);
	}
	
	/**
	 * Sets the identifiers of the queried mails.
	 * @param identifiers The identifiers to set.
	 */
	public void setMessageIdentifiers(long[] identifiers){
		if(identifiers == null) identifiers = new long[0];
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<identifiers.length; i++){
			if(i > 0) sb.append(";");
			sb.append(identifiers[i]);
		}
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN, sb.toString()), true);
	}
	
	/**
	 * Returns the identifiers of the queried mails.
	 * @return The identifiers of the queried mails.
	 */
	public long[] getMessageIdentifiers(){
		String value = getFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, "");
		if(value == null || value.isEmpty()) return new long[0];
		String[] vals = value.trim().split(";");
		long[] result = new long[vals.length];
		for(int i=0; i<result.length; i++){
			result[i] = Long.parseLong(vals[i]);
		}
		return result;
	}

}
