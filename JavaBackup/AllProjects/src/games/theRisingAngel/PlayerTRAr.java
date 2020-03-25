package games.theRisingAngel;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.subImpl.GEvent;
import games.generic.controlModel.subImpl.GModalityET;
import games.generic.controlModel.subImpl.PlayerInGameGeneric_ExampleRPG1;

public class PlayerTRAr extends PlayerInGameGeneric_ExampleRPG1 implements CreatureOfRPGs {

	public PlayerTRAr(GModality gameModality) {
		super(gameModality);
	}

	//

	@Override
	public int getLifeMax() {
		return this.attributes.getValue(AttributesTRAr.LifeMax.getIndex());
	}

	@Override
	public int getLife() {
		return this.attributes.getValue(AttributesTRAr.LifeCurrent.getIndex());
	}

	//

	@Override
	public void setLife(int life) {
		this.attributes.setOriginalValue(AttributesTRAr.LifeCurrent.getIndex(), life);
	}

	@Override
	public void setLifeMax(int lifeMax) {
		if (lifeMax > 0) {
			this.attributes.setOriginalValue(AttributesTRAr.LifeMax.getIndex(), lifeMax);
			if (this.getLife() > lifeMax)
				this.setLife(lifeMax);
		}
	}

	//

	// TODO other methods

	@Override
	public void receiveDamage(GModality gm, int damage) {
	}

	@Override
	public void receiveHealing(GModality gm, int healingAmount) {
	}

	@Override
	public boolean destroy() {
		if (!isDestroyed) {
			isDestroyed = true;
			fireDestruction(getGameModality());
			return true;
		}
		return false;
	}

	@Override
	public void act(GModality modality, long milliseconds) {
	}

	@Override
	public void notifyEvent(GModality modality, GEvent ge) {
	}

	@Override
	public void onStartingGame(GModality mg) {
	}

	@Override
	public void onLeavingMap() {
	}

	@Override
	public void onEnteringInGame(GModality gm) {
	}

	//

	// TODO FIRE EVENTS

	@Override
	public void fireDamageReceived(GModality gm, int originalDamage) {
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
	}

	@Override
	public void fireHealingReceived(GModality gm, int originalHealing) {
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
	}

	@Override
	public void fireDestruction(GModality gm) {
		GModalityET gmet;
		EvenMan
		if(gm==null || (!(gm instanceof GModalityET)))return;
gmet = (GModalityET)gm;
		gmet.getEventManager().f
	}

	@Override
	public void fireExpGainedEvent(GModality gm, int expGained) {
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
	}

	@Override
	public void fireLevelGainedEvent(GModality gm, int newLevel) {
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
	}
}