package de.iss.mv2.io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;

import de.iss.mv2.messaging.MV2Message;

/**
 * Represents a communication partner.
 * @author Marcel Singer
 *
 */
public interface CommunicationPartner {

	/**
	 * Sends the given message.
	 * @param m The message to send.
	 * @throws IOException If an I/O error occurs.
	 */
	public void send(MV2Message m) throws IOException;
	
	/**
	 * Returns the local address of this instance.
	 * @return The local address of this instance.
	 */
	@Deprecated
	public InetAddress getLocalAddress();
	
	/**
	 * Returns the remote address of the communication partner.
	 * @return The remote address of the communication partner.
	 */
	@Deprecated
	public SocketAddress getRemoteAddress();
	
	
	
	/**
	 * Returns the name of the host this partner is communicating to.
	 * @return The name of the host this partner is communicating to.
	 */
	public String getHostName();
	
	
}
