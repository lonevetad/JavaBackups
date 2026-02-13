package tools.remoteControl.view;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import tools.ObserverSimple;
import tools.gui.swing.HintTextFieldUI;
import tools.remoteControl.controllerModel.AModelControllerRC;
import tools.remoteControl.controllerModel.ConnectionStatus;
import tools.remoteControl.controllerModel.MCMasterRC;
import tools.remoteControl.controllerModel.MCSlaveRC;
import tools.remoteControl.msg.MouseAction;
import tools.remoteControl.msg.MsgKeyboard;
import tools.remoteControl.msg.MsgMouse;

public class ViewRC {
	public static final String[] PANELS_NAME;

	public static enum PanelName {
		Main, ClientSlave, MasterConnecting, MasterMastering
	}

	static {
		int i;
		PanelName[] vals = PanelName.values();
		String[] panNames;
		panNames = new String[i = vals.length];
		while (--i >= 0) {
			panNames[i] = vals[i].name();
		}
//		panNames = Arrays.stream(vals).map(pn -> pn.name()).collect(Collectors.toList()).toArray(panNames);
		PANELS_NAME = panNames;
	}

	//
	public ViewRC() {
		this.observerScreenshoot = this::showScreencast;
//		this.observerConnection = this::onConnectionEstablishedMasterToSlave;
	}

	private boolean alreadyInitialized = false;
	protected AModelControllerRC modelController;
	protected BufferedImage screenCastReceived;
	protected final ObserverSimple<BufferedImage> observerScreenshoot;
//	protected final ObserverSimple<ConnectionStatus> observerConnection;
	protected ObserverSimple<ConnectionStatus> observerConnection;
	//
	protected JFrame fin;
	protected JPanel jpFin, jpMainPanel, jpScreencast, jpSlave, jpMasterConnecting, jpMaster;
	protected JScrollPane jspScreencast;
	protected PanelName currentPanel;
	protected CardLayout allPanelsManager;
	protected JButton jbToSlave, jbToMaster, jbStartMastering;
	protected JLabel jlConnectionStatus;

