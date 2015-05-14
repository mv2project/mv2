package de.iss.mv2.client.messaging;

import java.io.IOException;

import javax.sound.midi.VoiceStatus;

import de.iss.mv2.client.io.MV2Client;

public class InitialProcedure extends MessageProcedure<RequestException, Void> {

	public InitialProcedure() {
	}

	@Override
	protected Void doPerform(MV2Client client) throws IOException,
			RequestException {
		if(client.getServerCertificate() != null) return null;
		
		
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
