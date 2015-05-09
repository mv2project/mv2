package de.iss.mv2.client.gui;

/**
 * Provides information about a current assistant step.
 * @author Marcel Singer
 *
 */
public interface AssistantStep {
	
	
	/**
	 * Tests if this step is completed and the assistant can proceed with the next step.
	 * @return {@code true} if this step is completed.
	 */
	public boolean canProceed();
	
	/**
	 * Tests if this step can be performed a second time.
	 * @return {@code true} if the assistant can go back to this step.
	 */
	public boolean canGoBack();
	
	

}
