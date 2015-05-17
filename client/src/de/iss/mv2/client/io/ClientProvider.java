package de.iss.mv2.client.io;

import java.io.IOException;

/**
 * A provider to open a connection to given points within the system. 
 * @author Marcel Singer
 *
 */
public interface ClientProvider {
	
	/**
	 * Returns a client connected to the server hosting the given mail address.
	 * @param mailAddress The mail address of the web space to connect to.
	 * @return A connected client.
	 * @throws IOException If an I/O error occurs.
	 */
	public MV2Client connectToWebSpace(String mailAddress) throws IOException;

}
