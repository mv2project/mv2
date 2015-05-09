package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.iss.mv2.gui.DialogHelper;
import de.iss.mv2.gui.EditableListListener;
import de.iss.mv2.gui.EditableListView;

/**
 * A window to show and modify the settings of the client application.
 * 
 * @author Marcel Singer
 *
 */
public class ClientSettingsWindow extends JFrame implements EditableListListener<String> {

	/**
	 * The serial.
	 */
	private static final long serialVersionUID = -5761614696129347167L;
	
	/**
	 * Holds the list with the available mail boxes.
	 */
	private EditableListView<String> mailBoxList;

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

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Accounts", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));

		mailBoxList = new EditableListView<String>();
		mailBoxList.addListener(this);
		mailBoxList.setPreferredSize(new Dimension(200, 1));
		panel_1.add(mailBoxList, BorderLayout.WEST);

	}

	@Override
	public void addItem(EditableListView<String> sender) {
		ServerSelectionControl cfa = new ServerSelectionControl();
		SpaceRequestControl src = new SpaceRequestControl();
		AssistantControl ast = new AssistantControl(new JComponent[]{cfa, src});
		DialogHelper.showBlockingSubmitDialog("Assistant", this, ast);
	}

	@Override
	public void removeItem(EditableListView<String> sender,
			String selectedItem, int selectedIndex) {
	}

}
