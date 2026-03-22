package tools.remoteControl.controllerModel;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import tools.ObservableSimple;
import tools.ObserverSimple;
import tools.remoteControl.msg.AMessageCommand;
import tools.remoteControl.msg.MsgPing;
import tools.remoteControl.msg.MsgShutDown;
import tools.remoteControl.view.ViewRC;

/**
 * Is the Client: has sensors to receive input from view (execute method) and
 * send it to the Slave.
 */
public class MCMasterRC extends AModelControllerRC {
	protected static final MsgPing PING_MSG = new MsgPing();

	public MCMasterRC(ViewRC view, String host, int port) throws AWTException, IOException {
		super(view);
		this.observersBuffImage = new LinkedList<>();
		this.observableScreenshoot = () -> this.observersBuffImage;
		System.out.println("master attempting to connect");
		asycnrhonouslyConnect(host, port);
	}

	protected boolean isAlive = true;
	protected final List<ObserverSimple<BufferedImage>> observersBuffImage;
	protected final ObservableSimple<BufferedImage> observableScreenshoot;
	//
	protected Socket socket;
	protected OutputStream os;
	protected ObjectOutputStream outputChannel;
	protected ObjectInputStream fromSlaveReceiver;
	protected MasterRunner masterRunner;
	protected Thread threadMasterRunner;

	@Override
	public boolean isClient() { return true; }

	public String getSlaveConnectionAddress() { return socket.getInetAddress().getHostAddress(); }

	public int getSlaveConnectionPort() { return socket.getLocalPort(); }

	public ObservableSimple<BufferedImage> getObservableScreenshoot() { return observableScreenshoot; }

	@Override
	public boolean execute(AMessageCommand msg) {
		// SEND the msg
		try {
			this.outputChannel.writeObject(msg);
			this.outputChannel.flush();
		} catch (SocketException e) {
			e.printStackTrace();
			shutDown(false);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void shutDown(boolean isUserAction) {
		if (!this.isAlive)
			return;
		this.isAlive = false;
		execute(new MsgShutDown());
//		outputChannel.close();
		this.masterRunner.shutDownRunner();
		this.masterRunner = null;
		this.threadMasterRunner = null;
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void ping() {
		execute(PING_MSG);
		System.out.println("master pinged");
	}

	protected void asycnrhonouslyConnect(String host, int port) {
		Executor connectionExecutor;
		connectionExecutor = Executors.newSingleThreadExecutor();
		connectionExecutor.execute(() -> {
			try {
				Thread.sleep(500); // just put a delay
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				System.out.println("master start to create a new Socket");
				this.socket = new Socket(host, port);
				this.os = this.socket.getOutputStream();
				this.outputChannel = new ObjectOutputStream(this.os);
				System.out.println("master got OUTput stream");
				this.fromSlaveReceiver = new ObjectInputStream(this.socket.getInputStream());
				System.out.println("master got INput stream");
				//
				System.out.println("master on setting up");
				onSettingUp();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	// TODO convert to ObserverObservable
	public void manageScreencast(BufferedImage bi) { this.observableScreenshoot.notifyObservers(bi); }

	//

	protected MasterRunner newMasterRunner() { return new MasterRunner(this); }

	protected void onSettingUp() {
		this.threadMasterRunner = new Thread(this.masterRunner = this.newMasterRunner());
		this.onConnectionEstablished();
		this.threadMasterRunner.start();
	}

	//

	/**
	 * Does works on background: Ping, check status (does shutdown or not), receive
	 * screencast, ..
	 */
	protected static class MasterRunner implements Runnable {
		protected boolean canRun = true;
		protected final MCMasterRC masterRCInstance;

		protected MasterRunner(MCMasterRC masterRCInstance) {
			super();
			this.masterRCInstance = masterRCInstance;
		}

		protected void shutDownRunner() {
			this.canRun = false;

		}

		@Override
		public void run() {
			System.out.println("MASTER RUNNER:\n\t this.masterRCInstance.isAlive: " + this.masterRCInstance.isAlive
					+ "\n\t canRun: " + canRun);
			while (this.masterRCInstance.isAlive && this.canRun) {
				try {
					Thread.sleep(MILLIS_BETWEEN_EACH_SCREENCAST);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (this.masterRCInstance.isAlive) {
					// try to read some incoming message
					System.out.println("master reading");
					try {
						if (this.masterRCInstance.fromSlaveReceiver.available() > 0) {
							try {
								Object objRead = this.masterRCInstance.fromSlaveReceiver.readObject();
								if (objRead instanceof AMessageCommand) {
//									shutDown(false);
									System.out.println(
											"master received " + objRead.getClass().getSimpleName() + " message");
									((AMessageCommand) objRead).execute(this.masterRCInstance);
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						} else
							System.out.println("master nothing to read");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (this.masterRCInstance.isAlive) { this.masterRCInstance.ping(); }
			}
		}
	}

	@Override
	protected void onConnectionEstablished() {
		System.out.println("master connected");
		super.onConnectionEstablished();
	}
}