package games.theRisingAngel.events;

import java.awt.Point;

import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.events.GEventManager;
import games.generic.controlModel.events.event.EventDestructionObj;
import games.generic.controlModel.events.event.EventDamage;
import games.generic.controlModel.events.event.EventEnteringOnMap;
import games.generic.controlModel.events.event.EventMoneyChange;
import games.generic.controlModel.events.event.EventMoviment;
import games.generic.controlModel.events.event.EventResourceRecharge;
import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.misc.Currency;
import games.generic.controlModel.objects.DestructibleObject;
import games.generic.controlModel.objects.LivingObject;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GEventManagerFineGrained;
import games.generic.controlModel.subimpl.GModalityET;
import games.theRisingAngel.enums.EventsTRAn;
import geometry.ObjectLocated;
import tools.ObjectWithID;

public class GEventInterfaceTRAn implements GEventInterfaceRPG {

	public GEventInterfaceTRAn() { super(); }

	protected GEventManager gem;

	@Override
	public void setNewGameEventManager(GModalityET gameModality) {
		this.gem = new GEventManagerFineGrained(gameModality);
	}

	@Override
	public GEventManager getGameEventManager() { return gem; }

	//

	// TODO EVENTS

	@Override
	public EventDestructionObj fireDestructionObjectEvent(GModalityET gaModality, DestructibleObject desObj) {
		EventDestructionObj doe;
		doe = new EventDestructionObj(desObj, EventsTRAn.Destroyed.getName());
		this.getGameEventManager().fireEvent(doe);
		return doe;
	}

// TODOOOOOOOOO dc'� da spostare molti eventi in un luogo pi� idoneo

	@Override
	public void firePlayerEnteringInMap(GModalityET gameModality, PlayerGeneric p) {
		EventEnteringOnMap eeom;
		eeom = new EventEnteringOnMap((ObjectLocated) p, EventsTRAn.ObjectAdded);
		this.getGameEventManager().fireEvent(eeom);
	}

	@Override
	public void fireGameObjectMoved(GModalityET gameModality, Point previousLocation, ObjectLocated o) {
		this.getGameEventManager().fireEvent(EventMoviment.newEventMoviment(o, previousLocation, o.getLocation()));
	}

	/***
	 * @param currencyType the index (or an id) of the currency earned-lost
	 */
	@Override
	public void fireCurrencyChangeEvent(GModalityET gm, Currency currency, int oldValue, int newValue) {
		this.getGameEventManager().fireEvent(new EventMoneyChange(gm.getPlayer(), currency, oldValue, newValue));
	}

	@Override
	public EventDamage fireDamageDealtEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage) {
		EventDamage ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageInflicted, source, target, damage);
		this.getGameEventManager().fireEvent(ed);
		return ed;
	}

	public void fireDamageAvoidedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage) {
		EventDamage ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageAvoided, source, target, damage);
		this.getGameEventManager().fireEvent(ed);
	}

	public void fireDamageMissedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage) {
		EventDamage ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageMissed, source, target, damage);
		this.getGameEventManager().fireEvent(ed);
	}

	@Override
	public EventDamage fireCriticalDamageDealtEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric originalDamage) {
		EventDamage ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageCriticalInflicted, source, target, originalDamage);
		this.getGameEventManager().fireEvent(ed);
		return ed;
	}

	@Override
	public EventDamage fireDamageReceivedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric originalDamage, int damageAmountToBeApplied) {
		EventDamageTRAn ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageReceived, source, target, originalDamage, damageAmountToBeApplied);
		this.getGameEventManager().fireEvent(ed);
		return ed;
	}

	@Override
	public EventDamage fireDamageCriticalReceivedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric originalDamage, int damageAmountToBeApplied) {
		EventDamageTRAn ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageCriticalReceived, source, target, originalDamage,
				damageAmountToBeApplied);
		this.getGameEventManager().fireEvent(ed);
		return ed;
	}

	@Override
	public <SourceRecharge extends ObjectWithID> EventResourceRecharge<SourceRecharge> fireResourceRechargeGivenEvent(
			GModalityET gaModality, SourceRecharge whoIsPerformingTheRecharge, ResourceRechargeableHolder receiver,
			ResourceAmountRecharged rechargeInstance) {
		EventResourceRecharge<SourceRecharge> eventRecharge;
		eventRecharge = new EventResourceRechargeTRAr<>(EventsTRAn.ResourceRechargeGiven, whoIsPerformingTheRecharge,
				receiver, rechargeInstance);
		this.getGameEventManager().fireEvent(eventRecharge);
		return eventRecharge;
	}

	@Override
	public <SourceRecharge extends ObjectWithID> EventResourceRecharge<SourceRecharge> fireResourceRechargeReceivedEvent(
			GModalityET gaModality, SourceRecharge whoIsPerformingTheRecharge, ResourceRechargeableHolder receiver,
			ResourceAmountRecharged rechargeInstance) {
		EventResourceRecharge<SourceRecharge> eventRecharge;
		eventRecharge = new EventResourceRechargeTRAr<>(EventsTRAn.ResourceRechargeReceived, whoIsPerformingTheRecharge,
				receiver, rechargeInstance);
		this.getGameEventManager().fireEvent(eventRecharge);
		return eventRecharge;
	}

	@Override
	public void fireGameObjectAdded(GModalityET gameModality, ObjectLocated o) { // TODO Auto-generated
																					// method stub
	}

	@Override
	public void fireGameObjectRemoved(GModalityET gameModality, ObjectLocated o) { // TODO Auto-generated
																					// method stub
	}

	@Override
	public void fireExpGainedEvent(GModalityET gm, int expGained) { // TODO Auto-generated method stub
	}

	@Override
	public void fireLevelGainedEvent(GModalityET gm, int levelGained) { // TODO Auto-generated method stub
	}

}