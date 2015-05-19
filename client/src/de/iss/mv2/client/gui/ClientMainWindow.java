package de.iss.mv2.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import de.iss.mv2.MV2Constants;
import de.iss.mv2.client.data.MV2ClientSettings;
import de.iss.mv2.client.data.MailBoxSettings;
import de.iss.mv2.client.data.MemoryMessageStore;
import de.iss.mv2.client.data.UserPreferences;
import de.iss.mv2.client.messaging.InboxUpdateProcedure;
import de.iss.mv2.client.messaging.MessageProcedure;
import de.iss.mv2.client.messaging.ProcedureException;
import de.iss.mv2.client.messaging.ProcedureListener;
import de.iss.mv2.data.EncryptedExportable;
import de.iss.mv2.io.PathBuilder;
import de.iss.mv2.logging.LoggerManager;

/**
 * The main window of the client application.
 * 
 * @author Marcel Singer
 *
 */
public class ClientMainWindow extends JFrame implements WindowListener,
		ActionListener, TreeSelectionListener {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 931571183140137367L;

	/**
	 * The menu item to open the client settings.
	 */
	private JMenuItem settingsButton;

	/**
	 * The menu item to open the certificate signing tool.
	 */
	private JMenuItem certSigningToolItem;
	/**
	 * The menu item to create a new mail.
	 */
	private JMenuItem newMailItem;
	
	/**
	 * Holds the mail box selector.
	 */
	private MailBoxSelector boxSelector = new MailBoxSelector();
	/**
	 * Holds the mail lists for the mail boxes.
	 */
	private final Map<MailBoxSettings, MailListControl> mailList = new HashMap<MailBoxSettings, MailListControl>();
	
	/**
	 * Holds the split pane thats splits the mail box selector and the mail list.
	 */
	private final JSplitPane mailBoxListSplitPane;
	
	/**
	 * Holds the split pane thats splits the {@link ClientMainWindow#mailBoxListSplitPane} and the mail view.
	 */
	private final JSplitPane splitPaneMailViewSplitPane;
	
	/**
	 * The component to display a mail.
	 */
	private final MailView mailView = new MailView();
	
	/**
	 * Holds the currently selected mail box.
	 */
	private MailBoxSettings selectedMailBox;
	
	/**
	 * The menu item to synchronize the current mail box.
	 */
	private final JMenuItem syncMail = new JMenuItem("Synchronize Account...");

	/**
	 * Creates a new instance of {@link ClientMainWindow}.
	 */
	public ClientMainWindow() {
		setLocationRelativeTo(null);
		setTitle("MV2 Client");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 600));
		addWindowListener(this);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("MV2");
		menuBar.add(mnNewMenu);
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		JMenu mailMenu = new JMenu("Mails");
		syncMail.setEnabled(false);
		syncMail.addActionListener(this);
		syncMail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, tk.getMenuShortcutKeyMask()));
		mailMenu.add(syncMail);
		menuBar.add(mailMenu);

		JMenu toolsMenu = new JMenu("Tools");
		certSigningToolItem = new JMenuItem("Certificate Signing Tool");
		certSigningToolItem.addActionListener(this);
		toolsMenu.add(certSigningToolItem);
		menuBar.add(toolsMenu);

		settingsButton = new JMenuItem("Settings");
		settingsButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA,
				tk.getMenuShortcutKeyMask()));
		settingsButton.addActionListener(this);

		newMailItem = new JMenuItem("New Mail");
		newMailItem.addActionListener(this);
		newMailItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				tk.getMenuShortcutKeyMask()));
		mnNewMenu.add(newMailItem);
		mnNewMenu.add(settingsButton);
		
		

		mailBoxListSplitPane = new JSplitPane();
		mailBoxListSplitPane.setDividerLocation(0.5);
		mailBoxListSplitPane.setResizeWeight(0.5);
		splitPaneMailViewSplitPane = new JSplitPane();
		splitPaneMailViewSplitPane.setDividerLocation(0.3);
		splitPaneMailViewSplitPane.setResizeWeight(0.3);
		splitPaneMailViewSplitPane.setLeftComponent(mailBoxListSplitPane);
		splitPaneMailViewSplitPane.setRightComponent(mailView);
		getContentPane().add(splitPaneMailViewSplitPane, BorderLayout.CENTER);

		
		boxSelector.addTreeSelectionListener(this);
		
		mailBoxListSplitPane.setLeftComponent(new JScrollPane(boxSelector));
		mailBoxListSplitPane.setRightComponent(new JPanel());
		
		for(MailBoxSettings mailBox : MV2ClientSettings.getRuntimeSettings().getMailBoxes()){
			final MailListControl mlc = new MailListControl(new MemoryMessageStore());
			mlc.addSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					mailView.setMessage(mlc.getSelected());
				}
			});
			mailList.put(mailBox, mlc);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			PathBuilder pb = new PathBuilder(new File(UserPreferences
					.getPreferences().getStoreAddress("")));
			File f = pb.getChildFile(MV2Constants.CLIENT_CONFIG_FILE_NAME);
			EncryptedExportable ee = new EncryptedExportable(
					MV2ClientSettings.getRuntimeSettings());
			OutputStream out = new FileOutputStream(f);
			ee.export(MV2ClientSettings.getRuntimeSettings().getPassphrase(),
					out);
			out.flush();
			out.close();
			MV2ClientSettings.getRuntimeSettings().saveExtras(f);
		} catch (Exception ex) {
			LoggerManager.getCurrentLogger().push(ex);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newMailItem) {
			JFrame frame = new JFrame("New Mail");
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(
					new MessageCreatorControl(MV2ClientSettings
							.getRuntimeSettings().getMailBoxesArray()),
					BorderLayout.CENTER);
			frame.setLocationRelativeTo(this);
			frame.pack();
			frame.setSize(new Dimension(800, 600));
			frame.setVisible(true);
		}
		if (e.getSource() == settingsButton) {
			ClientSettingsWindow csw = new ClientSettingsWindow();
			csw.setLocationRelativeTo(this);
			csw.setVisible(true);
		}
		if (e.getSource() == certSigningToolItem) {
			CertificateSigningTool cst = new CertificateSigningTool();
			cst.setLocationRelativeTo(this);
			cst.setVisible(true);
		}
		if(e.getSource() == syncMail){
			LoadingDialog ld = new LoadingDialog(this, false);
			ld.pack();
			ld.setVisible(true);
			InboxUpdateProcedure iup = new InboxUpdateProcedure(selectedMailBox, mailList.get(selectedMailBox).getMailStorage());
			iup.addProcedureListener(ld);
			iup.addProcedureListener(new ProcedureListener<Void>() {
				
				@Override
				public void procedureCompleted(
						MessageProcedure<? extends Throwable, Void> procedure, Void result) {
					
				}
				
				@Override
				public void handleProcedureException(
						MessageProcedure<? extends Throwable, Void> procedure,
						ProcedureException ex) {
					ex.printStackTrace();
				}
				
				@Override
				public void procedureStateChanged(
						MessageProcedure<? extends Throwable, Void> procedure,
						String state, int progress) {
					
				}
			});
			iup.run();
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath treePath = boxSelector.getSelectionPath();
		for(Object o : treePath.getPath()){
			if(o instanceof MailBoxSettings){
				selectedMailBox = (MailBoxSettings) o;
				MailListControl mlc = mailList.get(selectedMailBox);
				mailBoxListSplitPane.setRightComponent(mlc);
				syncMail.setEnabled(true);
				syncMail.updateUI();
				syncMail.repaint();
				return;
			}
		}
		syncMail.setEnabled(false);
	}

}
