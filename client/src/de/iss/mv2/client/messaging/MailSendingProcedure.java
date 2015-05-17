package de.iss.mv2.client.messaging;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import de.iss.mv2.client.data.MailBoxSettings;
import de.iss.mv2.client.data.MailMessage;
import de.iss.mv2.client.io.ClientProvider;
import de.iss.mv2.client.io.ClientProviderImpl;
import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.security.MessageCryptorSettings;

/**
 * A procedure that sends a message.
 * 
 * @author Marcel Singer
 *
 */
public class MailSendingProcedure extends
		MessageProcedure<RequestException, Void> {

	/**
	 * Holds the message to send.
	 */
	private final MailMessage message;
	/**
	 * Holds the mail account to use.
	 */
	private final MailBoxSettings account;

	/**
	 * The provider to encrypt the message content.
	 */
	private final MessageCryptorSettings cryptorSettings;

	/**
	 * Holds the client provider.
	 */
	private final ClientProvider clientProvider = new ClientProviderImpl();

	/**
	 * Creates a new instance of {@link MailSendingProcedure}.
	 * 
	 * @param cryptorSettings
	 *            The settings to use to encrypt the message.
	 * @param message
	 *            The message to send.
	 * @param account
	 *            The account to be used to send the given message.
	 */
	public MailSendingProcedure(MessageCryptorSettings cryptorSettings,
			MailMessage message, MailBoxSettings account) {
		super(null);
		this.cryptorSettings = cryptorSettings;
		this.message = message;
		this.account = account;
	}

	@Override
	protected Void doPerform(MV2Client client) throws IOException,
			RequestException {
		int steps = message.getReceivers().length;
		MV2Client current = null;
		Map<String, Exception> fails = new HashMap<String, Exception>();
		String receiver;
		X509Certificate cert = null;
		for (int i = 0; i < steps; i++) {
			receiver = message.getReceivers()[i];
			try {
			current = clientProvider
					.connectToWebSpace(receiver);
			
				new InitialProcedure(current).runImmediate();
			} catch (ProcedureException | IOException e ) {
				fails.put(message.getReceivers()[i], e);
				update("Can't connect to '" + receiver + "'.");
				continue;
			}
			update("Requesting the certificate of '" + receiver +"'.");
			try {
				cert = getCertificate(current, receiver);
			} catch (ProcedureException | IOException  e) {
				fails.put("Can't obtain the receivers ('" + receiver + "') certificate.", e);
				update("Can't obtain the receivers ('" + receiver + "') certificate.");
				continue;
			}
			
			update((int) (((i + 1.0) * 100.0) / (steps * 1.0)));
		}

		return null;
	}
	
	/**
	 * Encrypts the given message. 
	 * @param message The message to encrypt.
	 * @param cert The certificate to use for the encryption.
	 */
	private void encryptMessage(MailMessage message, X509Certificate cert){
		
	}
	
	/**
	 * Requests the certificate of the given receiver.
	 * @param client The client to use for the request.
	 * @param receiver The receiver thats certificate should be obtained.
	 * @return The certificate of the given receiver.
	 * @throws IOException If an I/O error occurs.
	 * @throws ProcedureException If the procedure fails for any other reason.
	 */
	private X509Certificate getCertificate(MV2Client client, String receiver) throws IOException, ProcedureException{
		ClientCertificateRequestProcedure ccrp = new ClientCertificateRequestProcedure(client, receiver);
		return ccrp.runImmediate();
	}

	@Override
	protected void handleCommunicationException(IOException exception) {

	}

	@Override
	protected void handleProcedureException(Throwable exception) {

	}

}
