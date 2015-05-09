package de.iss.mv2.client.data;

import java.security.KeyPair;
import java.util.Observable;

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
	
}
