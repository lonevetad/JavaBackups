package games.theRisingAngel.loaders;

import java.util.function.Consumer;

import games.generic.controlModel.GController;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.abilities.impl.AbilityBonusDependingOnOtherBonuses;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventDamage;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.objects.LivingObject;
import games.generic.controlModel.subimpl.LoaderAbilities;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.abilities.AAttrSingleBonusMalusRandom;
import games.theRisingAngel.abilities.AAttrSingleBonusMalusRandomFixedAmount;
import games.theRisingAngel.abilities.AAttrSingleBonusMalusRandomPercentage;
import games.theRisingAngel.abilities.ADamageReductionCurrencyBased;
import games.theRisingAngel.abilities.ADamageReductionOnLifeLowerToPhysicalAttributes;
import games.theRisingAngel.abilities.AFireShpereOrbiting;
import games.theRisingAngel.abilities.AIntelligenceToShield;
import games.theRisingAngel.abilities.ALifeHealingMakesEarnBaseCurrency;
import games.theRisingAngel.abilities.ALoseManaBeforeLife;
import games.theRisingAngel.abilities.AMeditationMoreRegen;
import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;
import games.theRisingAngel.abilities.AProtectButMakesSoft;
import games.theRisingAngel.abilities.ARandomScatteringOrbs;
import games.theRisingAngel.abilities.ARandomScatteringOrbsImpl;
import games.theRisingAngel.abilities.ARegenBonusPayingPrecisionBasedOnLifeMissing;
import games.theRisingAngel.abilities.ARegenToLeech;
import games.theRisingAngel.abilities.AShieldingButWeakining;
import games.theRisingAngel.abilities.AShieldingEachRchargeableResources;
import games.theRisingAngel.abilities.ASimpleFixedBufferVanishingTRAn;
import games.theRisingAngel.abilities.AVampireBerserker;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.enums.RaritiesTRAn;

public class LoaderAbilityTRAn extends LoaderAbilities {

	public LoaderAbilityTRAn(GameObjectsProvider<AbilityGeneric> objProvider) { super(objProvider); }

