package de.iss.mv2.server.processors;

import java.io.IOException;
import java.util.NoSuchElementException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageFetchRequest;
import de.iss.mv2.messaging.MessageFetchResponse;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.server.data.Message;
import de.iss.mv2.server.data.SessionManager;
import de.iss.mv2.server.data.WebSpace;
import de.iss.mv2.server.data.WebSpaceManager;

/**
 * A message processor to handle incoming {@link MessageFetchRequest}.
 * 
 * @author Marcel Singer
 * 
 */
public class MessageFetchProcessor extends AbstractProcessor {

	/**
	 * Holds the session manager to use.
	 */
	private final SessionManager sessionManager;
	/**
	 * Holds the web space manager to use.
	 */
	private final WebSpaceManager webSpaceManager;

	/**
	 * Creates a new instance of {@link MessageFetchProcessor}.
	 * 
	 * @param sessionManager
	 *            The session manager to use.
	 * @param webSpaceManager
	 *            The web space manager to use.
	 */
	public MessageFetchProcessor(SessionManager sessionManager,
			WebSpaceManager webSpaceManager) {
		this.sessionManager = sessionManager;
		this.webSpaceManager = webSpaceManager;
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if (message == null)
			return message;
		if (message.getMessageIdentifier() != STD_MESSAGE.MESSAGE_FETCH_REQUEST
				.getIdentifier())
			return message;
		MessageFetchRequest mfr = new MessageFetchRequest();
		MV2Message.merge(mfr, message);
		return mfr;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if (!(message instanceof MessageFetchRequest))
			return false;
		MessageFetchRequest mfr = (MessageFetchRequest) message;
		if(!sessionManager.isAuthenticated(client)){
			fail(client, "Not authenticated.");
			return true;
		}
		String loginIdentifier = sessionManager.getLoginIdentifier(client);
		WebSpace webSpace = null;
		try{
			webSpace = webSpaceManager.getWebSpace(loginIdentifier);
		}catch(NoSuchElementException e){
			fail(client, "There is no web space for the current client.");
			return true;
		}
		Message m = null;
		try{
			m = webSpaceManager.getMessage(webSpace, mfr.getIdentifier());
		}catch(NoSuchElementException e){
			fail(client, "There is no message for the given identifier.");
			return true;
		}
		MessageFetchResponse response = new MessageFetchResponse();
		response.setContentMessage(m.getContent());
		response.setTimestamp(m.getTimestamp());
		client.send(response);
		return true;
	}

}
