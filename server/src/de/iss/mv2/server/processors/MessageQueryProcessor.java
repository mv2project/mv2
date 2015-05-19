package de.iss.mv2.server.processors;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageQueryRequestMessage;
import de.iss.mv2.messaging.MessageQueryResponseMessage;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.server.data.SessionManager;
import de.iss.mv2.server.data.WebSpace;
import de.iss.mv2.server.data.WebSpaceManager;

/**
 * A processor to handle an incoming {@link MessageQueryRequestMessage}.
 * 
 * @author singer
 * 
 */
public class MessageQueryProcessor extends AbstractProcessor {

	/**
	 * Holds the current session manager.
	 */
	private final SessionManager sessionManager;
	/**
	 * Holds the current web space manager.
	 */
	private final WebSpaceManager webSpaceManager;

	/**
	 * Creates a new instance of {@link MessageQueryProcessor}.
	 * 
	 * @param sessionManager
	 *            The session manager to use.
	 * @param webSpaceManager
	 *            The web space manager to use.
	 */
	public MessageQueryProcessor(SessionManager sessionManager,
			WebSpaceManager webSpaceManager) {
		this.sessionManager = sessionManager;
		this.webSpaceManager = webSpaceManager;
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if (message == null)
			return null;
		if (message.getMessageIdentifier() != STD_MESSAGE.MESSAGE_QUERY_REQUEST
				.getIdentifier())
			return message;
		MessageQueryRequestMessage mqr = new MessageQueryRequestMessage();
		MV2Message.merge(mqr, message);
		return mqr;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if (message == null)
			return false;
		if (!(message instanceof MessageQueryRequestMessage))
			return false;
		MessageQueryRequestMessage mqr = (MessageQueryRequestMessage) message;
		if (!sessionManager.isAuthenticated(client)) {
			fail(client, "Not authenticated.");
			return true;
		}
		String identifier = sessionManager.getLoginIdentifier(client);
		WebSpace webSpace = null;
		try {
			webSpace = webSpaceManager.getWebSpace(identifier);
		} catch (NoSuchElementException ex) {
			fail(client, "There is no web space for the current client.");
			return true;
		}
		Date notBefore = mqr.getNotBefore();
		if (notBefore == null)
			notBefore = new Date(0);
		List<Long> messages = webSpaceManager.getMessages(webSpace, notBefore);
		long[] messageIdentifiers = new long[messages.size()];
		for (int i = 0; i < messageIdentifiers.length; i++)
			messageIdentifiers[i] = messages.get(i);
		MessageQueryResponseMessage response = new MessageQueryResponseMessage();
		response.setMessageIdentifiers(messageIdentifiers);
		client.send(response);
		return true;
	}

}
