package games.generic.controlModel.subimpl;

import games.generic.GameOptions;
import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;

public abstract class GControllerRPG extends GControllerET {

	public GControllerRPG() {
		super();
		this.gameObjectsProvidersHolderRPG = newGameObjectProvidersHolderFor(null);
	}

	protected GameObjectsProvidersHolderRPG gameObjectsProvidersHolderRPG;

	//

	@Override
	protected abstract GameObjectsProvidersHolderRPG newGameObjectProvidersHolderFor(GModality gm);

	//

	public GameObjectsProvidersHolderRPG getGameObjectsProvidersHolder() { return gameObjectsProvidersHolderRPG; }

	public void setGameObjectsProvidersHolderRPG(GameObjectsProvidersHolderRPG gameObjectsProvidersHolderRPG) {
		this.gameObjectsProvidersHolderRPG = gameObjectsProvidersHolderRPG;
	}

	//

	@Override
	protected GameOptions newGameOptions() { return new GameOptionsRPG(this); }
}