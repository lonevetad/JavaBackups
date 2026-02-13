package appunti.testRoomJoiner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import appunti.testRoomJoiner.ClientTRJ.ClientConnectionStatus;
import importedUtilities.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import importedUtilities.gui.LoggerMessagesJScrollPane;
import importedUtilities.mainTools.LoggerMessages;

public class ClientTRJ_GUI implements LoggerMessagesHolder {
	private static final long serialVersionUID = -950888812L;

	public ClientTRJ_GUI(ClientTRJ c) {
		this.client = c;
		notInit = true;
	}

	boolean notInit;
	JFrame fin;
	JTabbedPane jtp;
	JPanel jpMain;
	JPanelConnectionManager jpIPinfo;
	// JTextField jtfUsername;
	JButton jbSilly;
	ClientTRJ client;

	//

	@Override
	public LoggerMessages getLog() {
		return client.getLog();
	}

	public ClientConnectionStatus getConnectionStatus() {
		return client.getConnectionStatus();
	}

	public String getIP() {
		return jpIPinfo.jtfIP.getText();
	}

	public int getPort() {
		return Integer.parseInt(jpIPinfo.jtfPort.getText());
	}

	public String getUsername() {
		return jpIPinfo.jtfUsername.getText();
	}
	//

	@Override
	public LoggerMessagesHolder setLog(LoggerMessages log) {
		client.setLog(log);
		return this;
	}

	//

	// TODO PUBLIC

	public void init() {
		LoggerMessagesJScrollPane logger;
		if (notInit) {
			notInit = false;

			fin = new JFrame("client test room joiner");
			fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			jtp = new JTabbedPane();
			fin.add(jtp);
			fin.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					Dimension d;
					d = fin.getSize();
					d.height -= 25;
					jtp.setSize(d);
				}
			});
			fin.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					client.closeConnectionToServer();
				}
			});

			jpMain = new JPanel(new BorderLayout());
			jtp.addTab("Main", jpMain);
			setLog(logger = new LoggerMessagesJScrollPane(false));
			jtp.addTab("Log", logger);

			jpIPinfo = new JPanelConnectionManager(this);
			jpMain.add(jpIPinfo, BorderLayout.CENTER);

			jbSilly = new JButton("Silly Method");
			jpMain.add(jbSilly, BorderLayout.SOUTH);
			jbSilly.addActionListener(l -> client.sendSillyMessageToServer());

			client.addObserverToConnectionStatus(jpIPinfo);
			// TODO

			fin.setSize(400, 400);
			fin.setVisible(true);
		}
	}

	//

	protected void switchConnectionStatus() {
		this.getLog().log("++GUI++: switch");
		client.switchConnectionStatusToServer(getPort(), getIP(), getUsername());
	}

	//

	// TODO CLASS

	static class JPanelConnectionManager extends JPanel implements Observer {
		private static final long serialVersionUID = -896502548707L;

		public static enum JButtonTextConnection {
			Connected("Disconnect"), Disconnected("Connect"), Pending("Connecting..."), Invalid("BUG invalid status");
			final String text;

			JButtonTextConnection(String s) {
				this.text = s;
			}
		}

		public JPanelConnectionManager(ClientTRJ_GUI cgui) {
			super(
					// GUIConstantsUtilities.INFINITE_COLUMNS//
					new BorderLayout()//
			);
			this.cgui = cgui;
			jpInfo = new JPanel(new GridLayout(0, 2));
			this.add(jpInfo, BorderLayout.CENTER);
			jlIP = new JLabel("IP:");
			jpInfo.add(jlIP);
			jlIP.setHorizontalAlignment(SwingConstants.RIGHT);
			jtfIP = new JTextField("0.0.0.0");
			jpInfo.add(jtfIP);
			jlPort = new JLabel("Port:");
			jpInfo.add(jlPort);
			jlPort.setHorizontalAlignment(SwingConstants.RIGHT);
			jtfPort = new JTextField(Integer.toString(ServerTRJ.DEFAULT_SERVER_PORT));
			jpInfo.add(jtfPort);
			jlUsername = new JLabel("IP:");
			jpInfo.add(jlUsername);
			jlUsername.setHorizontalAlignment(SwingConstants.RIGHT);
			jtfUsername = new JTextField("Alphabetical-only username");
			jpInfo.add(jtfUsername);
			//
			jbSwitchConnStatus = new JButton("Connect???");
			jbSwitchConnStatus.setVisible(true);
			jbSwitchConnStatus.addActionListener(l -> cgui.switchConnectionStatus());
			this.add(jbSwitchConnStatus, BorderLayout.SOUTH);
			//
			jcomponentsInvalidable = new JComponent[] { jtfIP, jtfPort, jtfUsername, jbSwitchConnStatus };
		}

		JPanel jpInfo;
		JLabel jlIP, jlPort, jlUsername;
		JTextField jtfIP, jtfPort, jtfUsername;
		JButton jbSwitchConnStatus;
		JComponent[] jcomponentsInvalidable;
		ClientTRJ_GUI cgui;

		@Override
		public void update(Observable o, Object arg) {
			ClientConnectionStatus stat;
			if (arg != null && arg instanceof ClientConnectionStatus) {
				stat = (ClientConnectionStatus) arg;
				updateFromStatus(stat);
			} else
				System.out.println("CANNOT UPDATE");
		}

		void setEnableComponents(boolean isEnabledComponent) {
			for (JComponent jc : jcomponentsInvalidable) {
				jc.setEnabled(isEnabledComponent);
			}
		}

		void updateFromStatus(ClientConnectionStatus stat) {
			String jbText;
			switch (stat) {
			case Closed:
				setEnableComponents(true);
				jbText = JButtonTextConnection.Disconnected.text;
				break;
			case Running:
				setEnableComponents(false);
				jbSwitchConnStatus.setEnabled(true);
				jbText = JButtonTextConnection.Connected.text;
				break;
			case WaitingToConfirmation:
				jbText = JButtonTextConnection.Pending.text;
				break;
			default:
				jbText = JButtonTextConnection.Invalid.text;
				break;
			}
			jbSwitchConnStatus.setText(jbText);
		}
	}

	//
	/*
	 * public static void main(String[] args) { ClientTRJ_GUI cg;
	 * System.out.println("Stringzo"); cg = new ClientTRJ_GUI(null); cg.init();
	 * System.out.println("Stringzooo"); }
	 */
}