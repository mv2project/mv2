package de.iss.mv2.messaging;

/**
 * A message to request the server to delete a stored message.
 * @author Marcel Singer
 *
 */
public class MessageDeleteRequest extends MV2Message {

	/**
	 * Creates a new instance of {@link MessageDeleteRequest}.
	 */
	public MessageDeleteRequest(){
		super(STD_MESSAGE.MESSAGE_DELETE_REQUEST);
	}
	
	
	
}
