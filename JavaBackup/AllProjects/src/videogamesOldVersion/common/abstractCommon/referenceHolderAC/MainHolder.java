package common.abstractCommon.referenceHolderAC;

import java.io.Serializable;

import videogamesOldVersion.common.abstractCommon.MainController;

public interface MainHolder extends Serializable {

	public MainController getMain();

	public MainHolder setMain(MainController main);

}