package tools.remoteControl.msg;

import java.awt.Robot;

import tools.remoteControl.controllerModel.AModelControllerRC;

/** Mouse position are just deltas, not absolute coordinates. */
public class MsgMouse extends AMessageCommand {
	private static final long serialVersionUID = -25879603L;

	public MsgMouse(MouseAction mouseAction, int fp, int sp) {
		super(MessageTypeRC.Mouse);
		this.mouseAction = mouseAction;
		this.firstParameter = fp;
		this.secondParameter = sp;
	}

	public MsgMouse(MouseAction mouseAction, int fp) { this(mouseAction, fp, 0); }

	protected final int firstParameter, secondParameter;
	protected final MouseAction mouseAction;

	@Override
	public void execute(AModelControllerRC context) {
		boolean isClick;
		Robot r;
		r = context.getRobotActionSensorActuator();
		if (this.mouseAction == MouseAction.Move) {
//			Point p = MouseInfo.getPointerInfo().getLocation();
//			r.mouseMove(p.x + firstParameter, p.y + secondParameter);
			r.mouseMove(firstParameter, secondParameter);
			return;
		}
		isClick = this.mouseAction == MouseAction.Click;
		if (isClick || this.mouseAction == MouseAction.Press) { r.mousePress(firstParameter); }
		if (isClick) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (isClick || this.mouseAction == MouseAction.Release) { r.mouseRelease(firstParameter); }
		if (this.mouseAction == MouseAction.Wheel) { r.mouseWheel(firstParameter); }
	}
}