	@Override
	public LoadStatusResult loadInto(GController gcontroller) {
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAn.Physical.getName(),
//				ADamageReductionCurrencyBased.RARITY,
//				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAn.Physical));
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAn.Magical.getName(),
//				ADamageReductionCurrencyBased.RARITY, gc -> new ADamageReductionCurrencyBased(DamageTypesTRAn.Magical));
		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, AMoreDamageReceivedMoreLifeRegen.RARITY,
				AMoreDamageReceivedMoreLifeRegen::new);
		objProvider.addObj(AFireShpereOrbiting.NAME, AFireShpereOrbiting.RARITY, AFireShpereOrbiting::new);
		objProvider.addObj(AShieldingButWeakining.NAME, AShieldingButWeakining.RARITY.getRarityIndex(),
				AShieldingButWeakining::new);
		objProvider.addObj(ALifeHealingMakesEarnBaseCurrency.NAME, ALifeHealingMakesEarnBaseCurrency.RARITY,
				gm -> new ALifeHealingMakesEarnBaseCurrency());
		objProvider.addObj(ARandomScatteringOrbs.NAME,
				gm -> new ARandomScatteringOrbsImpl((GModalityTRAnBaseWorld) gm));
		objProvider.addObj("Wounded Berseker", 3, gm -> {
			ASimpleFixedBufferVanishingTRAn a;
			a = new ASimpleFixedBufferVanishingTRAn(gm, "Wounded Berseker",
					new AttributeModification[] { new AttributeModification(AttributesTRAn.Strength, 8),
							new AttributeModification(AttributesTRAn.Defense, 1),
							new AttributeModification(AttributesTRAn.Constitution, 4),
							new AttributeModification(AttributesTRAn.Intelligence, -6),
							new AttributeModification(AttributesTRAn.Wisdom, -4) }) {
				private static final long serialVersionUID = 2588519748901517L;

				@Override
				protected boolean isAcceptableEvent(IGEvent e) {
					return EventsTRAn.DamageReceived.getName() == e.getName() && //
					// I am the receiver?
					((EventDamage) e).isTarget((LivingObject) this.getOwner());
				}
			};
			a.addEventWatched(EventsTRAn.DamageReceived);
			a.setCumulative(true);
			a.setAbilityEffectDuration(5000);
			a.setVanishingEffectDuration(3000);
			a.setRarityIndex(3);
			a.setMaxAmountStackedTriggerCharges(5);
			return a;
		});
		objProvider.addObj("Frenzy for a miss", 2, gm -> {
			ASimpleFixedBufferVanishingTRAn a;
			a = new ASimpleFixedBufferVanishingTRAn(gm, "Frenzy for a miss",
					new AttributeModification[] { new AttributeModification(AttributesTRAn.Strength, 6),
							new AttributeModification(AttributesTRAn.PhysicalProbabilityPerThousandHit, 4), // frenzy
							new AttributeModification(AttributesTRAn.MagicalProbabilityPerThousandHit, 4), // frenzy
							new AttributeModification(AttributesTRAn.Dexterity, -5),
							new AttributeModification(AttributesTRAn.Intelligence, -6),
							new AttributeModification(AttributesTRAn.Wisdom, -7) }) {

				private static final long serialVersionUID = 777962548965262L;

				@Override
				protected boolean isAcceptableEvent(IGEvent e) {
					return EventsTRAn.DamageMissed.getName() == e.getName() && //
					// I am the one who missed the attack?
					((EventDamage) e).isSource((DamageDealerGeneric) this.getOwner());
				}
			};
			a.setCumulative(false);
			a.addEventWatched(EventsTRAn.DamageMissed);
			a.setAbilityEffectDuration(4000);
			a.setVanishingEffectDuration(2000);
			a.setRarityIndex(2);
			a.setMaxAmountStackedTriggerCharges(5);
			return a;
		});
		objProvider.addObj("Immunoadrenaline", 2, gm -> {
			ASimpleFixedBufferVanishingTRAn a;
			a = new ASimpleFixedBufferVanishingTRAn(gm, "Immunoadrenaline",
					new AttributeModification[] { new AttributeModification(AttributesTRAn.MagicalDamageReduction, -10),
							new AttributeModification(AttributesTRAn.PhysicalDamageReduction, -10),
							new AttributeModification(AttributesTRAn.Velocity,
									GModalityTRAnBaseWorld.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN), // frenzy
							new AttributeModification(AttributesTRAn.LifeRegen, 10) }) {
				private static final long serialVersionUID = 2588519748901515L;

				@Override
				protected boolean isAcceptableEvent(IGEvent e) {
					return EventsTRAn.DamageReceived.getName() == e.getName() && //
					// I am the receiver?
					((EventDamage) e).isTarget((LivingObject) this.getOwner());
				}
			};
			a.setCumulative(false);
			a.addEventWatched(EventsTRAn.DamageReceived);
			a.setAbilityEffectDuration(2000);
			a.setVanishingEffectDuration(1000);
			a.setRarityIndex(2);
			return a;
		});
		objProvider.addObj("Bloodlust", 3, gm -> {
			ASimpleFixedBufferVanishingTRAn a;
			a = new ASimpleFixedBufferVanishingTRAn(gm, "Bloodlust",
					new AttributeModification[] { new AttributeModification(AttributesTRAn.Strength, 4),
							new AttributeModification(AttributesTRAn.Health, 4),
							new AttributeModification(AttributesTRAn.LifeRegen, 1),
							new AttributeModification(AttributesTRAn.Velocity, // frenzy
									GModalityTRAnBaseWorld.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN >> 2),
							new AttributeModification(AttributesTRAn.Intelligence, -1),
							new AttributeModification(AttributesTRAn.Wisdom, -3),
							new AttributeModification(AttributesTRAn.ManaRegen, 2) }) {
				private static final long serialVersionUID = 287962548965262L;

				@Override
				protected boolean isAcceptableEvent(IGEvent e) {
					return EventsTRAn.DamageInflicted.getName() == e.getName() && //
					// I am the one who missed the attack?
					((EventDamage) e).isSource((DamageDealerGeneric) this.getOwner());
				}
			};
			a.setCumulative(true);
			a.addEventWatched(EventsTRAn.DamageInflicted);
			a.setAbilityEffectDuration(2000);
			a.setVanishingEffectDuration(2500);
			a.setRarityIndex(3);
			a.setMaxAmountStackedTriggerCharges(7);
			return a;
		});
		objProvider.addObj(ALoseManaBeforeLife.NAME, ALoseManaBeforeLife.RARITY, gm -> new ALoseManaBeforeLife());
		objProvider.addObj(AShieldingEachRchargeableResources.NAME, AShieldingEachRchargeableResources.RARITY,
				gm -> new AShieldingEachRchargeableResources());
		objProvider.addObj(AVampireBerserker.NAME, AVampireBerserker.RARITY, AVampireBerserker::new);
		objProvider.addObj(ARegenToLeech.NAME, ARegenToLeech.RARITY, ARegenToLeech::new);
		objProvider.addObj(AAttrSingleBonusMalusRandomFixedAmount.NAME, AAttrSingleBonusMalusRandom.RARITY,
				AAttrSingleBonusMalusRandomFixedAmount::new);
		objProvider.addObj(AAttrSingleBonusMalusRandomPercentage.NAME, AAttrSingleBonusMalusRandom.RARITY,
				AAttrSingleBonusMalusRandomPercentage::new);

