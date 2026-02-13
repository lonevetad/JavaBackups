package appunti.testRoomJoiner;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import appunti.testRoomJoiner.ServerTRJ.ConnectionStatus;
import appunti.testRoomJoiner.commands.MessageTRJ_DisautenticateMe;
import appunti.testRoomJoiner.messages.MessageTRJ_Autentication;
import appunti.testRoomJoiner.messages.MessageTRJ_ConnectionStatus;
import appunti.testRoomJoiner.messages.MessageTRJ_SillyPrint;
import importedUtilities.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import importedUtilities.mainTools.LoggerMessages;

/**
 *
 * //////
 *
 * TODO fare il messaggio di disconnessione che permetta al server di rimuovere
 * un Player da connectedPlayer !!!
 *
 *
 * //
 *
 *
 **/
public class ClientTRJ implements LoggerMessagesHolder {
	private static final long serialVersionUID = -6250455555078206L;

	public static enum ClientConnectionStatus {
		Closed, Running, WaitingToConfirmation;
	}

	public ClientTRJ() {
		neverInit = alive = true;
		connectionStatus = ClientConnectionStatus.Closed;
		obsConnectionStatus = new MyObservable();
	}

	boolean neverInit, alive;
	// String username;
	ClientConnectionStatus connectionStatus;
	// Socket socketToServer, socketToMatchManager;
	ConnectionTRJ connectionToServer, connectionToMatchManager;
	Player playerToServer;
	//
	LoggerMessages log;
	ClientTRJ_GUI cgui;
	Observable obsConnectionStatus;
	// Thread threadCommandExecutor;
	ExecutorService threads;
	RunnCommandExecutor runnCommandExecutor;

	@Override
	protected void finalize() throws Throwable {
		closeConnectionToServer();
		super.finalize();
	}

	//

	// TODO GETTER

	@Override
	public LoggerMessages getLog() {
		return log;
	}

	public String getUsername() {
		return playerToServer == null ? null : playerToServer.getName();
	}

	public ClientConnectionStatus getConnectionStatus() {
		return connectionStatus;
	}

	//

	// TODO SETTER

	@Override
	public ClientTRJ setLog(LoggerMessages log) {
		this.log = LoggerMessages.loggerOrDefault(log);
		return this;
	}

	public ClientTRJ setUsername(String username) {
		if (playerToServer != null) {
			this.playerToServer.setName(username);
		} else
			log("ERROR: playerToServer is null while setting the username");
		return this;
	}

	//

	// TODO INIT

	void init() {
		if (neverInit) {
			neverInit = false;
			initGUI();
			initNonGui();
		}
	}

	void initGUI() {
		cgui = new ClientTRJ_GUI(this);
		cgui.init();
		// this.log = sGUI.getLog();
	}

	void initNonGui() {
		setConnectionStatus(ClientConnectionStatus.Closed);
		//
		runnCommandExecutor = new RunnCommandExecutor(this);
		threads = Executors.newFixedThreadPool(2);
		threads.execute(runnCommandExecutor);
	}

	//

	// TODO PUBLIC

	public boolean connectToServer(int port, String ip, String username) {
		Socket sock;
		String error;
		MessageTRJ_Autentication msgAutent;
		if (connectionStatus == ClientConnectionStatus.Closed && port >= 0 && ip != null && username != null) {

			log("trying to connect to server, creating a Socket");
			sock = connectTo(true, port, ip);
			if (sock != null) {
				log("Socket to server created");
				this.connectionToServer = new ConnectionTRJ(sock);
				this.playerToServer = new Player(connectionToServer);
				this.playerToServer.setName(username);
				setConnectionStatus(ClientConnectionStatus.WaitingToConfirmation);
				// now, send a message to "handshake"
				msgAutent = new MessageTRJ_Autentication(playerToServer);
				msgAutent.setUsername(getUsername());
				log("++System++: sending autentication message to server Socket");
				error = connectionToServer.sendMessage(msgAutent);
				if (error != null) {
					log("ERRORS:");
					log(error);
				}
				//
				// then, read the acknowledgement
				startWaitingAcknowledgmentConnectionToServer();
				return true;
			}
		}
		return false;
	}

	public void abortPendingConnectionToServer() {
		if (connectionStatus == ClientConnectionStatus.WaitingToConfirmation) {
			closeConnectionToServer();
			log("connection ABORTED");
		}
	}

	public void addObserverToConnectionStatus(Observer o) {
		if (o != null) {
			obsConnectionStatus.addObserver(o);
		}
	}

	public void sendSillyMessageToServer() {
		MessageTRJ_SillyPrint msp;
		String error;
		if (connectionStatus == ClientConnectionStatus.Running) {
			msp = new MessageTRJ_SillyPrint(playerToServer);
			log("sending silly print");
			error = connectionToServer.sendMessage(msp);
			if (error == null)
				log("silly print sent");
			else
				log("ERROR on silly print:\n\r" + error);
		} else
			log("\n\r silly method cannot start due to uncorrect status: " + connectionStatus.name());
	}

	//

	// TODO non-PUBLIC

	// key method

	protected void setConnectionStatus(ClientConnectionStatus stat) {
		this.connectionStatus = stat;
		this.obsConnectionStatus.notifyObservers(stat);
	}

	protected Socket connectTo(int port, String ip) {
		return connectTo(true, port, ip);
	}

