package de.iss.mv2.client.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;

import javax.crypto.NoSuchPaddingException;

import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.data.BinaryTools;
import de.iss.mv2.messaging.ClientLoginData;
import de.iss.mv2.messaging.ClientLoginRequest;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.messaging.ServerLoginResponse;
import de.iss.mv2.messaging.UnableToProcessMessage;
import de.iss.mv2.security.RSAInputStream;

/**
 * A procedure performing the login for a web space.
 * 
 * @author Marcel Singer
 *
 */
public class LoginProcedure extends
		SwingMessageProcedure<LoginException, Boolean> {

	/**
	 * Holds the identifier of the web space to connect to.
	 */
	private final String identifier;

	/**
	 * Holds the private key of the web space to connect to.
	 */
	private final PrivateKey privateKey;

	/**
	 * Creates a new instance of {@link LoginProcedure} with the given client
	 * and web space identifier.
	 * 
	 * @param client
	 *            The client connected to the server.
	 * @param identifier
	 *            The identifier of the web space to connect to.
	 * @param privateKey
	 *            The private key of the web space to connect to.
	 */
	public LoginProcedure(MV2Client client, String identifier,
			PrivateKey privateKey) {
		super(client);
		this.identifier = identifier;
		this.privateKey = privateKey;
	}

	@Override
	protected Boolean doPerform(MV2Client client) throws IOException,
			LoginException {
		if(client.isLoggedIn()) return true;
		requestCertificate(client);
		ClientLoginRequest clr = new ClientLoginRequest();
		clr.setIdentifier(identifier);
		client.send(clr);
		MV2Message m = client.handleNext();
		testFailure(m);
		testMessageType(m, STD_MESSAGE.SERVER_LOGIN_RESPONSE);
		ServerLoginResponse slr = new ServerLoginResponse();
		MV2Message.merge(slr, m);
		byte[] testPhraseClear = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(slr
					.getHashAlgorithm());
			byte[] testHash = slr.getTestPhraseHash();
			byte[] testPhrase = slr.getTestPhrase();
			if (testHash == null)
				throw new LoginException("The test phrase hash is missing.");
			if (testPhrase == null)
				throw new LoginException("The test phrase is missing.");
			RSAInputStream ciperIn = new RSAInputStream(
					new ByteArrayInputStream(testHash), privateKey);
			testPhraseClear = BinaryTools.readAll(ciperIn);
			ciperIn.close();
			byte[] myHash = digest.digest(testPhraseClear);
			if (!Arrays.equals(myHash, testHash))
				throw new LoginException(
						"The server supplied an invalid test phrase hash. Maybe the server is not reliable.");
		} catch (NoSuchAlgorithmException ex) {
			throw new LoginException(
					"The server used an unknown hash algorithm.");
		} catch (InvalidKeyException e) {
			throw new LoginException("The supplied key is invalid.");
		} catch (NoSuchPaddingException e) {
			throw new LoginException("The padding to use was not found.");
		} catch (InvalidAlgorithmParameterException e) {
			throw new LoginException(
					"The supplied key does not match the used algorithm.");
		}
		ClientLoginData cld = new ClientLoginData();
		cld.setDecryptedTestPhrase(testPhraseClear);
		client.send(cld);
		m = client.handleNext();
		if (m.getMessageIdentifier() == STD_MESSAGE.UNABLE_TO_PROCESS
				.getIdentifier()
				&& m.getFieldStringValue(DEF_MESSAGE_FIELD.CAUSE, "").equals(
						"Invalid login data."))
			return false;
		testFailure(m);
		testMessageType(m, STD_MESSAGE.SERVER_LOGIN_RESULT);
		client.setLoggedIn(true);
		return true;
	}

	/**
	 * Tests the type of the given message with the given type.
	 * 
	 * @param message
	 *            The message to test.
	 * @param expectedType
	 *            The type to match.
	 * @throws LoginException
	 *             Is thrown if the given message does not match the expected
	 *             type.
	 */
	private void testMessageType(MV2Message message, STD_MESSAGE expectedType)
			throws LoginException {
		if (message.getMessageIdentifier() != expectedType.getIdentifier())
			throw new LoginException(
					"The server did not respond with the expected result.");
	}

	/**
	 * Tests if the given message is a failure message.
	 * 
	 * @param message
	 *            The message to test.
	 * @throws LoginException
	 *             Is thrown if the given message is a failure message.
	 */
	private void testFailure(MV2Message message) throws LoginException {
		if (message.getMessageIdentifier() != STD_MESSAGE.UNABLE_TO_PROCESS
				.getIdentifier())
			return;
		UnableToProcessMessage utp = new UnableToProcessMessage();
		MV2Message.merge(utp, message);
		throw new LoginException(utp.getCause());
	}

	/**
	 * Assures that the severs certificate is loaded.
	 * 
	 * @param client
	 *            The client to use.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws LoginException
	 *             If the login failed because of an exception.
	 */
	private void requestCertificate(MV2Client client) throws IOException,
			LoginException {
		if (client.getServerCertificate() == null) {
			client.send(new MV2Message(STD_MESSAGE.CERT_REQUEST));
		}
		client.handleNext();
		if (client.getServerCertificate() == null)
			throw new LoginException("Could not get the servers certificate.");
	}

	@Override
	protected void handleCommunicationException(IOException exception) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleProcedureException(Throwable exception) {
		// TODO Auto-generated method stub

	}

}
