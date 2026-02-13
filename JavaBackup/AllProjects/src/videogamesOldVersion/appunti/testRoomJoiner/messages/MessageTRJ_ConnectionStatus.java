package appunti.testRoomJoiner.messages;

import appunti.testRoomJoiner.ServerTRJ.ConnectionStatus;
import videogamesOldVersion.appunti.testRoomJoiner.MessageTestRoomJoiner;
import videogamesOldVersion.appunti.testRoomJoiner.Player;
import videogamesOldVersion.appunti.testRoomJoiner.ServerTRJ;

public class MessageTRJ_ConnectionStatus extends MessageTestRoomJoiner {
	private static final long serialVersionUID = 526087070833220L;

	public MessageTRJ_ConnectionStatus(Player sender) {
		super(sender);
		connectionStatus = ConnectionStatus.UNDEFINED.id;
	}

	public MessageTRJ_ConnectionStatus(Player sender, ConnectionStatus cs) {
		this(sender);
		this.setConnectionStatus(cs);
	}

	int connectionStatus;

	//

	public void setConnectionStatus(ConnectionStatus cs) {
		connectionStatus = (cs == null ? ConnectionStatus.UNDEFINED : cs).ID;
	}

	public int getConnectionStatusID() {
		return connectionStatus;
	}

	public ConnectionStatus getConnectionStatus() {
		return ServerTRJ.valuesConnectionStatus[connectionStatus];
	}
}