	protected Socket connectTo(boolean isServer, int port, String ip) {
		Socket s;
		s = null;
		if (port >= 0 && ip != null) {
			try {
				s = new Socket(ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	protected void closeConnectionToServer() {
		// MessageTestRoomJoiner msgClosing;
		if (this.connectionStatus == ClientConnectionStatus.Running) {
			log("Closing connection to server");
			// send message
			connectionToServer.sendMessage(new MessageTRJ_DisautenticateMe(playerToServer).setUsername(getUsername()));
			//
			try {
				connectionToServer.close();
			} catch (IOException e) {
				log(e);
			} finally {
				connectionToServer = null;
			}
			setConnectionStatus(ClientConnectionStatus.Closed);
			//
		}
	}

	protected void switchConnectionStatusToServer(int port, String ip, String username) {
		if (connectionStatus == ClientConnectionStatus.Running) {
			closeConnectionToServer();
		} else if (connectionStatus == ClientConnectionStatus.Closed) {
			connectToServer(port, ip, username);
		}
	}

	protected boolean checkIfConnectionAcknowledgmentArrived() {
		boolean arrived;
		MessageTRJ_ConnectionStatus msgStatus;
		MessageTestRoomJoiner msg;
		ConnectionStatus cs;
		arrived = false;
		if (connectionStatus == ClientConnectionStatus.WaitingToConfirmation) {
			msg = connectionToServer.readMessage();
			if (msg != null) {
				// message
				if (msg instanceof MessageTRJ_ConnectionStatus) {
					msgStatus = (MessageTRJ_ConnectionStatus) msg;
					cs = ConnectionStatus.idToConnectionStatus(msgStatus.getConnectionStatusID());
					log("ConnectionStatus received from server: " + cs.name());
					if (cs.id == ConnectionStatus.ACCEPTED.id) {
						log("EVVIVA ! CONNESSIONE ACCETTATA");
						arrived = true;
					}
				} else {
					log("ERROR: wtf i've received from server? " + msg.getClass().getSimpleName());
				}
			}
		}
		return arrived;
	}

	protected boolean checkAcknowledgmentAndEndHandshake() {
		boolean r;
		r = false;
		if (checkIfConnectionAcknowledgmentArrived()) {
			r = true;
			this.setConnectionStatus(ClientConnectionStatus.Running);
			log("CLIENT RUNNING !!");
		}
		return r;
	}

	protected void startWaitingAcknowledgmentConnectionToServer() {
		threads.execute(new RunnAckChecker(this));
	}

	// utilities

	protected void log(Exception e) {
		e.printStackTrace();
		log(e.toString());
	}

	protected void log(String e) {
		if (e != null) log.log("++CLIENT++: " + e);
	}

	//

	// TODO CLASS

	static abstract class RunnClientTRJ extends RunnableTask {
		private static final long serialVersionUID = 95415063003041822L;

		public RunnClientTRJ(ClientTRJ loggerHolder) {
			super(loggerHolder);
			this.client = loggerHolder;
		}

		ClientTRJ client;

		@Override
		public boolean canContinueCycle() {
			return client.alive;
		}
	}

	static class RunnCommandExecutor extends RunnClientTRJ {
		private static final long serialVersionUID = 840906277711122L;

		public RunnCommandExecutor(ClientTRJ loggerHolder) {
			super(loggerHolder);
		}

		@Override
		public void doEachCycle() {
			// TODO
			this.suspendMyExecution();
		}
	}

	@Deprecated
	static class RunnDelayedCommandExecutor extends RunnClientTRJ {
		private static final long serialVersionUID = 840906277711122L;
		static final int MIN_MILLISEC_EACH_CYCLE = 10;

		public RunnDelayedCommandExecutor(ClientTRJ loggerHolder) {
			super(loggerHolder);
		}

		@Override
		public void doEachCycle() {

		}
	}

	static class RunnAckChecker extends RunnClientTRJ {
		private static final long serialVersionUID = 840906277711122L;
		static final int SECONDS_BEFORE_ABORT_CONNECTION = 30, SECONDS_BETWEEN_EACH_CHECK = 5,
				CHECKS_BEFORE_ABORT = SECONDS_BEFORE_ABORT_CONNECTION / SECONDS_BEFORE_ABORT_CONNECTION;

		public RunnAckChecker(ClientTRJ loggerHolder) {
			super(loggerHolder);
			checksPerformed = 0;
			notConnected = true;
		}

		boolean notConnected;
		int checksPerformed;

		@Override
		public void doEachCycle() {
			/*
			 * int sleeps; sleeps = 0; while(sleeps++ <
			 * SECONDS_BETWEEN_EACH_CHECK)
			 */
			try {
				Thread.sleep(SECONDS_BETWEEN_EACH_CHECK);
			} catch (InterruptedException e) {
				client.log(e);
			}
			client.log("check started");
			if (client.checkAcknowledgmentAndEndHandshake()) notConnected = false;
		}

		@Override
		public boolean canContinueCycle() {
			return super.canContinueCycle() && notConnected && checksPerformed < CHECKS_BEFORE_ABORT;
		}

		@Override
		public void run() {
			super.run();
			if (notConnected) {
				client.abortPendingConnectionToServer();
			}
		}
	}

	// checkIfConnectionAcknowledgmentArrived

	//

	//

	public static void main(String[] args) {
		ClientTRJ c;
		c = new ClientTRJ();
		c.init();
	}

}
