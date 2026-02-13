package games.theRisingAngel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.EssenceExtractor;
import games.generic.controlModel.providers.AbilitiesProvider;
import games.generic.controlModel.providers.EquipmentUpgradesProvider;

/**
 *
 * May apply some prices and discounts to bulk of extraction / appliance.
 */
public class EssenceExtractorTRAn extends EssenceExtractor {

	public EssenceExtractorTRAn() {}

	@Override
	public String getEquipmentUpgradeObjProviderName(GModality gm) { return EquipmentUpgradesProvider.NAME; }

	@Override
	public String getAbilityObjProviderName(GModality gm) { return AbilitiesProvider.NAME; }
}