package de.iss.mv2.messaging;

import de.iss.mv2.io.DataSource;


/**
 * A message containing an encrypted mail.
 * 
 * @author Marcel Singer
 *
 */
public class MessageDeliveryRequest extends MV2Message {

	/**
	 * Creates a new instance of {@link MessageDeliveryRequest}.
	 */
	public MessageDeliveryRequest() {
		super(STD_MESSAGE.MESSAGE_DELIVERY_REQUEST);
	}

	/**
	 * Sets the content message.
	 * 
	 * @param contentData
	 *            The content message to set.
	 */
	public void setContent(byte[] contentData) {
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY,
				contentData), true);
	}

	/**
	 * Returns the {@link DataSource} that stores the content message.
	 * 
	 * @return The {@link DataSource} that stores the content message.
	 */
	public DataSource getContent() {
		return getFieldData(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
	}
	
	/**
	 * Sets the receiver of this message.
	 * @param receiver The receiver of this message.
	 */
	public void setReceiver(String receiver){
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.RECEIVER, receiver), true); 
	}
	
	/**
	 * Returns the receiver of this message.
	 * @return The receiver of this message.
	 */
	public String getReceiver(){
		return getFieldStringValue(DEF_MESSAGE_FIELD.RECEIVER, null);
	}

}
