package tools.remoteControl.msg;

import tools.remoteControl.controllerModel.AModelControllerRC;

public class MsgPing extends AMessageCommand {
	private static final long serialVersionUID = 650788L;

	public MsgPing() { super(MessageTypeRC.Ping); }

	@Override
	public void execute(AModelControllerRC context) {}
}