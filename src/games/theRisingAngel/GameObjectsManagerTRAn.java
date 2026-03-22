package games.theRisingAngel;

import java.util.Random;

import dataStructures.isom.MultiISOMRetangularMap;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageTypeGeneric;
import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.events.GEventManager;
import games.generic.controlModel.events.event.EventDamage;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import games.generic.controlModel.subimpl.GModalityET;
import games.theRisingAngel.creatures.BaseCreatureTRAn;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.RechargeableResourcesTRAn;
import games.theRisingAngel.events.GEventInterfaceTRAn;
import games.theRisingAngel.misc.GObjectsInSpaceManagerTRAn;
import tools.NumberManager;

public class GameObjectsManagerTRAn implements GameObjectsManager {
	public static final int THRESHOLD_PROBABILITY_BASE_TO_HIT = 50,
			THRESHOLD_PROBABILITY_BASE_TO_HIT_PER_THOUSAND = THRESHOLD_PROBABILITY_BASE_TO_HIT * 10,
			MAX_PROBABILITY_VALUE = 100, MAX_PROBABILITY_VALUE_PER_THOUSAND = 10 * MAX_PROBABILITY_VALUE;

	/** DO NOT ALTER */
	public static final AttributesTRAn[] leechableResources = { AttributesTRAn.LifeLeechPercentage,
			AttributesTRAn.ManaLeechPercentage, AttributesTRAn.ShieldLeechPercentage,
			AttributesTRAn.StaminaLeechPercentage };
	/** DO NOT ALTER */
	public static final RechargeableResourcesTRAn[] leechableResourcesType = { RechargeableResourcesTRAn.Life,
			RechargeableResourcesTRAn.Mana, RechargeableResourcesTRAn.Shield, RechargeableResourcesTRAn.Stamina };

	public GameObjectsManagerTRAn(GModalityTRAnBaseWorld gmodalityTrar) {
		super();
		MultiISOMRetangularMap<Double> isom;
		setGameModality(gmodalityTrar);
		// isom = new MISOMImpl(false, 1, 1, NumberManager.getDoubleManager());
		isom = new MultiISOMRetangularMap<Double>();
		isom.setWeightManager(NumberManager.getDoubleManager());
		this.goism = new GObjectsInSpaceManagerTRAn(isom);
	}

	protected GModalityTRAnBaseWorld gmodalityTran;
	protected GObjectsInSpaceManager goism;

	@Override
	public GModality getGameModality() { return gmodalityTran; }

	@Override
	public void setGameModality(GModality gameModality) { this.gmodalityTran = (GModalityTRAnBaseWorld) gameModality; }

	@Override
	public GObjectsInSpaceManager getGObjectInSpaceManager() { return goism; }

	@Override
	public GEventInterface getGEventInterface() { return gmodalityTran.getEventInterface(); }

	@Override
	public void setGObjectsInSpaceManager(GObjectsInSpaceManager gisom) { this.goism = gisom; }

	@Override
	public void setGEventInterface(GEventInterface gei) {
//		this.gei = (GEventInterfaceTRAr) gei;
		gmodalityTran.setEventInterface(gei);
	}

	//

