package de.iss.mv2.messaging;

/**
 * The response to a {@link DomainNamesRequest} containing the available domain names.
 * @author Marcel Singer
 *
 */
public class DomainNamesResponse extends MV2Message {

	/**
	 * Creates a new instance of  {@link DomainNamesResponse}.
	 */
	public DomainNamesResponse() {
		super(STD_MESSAGE.DOMAIN_NAMES_RESPONSE);
	}
	
	
	/**
	 * Sets the available domain names.
	 * @param domainNames The domain names to set.
	 */
	public void setAvailableDomainNames(String[] domainNames){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<domainNames.length; i++){
			if(i > 0) sb.append(";");
			sb.append(domainNames[i]);
		}
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN, sb.toString()), true);
	}

	/**
	 * Returns the available domain names.
	 * @return The available domain names.
	 */
	public String[] getAvailableDomainNames(){
		String cont = getFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, null);
		if(cont == null) return new String[0];
		return cont.split(";");
	}
	
	
	
}
