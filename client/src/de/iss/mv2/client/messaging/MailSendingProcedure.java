package de.iss.mv2.client.messaging;

import java.io.IOException;

import de.iss.mv2.client.io.MV2Client;

/**
 * A procedure that sends a message.
 * @author Marcel Singer
 *
 */
public class MailSendingProcedure extends MessageProcedure {

	
	public MailSendingProcedure(MV2Client client) {
		super(client);
		
	}

	@Override
	protected Object doPerform(MV2Client client) throws IOException, Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void handleCommunicationException(IOException exception) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleProcedureException(Throwable exception) {
		// TODO Auto-generated method stub

	}

}