	/**
	 * Deals damage considering the probabilities to hit and avoid strikes, both
	 * normal and, in case of success of the former, critical damage.<br>
	 * At each test an event is fired (the default implementation of the
	 * {@link GEventManager} lets to define if the events are performed as they are
	 * fired or just "posted" into a queue, see
	 * {@link GEvent#isRequirigImmediateProcessing()}.) to allow modifications of
	 * damage amount or something else to be applied.<br>
	 * The leech mechanism is embedded and instantaneous.
	 * <p>
	 * Inherited documentation:<br>
	 * {@inheritDoc}
	 */
	@Override
	public void dealsDamageTo(DamageDealerGeneric source, CreatureSimple target, DamageGeneric damage) {
		int rollOfHitting, thresholdWithinHitting;
		final int luckAdvantage;
		DamageTypeGeneric damageType;
		Random rand;
		EventDamage ed;
		GModalityET gm;
		GEventInterface eventInterface;

		rand = this.getGameModality().getRandom();
		luckAdvantage = (source.getLuckPerThousand() - target.getLuckPerThousand());
		rollOfHitting = rand.nextInt(MAX_PROBABILITY_VALUE_PER_THOUSAND);
		damageType = damage.getDamageType();

		// apply bonus, multipliers, reductions, etc ..
		{
			int damageAmount, multiplierPercentage;
			damageAmount = damage.getDamageAmount();
			// bonus and reductions
			damageAmount += source.getDamageBonus(damageType) - target.getDamageReduction(damageType);
			if (damageAmount <= 0) { return; }
			multiplierPercentage = source.getDamageBonusPercentage(damageType)
					- target.getDamageBonusPercentage(damageType);
			if (multiplierPercentage != 0) {
				damageAmount = (int) (//
				((100 + multiplierPercentage) * (long) damageAmount) //
						/ 100);
			}
			if (damageAmount <= 0) { return; }
			damage.setDamageAmount(damageAmount);
		}

		// consider source and target chances
		thresholdWithinHitting = THRESHOLD_PROBABILITY_BASE_TO_HIT_PER_THOUSAND + luckAdvantage + //
				(source.getProbabilityPerThousandHit(damageType) - target.getProbabilityPerThousandAvoid(damageType));
		gm = (GModalityET) getGameModality();
		eventInterface = this.getGEventInterface();
		if (rollOfHitting <= thresholdWithinHitting) {
//			GameObjectsManager.super.dealsDamageTo(source, target, damage);
			ed = eventInterface.fireDamageDealtEvent(gm, source, target, damage);
//			update the damage amount
			damage.setValue(ed.getDamageAmountToBeApplied());

			if (ed.getDamageAmountToBeApplied() > 0) {

				// does it crits?

				// use "rollOfHitting" as a "multiplier"
				rollOfHitting = (source.getPercentageCriticalStrikeMultiplier(damageType)
						- target.getPercentageCriticalStrikeReduction(damageType));

				// now uses "r" as a "temp"
				if (rollOfHitting > 0) { // no positive multiplier -> no crit applied
					thresholdWithinHitting = luckAdvantage + //
							(source.getPercentageCriticalStrikeMultiplier(damageType)
									- target.getPercentageCriticalStrikeReduction(damageType));

					// use thresholdWithinHitting as "is positive: crit"
					thresholdWithinHitting = luckAdvantage + //
							(source.getProbabilityPerThousandCriticalStrike(damageType)
									- target.getProbabilityPerThousandCriticalStrike(damageType));

					thresholdWithinHitting -= rand.nextInt(MAX_PROBABILITY_VALUE_PER_THOUSAND);
					if (thresholdWithinHitting >= 0) {
						// crit dealt !
						damage.setValue((damage.getDamageAmount() * (100 + rollOfHitting)) / 100);
						ed = eventInterface.fireCriticalDamageDealtEvent(gm, source, target, damage);
						// update the damage amount
						damage.setValue(ed.getDamageAmountToBeApplied());
					}
				}
				// recycle "r" as "damage amount to be inflicted"
				rollOfHitting = damage.getDamageAmount();
				if (rollOfHitting > 0) {
					if (source instanceof BaseCreatureTRAn) {
						int i;
						BaseCreatureTRAn bc;
						ResourceAmountRecharged healing;
						bc = (BaseCreatureTRAn) source;
						/*
						 * Recycle "thresholdToHitting" as "amount to leech". Also accepts negative
						 * values: some mechanism like "guilt".
						 */
						i = leechableResources.length;
						while (--i >= 0) {
							thresholdWithinHitting = bc.getAttributes().getValue(leechableResources[i]);
							if (thresholdWithinHitting != 0) {
								thresholdWithinHitting = (thresholdWithinHitting * rollOfHitting) / 100;
								if (thresholdWithinHitting != 0) {
									healing = new ResourceAmountRecharged(leechableResourcesType[i],
											thresholdWithinHitting);
									bc.performRechargeOf(healing, source);
								}
							}
						}
					}
					// in the end, the damage is ready to be delivered
					target.receiveDamage(gm, damage, source);
				}
			}
		} else {
			GEventInterfaceTRAn geiTran;
			geiTran = (GEventInterfaceTRAn) eventInterface; // this.getGEventInterface();
			geiTran.fireDamageAvoidedEvent(gm, source, target, damage);
			geiTran.fireDamageMissedEvent(gm, source, target, damage);
		}
	}
}