	public void init() {
		if (alreadyInitialized)
			return;
		alreadyInitialized = true;
		allPanelsManager = new CardLayout();
		currentPanel = PanelName.Main;
		fin = new JFrame("Remote controller - by lonevetad");
		fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (modelController != null) {
					modelController.shutDown(true);
					modelController = null;
				}
			}
		});
		jpFin = new JPanel(allPanelsManager);
		fin.getContentPane().add(jpFin);

		// main

		jpMainPanel = new JPanel();
		jpFin.add(jpMainPanel, PanelName.Main.name());
		jbToSlave = new JButton("Be controlled by remote");
		jbToMaster = new JButton("Control the remote PC");
		jpMainPanel.add(jbToSlave);
		jbToSlave.addActionListener(l -> toSlave());
		jpMainPanel.add(jbToMaster);
		jbToMaster.addActionListener(l -> toMaster());

		// slave
		jpSlave = new JPanel();
		jpFin.add(jpSlave, PanelName.ClientSlave.name());

		// master connecting to slave
		jpMasterConnecting = new JPanel();
		jpFin.add(jpMasterConnecting, PanelName.MasterConnecting.name());

		// master
		jpMaster = new JPanel(new BorderLayout());
		jpFin.add(jpMaster, PanelName.MasterMastering.name());

		// TODO todo altre robe
		jlConnectionStatus = new JLabel("NOT connected yet");

		fin.setSize(500, 500);
		fin.setVisible(true);
	}

	//

	public ObserverSimple<BufferedImage> getObserverScreenshoot() { return observerScreenshoot; }

	public ObserverSimple<ConnectionStatus> getObserverConnection() { return observerConnection; }

	protected void setModelController(AModelControllerRC mc) {
		this.modelController = mc;
//		if(mc!=null) {		}
	}

	protected void toPanel(PanelName pn) { allPanelsManager.show(jpFin, pn.name()); }

	protected void onConnectionEstablishedMasterToSlave(ConnectionStatus cs) { toTheMasterMasteringUponConnection(); }

	protected void tryToConnectMasterToSlave(String hostAddress, int port) {
		try {
			MCMasterRC master;
			master = new MCMasterRC(this, hostAddress, port);
			this.observerConnection = this::onConnectionEstablishedMasterToSlave;
			setModelController(master);
			startRepaintingScreencastVisualizer();
			master.getObservableScreenshoot().addObserver(this.getObserverScreenshoot());
			master.getObservableConnectionStatus().addObserver(this.getObserverConnection());
			// DONE :D
			toTheMasterMasteringUponConnection();
			jlConnectionStatus.setText("Connected to the Slave :D");
		} catch (Exception e) {
			e.printStackTrace();
			jlConnectionStatus.setText("Something went wrong:\n" + e.getMessage());
		}
	}

	//

	/** Just saves the instance */
	protected void showScreencast(BufferedImage im) {
		this.screenCastReceived = im;
//resize the image
		jpScreencast.setSize(im.getWidth(), im.getHeight());
		jpScreencast.setPreferredSize(jpScreencast.getSize());
		jpScreencast.repaint();
	}

	protected void startRepaintingScreencastVisualizer() {
		// should run a runner to paint the jpScreencast, but it's already done by the
		// observer
	}

	// TODO OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
	// transform it into observer-observable
	protected void onConnectionEstablished() { jlConnectionStatus.setText("Connected :D"); }

	//

	/** But it's the part where it connects to the slave... */
	protected void setupGuiMaster() {
		ConnectionInfoView civ;
		civ = new ConnectionInfoView(modelController, false) {
			private static final long serialVersionUID = 20123016578339L;

			@Override
			protected String getConnectionAddress(AModelControllerRC theController) {
				return "ask if to the \"slave\" (the remote PC).";
				// ((MCMasterRC) theController).getSlaveConnectionAddress();
			}

			@Override
			protected int getConnectionPort(AModelControllerRC theController) {
				return 0; // ((MCMasterRC) theController).getSlaveConnectionPort();
			}

		};
		jpMasterConnecting.add(civ);
		jpMasterConnecting.add(jlConnectionStatus);
		jbStartMastering = new JButton("Try to connect");
		jbStartMastering.addActionListener(l -> {
			try {
				int port;
				String hostAddress;
				hostAddress = civ.jtHostAddress.getText().trim();
				port = Integer.parseInt(civ.jtPort.getText().trim());

				// I don't want to block the GUI
				ExecutorService executor = Executors.newSingleThreadExecutor();
				executor.submit(() -> {
					jlConnectionStatus.setText("Connecting ...");
					tryToConnectMasterToSlave(hostAddress, port);
					System.out.println("RETURNED FROM MASTER CONNECTING");
//					try {
//						System.out.println("attempt to shutdown executor");
//						executor.shutdown();
//						executor.awaitTermination(5, TimeUnit.SECONDS);
//					} catch (InterruptedException e) {
//						System.err.println("tasks interrupted");
//					} finally {
//						if (!executor.isTerminated()) { System.err.println("cancel non-finished tasks"); }
//						executor.shutdownNow();
//						System.out.println("shutdown finished");
//					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				jlConnectionStatus.setText("Something went wrong:\n" + e.getMessage());
			}
		});
		jpMasterConnecting.add(jbStartMastering);
		jpMasterConnecting.setLayout(new BoxLayout(jpMasterConnecting, BoxLayout.Y_AXIS));
	}

	protected void toMaster() {
		toPanel(PanelName.MasterConnecting);
		setupGuiMaster();
		System.out.println("shown panel master connecting");
	}

	protected void newScreencastVisualizer(JPanel whereToAdd) {
		jpScreencast = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				paintScreencastedImage(g);
			}
		};
		jspScreencast = new JScrollPane(jpScreencast);
		jspScreencast.setViewportView(jpScreencast);
		jspScreencast.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jspScreencast.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		whereToAdd.add(jspScreencast, BorderLayout.CENTER);
		jpScreencast.setSize(200, 200);
		jpScreencast.setBackground(new Color(128, 255, 196));
	}

	protected void setupGuiMasterMastering() {
		newScreencastVisualizer(jpMaster);

		// LISTENERS
		jpScreencast.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				keyPressed(e);
				keyReleased(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				MCMasterRC master;
				master = (MCMasterRC) modelController;
				master.execute(new MsgKeyboard(false, e.getKeyCode()));
			}

			@Override
			public void keyPressed(KeyEvent e) {
				MCMasterRC master;
				master = (MCMasterRC) modelController;
				master.execute(new MsgKeyboard(true, e.getKeyCode()));
			}
		});
		MouseAdapter ma;
		ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MCMasterRC master;
				master = (MCMasterRC) modelController;
				master.execute(new MsgMouse(MouseAction.Click, e.getButton()));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				MCMasterRC master;
				master = (MCMasterRC) modelController;
				master.execute(new MsgMouse(MouseAction.Press, e.getButton()));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MCMasterRC master;
				master = (MCMasterRC) modelController;
				master.execute(new MsgMouse(MouseAction.Release, e.getButton()));
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				MCMasterRC master;
				master = (MCMasterRC) modelController;
				master.execute(new MsgMouse(MouseAction.Wheel, e.getButton()));
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mousePressed(e);
				mouseMoved(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				MCMasterRC master;
				master = (MCMasterRC) modelController;
				master.execute(new MsgMouse(MouseAction.Move, e.getX(), e.getY()));
			}
		};
		jpScreencast.addMouseListener(ma);
		jpScreencast.addMouseMotionListener(ma);
		jpScreencast.addMouseWheelListener(ma);
	}

	protected void toTheMasterMasteringUponConnection() {
		toPanel(PanelName.MasterMastering);
		setupGuiMasterMastering();
	}

	//

	protected void setupGuiSlave() {
		ConnectionInfoView civ;
		civ = new ConnectionInfoView(modelController, true) {
			private static final long serialVersionUID = 89753134583L;

			@Override
			protected String getConnectionAddress(AModelControllerRC theController) {
				return ((MCSlaveRC) theController).getConnectionAddress();
			}

			@Override
			protected int getConnectionPort(AModelControllerRC theController) {
				return ((MCSlaveRC) theController).getConnectionPort();
			}

		};
		jpSlave.add(civ);
		newScreencastVisualizer(jpSlave);
		jpSlave.add(jlConnectionStatus);
	}

	protected void toSlave() {
		try {
			MCSlaveRC slave;
			slave = new MCSlaveRC(this);
			this.observerConnection = this::onConnectionEstablishedSlaveAcceptingMaster;
			setModelController(slave);
			slave.getObservableConnectionStatus().addObserver(getObserverConnection());
			slave.getObservableScreenshoot().addObserver(this.getObserverScreenshoot());
			setupGuiSlave();
			System.out.println("I'm slaving :D");
			toPanel(PanelName.ClientSlave);
		} catch (AWTException | IOException e) {
			e.printStackTrace();
		}
	}

	protected void onConnectionEstablishedSlaveAcceptingMaster(ConnectionStatus cs) {

	}

	//

	protected void paintScreencastedImage(Graphics g) {
		if (screenCastReceived == null)
			return;
		jpScreencast.setSize(screenCastReceived.getWidth(), screenCastReceived.getHeight());
		g.drawImage(screenCastReceived, 0, 0, null);
	}

	// TODO GUI inner class
	abstract static class ConnectionInfoView extends JPanel {
		private static final long serialVersionUID = -12334215L;
		protected JTextField jtHostAddress, jtPort;
		protected AModelControllerRC theController;

		protected ConnectionInfoView(AModelControllerRC modelController, boolean isSlave) {
			super();
			String txt;
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.theController = modelController;
			txt = "IP address: " + getConnectionAddress(theController);
			System.out.println(txt);
			jtHostAddress = new JTextField();
			jtHostAddress.setToolTipText(txt);
//			jtHostAddress.addFocusListener(new FocusListenerWithPrompt(jtHostAddress, txt));
			jtHostAddress.setUI(new HintTextFieldUI(txt));
			jtHostAddress.setSize(200, 35);
			this.add(jtHostAddress);
			txt = "IP port: " + getConnectionPort(theController);
			System.out.println(txt);
			jtPort = new JTextField();
			jtPort.setToolTipText(txt);
//			jtPort.addFocusListener(new FocusListenerWithPrompt(jtPort, txt));
			jtPort.setUI(new HintTextFieldUI(txt));
			jtPort.setSize(200, 35);
			if (isSlave) {
				jtHostAddress.setEditable(false);
				jtPort.setEditable(false);
			}
			this.add(jtPort);
			this.setSize(450, 100);
			this.setPreferredSize(this.getSize());
		}

		protected abstract String getConnectionAddress(AModelControllerRC theController);

		protected abstract int getConnectionPort(AModelControllerRC theController);

		@Deprecated
		protected class FocusListenerWithPrompt implements FocusListener {
			final String txt;
			final JTextField textField;

			protected FocusListenerWithPrompt(JTextField textField, String txt) {
				super();
				this.textField = textField;
				this.txt = txt;
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().equals(""))
					textField.setText(txt);
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(txt))
					textField.setText("");
			}
		}
	}
}