package games.theRisingAngel;

import games.generic.GameOptions;
import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.loaders.LoaderManager;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GControllerRPG;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.loaders.LoaderManagerTRAn;
import games.theRisingAngel.providers.GameObjectsProvidersHolderTRAn;

public class GControllerTRAn extends GControllerRPG {

	public GControllerTRAn() {
		super();
		this.initNonFinalStuffs();
	}

	//

	//

	//

	@Override
	protected GameOptions newGameOptions() { return new GameOptionsTRAn(this); }

	@Override
	protected void defineGameModalitiesFactories() {
		System.out.println("DEFINE GAME MODALITIES FACTORIES IN GControllerTRAn");
		this.getGameModalitiesFactories().put(GModalityTRAnBaseWorld.NAME,
				(gc, name) -> new GModalityTRAnBaseWorld(gc, name));
	}

	@Override
	protected GameObjectsProvidersHolderRPG newGameObjectProvidersHolderFor(GModality gm) {
		return new GameObjectsProvidersHolderTRAn((GModalityRPG) gm);
	}

	@Override
	protected LoaderManager newLoaderManager() { return new LoaderManagerTRAn(this); }

	@Override
	protected UserAccountGeneric newUserAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initNonFinalStuffs() {
		((GameObjectsProvidersHolderTRAn) getGameObjectsProvidersHolder())
				.setGameModality((GModalityRPG) getCurrentGameModality());

		super.initNonFinalStuffs();
		System.out.println("GControllerTRAn init non final stuff done\n\n");
//		this.gameObjectsProvidersHolderRPG.getEquipmentsProvider().getObjectsIdentified().forEach((n, f) -> {
//			System.out.println("daffaking equip name: " + n);
//		});
	}

}