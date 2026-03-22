package tools.remoteControl.controllerModel;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import tools.ObservableSimple;
import tools.ObserverSimple;
import tools.remoteControl.msg.AMessageCommand;
import tools.remoteControl.view.ViewRC;

public abstract class AModelControllerRC {
	protected static final long MILLIS_BETWEEN_EACH_SCREENCAST = 500; // 250;

	protected final Robot robotActionSensorActuator;
	protected final ViewRC view;
	protected final List<ObserverSimple<ConnectionStatus>> observersConnectionStatus;
	protected final ObservableSimple<ConnectionStatus> observableConnectionStatus;

	public AModelControllerRC(ViewRC view) throws AWTException, IOException {
		super();
		this.view = view;
		this.robotActionSensorActuator = new Robot();
		this.observersConnectionStatus = new LinkedList<>();
		this.observableConnectionStatus = () -> this.observersConnectionStatus;
	}

	public Robot getRobotActionSensorActuator() { return robotActionSensorActuator; }

	public ObservableSimple<ConnectionStatus> getObservableConnectionStatus() { return observableConnectionStatus; }

	public abstract boolean isClient();

	public abstract boolean execute(AMessageCommand msg);

	public abstract void shutDown(boolean isUserAction);

	//

	protected void onConnectionEstablished() {
		this.getObservableConnectionStatus().notifyObservers(ConnectionStatus.Connected);
	}
}