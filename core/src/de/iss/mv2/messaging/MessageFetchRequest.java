package de.iss.mv2.messaging;

import de.iss.mv2.data.BinaryTools;

/**
 * A message to request a mail message with the given identifier.
 * @author Marcel Singer
 */
public class MessageFetchRequest extends MV2Message {

	/**
	 * Creates a new instance of {@link MessageFetchRequest}.
	 */
	public MessageFetchRequest() {
		super(STD_MESSAGE.MESSAGE_FETCH_REQUEST);
	}
	
	/**
	 * Sets the identifier of the message to fetch.
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(long identifier){
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY, BinaryTools.toByteArray(identifier)), true);
	}
	
	/**
	 * Returns the identifier of the message to fetch.
	 * @return The identifier of the message to fetch.
	 */
	public long getIdentifier(){
		return BinaryTools.toLong(getFieldArrayValue(DEF_MESSAGE_FIELD.CONTENT_BINARY, BinaryTools.toByteArray(0l)));
	}

}
