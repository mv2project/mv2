package de.iss.mv2.client.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.iss.mv2.client.io.MV2Client;
import de.iss.mv2.messaging.DEF_MESSAGE_FIELD;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageQueryRequestMessage;
import de.iss.mv2.messaging.MessageQueryResponseMessage;
import de.iss.mv2.messaging.STD_MESSAGE;

/**
 * A procedure to request a list of available message identifiers.
 * @author Marcel Singer
 *
 */
public class MessageQueryProcedure extends MessageProcedure<RequestException, List<Long>> {

	/**
	 * Holds the date of the first message identifier to return.
	 */
	private final Date notBefore;
	
	/**
	 * Creates a new instance of {@link MessageQueryProcedure}.
	 * @param client The client to use.
	 * @param notBefore The date of the earliest message to return. If {@code null} is given, all available message identifiers will be returned.
	 */
	public MessageQueryProcedure(MV2Client client, Date notBefore) {
		super(client);
		this.notBefore = notBefore;
	}

	@Override
	protected List<Long> doPerform(MV2Client client) throws IOException,
			RequestException {
		if(!client.isLoggedIn()) throw new RequestException("The client is not logged in");
		MessageQueryRequestMessage mqr = new MessageQueryRequestMessage();
		if(notBefore != null) mqr.setNotBefore(notBefore);
		client.send(mqr);
		MV2Message m = client.handleNext();
		if(m.getMessageIdentifier() == STD_MESSAGE.UNABLE_TO_PROCESS.getIdentifier()){
			throw new RequestException("The server responsed with: " + m.getFieldStringValue(DEF_MESSAGE_FIELD.CAUSE, ""));
		}
		if(m.getMessageIdentifier() != STD_MESSAGE.MESSAGE_FETCH_RESPONSE.getIdentifier()) throw new RequestException("The server did not answert with the expected message.");
		MessageQueryResponseMessage response = new MessageQueryResponseMessage();
		MV2Message.merge(response, m);
		return new ArrayList<Long>(response.getMessageIdentifier());
	}

	@Override
	protected void handleCommunicationException(IOException exception) {
		
	}

	@Override
	protected void handleProcedureException(Throwable exception) {
		
	}

}
