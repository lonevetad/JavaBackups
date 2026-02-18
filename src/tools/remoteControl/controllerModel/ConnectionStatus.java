package tools.remoteControl.controllerModel;

import java.io.Serializable;

public enum ConnectionStatus implements Serializable {
	NewBorn, Connecting, Connected, ShuttedDown;
}