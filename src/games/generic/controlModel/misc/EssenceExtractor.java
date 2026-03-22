package games.generic.controlModel.misc;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.attributes.MaxUpgradesPerCategory;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EssenceStorage;
import games.generic.controlModel.items.FactoryEquipUpgrade;
import games.generic.controlModel.items.IEquipmentUpgrade;

/**
 * NPC-like that manipulates {@link EquipmentItem} to extract (for example,
 * {@link #extractEssenceByName(EssenceStorage, EquipmentItem, String)}) some
 * {@link IEquipmentUpgrade} into a {@link EssenceStorage}.
 */
public abstract class EssenceExtractor {

	public static enum EssenceApplianceStatus {
		Success, ErrorNullParameter, Error404EssenceNotFound, ErrorYetPresent, ErrorVialEmpty,
		ErrorMaxCategoryCapacityReached
	}

	public EssenceExtractor() {
	}

	//

	/**
	 * Get the name of the {@link GameObjectsProvider} associated with
	 * {@link IEquipmentUpgrade} (it will probably be a {@link FactoryEquipUpgrade}.
	 */
	public abstract String getEquipmentUpgradeObjProviderName(GModality gm);

	/**
	 * Get the name of the {@link GameObjectsProvider} associated with
	 * {@link AbilityGeneric}.
	 */
	public abstract String getAbilityObjProviderName(GModality gm);

	public boolean storeEssence(EssenceStorage vial, IEquipmentUpgrade upgrade) {
		if (vial == null || upgrade == null || (!vial.isEmpty())) {
			return false;
		}
		vial.storeEssence(upgrade);
		return true;
	}

	public boolean storeEssence(EssenceStorage vial, AbilityGeneric ability) {
		if (vial == null || ability == null || (!vial.isEmpty())) {
			return false;
		}
		vial.storeEssence(ability);
		return true;
	}

	/**
	 * Remove the essence from a given {@link EquipmentItem} and store it into a
	 * {@link EssenceStorage}. No costs are applied: it depends on the given
	 * {@link GModality} implementing this feature.
	 */
	public boolean extractEssenceByName(EssenceStorage vial, EquipmentItem equipment, String nameEssence) {
		Map<String, IEquipmentUpgrade> equips;
		Map<String, AbilityGeneric> abil;
		IEquipmentUpgrade upgrade;
		AbilityGeneric ability;
		if (vial == null || equipment == null || vial.isEmpty()) {
			return false;
		}
		abil = equipment.getAbilities();
		equips = equipment.getUpgradesMap();
		upgrade = equips.get(nameEssence);
		if (upgrade != null) {
			storeEssence(vial, upgrade);
			equips.remove(nameEssence);
		} else {
			ability = abil.get(nameEssence);
			if (ability != null) {
				storeEssence(vial, ability);
				abil.remove(nameEssence);
			}
		}
		return true;
	}

	/**
	 * If possible, apply the given essence, taken from the given essence, to the
	 * equipment, returning {@link EssenceApplianceStatus#Success}. In case of
	 * error, another {@link EssenceApplianceStatus}'s value is returned.
	 * 
	 * TODO : 22/03/2026 : check for {@link MaxUpgradesPerCategory} inside the
	 * equipment to limit the appliance (ErrorMaxCategoryCapacityReached)
	 */
	@SuppressWarnings("unchecked")
	public EssenceApplianceStatus applyEssence(GModality gm, EssenceStorage vial, EquipmentItem equipment) {
		Map<String, IEquipmentUpgrade> equips;
		Map<String, AbilityGeneric> abil;
		String essenceName;
		FactoryObjGModalityBased<IEquipmentUpgrade> factoryEquipUpgrade;
		FactoryObjGModalityBased<AbilityGeneric> factoryAbility;
		if (gm == null || vial == null || equipment == null) {
			return EssenceApplianceStatus.ErrorNullParameter;
		}
		if (vial.isEmpty()) {
			return EssenceApplianceStatus.ErrorVialEmpty;
		}
		essenceName = vial.getEssenceName();
		abil = equipment.getAbilities();
		equips = equipment.getUpgradesMap();
		factoryEquipUpgrade = (FactoryObjGModalityBased<IEquipmentUpgrade>) gm.getGameObjectsProvider()
				.getProvider(getEquipmentUpgradeObjProviderName(gm)).getObjByName(essenceName);
		factoryAbility = (FactoryObjGModalityBased<AbilityGeneric>) gm.getGameObjectsProvider()
				.getProvider(getAbilityObjProviderName(gm)).getObjByName(essenceName);
		if (factoryEquipUpgrade != null) {
			if (equips.containsKey(essenceName)) {
				return EssenceApplianceStatus.ErrorYetPresent;
			}
			equipment.addUpgrade(factoryEquipUpgrade.newInstance(gm));
			vial.removeEssence();
		} else if (factoryAbility != null) {
			if (abil.containsKey(essenceName)) {
				return EssenceApplianceStatus.ErrorYetPresent;
			}
			equipment.addAbility(factoryAbility.newInstance(gm));
			vial.removeEssence();
		} else {
			return EssenceApplianceStatus.Error404EssenceNotFound;
		}
		return EssenceApplianceStatus.Success;
	}
}