package de.iss.mv2.client.data;

import java.io.IOException;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Observable;

import org.bouncycastle.operator.OperatorCreationException;

import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.messaging.DomainNamesRequest;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.STD_MESSAGE;

/**
 * A class to store all data used in the process of creating/requesting a web space.
 * @author Marcel Singer
 *
 */
public class WebSpaceSetup extends Observable {

	

	/**
	 * The clients key pair.
	 */
	private KeyPair clientKey;

	/**
	 * The host to connect to.
	 */
	private String host;
	/**
	 * The port to connect to.
	 */
	private int port;
	
	/**
	 * The servers certificate.
	 */
	private X509Certificate serverCertificate;
	
	/**
	 * The clients certificate.
	 */
	private X509Certificate clientCertificate;
	
	/**
	 * Creates a new instance of {@link WebSpaceSetup}.
	 */
	public WebSpaceSetup() {
		
	}
	
	/**
	 * Returns the host to connect to.
	 * @return The host to connect to.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host to connect to.
	 * @param host The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns the port to connect to.
	 * @return The port to connect to.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port to connect to.
	 * @param port The port to set.
	 */
	public void setPort(int port) {
		this.port = port;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Connects to the given server.
	 * 
	 * @param host
	 *            The address of the server.
	 * @param port
	 *            The port of the server.
	 * @return A connected client instance.
	 * @throws Exception
	 *             If the connection can't be opened for any reasons.
	 */
	public MV2Client tryConnect(String host, int port) throws Exception {
		MV2Client client = new MV2Client();
		client.connect(host, port);
		MV2Message message = new MV2Message(STD_MESSAGE.CERT_REQUEST);
		client.send(message);
		message = client.handleNext();

		if (message.getMessageIdentifier() != STD_MESSAGE.DOMAIN_NAMES_RESPONSE
				.getIdentifier()) {
			message = new DomainNamesRequest();
			client.send(message);
			client.handleNext();
		}
		return client;
	}
	
	/**
	 * Connects to the given server.
	 * @return A connected client instance.
	 * @throws Exception If the connection can't be opened for any reasons.
	 */
	public MV2Client tryConnect() throws Exception{
		return tryConnect(host, port);
	}
	
	/**
	 * Returns the clients key.
	 * @return The clients key.
	 */
	public KeyPair getClientKey(){ return clientKey; }
	
	/**
	 * Sets the clients key.
	 * @param key The key to set.
	 */
	public void setClientKey(KeyPair key){
		this.clientKey = key;
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns the servers certificate.
	 * @return The servers certificate.
	 */
	public X509Certificate getServerCertificate() {
		return serverCertificate;
	}

	/**
	 * Sets the servers certificate.
	 * @param serverCertificate The server certificate to set.
	 */
	public void setServerCertificate(X509Certificate serverCertificate) {
		this.serverCertificate = serverCertificate;
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns the clients certificate.
	 * @return The clients certificate.
	 */
	public X509Certificate getClientCertificate() {
		return clientCertificate;
	}

	/**
	 * Sets the clients certificate.
	 * @param clientCertificate The certificate to set.
	 */
	public void setClientCertificate(X509Certificate clientCertificate) {
		this.clientCertificate = clientCertificate;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Completes this setup.
	 * @param identifier The identifier of the created web space.
	 * @throws IOException If an I/O exception occurs.
	 * @throws CertificateEncodingException  If the certificate can not be encoded.
	 * @throws IllegalStateException If the settings are in a illegal state.
	 * @throws OperatorCreationException If the operator used to encrypt the key can not be found.
	 */
	public void complete(String identifier) throws CertificateEncodingException, IOException, OperatorCreationException, IllegalStateException{
		MailBoxSettings mbs = new MailBoxSettings();
		mbs.setAddress(identifier + "@" + host);
		mbs.setClientCertificate(clientCertificate);
		mbs.setServerCertificate(serverCertificate);
		mbs.setClientKey(clientKey.getPrivate());
		mbs.setHost(host);
		mbs.setServerPort(port);
		MV2ClientSettings.getRuntimeSettings().addMailBox(mbs);
	}
	
}
