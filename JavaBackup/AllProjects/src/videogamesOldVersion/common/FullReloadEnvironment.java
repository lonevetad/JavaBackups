package common;

import videogamesOldVersion.common.abstractCommon.MainController;

/**
 * 03/01/2018: Probabilmente questa classe raccoglie delle istanze utili nella
 * fase di avvio del gioco, o comunque nella fase in cui si caricano dei dati
 * salvati sottoforma di file secondo il pattern "Memento".
 */
public class FullReloadEnvironment {

	public FullReloadEnvironment() {
		super();
	}

	public FullReloadEnvironment(MainController main, EnumGameObjectTileImageCollection listTileMapEnum) {
		this();
		this.main = main;
		this.enumGOTICollection = listTileMapEnum;
	}

	MainController main;
	EnumGameObjectTileImageCollection enumGOTICollection;

	//

	public MainController getMain() {
		return main;
	}

	public EnumGameObjectTileImageCollection getEnumGOTICollection() {
		return enumGOTICollection;
	}

	//

	public FullReloadEnvironment setMain(MainController main) {
		this.main = main;
		return this;
	}

	public FullReloadEnvironment setEnumGOTICollection(EnumGameObjectTileImageCollection listTileMapEnum) {
		this.enumGOTICollection = listTileMapEnum;
		return this;
	}

	//

	public boolean canBeUsedToReload() {
		return main != null && enumGOTICollection != null;
	}
}