package de.iss.mv2.messaging;

import java.util.Date;

import de.iss.mv2.data.BinaryTools;
import de.iss.mv2.io.DataSource;

/**
 * A message containing the response to a {@link MessageFetchRequest}.
 * 
 * @author Marcel Singer
 *
 */
public class MessageFetchResponse extends MV2Message {

	/**
	 * Creates a new instance of {@link MessageFetchResponse}.
	 */
	public MessageFetchResponse() {
		super(STD_MESSAGE.MESSAGE_FETCH_RESPONSE);
	}

	/**
	 * Sets the fetched message.
	 * 
	 * @param message
	 *            The encoded message to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given data is {@code null} or empty.
	 */
	public void setContentMessage(byte[] message)
			throws IllegalArgumentException {
		if (message == null || message.length == 0)
			throw new IllegalArgumentException(
					"The message must not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY,
				message), true);
	}

	/**
	 * Returns the {@link DataSource} of the fetched message.
	 * 
	 * @return The {@link DataSource} of the fetched message.
	 */
	public DataSource getContentMessage() {
		return getFieldData(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
	}

	/**
	 * Sets the timestamp specifying when the containing message was received.
	 * 
	 * @param timestamp
	 *            The time to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given timestamp is {@code null}.
	 */
	public void setTimestamp(Date timestamp) throws IllegalArgumentException {
		if (timestamp == null)
			throw new IllegalArgumentException(
					"The timestamp must not be null.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.TIMESTAMP,
				BinaryTools.toByteArray(timestamp.getTime())), true);
	}

	/**
	 * Returns the timestamp specifying when the containing message was
	 * received.
	 * 
	 * @return The timestamp specifying when the containing message was
	 *         received.
	 */
	public Date getTimestamp() {
		byte[] cont = getFieldArrayValue(DEF_MESSAGE_FIELD.TIMESTAMP, null);
		if (cont == null || cont.length == 0)
			return null;
		return new Date(BinaryTools.toLong(cont));
	}

}
