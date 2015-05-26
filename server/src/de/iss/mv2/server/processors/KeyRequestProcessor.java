package de.iss.mv2.server.processors;

import java.io.IOException;
import java.util.NoSuchElementException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.KeyRequest;
import de.iss.mv2.messaging.KeyResponse;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.server.data.WebSpace;
import de.iss.mv2.server.data.WebSpaceManager;

/**
 * A processor to handle incoming {@link KeyRequest} messages.
 * 
 * @author Marcel Singer
 *
 */
public class KeyRequestProcessor extends AbstractProcessor {

	/**
	 * Holds the current web space manager.
	 */
	private final WebSpaceManager webSpaceManager;

	/**
	 * Creates a new instance of {@link KeyRequestProcessor}.
	 * 
	 * @param webSpaceManager
	 *            The current web space manager.
	 */
	public KeyRequestProcessor(WebSpaceManager webSpaceManager) {
		this.webSpaceManager = webSpaceManager;
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if (message == null)
			return null;
		if (message.getMessageIdentifier() != STD_MESSAGE.KEY_REQUEST
				.getIdentifier())
			return message;
		KeyRequest kr = new KeyRequest();
		MV2Message.merge(kr, message);
		return kr;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if (message == null || !(message instanceof KeyRequest))
			return false;
		KeyRequest kr = (KeyRequest) message;
		byte[] passphrase = kr.getPassphrase();

		WebSpace webSpace;
		try {
			webSpace = webSpaceManager.getWebSpace(kr.getIdentifier());
		} catch (NoSuchElementException e) {
			fail(client, "Invalid identifier.");
			return true;
		}
		byte[] key = null;
		try {
			key = webSpaceManager.getPrivateKey(webSpace, passphrase);
		} catch (IllegalArgumentException ex) {
			fail(client, "Invalid passphrase.");
			return true;
		} catch (NoSuchElementException ex) {
			fail(client, "Key not found.");
			return true;
		}
		KeyResponse response = new KeyResponse();
		response.setKey(key);
		client.send(response);
		return true;
	}

}
