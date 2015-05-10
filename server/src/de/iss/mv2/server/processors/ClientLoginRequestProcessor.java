package de.iss.mv2.server.processors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.NoSuchElementException;

import javax.crypto.NoSuchPaddingException;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.logging.LoggerManager;
import de.iss.mv2.messaging.ClientLoginRequest;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageField;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.messaging.ServerLoginResponse;
import de.iss.mv2.security.RSAOutputStream;
import de.iss.mv2.server.data.SessionManager;
import de.iss.mv2.server.data.WebSpace;
import de.iss.mv2.server.data.WebSpaceManager;

/**
 * A processor for incoming {@link ClientLoginRequest} messages.
 * 
 * @author Marcel Singer
 *
 */
public class ClientLoginRequestProcessor implements MessagePreProcessor,
		MessageProcessor {

	/**
	 * The session manager to use.
	 */
	private final SessionManager sessionManager;

	/**
	 * The web space manager to use.
	 */
	private final WebSpaceManager spaceManager;
	
	/**
	 * The secure random to create the test phrases.
	 */
	private SecureRandom secureRandom = new SecureRandom();

	/**
	 * The digest to compose the test phrases hash.
	 */
	private MessageDigest digest;
	
	/**
	 * The length of the test phrase to create.
	 */
	private int testPhraseLength = 12;
	
	/**
	 * Creates a new instance of {@link ClientLoginRequestProcessor}.
	 * 
	 * @param sessionManager
	 *            A manager to manage the clients sessions.
	 * @param spaceManager
	 *            A manager to manage the clients web space.
	 * @throws NoSuchAlgorithmException If the default hash algorithm (SHA-384) was not found.
	 */
	public ClientLoginRequestProcessor(SessionManager sessionManager,
			WebSpaceManager spaceManager) throws NoSuchAlgorithmException {
		this.sessionManager = sessionManager;
		this.spaceManager = spaceManager;
		this.digest = MessageDigest.getInstance("SHA384");
	}

	@Override
	public boolean process(CommunicationPartner client, MV2Message message)
			throws IOException {
		if(message == null) return false;
		if(!ClientLoginRequest.class.isAssignableFrom(message.getClass())) return false;
		ClientLoginRequest clr = (ClientLoginRequest) message;
		if(sessionManager.hasActiveLoginProcedure(client)){
			fail(client, "Illegal session state.");
			return true;
		}
		if(sessionManager.isAuthenticated(client)){
			fail(client, "Illegal session state.");
			return true;
		}
		String identifier = clr.getIdentifier();
		WebSpace ws = null;
		try{
			ws = spaceManager.getWebSpace(identifier);
		}catch(NoSuchElementException e){
			ws = null;
		}
		if(ws == null){
			fail(client, "The provided identifier was invalid.");
			return true;
		}
		byte[] testPhrase = new byte[testPhraseLength];
		secureRandom.nextBytes(testPhrase);
		MessageDigest digest = null;
		try{
			digest = MessageDigest.getInstance(this.digest.getAlgorithm());
		}catch(NoSuchAlgorithmException ex){
			LoggerManager.getCurrentLogger().push(ex);
			fail(client, "Internal server error.");
			return true;
		}
		byte[] testPhraseHash = digest.digest(testPhrase);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RSAOutputStream encryptionStream = null;
		try {
			encryptionStream = new RSAOutputStream(baos, ws.getCertificate().getCertificate().getPublicKey());
		} catch (InvalidKeyException | NoSuchPaddingException
				| NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			LoggerManager.getCurrentLogger().push(e);
			fail(client, "Internal sever error.");
			return true;
		}
		encryptionStream.write(testPhrase);
		encryptionStream.flush();
		encryptionStream.close();
		byte[] encryptedTestPhrase = baos.toByteArray();
		ServerLoginResponse response = new ServerLoginResponse();
		response.setHashAlgorithm(digest.getAlgorithm());
		response.setTestPhrase(encryptedTestPhrase);
		response.setTestPhraseHash(testPhraseHash);
		sessionManager.startLoginProcedure(client, identifier, testPhrase);
		client.send(response);
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
		if(message.getMessageIdentifier() == STD_MESSAGE.CLIENT_LOGIN_REQUEST.getIdentifier()){
			ClientLoginRequest clr = new ClientLoginRequest();
			MV2Message.merge(clr, message);
			return clr;
		}
		return message;
	}

}
