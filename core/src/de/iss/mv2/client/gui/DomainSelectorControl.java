package de.iss.mv2.client.gui;

import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * A control to select a specified domain.
 * 
 * @author Marcel Singer
 *
 */
public class DomainSelectorControl extends JComponent {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -3763556809639219677L;
	
	/**
	 * The drop down control containing the available domains.
	 */
	private JComboBox<String> selector;

	/**
	 * Creates a new instance of {@link DomainSelectorControl}.
	 */
	public DomainSelectorControl() {
		setLayout(new BorderLayout(0, 0));

		JLabel lblPleaseSelectOn = new JLabel(
				"Please select on of the following domain names: ");
		add(lblPleaseSelectOn, BorderLayout.NORTH);

		selector = new JComboBox<String>();
		add(selector, BorderLayout.CENTER);
	}
	
	/**
	 * Sets the available domain names.
	 * @param domainNames The domain names to set.
	 */
	public void setAvailableDomainNames(String[] domainNames){
		selector.setModel(new DefaultComboBoxModel<String>(domainNames));
	}
	
	/**
	 * Returns the selected index.
	 * @return The selected index.
	 */
	public int getSelectedIndex(){
		return selector.getSelectedIndex();
	}
	
	/**
	 * Returns the selected domain name.
	 * @return The selected domain name or {@code null} if no one is selected.
	 */
	public String getSelectedDomainName(){
		if(selector.getSelectedIndex() == -1) return null;
		return (String) selector.getItemAt(selector.getSelectedIndex());
	}
	

}
