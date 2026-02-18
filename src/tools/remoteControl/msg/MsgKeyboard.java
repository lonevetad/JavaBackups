package tools.remoteControl.msg;

import java.awt.Robot;

import tools.remoteControl.controllerModel.AModelControllerRC;

public class MsgKeyboard extends AMessageCommand {
	private static final long serialVersionUID = 65210048000L;

	public MsgKeyboard(boolean isPressed, int key) {
		super(MessageTypeRC.Keyboard);
		this.isPressed = isPressed;
		this.key = key;
	}

	protected final boolean isPressed;
	protected final int key;

	@Override
	public void execute(AModelControllerRC context) {
		Robot r;
		r = context.getRobotActionSensorActuator();
//		Consumer<Integer> act = isPressed ? r::keyPress : r::keyRelease;
//		act.accept(key);
		if (isPressed)
			r.keyPress(key);
		else
			r.keyRelease(key);
	}
}