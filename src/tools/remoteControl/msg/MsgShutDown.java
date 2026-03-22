package tools.remoteControl.msg;

import tools.remoteControl.controllerModel.AModelControllerRC;

public class MsgShutDown extends AMessageCommand {
	private static final long serialVersionUID = 6507858L;

	public MsgShutDown() { super(MessageTypeRC.ShutDown); }

	@Override
	public void execute(AModelControllerRC context) { context.shutDown(false); }
}