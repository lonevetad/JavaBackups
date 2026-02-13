package appunti.testRoomJoiner;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import appunti.testRoomJoiner.TRJ_Shared.JPIV_R_Fields;
import importedUtilities.abstractCommon.behaviouralObjectsAC.MyComparator;
import importedUtilities.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import importedUtilities.gui.GUIConstantsUtilities;
import importedUtilities.gui.ListBoxView;
import importedUtilities.gui.LoggerMessagesJScrollPane;
import importedUtilities.mainTools.LoggerMessages;

public class ServerTRJ_GUI implements LoggerMessagesHolder {
	private static final long serialVersionUID = 950888812L;

	public ServerTRJ_GUI(ServerTRJ server) {
		notInit = true;
		this.server = server;
		setLog(null);
	}

	private boolean notInit;
	// LoggerMessages log;
	//
	JFrame fin;
	JLabel jlIP, jlPort;
	JPanel jpFin, jpIPinfo, jpCenter;
	JTextField jtfPort, jtfIP;
	// JScrollPane jspList;JPanel jpList;
	// LoggerMessagesJScrollPane log;
	ListBoxView<Room> lbwOpenedRoom;
	ServerTRJ server;

	//

	@Override
	public LoggerMessages getLog() {
		return server.log;
	}

	//

	@Override
	public ServerTRJ_GUI setLog(LoggerMessages log) {
		// this.log = log;
		this.server.setLog(log);
		return this;
	}

	//

	// TODO PUBLIC

	public void init() {
		if (notInit) {
			notInit = false;

			fin = new JFrame("Server test room joiner");
			fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			jpFin = new JPanel(new BorderLayout());
			fin.add(jpFin);
			fin.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					jpFin.setSize(fin.getSize());
				}
			});

			jpIPinfo = new JPanel(
					// new GridLayout(1, 0)
					GUIConstantsUtilities.INFINITE_COLUMNS);
			jpFin.add(jpIPinfo, BorderLayout.NORTH);

			jlIP = new JLabel("IP:");
			jpIPinfo.add(jlIP);
			jlIP.setHorizontalAlignment(SwingConstants.RIGHT);
			jtfIP = new JTextField("255.255.255.255");
			jpIPinfo.add(jtfIP);
			jlPort = new JLabel("Port:");
			jpIPinfo.add(jlPort);
			jlPort.setHorizontalAlignment(SwingConstants.RIGHT);
			jtfPort = new JTextField(Integer.toString(ServerTRJ.DEFAULT_SERVER_PORT));
			jpIPinfo.add(jtfPort);

			jpCenter = new JPanel(GUIConstantsUtilities.INFINITE_ROWS);
			jpFin.add(jpCenter, BorderLayout.CENTER);

			// jpList=new JP
			lbwOpenedRoom = new ListBoxView_Room(Room.ROOM_COMPARATOR);
			jpCenter.add(lbwOpenedRoom);

			setLog(new LoggerMessagesJScrollPane(false));
			jpCenter.add((LoggerMessagesJScrollPane) getLog());

			// TODO

			fin.setSize(400, 400);
			fin.setVisible(true);
		}
	}

	public void showIPAddress(String ip) {
		jtfIP.setText(ip);
	}

	public void showPort(int port) {
		jtfPort.setText(Integer.toString(port));
	}

	//

	static class ListBoxView_Room extends ListBoxView<Room> {
		private static final long serialVersionUID = -13201874980L;

		public ListBoxView_Room(MyComparator<Room> comp) {
			super(comp);
		}

		@Override
		public ListBoxView<Room>.JPanelItemVisualizer newRow(Room item) {
			return new JPanelItemVisualizer_Room(item);
		}

		class JPanelItemVisualizer_Room extends JPanelItemVisualizer {
			private static final long serialVersionUID = 13201874980L;

			public JPanelItemVisualizer_Room(Room item) {
				super(item);
			}

			@Override
			public void updateItemShow() {
				Room r;
				r = this.getItem();
				/*
				 * jls[JPIV_R_Fields.RobodromeName.ordinal()].
				 * setText("Robodrome: " + r.getRobodromeName());
				 * jls[JPIV_R_Fields.OwnerName.ordinal()].setText("Owner: " +
				 * r.getOwner().getName());
				 * jls[JPIV_R_Fields.PlayerMax.ordinal()].setText("Players: " +
				 * r.getMaxAmountPlayer());
				 * jls[JPIV_R_Fields.PlayersCountLogged.ordinal()].setText("/ "
				 * + r.getRobodromeName());
				 */
				for (TRJ_Shared.JPIV_R_Fields f : TRJ_Shared.valuesJPIV_R_Fields) {
					jls[f.ordinal()].setText(f.pretext + f.roomInformationExtractor.extractInfo(r));
				}
			}

			JLabel[] jls;
			JPanel jpPlayerCount;

			@Override
			public void initGUI() {
				int i, len;
				JPIV_R_Fields j;
				this.setLayout(GUIConstantsUtilities.INFINITE_ROWS);

				jls = new JLabel[len = TRJ_Shared.valuesJPIV_R_Fields.length];
				i = -1;
				while (++i < len) {
					j = TRJ_Shared.valuesJPIV_R_Fields[i];
					jls[i] = new JLabel(j.name() + ": ");
				}
				this.add(jls[TRJ_Shared.JPIV_R_Fields.RobodromeName.ordinal()]);
				this.add(jls[TRJ_Shared.JPIV_R_Fields.OwnerName.ordinal()]);
				jpPlayerCount = new JPanel(GUIConstantsUtilities.INFINITE_COLUMNS);
				this.add(jpPlayerCount);
				jls[JPIV_R_Fields.PlayersCountLogged.ordinal()].setHorizontalTextPosition(SwingConstants.RIGHT);
				jpPlayerCount.add(jls[JPIV_R_Fields.PlayerMax.ordinal()]);
			}

		}
	}

	//
	public static void main(String[] args) {
		ServerTRJ_GUI sg;
		sg = new ServerTRJ_GUI(null);
		sg.init();
	}
}