package appunti.testRoomJoiner.commands;

import appunti.testRoomJoiner.AbstractCommand;
import appunti.testRoomJoiner.ClientTRJ;

public class CommandClient extends AbstractCommand {
	private static final long serialVersionUID = 210990090654L;

	public CommandClient(ClientTRJ client) {
		super();
		this.client = client;
	}

	ClientTRJ client;

	//

	public ClientTRJ getClient() {
		return client;
	}

	public CommandClient setClient(ClientTRJ client) {
		this.client = client;
		return this;
	}

}
