package de.iss.mv2.server.processors;

import java.io.IOException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.KeyPutRequest;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.server.data.SessionManager;
import de.iss.mv2.server.data.WebSpace;
import de.iss.mv2.server.data.WebSpaceManager;
import de.iss.mv2.server.io.ReadOnlyAccountsConfig;

/**
 * A processor to handle incoming {@link KeyPutRequest} messages.
 * @author Marcel Singer
 *
 */
public class KeyPutRequestProcessor extends AbstractProcessor {

	/**
	 * Holds the session manager.
	 */
	private final SessionManager sessionManager;
	
	/**
	 * Holds the current {@link WebSpaceManager}.
	 */
	private final WebSpaceManager webSpaceManager;
	
	/**
	 * Holds the configuration of web spaces that are read only.
	 */
	private final ReadOnlyAccountsConfig readOnlyConfig;
	
	/**
	 * Creates a new instance of {@link KeyPutRequestProcessor}.
	 * @param sessionManager The current {@link SessionManager}.
	 * @param webSpaceManager The current {@link WebSpaceManager}.
	 * @param readOnlyConfig The configuration of web spaces that are read only.
	 */
	public KeyPutRequestProcessor(SessionManager sessionManager, WebSpaceManager webSpaceManager, ReadOnlyAccountsConfig readOnlyConfig) {
		this.sessionManager = sessionManager;
		this.webSpaceManager = webSpaceManager;
		this.readOnlyConfig = readOnlyConfig;
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if(message == null) return null;
		if(message.getMessageIdentifier() != STD_MESSAGE.KEY_PUT_REQUEST.getIdentifier()) return message;
		KeyPutRequest kpr = new KeyPutRequest();
		MV2Message.merge(kpr, message);
		return kpr;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if(message == null) return false;
		if(!(message instanceof KeyPutRequest)) return false;
		KeyPutRequest kpr = (KeyPutRequest) message;
		if(!sessionManager.isAuthenticated(client)) {
			fail(client, "Not authenticated.");
			return true;
		}
		if(isReadOnly(sessionManager.getLoginIdentifier(client))){
			fail(client, "Key is read-only.");
			return true;
		}
		WebSpace webSpace = webSpaceManager.getWebSpace(sessionManager.getLoginIdentifier(client));
		webSpaceManager.setPrivateKey(webSpace, kpr.getPassphrase(), kpr.getPrivateKey());
		client.send(new MV2Message(STD_MESSAGE.KEY_PUT_RESPONSE));
		return true;
	}

	/**
	 * Tests if the private key of the given account is read only.
	 * @param identifier The identifier of the account.
	 * @return {@code true} if the private key can not be updated.
	 */
	protected boolean isReadOnly(String identifier){
		return readOnlyConfig.isReadOnly(identifier);
	}
	
}
