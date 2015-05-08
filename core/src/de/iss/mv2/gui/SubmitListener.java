package de.iss.mv2.gui;

/**
 * A listener to handle submit or cancel actions.
 * @author Marcel Singer
 *
 */
public interface SubmitListener {

	/**
	 * Handles the submit of a control.
	 * @param sender The submitted control.
	 */
	public void submitted(Object sender);
	
	/**
	 * Handles the abort of a control. 
	 * @param sender The canceled control.
	 */
	public void canceled(Object sender);
	
}
