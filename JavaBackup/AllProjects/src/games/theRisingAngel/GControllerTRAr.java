package games.theRisingAngel;

import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GControllerRPG;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;
import games.theRisingAngel.loaders.LoaderAbilityTRAr;
import games.theRisingAngel.loaders.LoaderCreatureTRAr;
import games.theRisingAngel.loaders.LoaderEquipTRAr;
import games.theRisingAngel.loaders.LoaderEquipUpgradesTRAr;

public class GControllerTRAr extends GControllerRPG {
	public static final String GM_NAME_TRAR_BASE = "gc_trar_base";

	public GControllerTRAr() {
		super();
	}

	@Override
	protected GameObjectsProvidersHolderRPG newGameObjectsProvider() {
		return new GameObjectsProvidersHolderTRAr();
	}

	@Override
	protected void defineGameModalitiesFactories() {
		this.getGameModalitiesFactories().put(GM_NAME_TRAR_BASE, (name, gc) -> {
			return new GModalityTRAr(name, gc);
		});
	}

	@Override
	protected UserAccountGeneric newUserAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initNonFinalStuffs() {
		super.addGameObjectLoader(new LoaderAbilityTRAr(this.gameObjectsProvidersHolderRPG.getAbilitiesProvider()));
		super.addGameObjectLoader(
				new LoaderEquipUpgradesTRAr(this.gameObjectsProvidersHolderRPG.getEquipUpgradesProvider()));
		super.addGameObjectLoader(new LoaderEquipTRAr(this.gameObjectsProvidersHolderRPG.getEquipmentsProvider()));
		super.addGameObjectLoader(new LoaderCreatureTRAr(this.gameObjectsProvidersHolderRPG.getCreaturesProvider()));

		super.initNonFinalStuffs();
		System.out.println("GControllerTRAr init non final stuff done\n\n");
//		this.gameObjectsProvidersHolderRPG.getEquipmentsProvider().getObjectsIdentified().forEach((n, f) -> {
//			System.out.println("daffaking equip name: " + n);
//		});
	}

}