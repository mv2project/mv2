package de.iss.mv2.server.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.EncryptedMessage;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageParser;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.security.MessageCryptorSettings;
import de.iss.mv2.security.SymetricKeyGenerator;
import de.iss.mv2.server.processors.EncryptedMessagePreProcessor;

/**
 * A thread to perform the communication with a client.
 * 
 * @author Marcel Singer
 *
 */
public class ClientThread extends Thread implements CommunicationPartner {

	/**
	 * Holds the connection to the client.
	 */
	private final Socket connection;
	/**
	 * Indicates if the communication is about to cancel.
	 */
	private boolean isCanceled = false;
	/**
	 * Holds the parser to be used for incoming messages.
	 */
	private final MessageParser parser;
	/**
	 * Holds a list with all registered listeners.
	 */
	private final List<ClientListener> listeners = new ArrayList<ClientListener>();
	/**
	 * Holds a list with all registered {@link MessagePreProcessor}s.
	 */
	private final List<MessagePreProcessor> preProcessors = new ArrayList<MessagePreProcessor>();

	/**
	 * Holds the name of the host this client is connecting to.
	 */
	private final String hostName;

	/**
	 * {@code true} if this client is actually performing a login procedure.
	 */
	private boolean hasActiveLoginProcedure = false;

	/**
	 * Holds the identifier of the web space this client is currently connected
	 * to.
	 */
	private String identifier;
	
	/**
	 * The test phrase of the login procedure.
	 */
	private byte[] loginTestPhrase;

	/**
	 * {@code true} if this client is authenticated.
	 */
	private boolean isAuthenticated;

	/**
	 * Creates a new instance of {@link ClientThread}.
	 * 
	 * @param connection
	 *            The connection to the client.
	 * @param hostName
	 *            The host name of the host this client is connected to.
	 * @param settings
	 *            The encryption settings to be used.
	 * @param key
	 *            The private key of this instance.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public ClientThread(Socket connection, String hostName,
			MessageCryptorSettings settings, KeyPair key) throws IOException {
		this.connection = connection;
		this.hostName = hostName;
		this.connection.setSoTimeout(1000);
		this.parser = new MessageParser(connection.getInputStream());
		parser.setEncryptionSetting(settings);
		parser.setKey(key);
		register(new EncryptedMessagePreProcessor(this));
	}

	/**
	 * Registers a {@link MessagePreProcessor}.
	 * 
	 * @param p
	 *            The {@link MessagePreProcessor} to register.
	 */
	public void register(MessagePreProcessor p) {
		if (!preProcessors.contains(p))
			preProcessors.add(p);
	}

	/**
	 * Marks the current communication to be about to cancel.
	 */
	public void cancel() {
		isCanceled = true;
	}

	/**
	 * Adds a listener.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addClientListener(ClientListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	/**
	 * Reports the disconnected client to all listeners.
	 */
	private void reportDisconnect() {
		for (ClientListener l : listeners)
			l.clientDisconnected(this);
	}

	/**
	 * Reports a received message to all listeners.
	 * 
	 * @param m
	 */
	private void reportMessage(MV2Message m) {
		for (ClientListener l : listeners)
			l.messageReceived(this, m);
	}

	/**
	 * Returns the connection to the client.
	 * 
	 * @return The connection to the client.
	 */
	public Socket getConnection() {
		return connection;
	}

	@Override
	public void send(MV2Message m) throws IOException {
		OutputStream out = connection.getOutputStream();
		if (parser.getCryptorSettings().getKeyGenerator().hasFixedKeyAndIV()) {
			EncryptedMessage enc = EncryptedMessage.encrypt(m,
					parser.getCryptorSettings(), null, true);
			enc.serialize(out);
			out.flush();
			if (!parser.getCryptorSettings().getKeyGenerator()
					.hasFixedKeyAndIV()) {
				SymetricKeyGenerator keyGen = parser.getCryptorSettings()
						.getKeyGenerator();
				byte[] keyS = enc.getUsedSymmetricKey();
				byte[] ivS = enc.getUsedSymmetricIV();
				keyGen.setFixedKey(keyS);
				keyGen.setFixedIV(ivS);
			}
			return;
		}
		m.serialize(out);
	}

	/**
	 * Returns the current parser.
	 * 
	 * @return The current parser.
	 */
	public MessageParser getParser() {
		return parser;
	}

	@Override
	public void run() {
		super.run();
		MV2Message received;
		while (!isCanceled) {
			try {
				received = parser.readNext();
				for (MessagePreProcessor pp : preProcessors) {
					received = pp.prepare(received);
				}
				reportMessage(received);
			} catch (SocketTimeoutException ex) {

			} catch (EOFException e) {
				cancel();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		System.out.println("Client disconnected!");
		reportDisconnect();
		try {
			connection.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public InetAddress getLocalAddress() {
		return connection.getLocalAddress();
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return connection.getRemoteSocketAddress();
	}

	@Override
	public String getHostName() {
		return hostName;
	}

	/**
	 * Returns {@code true} if this client is currently performing a login procedure.
	 * @return {@code true} if this client is currently performing a login procedure.
	 */
	public boolean hasActiveLoginProcedure() {
		return hasActiveLoginProcedure;
	}

	/**
	 * Sets if this client is currently performing a login procedure.
	 * @param hasActiveLoginProcedure {@code true} if this client is currently performing a login procedure.
	 */
	public void setHasActiveLoginProcedure(boolean hasActiveLoginProcedure) {
		this.hasActiveLoginProcedure = hasActiveLoginProcedure;
	}

	/**
	 * Returns the identifier of the web space this client is connected to.
	 * @return The identifier of the web space this client is connected to.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the identifier of the web space this client is connected to.
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns if the client is authenticated.
	 * @return {@code true} if this client is authenticated.
	 */
	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	/**
	 * Sets if the client is authenticated.
	 * @param isAuthenticated {@code true} if this client is authenticated.
	 */
	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	/**
	 * Returns the login test phrase.
	 * @return The login test phrase.
	 */
	public byte[] getLoginTestPhrase(){
		return loginTestPhrase;
	}
	
	/**
	 * Sets the login test phrase of this client.
	 * @param testPhrase The test phrase to set.
	 */
	public void setLoginTestPhrase(byte[] testPhrase){
		this.loginTestPhrase = testPhrase;
	}
}
