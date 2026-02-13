package appunti.testRoomJoiner.messages;

import videogamesOldVersion.appunti.testRoomJoiner.MessageTestRoomJoiner;
import videogamesOldVersion.appunti.testRoomJoiner.Player;
import videogamesOldVersion.appunti.testRoomJoiner.ServerTRJ;

public abstract class MessageTRJ_Command extends MessageTestRoomJoiner implements Runnable {
	private static final long serialVersionUID = -92907170770L;

	public MessageTRJ_Command(Player sender) {
		super(sender);
	}

	ServerTRJ environment;

	public ServerTRJ getEnvironment() {
		return environment;
	}

	public MessageTRJ_Command setEnvironment(ServerTRJ environment) {
		this.environment = environment;
		return this;
	}

}