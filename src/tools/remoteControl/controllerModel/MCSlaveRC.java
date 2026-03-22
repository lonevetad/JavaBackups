package tools.remoteControl.controllerModel;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tools.ObservableSimple;
import tools.ObserverSimple;
import tools.remoteControl.msg.AMessageCommand;
import tools.remoteControl.msg.MsgScreencast;
import tools.remoteControl.msg.MsgShutDown;
import tools.remoteControl.view.ViewRC;

/** Is the Server: receive orders (inputs) from the Master and execute them. */
public class MCSlaveRC extends AModelControllerRC {

	public MCSlaveRC(ViewRC view) throws AWTException, IOException { this(view, 0); }

	public MCSlaveRC(ViewRC view, int port) throws AWTException, IOException {
		super(view);
		this.server = new ServerSocket(port, 4, InetAddress.getLocalHost());
		threadExecutor = Executors.newFixedThreadPool(2);

		// start debug stuff
		this.observersBuffImage = new LinkedList<>();
		this.observableScreenshoot = () -> this.observersBuffImage;

		// end debug stuff

		startConnectionAndReading();
	}

	protected ConnectionStatus status = ConnectionStatus.NewBorn;
	protected final ServerSocket server;
	protected Socket master;
	protected ObjectInputStream msgReceiver;
	protected ObjectOutputStream msgSender;
	protected ConnectorAndReaderMsg runnerReader;
	protected ScreencasterRunner runnerScreencaster;
	protected ExecutorService threadExecutor;
	// added for debug purposes
	protected final List<ObserverSimple<BufferedImage>> observersBuffImage;
	protected final ObservableSimple<BufferedImage> observableScreenshoot;

	public ObservableSimple<BufferedImage> getObservableScreenshoot() { return observableScreenshoot; }

	@Override
	public boolean isClient() { return false; }

	public String getConnectionAddress() { return server.getInetAddress().getHostAddress(); }

	public int getConnectionPort() { return server.getLocalPort(); }

	@Override
	public boolean execute(AMessageCommand msg) {
//		final MessageType mt;
		if (msg == null)
			return false;
		/*
		 * mt = msg.msgType; switch (mt) { case Mouse: { executeCommandMouse((MsgMouse)
		 * msg); } default: throw new IllegalArgumentException("Unexpected value: " +
		 * mt); }
		 */
		msg.execute(this);
		return true;
	}

	public void sendScreenshoot(BufferedImage screenshot) {
		MsgScreencast msg = new MsgScreencast(screenshot);
		try {
			System.out.println("slave sending screenshot.....");
			msgSender.writeObject(msg);
			System.out.println("slave sent screenshot :D");
			msgSender.flush();
			System.out.println("slave flushed screenshot :D");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @Override protected void executeCommandMouse(MsgMouse mm) {
	 * this.robotActionSensorActuator. }
	 */
	@Override
	public void shutDown(boolean isUserAction) {
		switch (this.status) {
		case NewBorn:
		case ShuttedDown: {
			return;
		}
		case Connecting:
		case Connected: {
			if (runnerReader != null)
				this.runnerReader.shutDownReader();
			if (runnerScreencaster != null)
				this.runnerScreencaster.shutDownScreencaster();
			status = ConnectionStatus.ShuttedDown;
			if (master == null)
				return;
			if (isUserAction) {
				ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(this.master.getOutputStream());
					oos.writeObject(new MsgShutDown());
					oos.flush();
//					oos.close(); // removed because of the "master.close()" below
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				this.master.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				this.master.shutdownInput();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				this.master.shutdownOutput();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.master = null;
			this.msgReceiver = null;
			this.msgSender = null;
			this.runnerReader = null;
			this.runnerScreencaster = null;
			if (threadExecutor != null) {
				threadExecutor.shutdownNow();
				this.threadExecutor = null;
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + this.status);
		}
	}

	protected void startConnectionAndReading() {
		if (this.runnerReader != null)
			return;
		threadExecutor.submit(() -> {
			try {
				Thread.sleep(500); // just put a delay
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			threadExecutor.submit(this.runnerReader = newReader());
		});
	}

	/** Override designed */
	protected ConnectorAndReaderMsg newReader() { return new ConnectorAndReaderMsg(); }

	protected ScreencasterRunner newScreencasterRunner() { return new ScreencasterRunner(); }

	@Override
	protected void onConnectionEstablished() {
		System.out.println("slave connected");
		threadExecutor.submit(runnerScreencaster = newScreencasterRunner());
		super.onConnectionEstablished();
	}

	//
	// TODO SLAVE RUNNERs
	protected class ConnectorAndReaderMsg implements Runnable {
		protected boolean canRead = true;

		@Override
		public void run() {
			connectToMaster();
			readMsg();
		}

		protected void shutDownReader() {
			this.canRead = false;
			try {
//				threadReader.interrupt();
				threadExecutor.shutdown();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		protected void connectToMaster() {
			while (canRead && master == null && status == ConnectionStatus.NewBorn) {
				try {
					status = ConnectionStatus.Connecting;
					master = server.accept();
					status = ConnectionStatus.Connected;
					System.out.println("slave has accepted a socket");
					msgReceiver = new ObjectInputStream(master.getInputStream());
					System.out.println("slave got IN	put stream");
					msgSender = new ObjectOutputStream(master.getOutputStream());
					System.out.println("slave got OUTput stream");
					onConnectionEstablished();
				} catch (IOException e) {
					status = ConnectionStatus.NewBorn;
					e.printStackTrace();
				}
			}
		}

		protected boolean checkReader() {
			if (msgReceiver == null)
				status = ConnectionStatus.ShuttedDown;
			return msgReceiver == null;
		}

		protected void readMsg() {
			int avail;
			Object objRead;
			System.out.println("slave start reading a message");
			while (this.canRead) {
				avail = 0;
				do {
					try {
						if (checkReader())
							return;
						avail = msgReceiver.available();
					} catch (IOException e) {
						e.printStackTrace();
						avail = 0;
					}
					if (avail == 0) {
						try {
							System.out.println("slave sleep : no msg to read");
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						try {
							System.out.println("slave something to read");
							if (checkReader())
								return;
							objRead = msgReceiver.readObject();
							if (objRead instanceof AMessageCommand) {
								System.out.println("slave received a message: " + objRead.getClass().getName());
								execute((AMessageCommand) objRead);
							}
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					}
				} while (this.canRead && avail > 0);
			}
			System.out.println("No more messages to read");
		}
	}

	protected class ScreencasterRunner implements Runnable {

		protected boolean canScreencast = true;

		protected void shutDownScreencaster() {
			this.canScreencast = false;
			try {
//				threadScreencaster.interrupt();
				threadExecutor.shutdown();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			long delta;
			BufferedImage screenshot;
			System.out.println("slave screencasting :D " + canScreencast);
			try {
				while (canScreencast) {
					delta = System.currentTimeMillis();
					try {
						Rectangle areaScreenshot;
						areaScreenshot = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
						screenshot = robotActionSensorActuator.createScreenCapture(areaScreenshot);
						System.out.println(areaScreenshot);
						observableScreenshoot.notifyObservers(screenshot);
						sendScreenshoot(screenshot);
						System.out.println("sent screenshoot");
					} catch (Exception e) {
						e.printStackTrace();
					}
					delta = MILLIS_BETWEEN_EACH_SCREENCAST - (System.currentTimeMillis() - delta);
					if (delta > 0) { Thread.sleep(delta); }
				}
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}