package de.iss.mv2.client.io;

import java.io.IOException;

/**
 * The default implementation of {@link ClientProvider}.
 * @author Marcel Singer
 *
 */
public class ClientProviderImpl implements ClientProvider {

	/**
	 * Holds the default port.
	 */
	private int port = 9898;
	
	/**
	 * Creates a new instance of {@link ClientProviderImpl}.
	 */
	public ClientProviderImpl() {
		
	}

	@Override
	public MV2Client connectToWebSpace(String mailAddress) throws IOException {
		if(mailAddress == null) throw new IllegalArgumentException("The mail address may not not be null.");
		String[] args = mailAddress.split("@");
		if(args.length != 2) throw new IllegalArgumentException("The given mail address is invalid.");
		String host = args[1];
		MV2Client client = new MV2Client();
		client.connect(host, port);
		return client;
	}

}
