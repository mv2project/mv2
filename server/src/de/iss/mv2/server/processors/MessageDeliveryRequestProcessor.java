package de.iss.mv2.server.processors;

import java.io.IOException;
import java.util.NoSuchElementException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageDeliveryRequest;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.messaging.UnableToProcessMessage;
import de.iss.mv2.server.data.WebSpace;
import de.iss.mv2.server.data.WebSpaceManager;

/**
 * A processor to handle incoming {@link MessageDeliveryRequest} message.
 * 
 * @author Marcel Singer
 *
 */
public class MessageDeliveryRequestProcessor implements MessagePreProcessor,
		MessageProcessor {

	/**
	 * Holds the current {@link WebSpaceManager}.
	 */
	private final WebSpaceManager webSpaceManager;

	/**
	 * Creates a new instance of {@link MessageDeliveryRequestProcessor}.
	 * 
	 * @param webSpaceManager
	 *            An object that manages the web spaces of this server.
	 */
	public MessageDeliveryRequestProcessor(WebSpaceManager webSpaceManager) {
		this.webSpaceManager = webSpaceManager;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if (message == null)
			return false;
		if (message instanceof MessageDeliveryRequest) {
			MessageDeliveryRequest mdr = (MessageDeliveryRequest) message;
			WebSpace ws = null;
			try {
				ws = webSpaceManager.getWebSpace(mdr.getReceiver());
			} catch (NoSuchElementException ex) {
				ws = null;
			}
			if (ws == null) {
				fail(client, "No such web space.");
				return true;
			}
			webSpaceManager.storeMessage(ws, mdr.getContent(),
					mdr.getEncryptedSymmetricKey(),
					mdr.getSymmetricAlgorithmName());
			MV2Message m = new MV2Message(STD_MESSAGE.MESSAGE_DELIVERY_RESPONSE);
			client.send(m);
		}
		return false;
	}

	/**
	 * Fails the request with the given cause.
	 * 
	 * @param client
	 *            The requesting client.
	 * @param cause
	 *            The cause to supply.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	private void fail(CommunicationPartner client, String cause)
			throws IOException {
		UnableToProcessMessage utp = new UnableToProcessMessage();
		utp.setCause(cause);
		client.send(utp);
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if (message.getMessageIdentifier() == STD_MESSAGE.MESSAGE_DELIVERY_REQUEST
				.getIdentifier()) {
			MessageDeliveryRequest mdr = new MessageDeliveryRequest();
			MV2Message.merge(mdr, message);
			return mdr;
		}
		return message;
	}

}
