package de.iss.mv2.server.processors;

import java.io.IOException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.ClientLoginData;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageField;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.server.data.SessionManager;

/**
 * A processor to handle incoming {@link ClientLoginData} messages.
 * @author Marcel Singer
 *
 */
public class ClientLoginDataProcessor implements MessagePreProcessor, MessageProcessor {

	/**
	 * The session manager to use.
	 */
	private final SessionManager sessionManager;
	
	/**
	 * Creates a new instance of {@link ClientLoginDataProcessor}.
	 * @param sessionManager An instance of {@link SessionManager} to manage the session states of the requesting clients.
	 */
	public ClientLoginDataProcessor(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if(message == null) return false;
		if(!ClientLoginData.class.isAssignableFrom(message.getClass())) return false;
		ClientLoginData cld = (ClientLoginData) message;
		if(!sessionManager.hasActiveLoginProcedure(client)){
			fail(client, "Illegal state.");
			return true;
		}
		if(sessionManager.isAuthenticated(client)){
			fail(client, "Illegal state.");
			return true;
		}
		byte[] testPhrase = sessionManager.getLoginTestPhrase(client);
		byte[] clientPhrase = cld.getDecryptedTestPhrase();
		boolean correct = (testPhrase.length == clientPhrase.length);
		if(correct){
			for(int i=0; i<testPhrase.length; i++){
				if(testPhrase[i] != clientPhrase[i]){
					correct = false;
					break;
				}
			}
		}
		if(!correct){
			fail(client, "Invalid login data.");
			return true;
		}
		sessionManager.completeLoginProcedure(client);
		MV2Message m = new MV2Message(STD_MESSAGE.SERVER_LOGIN_RESULT);
		client.send(m);
		return true;
	}
	
	/**
	 * Fails the clients request with the given reason.
	 * @param client The client thats request failed.
	 * @param message The reason why the request failed.
	 * @throws IOException If an I/O error occurs.
	 */
	private void fail(CommunicationPartner client, String message) throws IOException{
		MV2Message m = new MV2Message(STD_MESSAGE.UNABLE_TO_PROCESS);
		m.setMessageField(new MessageField(DEF_MESSAGE_FIELD.CAUSE, message), true);
		client.send(m);
	}

	@Override
	public MV2Message prepare(MV2Message message) {
		if(message == null) return message;
		if(message.getMessageIdentifier() == STD_MESSAGE.CLIENT_LOGIN_DATA.getIdentifier()){
			ClientLoginData cld = new ClientLoginData();
			MV2Message.merge(cld, message);
			return cld;
		}
		return message;
	}

}
