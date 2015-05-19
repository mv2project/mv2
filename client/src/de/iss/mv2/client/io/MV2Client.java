package de.iss.mv2.client.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.AlreadyBoundException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.iss.mv2.client.processors.CertResponseProcessor;
import de.iss.mv2.client.processors.DomainNamesResponseProcessor;
import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.messaging.EncryptedMessage;
import de.iss.mv2.messaging.HelloMessage;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageParser;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.security.AESWithRSACryptoSettings;
import de.iss.mv2.security.CertificateLoader;
import de.iss.mv2.security.CertificateVerificationException;
import de.iss.mv2.security.CertificateVerifier;
import de.iss.mv2.security.MessageCryptorSettings;
import de.iss.mv2.security.SymetricKeyGenerator;

/**
 * A client to connect to a MV2-Server.
 * 
 * @author Marcel Singer
 *
 */
public class MV2Client implements CommunicationPartner {

	/**
	 * Holds the connection to the server.
	 */
	private Socket connection;
	/**
	 * Holds the output stream.
	 */
	private OutputStream out;
	/**
	 * Holds the input stream.
	 */
	private InputStream in;
	/**
	 * Holds the parse to be used.
	 */
	private MessageParser parser;
	/**
	 * Holds a list with registered {@link MessagePreProcessor}s.
	 */
	private final List<MessagePreProcessor> preProcessors = new ArrayList<MessagePreProcessor>();
	/**
	 * Holds a list with registered {@link MessageProcessor}s.
	 */
	private final List<MessageProcessor> processors = new ArrayList<MessageProcessor>();
	/**
	 * Holds a set of trusted certificate authorities.
	 */
	private final Set<X509Certificate> trustedCAs = new HashSet<X509Certificate>();
	/**
	 * Holds the cryptographic settings to be used.
	 */
	private MessageCryptorSettings cryptoSettings = new AESWithRSACryptoSettings();

	/**
	 * Holds the certificate of the server.
	 */
	private X509Certificate serverCert = null;
	
	/**
	 * Holds the alternative domain names of the server.
	 */
	private String[] alternativeNames = new String[0];

	/**
	 * Holds the login state of this client.
	 */
	private boolean isLoggedIn = false;
	
	/**
	 * Creates a new client.
	 */
	public MV2Client() {
		try {
			trust(CertificateLoader.loadFromResource("ca.cert.pem"));
		} catch (Exception ex) {
			System.err.println("Failed to load default root-CA.");
		}
		installDefaultProcessors();
	}
	
	/**
	 * Adds the default processors.
	 */
	private void installDefaultProcessors(){
		registerProcessor(new CertResponseProcessor(this));
		DomainNamesResponseProcessor dnrp = new DomainNamesResponseProcessor(this);
		registerProcessor(dnrp);
		register(dnrp);
	}

	/**
	 * Sets the certificate of the server.
	 * 
	 * @param cert
	 *            The certificate of the server.
	 */
	public void setServerCert(X509Certificate cert) {
		this.serverCert = cert;
	}

	/**
	 * Returns the certificate of the server.
	 * 
	 * @return The certificate of the server.
	 */
	public X509Certificate getServerCertificate() {
		return serverCert;
	}

	/**
	 * Registers the given certificate as trusted CA.
	 * 
	 * @param cert
	 *            The certificate to register.
	 */
	public void trust(X509Certificate cert) {
		trustedCAs.add(cert);
	}

	/**
	 * Removes the given certificate from the set of trusted CAs.
	 * 
	 * @param cert
	 *            The certificate to remove.
	 */
	public void remove(X509Certificate cert) {
		trustedCAs.remove(cert);
	}

	/**
	 * Check if this client trusts the given certificate.
	 * 
	 * @param cert
	 *            The certificate to check.
	 * @return {@code true}, if the given certificate is trusted by this client.
	 */
	public boolean checkTrust(X509Certificate cert) {
		try {
			CertificateVerifier.verifyCertificate(cert, trustedCAs);
		} catch (CertificateVerificationException e) {
			return false;
		}
		return true;
	}

