package de.iss.mv2.client.messaging;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.List;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;

import de.iss.mv2.client.data.MailBoxSettings;
import de.iss.mv2.client.data.MailMessage;
import de.iss.mv2.client.data.MailStorage;
import de.iss.mv2.client.io.ClientProvider;
import de.iss.mv2.client.io.ClientProviderImpl;
import de.iss.mv2.client.io.MV2Client;

/**
 * A procedure to update the inbox of a mail account.
 * 
 * @author Marcel Singer
 *
 */
public class InboxUpdateProcedure extends
		MessageProcedure<RequestException, Void> {

	/**
	 * Holds the mail box account to update.
	 */
	private final MailBoxSettings mailBox;

	/**
	 * Holds an object to store the mails.
	 */
	private final MailStorage mailStore;

	/**
	 * Holds the client provider to use.
	 */
	private final ClientProvider clientProvider = new ClientProviderImpl();

	/**
	 * Creates a new instance of {@link InboxUpdateProcedure}.
	 * 
	 * @param account
	 *            The mail box account to update.
	 * @param store
	 *            The store to manage incoming mails.
	 */
	public InboxUpdateProcedure(MailBoxSettings account, MailStorage store) {
		super(null);
		this.mailBox = account;
		this.mailStore = store;
	}

	@Override
	protected Void doPerform(MV2Client client) throws IOException,
			RequestException {
		update("Connecting to the server.");
		client = clientProvider.connectToWebSpace(mailBox.getAddress());
		try {
			new InitialProcedure(client).runImmediate();
		} catch (ProcedureException e) {
			client.disconnect();
			throw new RuntimeException("Could not initialize the connection.",
					e);
		}
		update("Logging in.");
		boolean loggedIn = false;
		PrivateKey key;
		PublicKey publicKey;
		try {
			key = mailBox.getClientKey();
			publicKey = mailBox.getClientCertificate().getPublicKey();
			loggedIn = new LoginProcedure(client, mailBox.getAddress(), key)
					.runImmediate();
		} catch (OperatorCreationException | IllegalStateException
				| ProcedureException | PKCSException | CertificateException e) {
			client.disconnect();
			throw new RequestException("Could not login.", e);
		}
		if (!loggedIn)
			throw new RequestException("The login was invalid.");
		MailMessage newest = mailStore.getNewest();
		Date notBefore = null;
		if (newest != null)
			notBefore = newest.getTimestamp();
		List<Long> newIdentifiers = null;
		update("Fetching list of new messages.");
		try {
			newIdentifiers = new MessageQueryProcedure(client, notBefore)
					.runImmediate();
		} catch (ProcedureException e) {
			client.disconnect();
			throw new RequestException("Could not query new messages.", e);
		}
		long id;
		MailMessage requested = null;
		for (int i = 0; i < newIdentifiers.size(); i++) {
			id = newIdentifiers.get(i);
			update("Fetching message " + (i + 1) + " of "
					+ newIdentifiers.size());
			update((int) ((i * 100.0) / (newIdentifiers.size() * 1.0)));
			try {
				requested = new MessageFetchProcedure(client, id, new KeyPair(
						publicKey, key)).runImmediate();
				mailStore.add(requested);
				mailStore.markChanged();
			} catch (ProcedureException ex) {
				ex.printStackTrace();
				client.disconnect();
				update("Failed to request the message with the identifier '"
						+ id + "'");
			}
		}
		client.disconnect();
		return null;
	}

	@Override
	protected void handleCommunicationException(IOException exception) {

	}

	@Override
	protected void handleProcedureException(Throwable exception) {
	}

}
