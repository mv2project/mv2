package de.iss.mv2.messaging;

/**
 * Represents a message informing about the inability to process a prior message.
 * @author Marcel Singer
 *
 */
public class UnableToProcessMessage extends MV2Message {

	
	
	/**
	 * Creates a new instance of {@link UnableToProcessMessage}.
	 */
	public UnableToProcessMessage(){
		super(STD_MESSAGE.UNABLE_TO_PROCESS);
	}
	
	/**
	 * Sets the cause for this message.
	 * @param cause The cause to set.
	 */
	public void setCause(String cause){
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CAUSE, cause), true);
	}
	
	/**
	 * Returns the cause for this message.
	 * @return The cause for this message.
	 */
	public String getCause(){
		return getFieldStringValue(DEF_MESSAGE_FIELD.CAUSE, "");
	}
	
	

}
