package games.theRisingAngel;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GModalityRPG;

// TODO todo tons of stuffs
public class GModalityTRAr extends GModalityRPG {

	public GModalityTRAr(GController controller, String modalityName) {
		super(controller, modalityName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GEventInterface newEventInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GameObjectsManager newGameObjectsManager(GEventInterface gei) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CurrencySet newCurrencyHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub

	}

}