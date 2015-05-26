package de.iss.mv2.client.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;

import de.iss.mv2.client.data.MailBoxSettings;
import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.KeyPutRequest;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.STD_MESSAGE;
import de.iss.mv2.security.AESOutputStream;
import de.iss.mv2.security.PassphraseInflator;

/**
 * A procedure to send the clients private key to the mail server.
 * 
 * @author Marcel Singer
 *
 */
public class KeyPutProcedure extends MessageProcedure<RequestException, Void> {

	/**
	 * Holds the passphrase used to generate the encryption- and request-key.
	 */
	private char[] passphrase;

	/**
	 * Holds the mail box.
	 */
	private MailBoxSettings mailBox;

	/**
	 * Creates a new instance of {@link KeyPutProcedure}.
	 * 
	 * @param mailBox
	 *            The mail box thats private key should be stored on the server.
	 * @param passphrase
	 *            The passphrase used to generate the encryption- and
	 *            request-key.
	 */
	public KeyPutProcedure(MailBoxSettings mailBox, char[] passphrase) {
		super(null);
		this.passphrase = passphrase;
		this.mailBox = mailBox;
	}

	@Override
	protected Void doPerform(MV2Client client) throws IOException,
			RequestException {
		update("Connecting to the server...");
		try {
			client = mailBox.getClient();
		} catch (CertificateException e) {
			throw new RequestException(e);
		}
		update("Logging in...");
		LoginProcedure loginProcedure = null;
		PrivateKey privateKey = null;
			try {
				privateKey = mailBox.getClientKey();
				loginProcedure = new LoginProcedure(client, mailBox.getAddress(), privateKey);
			} catch (OperatorCreationException | IllegalStateException
					| PKCSException e) {
				throw new RequestException(e);
			}
		boolean result = false;
		try {
			result = loginProcedure.runImmediate();
		} catch (ProcedureException e) {
			update("Login failed!\n\t" + e.getMessage());
			throw new RequestException("The login failed.", e);
		}
		if(!result) throw new RequestException("Invalid login.");
		PassphraseInflator inflator;
		try {
			inflator = new PassphraseInflator();
		} catch (NoSuchAlgorithmException e) {
			throw new RequestException(e);
		}
		update("Encrypting the private key...");
		byte[] primary = inflator.getPrimaryPassphraseDigest(passphrase);
		byte[] secondary = inflator.getSecondaryPasspharseDigest(passphrase);
		Release();
		KeyPutRequest kpr = new KeyPutRequest();
		kpr.setPassphrase(secondary);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		AESOutputStream aesOut = null;
		try {
			aesOut = new AESOutputStream(baos, primary, Arrays.copyOf(primary, 16));
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new RequestException("Can't initialize the key encryption.", e);
		}
		aesOut.write(privateKey.getEncoded());
		aesOut.flush();
		aesOut.close();
		kpr.setPrivateKey(baos.toByteArray());
		kpr.setPassphrase(baos.toByteArray());
		update("Sending the encrypted privat key.");
		client.send(kpr);
		MV2Message response = client.handleNext();
		if(response.getMessageIdentifier() == STD_MESSAGE.UNABLE_TO_PROCESS.getIdentifier()){
			throw new RequestException(response.getFieldStringValue(DEF_MESSAGE_FIELD.CAUSE, ""));
		}
		if(response.getMessageIdentifier() != STD_MESSAGE.KEY_PUT_RESPONSE.getIdentifier()) throw new RequestException("The server did not respond with the expected message.");
		return null;
	}

	@Override
	protected void handleCommunicationException(IOException exception) {
		Release();
	}

	@Override
	protected void handleProcedureException(Throwable exception) {
		Release();
	}

	/**
	 * Releases hold resources.
	 */
	public void Release() {
		if (passphrase == null)
			return;
		for (int i = 0; i < passphrase.length; i++)
			passphrase[i] = ' ';
		passphrase = new char[0];
		passphrase = null;
		System.gc();
	}

}