	/**
	 * Registers a {@link MessagePreProcessor}.
	 * 
	 * @param preProcessor
	 *            The preprocessor to register.
	 */
	public void register(MessagePreProcessor preProcessor) {
		if (!preProcessors.contains(preProcessor))
			preProcessors.add(preProcessor);
	}

	/**
	 * Registers a {@link MessageProcessor}.
	 * 
	 * @param processor
	 *            The processor to register.
	 */
	public void registerProcessor(MessageProcessor processor) {
		if (!processors.contains(processor))
			processors.add(processor);
	}
	
	/**
	 * Returns {@code true} if this client is logged in.
	 * @return {@code true} if this client is logged in.
	 */
	public boolean isLoggedIn(){
		return isLoggedIn;
	}
	
	/**
	 * Sets if this client is logged in.
	 * @param loggedIn {@code true} to indicate that this client is logged in.
	 */
	public void setLoggedIn(boolean loggedIn){
		this.isLoggedIn = loggedIn;
	}

	/**
	 * Sets the cryptographic settings to be used by this client.
	 * 
	 * @param settings
	 *            The settings to set.
	 */
	public void setCryptoSettings(MessageCryptorSettings settings) {
		this.cryptoSettings = settings;
		if (parser != null)
			parser.setEncryptionSetting(settings);
	}
	
	/**
	 * Sets the alternative names of the server.
	 * @param alternativeDomainNames The alternative names to set.
	 */
	public void setAlternativeNames(String[] alternativeDomainNames){
		this.alternativeNames = alternativeDomainNames;
	}
	
	/**
	 * Returns the alternative names of the server.
	 * @return The alternative names of the server.
	 */
	public String[] getAlternativeNames(){
		return alternativeNames;
	}

	/**
	 * Connects this client with the specified server.
	 * 
	 * @param host
	 *            The address of the server to connect to.
	 * @param port
	 *            The port of the server to connect to.
	 * @throws UnknownHostException
	 *             If the address of the server can not be resolved.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void connect(String host, int port) throws UnknownHostException,
			IOException {
		if (connection != null)
			throw new AlreadyBoundException();
		connection = new Socket(host, port);
		this.out = connection.getOutputStream();
		this.in = connection.getInputStream();
		this.parser = new MessageParser(in);
		this.parser.setEncryptionSetting(cryptoSettings);
		HelloMessage helloMessage = new HelloMessage();
		helloMessage.setHostName(host);
		send(helloMessage);
	}

	@Override
	public void send(MV2Message m) throws IOException {
		if (connection == null)
			throw new IllegalStateException("This client isn't connected.");
		if (m == null)
			return;
		if (serverCert != null && cryptoSettings != null) {
			EncryptedMessage enc = EncryptedMessage.encrypt(m, cryptoSettings,
					serverCert.getPublicKey(), true);
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
		out.flush();
	}

	/**
	 * Receives a message without processing it.
	 * 
	 * @return The received message.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public MV2Message receive() throws IOException {
		MV2Message message = parser.readNext();
		for (MessagePreProcessor pp : preProcessors) {
			message = pp.prepare(message);
		}
		return message;
	}

	/**
	 * Receives a message and processes it with the registered
	 * {@link MessageProcessor}.
	 * 
	 * @return The received message.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public MV2Message handleNext() throws IOException {
		MV2Message m = receive();
		for (MessageProcessor p : processors) {
			if (p.process(this, m))
				return m;
		}
		return m;
	}

	/**
	 * Disconnects from the server.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void disconnect() throws IOException {
		if (connection == null)
			return;
		out.flush();
		connection.close();
		connection = null;
		out = null;
		in = null;
		parser = null;
		serverCert = null;
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
		return "localhost";
	}

}