		//
		forEachLevel_ZeroToMaximum(ml -> {
			objProvider.addObj(AMeditationMoreRegen.NAME + ml, ml,
//					((Function<Integer, FactoryObjGModalityBased<AbilityGeneric>>) (level -> {
//						return gm -> new AMeditationMoreRegen(level);
//					})).apply(maxLevel) // moved to a function because reminds TOO MUCH to JavaScript ...
					gm -> new AMeditationMoreRegen(gm, ml)//
			);
			objProvider.addObj(ADamageReductionCurrencyBased.NAME + ml, ml,
					gm -> new ADamageReductionCurrencyBased(gm, ml));

			// other ones using applicable to a set of levels/rarities/ ?
		});
		objProvider.addObj(AProtectButMakesSoft.NAME, AProtectButMakesSoft.RARITY, AProtectButMakesSoft::new);

		objProvider.addObj("Mag(ic)netic Dynamo", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses(gm, "Mag(ic)netic Dynamo", //
					new AttributeIdentifier[] { //
							AttributesTRAn.MagicalDamageBonus, //
							AttributesTRAn.MagicalDamageReduction, AttributesTRAn.ShieldMax, //
							AttributesTRAn.ShieldRegen }//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.ShieldRegen }, //
					new AttributeIdentifier[] { AttributesTRAn.ShieldMax }, //
					new AttributeIdentifier[] { AttributesTRAn.MagicalDamageReduction }, //
					new AttributeIdentifier[] { AttributesTRAn.MagicalDamageBonus }//
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("Muscles Meat", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses(gm, "Muscles Meat", //
					new AttributeIdentifier[] { //
							AttributesTRAn.PhysicalDamageBonus, //
							AttributesTRAn.PhysicalDamageReduction, //
							AttributesTRAn.LifeMax, //
							AttributesTRAn.LifeRegen }//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.LifeRegen }, //
					new AttributeIdentifier[] { AttributesTRAn.LifeMax }, //
					new AttributeIdentifier[] { AttributesTRAn.PhysicalDamageReduction }, //
					new AttributeIdentifier[] { AttributesTRAn.PhysicalDamageBonus }//
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("Assassin's Instinct", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses(gm, "Assassin's Instinct", //
					new AttributeIdentifier[] { //
							AttributesTRAn.PhysicalProbabilityPerThousandHit, //
							AttributesTRAn.MagicalProbabilityPerThousandHit, //
							AttributesTRAn.CriticalProbabilityPerThousandHit, //
							AttributesTRAn.PhysicalProbabilityPerThousandAvoid, //
							AttributesTRAn.MagicalProbabilityPerThousandAvoid, //
							AttributesTRAn.CriticalProbabilityPerThousandAvoid, //
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.PhysicalProbabilityPerThousandAvoid }, //
					new AttributeIdentifier[] { AttributesTRAn.MagicalProbabilityPerThousandAvoid }, //
					new AttributeIdentifier[] { AttributesTRAn.CriticalProbabilityPerThousandAvoid }, //
					new AttributeIdentifier[] { AttributesTRAn.PhysicalProbabilityPerThousandHit }, //
					new AttributeIdentifier[] { AttributesTRAn.MagicalProbabilityPerThousandHit }, //
					new AttributeIdentifier[] { AttributesTRAn.CriticalProbabilityPerThousandHit }//
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("Siphon of Will", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses(gm, "Siphon of Will", //
					new AttributeIdentifier[] { //
							AttributesTRAn.LifeLeechPercentage, //
							AttributesTRAn.ManaLeechPercentage //
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.LifeRegen, AttributesTRAn.PhysicalDamageBonus }, //
					new AttributeIdentifier[] { AttributesTRAn.ManaRegen, AttributesTRAn.MagicalDamageBonus } //
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("Offense is the best Defence", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses(gm, "Offense is the best Defence", //
					new AttributeIdentifier[] { //
							AttributesTRAn.PhysicalDamageBonus, //
							AttributesTRAn.MagicalDamageBonus, //
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.PhysicalDamageReduction }, //
					new AttributeIdentifier[] { AttributesTRAn.MagicalDamageReduction } //
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("You cannot touch me, but...", 2, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses(gm, "You cannot touch me, but...", //
					new AttributeIdentifier[] { //
							AttributesTRAn.PhysicalDamageReduction, //
							AttributesTRAn.MagicalDamageReduction, //
							AttributesTRAn.CriticalMultiplierPercentageReduction //
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.PhysicalProbabilityPerThousandAvoid }, //
					new AttributeIdentifier[] { AttributesTRAn.MagicalProbabilityPerThousandAvoid }, //
					new AttributeIdentifier[] { AttributesTRAn.CriticalProbabilityPerThousandAvoid } //
			});
			a.setRarityIndex(2);
			return a;
		});
		objProvider.addObj("Gonna deflect 'em all", 2, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses(gm, "Gonna deflect 'em all", //
					new AttributeIdentifier[] { //
							AttributesTRAn.PhysicalProbabilityPerThousandAvoid, //
							AttributesTRAn.MagicalProbabilityPerThousandAvoid, //
							AttributesTRAn.CriticalProbabilityPerThousandAvoid//
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.PhysicalDamageReduction }, //
					new AttributeIdentifier[] { AttributesTRAn.MagicalDamageReduction }, //
					new AttributeIdentifier[] { AttributesTRAn.CriticalMultiplierPercentageReduction } //
			});
			a.setRarityIndex(2);
			return a;
		});
		objProvider.addObj(ADamageReductionOnLifeLowerToPhysicalAttributes.NAME,
				ADamageReductionOnLifeLowerToPhysicalAttributes.RARITY,
				ADamageReductionOnLifeLowerToPhysicalAttributes::new);

		objProvider.addObj(ARegenBonusPayingPrecisionBasedOnLifeMissing.NAME,
				ARegenBonusPayingPrecisionBasedOnLifeMissing.RARITY, ARegenBonusPayingPrecisionBasedOnLifeMissing::new);

		this.saveObjectFactory(AIntelligenceToShield.NAME, AIntelligenceToShield.RARITY.getIndex(),
				AIntelligenceToShield::new);

		//

		System.out.println("objProvider ABILITY size: " + objProvider.getObjectsFactoriesCount());
		return LoadStatusResult.Success;
	}

	private void forEachLevel_ZeroToMaximum(Consumer<Integer> c) {
		for (int maxLevel = RaritiesTRAn.Legendary.getRarityIndex(), lowestLevel = RaritiesTRAn.Scrap.getRarityIndex(); //
				lowestLevel <= maxLevel; lowestLevel++) {
			c.accept(lowestLevel);
		}
	}
}