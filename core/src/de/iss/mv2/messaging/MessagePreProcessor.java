package de.iss.mv2.messaging;

/**
 * Modifies a received {@link MV2Message}.
 * @author Marcel Singer
 *
 */
public interface MessagePreProcessor {

	/**
	 * Modifies the given message.
	 * @param message The message to modify. 
	 * @return The modified message.
	 */
	public MV2Message prepare(MV2Message message);

}
