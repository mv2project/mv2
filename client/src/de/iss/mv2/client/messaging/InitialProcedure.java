package de.iss.mv2.client.messaging;

import java.io.IOException;

import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.STD_MESSAGE;

/**
 * A procedure that initially requests the servers certificate.
 * @author Marcel Singer
 *
 */
public class InitialProcedure extends MessageProcedure<RequestException, Void> {

	/**
	 * Creates a new {@link InitialProcedure}.
	 * @param client The client to use.
	 */
	public InitialProcedure(MV2Client client) {
		super(client);
	}

	@Override
	protected Void doPerform(MV2Client client) throws IOException,
			RequestException {
		if (client.getServerCertificate() != null)
			return null;
		MV2Message m = new MV2Message(STD_MESSAGE.CERT_REQUEST);
		client.send(m);
		m = client.handleNext();
		if(client.getServerCertificate() == null) throw new RequestException("The client was unable to retrieve the servers certificate.");
		return null;
	}

	@Override
	protected void handleCommunicationException(IOException exception) {
		
	}

	@Override
	protected void handleProcedureException(Throwable exception) {
		
	}

}
