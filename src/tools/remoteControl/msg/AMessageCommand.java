package tools.remoteControl.msg;

import java.io.Serializable;

import tools.remoteControl.controllerModel.AModelControllerRC;

public abstract class AMessageCommand implements Serializable {
	private static final long serialVersionUID = 54120L;
	protected final MessageTypeRC msgType;

	public AMessageCommand(MessageTypeRC msgType) {
		super();
		this.msgType = msgType;
	}

	public MessageTypeRC getMsgType() { return msgType; }

	public abstract void execute(AModelControllerRC context);
}