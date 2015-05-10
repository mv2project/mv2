package de.iss.mv2.server.data;

import de.iss.mv2.io.CommunicationPartner;
import de.iss.mv2.server.io.ClientThread;

/**
 * Default implementation of {@link SessionManager}.
 * @author Marcel Singer
 *
 */
public class SessionManagerImpl implements SessionManager {

	/**
	 * Creates a new instance of {@link SessionManager}.
	 */
	public SessionManagerImpl() {
		
	}

	@Override
	public boolean hasActiveLoginProcedure(CommunicationPartner client) {
		return ((ClientThread) client).hasActiveLoginProcedure();
	}

	@Override
	public String getLoginIdentifier(CommunicationPartner client) {
		return ((ClientThread) client).getIdentifier();
	}

	@Override
	public boolean isAuthenticated(CommunicationPartner client) {
		return ((ClientThread) client).isAuthenticated();
	}

	@Override
	public void startLoginProcedure(CommunicationPartner client,
			String identifier, byte[] testPhrase) {
		ClientThread cT = (ClientThread) client;
		cT.setHasActiveLoginProcedure(true);
		cT.setIdentifier(identifier);
		cT.setLoginTestPhrase(testPhrase);
	}

	@Override
	public void cancelLoginProcedure(CommunicationPartner client) {
		ClientThread cT = (ClientThread) client;
		cT.setHasActiveLoginProcedure(false);
		cT.setAuthenticated(false);
		cT.setHasActiveLoginProcedure(false);
		cT.setLoginTestPhrase(null);
	}

	@Override
	public void completeLoginProcedure(CommunicationPartner client) {
		ClientThread cT = (ClientThread) client;
		cT.setHasActiveLoginProcedure(false);
		cT.setAuthenticated(true);
	}

	@Override
	public String getIdentifier(CommunicationPartner client) {
		return ((ClientThread) client).getIdentifier();
	}

	@Override
	public byte[] getLoginTestPhrase(CommunicationPartner client) {
		return ((ClientThread) client).getLoginTestPhrase();
	}

}
