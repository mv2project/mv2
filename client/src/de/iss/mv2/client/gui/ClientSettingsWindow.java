package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.iss.mv2.client.data.MV2ClientSettings;
import de.iss.mv2.client.data.MailBoxSettings;
import de.iss.mv2.client.data.WebSpaceSetup;
import de.iss.mv2.gui.DialogHelper;
import de.iss.mv2.gui.EditableListListener;
import de.iss.mv2.gui.EditableListView;

/**
 * A window to show and modify the settings of the client application.
 * 
 * @author Marcel Singer
 *
 */
public class ClientSettingsWindow extends JFrame implements
		EditableListListener<String>, ListSelectionListener {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -5761614696129347167L;

	/**
	 * Holds the list with the available mail boxes.
	 */
	private EditableListView<String> mailBoxList;

	/**
	 * The mail accounts tab.
	 */
	private JPanel accountsTab;
	
	/**
	 * The currently shown details control.
	 */
	private JComponent currentlyShown = null;
	
	/**
	 * Creates a new instance of {@link ClientSettingsWindow}.
	 */
	public ClientSettingsWindow() {
		setPreferredSize(new Dimension(800, 600));
		setSize(getPreferredSize());
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("General", null, panel, null);

		 accountsTab = new JPanel();
		tabbedPane.addTab("Accounts", null, accountsTab, null);
		accountsTab.setLayout(new BorderLayout(0, 0));

		mailBoxList = new EditableListView<String>();
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (MailBoxSettings mbs : MV2ClientSettings.getRuntimeSettings()
				.getMailBoxes()) {
			model.addElement(mbs.getAddress());

		}
		mailBoxList.setListModel(model);
		mailBoxList.addSelectionListener(this);
		mailBoxList.addListener(this);
		mailBoxList.setPreferredSize(new Dimension(200, 1));
		accountsTab.add(mailBoxList, BorderLayout.WEST);

	}

	@Override
	public void addItem(EditableListView<String> sender) {
		WebSpaceSetup wss = new WebSpaceSetup();
		ServerSelectionControl cfa = new ServerSelectionControl(wss);
		SpaceRequestControl src = new SpaceRequestControl(wss);
		AssistantControl ast = new AssistantControl(
				new JComponent[] { cfa, src });
		DialogHelper.showBlockingSubmitDialog("Assistant", this, ast);
	}

	@Override
	public void removeItem(EditableListView<String> sender,
			String selectedItem, int selectedIndex) {
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		if(mailBoxList.getSelectedIndex() == -1) return;
		MailBoxSettingsControl settingsControl = new MailBoxSettingsControl(MV2ClientSettings.getRuntimeSettings().getMailBoxes().get(mailBoxList.getSelectedIndex()));
		if(currentlyShown != null && Arrays.asList(accountsTab.getComponents()).contains(currentlyShown)){
			accountsTab.remove(currentlyShown);
			currentlyShown = null;
		}
		currentlyShown = settingsControl;
		accountsTab.add(settingsControl, BorderLayout.CENTER);
		invalidate();
		accountsTab.updateUI();
		repaint();
	}

}
