package games.theRisingAngel.inventory.equipsWithAbilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.providers.AbilitiesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EquipmentTypesTRAn;
import games.theRisingAngel.inventory.EIJewelry;

/** See {@link AMoreDamageReceivedMoreLifeRegen} */
public class NecklaceOfPainRinvigoring extends EIJewelry {
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Necklace of Pain Rinvigoring";
	protected AMoreDamageReceivedMoreLifeRegen abilityDamageToLifeRegen;

	public NecklaceOfPainRinvigoring(GModalityRPG gmrpg) {
		super(gmrpg, EquipmentTypesTRAn.Necklace, NAME, //
				new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 35),
						new AttributeModification(AttributesTRAn.LifeRegen, 1), });
	}

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
		AbilitiesProvider ap;
		ap = ((GameObjectsProvidersHolderRPG) providersHolder).getAbilitiesProvider();
		this.abilityDamageToLifeRegen = (AMoreDamageReceivedMoreLifeRegen) ap.getAbilityByName(gm,
				AMoreDamageReceivedMoreLifeRegen.NAME);
		this.abilityDamageToLifeRegen.setOwner(this.getOwner());
		super.addAbility(this.abilityDamageToLifeRegen);
	}
}