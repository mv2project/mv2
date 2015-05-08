package de.iss.mv2.server.processors;

import de.iss.mv2.messaging.HelloMessage;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;

/**
 * A processor to handle an incoming {@link HelloMessage}.
 * 
 * @author Marcel Singer
 *
 */
public class HelloMessageProcessor implements MessagePreProcessor {

	/**
	 * Creates a new instance of {@link HelloMessageProcessor}.
	 */
	public HelloMessageProcessor() {
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if (message.getMessageIdentifier() == STD_MESSAGE.HELLO.getIdentifier()) {
			HelloMessage hm = new HelloMessage();
			MV2Message.merge(hm, message);
			return hm;
		}
		return message;
	}
	
	/**
	 * Reads the host name from the message.
	 * @param message The message to read from.
	 * @return The read host name.
	 */
	public String getHostName(MV2Message message){
		if(message == null) return null;
		if(!HelloMessage.class.isAssignableFrom(message.getClass())) return null;
		return ((HelloMessage) message).getHostName();
	}

}
