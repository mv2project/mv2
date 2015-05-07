package de.iss.mv2.messaging;

/**
 * Represents the response message to a {@link SpaceCreationRequest}.
 * @author Marcel Singer
 *
 */
public class SpaceCreationResponse extends MV2Message {
	
	/**
	 * Creates a new instance of {@link SpaceCreationResponse}.
	 */
	public SpaceCreationResponse(){
		super(STD_MESSAGE.SPACE_CREATION_RESPONSE);
	}
	

}
