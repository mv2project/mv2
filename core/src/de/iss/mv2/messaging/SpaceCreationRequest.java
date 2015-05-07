package de.iss.mv2.messaging;

/**
 * The request to create a user space.
 * @author Marcel Singer
 *
 */
public class SpaceCreationRequest extends MV2Message {
	
	
	/**
	 * Creates a new instance of {@link SpaceCreationRequest}.
	 */
	public SpaceCreationRequest(){
		super(STD_MESSAGE.SPACE_CREATION_REQUEST);
	}
	
	/**
	 * Returns the identifier of the message space to create.
	 * @return The identifier of the message space to create.
	 */
	public String getSpaceIdentifier(){
		return getFieldValue(DEF_MESSAGE_FIELD.SPACE_IDENTIFIER, "");
	}
	
	/**
	 * Sets the identifier of the message space to create.
	 * @param spaceIdentifier The identifier to set.
	 * @throws IllegalArgumentException Is thrown if the given identifier is {@code null} or empty.
	 */
	public void setSpaceIdentifier(String spaceIdentifier) throws IllegalArgumentException{
		if(spaceIdentifier == null || spaceIdentifier.isEmpty()) throw new IllegalArgumentException("The identifier may not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.SPACE_IDENTIFIER, spaceIdentifier), true);
	}
	
	/**
	 * Returns the state of the requesting instance.
	 * @return The state of the requesting instance.
	 */
	public String getState(){
		return getFieldValue(DEF_MESSAGE_FIELD.STATE, "");
	}
	
	/**
	 * Sets the state of the requesting instance.
	 * @param state The state to set.
	 * @throws IllegalArgumentException If the given state is {@code null}.
	 */
	public void setState(String state) throws IllegalArgumentException{
		if(state == null) throw new IllegalArgumentException("The state may not be null.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.STATE, state), true);
	}
	
	/**
	 * Returns the location of the requesting instance.
	 * @return The location of the requesting instance.
	 */
	public String getLocation(){
		return getFieldValue(DEF_MESSAGE_FIELD.LOCATION, "");
	}
	
	/**
	 * Sets the location of the requesting instance.
	 * @param location The location to set.
	 * @throws IllegalArgumentException Is thrown if the given location is {@code null}.
	 */
	public void setLocation(String location) throws IllegalArgumentException{
		if(location == null) throw new IllegalArgumentException("The location may not be null.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.LOCATION, location), true);
	}
	
	/**
	 * Returns the country code of the requesting instance.
	 * @return The country code of the requesting instance.
	 */
	public String getCountry(){
		return getFieldValue(DEF_MESSAGE_FIELD.COUNTRY, "");
	}
	
	/**
	 * Sets the county code of the requesting instance.
	 * @param country The country code to set.
	 * @throws IllegalArgumentException Is thrown if the given country code is {@code null} or it's length is not zero or two.
	 */
	public void setCountry(String country) throws IllegalArgumentException{
		if(country == null || (country.length() != 0 && country.length() != 2)) throw new IllegalArgumentException("The country code may not be null and must contain zero or two characters.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.COUNTRY, country), true);
	}
	
	/**
	 * Returns the organization of the requesting instance.
	 * @return The organization of the requesting instance.
	 */
	public String getOrganization() {
		return getFieldValue(DEF_MESSAGE_FIELD.ORGANIZATION, "");
	}
	
	/**
	 * Sets the organization of the requesting instance.
	 * @param organization The organization to set.
	 * @throws IllegalArgumentException Is thrown if the given organization is {@code null}.
	 */
	public void setOrganization(String organization) throws IllegalArgumentException{
		if(organization == null) throw new IllegalArgumentException("The organization may not be null.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.ORGANIZATION, organization), true);
	}
	
	/**
	 * Returns the organization unit of the requesting instance.
	 * @return The organization unit of the requesting instance.
	 */
	public String getOrganizationUnit(){
		return getFieldValue(DEF_MESSAGE_FIELD.ORGANIZATION_UNIT, "");
	}
	
	/**
	 * Sets the organization unit of the requesting instance.
	 * @param organizationUnit The organization unit to set.
	 * @throws IllegalArgumentException Is thrown if the given organization unit is {@code null}.
	 */
	public void setOrganizationUnit(String organizationUnit) throws IllegalArgumentException{
		if(organizationUnit == null) throw new IllegalArgumentException("The organization unit may not be null");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.ORGANIZATION_UNIT, organizationUnit), true);
	}
	
	
}
