package appunti.testRoomJoiner.messages;

import videogamesOldVersion.appunti.testRoomJoiner.MessageTestRoomJoiner;
import videogamesOldVersion.appunti.testRoomJoiner.Player;

public class MessageTRJ_Autentication extends MessageTestRoomJoiner {
	private static final long serialVersionUID = 650300011505888056L;

	public MessageTRJ_Autentication(Player sender) {
		super(sender);
		if (sender != null) setUsername(sender.getName());
	}

	public void setUsername(String s) {
		super.setText(s);
	}

	public String getUsername() {
		return getText();
	}
}