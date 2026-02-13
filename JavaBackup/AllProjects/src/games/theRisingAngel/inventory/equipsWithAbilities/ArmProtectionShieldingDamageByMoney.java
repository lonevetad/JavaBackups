package games.theRisingAngel.inventory.equipsWithAbilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.providers.AbilitiesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.abilities.ADamageReductionCurrencyBased;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EquipmentTypesTRAn;
import games.theRisingAngel.inventory.EINotJewelry;

/**
 * See {@link ADamageReductionCurrencyBased} , grants 10% of money as damage
 * reduction, for at maximum of 50 of reduction.
 */
public class ArmProtectionShieldingDamageByMoney extends EINotJewelry {
	private static final long serialVersionUID = 4778562323222481L;
	public static final String NAME = "Goldchain Forearm of Fat Cat";
	protected ADamageReductionCurrencyBased abilityDamageReductionByPaying;

	public ArmProtectionShieldingDamageByMoney(GModalityRPG gmrpg) {
		super(gmrpg, EquipmentTypesTRAn.Arms, NAME, //
				new AttributeModification[] { new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 3),
						new AttributeModification(AttributesTRAn.MagicalDamageReduction, 5),
						new AttributeModification(AttributesTRAn.Defense, 3),
						new AttributeModification(AttributesTRAn.Dexterity, -5),
						new AttributeModification(AttributesTRAn.Precision, -2),
						new AttributeModification(AttributesTRAn.Strength, -1) });
	}

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
		AbilitiesProvider ap;
		ap = ((GameObjectsProvidersHolderRPG) providersHolder).getAbilitiesProvider();
		this.abilityDamageReductionByPaying = (ADamageReductionCurrencyBased) ap.getAbilityByName(gm,
				ADamageReductionCurrencyBased.NAME // + DamageTypesTRAn.Physical.getName()
						+ ADamageReductionCurrencyBased.RARITY);
//		this.abilityDamageReductionByPaying.setPerThousandFraction(100);
//		this.abilityDamageReductionByPaying.setMaximumReduction(50);
		this.abilityDamageReductionByPaying.setOwner(this);
		super.addAbility(this.abilityDamageReductionByPaying);
	}
}