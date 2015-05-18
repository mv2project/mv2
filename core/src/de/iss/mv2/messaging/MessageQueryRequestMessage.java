package de.iss.mv2.messaging;

import java.util.Date;

/**
 * A message to request in-box messages of the current client.
 * 
 * @author Marcel Singer
 *
 */
public class MessageQueryRequestMessage extends MV2Message {

	/**
	 * Creates a new instance of {@link MessageQueryRequestMessage}.
	 */
	public MessageQueryRequestMessage() {
		super(STD_MESSAGE.MESSAGE_QUERY_REQUEST);
	}

	/**
	 * Sets the date of the earliest message to return.
	 * 
	 * @param date
	 *            The date of the earliest message to include.
	 */
	public void setNotBefore(Date date) {
		String value = "";
		if (date != null) {
			value = "" + date.getTime();
		}
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.NOT_BEFORE, value),
				true);
	}

	/**
	 * Returns the date of the earliest message to return.
	 * 
	 * @return The date of the earliest message to return.
	 */
	public Date getNotBefore() {
		String value = getFieldStringValue(DEF_MESSAGE_FIELD.NOT_BEFORE, null);
		if (value == null || value.isEmpty())
			return null;
		return new Date(Long.parseLong(value));
	}

}
