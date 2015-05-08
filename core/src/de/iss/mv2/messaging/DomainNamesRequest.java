package de.iss.mv2.messaging;

/**
 * A message to request the available domain names from a server.
 * @author Marcel Singer
 *
 */
public class DomainNamesRequest extends MV2Message{

	/**
	 * Creates a new instance of {@link DomainNamesRequest}.
	 */
	public DomainNamesRequest() {
		super(STD_MESSAGE.DOMAIN_NAMES_REQUEST);
	}
	
	

}
