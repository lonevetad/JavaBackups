package games.generic.controlModel.subimpl;

import games.generic.GameOptions;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;

public abstract class GControllerRPG extends GControllerET {

	public GControllerRPG() {
		super();
		this.sharedGameObjectsProvidersHolderRPG = newSharedGameObjectProvidersHolder();
	}

	protected GameObjectsProvidersHolderRPG sharedGameObjectsProvidersHolderRPG;

	//

	@Override
	protected abstract GameObjectsProvidersHolderRPG newSharedGameObjectProvidersHolder();

	//

	public GameObjectsProvidersHolderRPG getSharedGameObjectsProvidersHolder() {
		return sharedGameObjectsProvidersHolderRPG;
	}

	public void setSharedGameObjectsProvidersHolderRPG(
			GameObjectsProvidersHolderRPG sharedGameObjectsProvidersHolderRPG) {
		this.sharedGameObjectsProvidersHolderRPG = sharedGameObjectsProvidersHolderRPG;
	}

	//

	@Override
	protected GameOptions newGameOptions() {
		return new GameOptionsRPG(this);
	}

	@Override
	public void prepareLoadingAll() {
		super.prepareLoadingAll();
	}
}