package games.theRisingAngel.loaders.factories;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.providers.AbilitiesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentTypesTRAn;

public class FactoryEquip implements FactoryObjGModalityBased<EquipmentItem> {
	public final FactoryItems fi;
	public EquipmentTypesTRAn type;
	public List<AbilityData> abilities = null;
	public AttributeModification[] attrMods = null;

	public FactoryEquip() {
		super();
		this.fi = new FactoryItems();
	}

	@Override
	public EquipmentItem newInstance(GModality gm) {
		EquipmentItem ei;
//	ei = new EquipmentItemImpl((GModalityRPG) gm, type, name);
		ei = type.factory.newEquipItem((GModalityRPG) gm, type, getFactoryItem().name, attrMods);
		setValuesInto(gm, ei);
		return ei;
	}

	protected void setValuesInto(GModality gm, EquipmentItem ei) {
//	EquipmentItem ei;
//	ei = (EquipmentItem) ii;
		AbilityGeneric abil;
		getFactoryItem().setValuesInto(gm, ei);
		if (abilities != null) {
			GameObjectsProvidersHolderRPG gophRpg;
			AbilitiesProvider ap;
			gophRpg = (GameObjectsProvidersHolderRPG) gm.getGameObjectsProvider();
			ap = gophRpg.getAbilitiesProvider();
			for (AbilityData ad : abilities) {
				abil = ap.getAbilityByName(gm, ad.name);
				abil.setLevel(ad.level);
				ei.addAbility(abil);
			}
		}
	}

	public FactoryItems getFactoryItem() { return fi; }

	public int getRarity() { return fi.getRarity(); }

	public String getName() { return fi.getName(); }

	public String getDescription() { return fi.getDescription(); }

	public Dimension getDimensionInInventory() { return fi.getDimensionInInventory(); }

	public int[] getPrice() { return fi.getPrice(); }

	public InventoryItemFactory getInventoryItemFactory() { return fi.getInventoryItemFactory(); }

	public EquipmentTypesTRAn getType() { return type; }

	public List<AbilityData> getAbilities() { return abilities; }

	public AttributeModification[] getAttrMods() { return attrMods; }

	@Override
	public String toString() {
		return "FactoryEquip [\n name=" + getName() + ", type=" + type + //
				",\n description: " + getDescription() + ",\n rarity=" + getRarity() + ", sell price: "
				+ Arrays.toString(getPrice())//
				+ ",\n dimensions in inventory: " + getFactoryItem().dimensionInInventory + ",\n abilities=\n\t"
				+ (abilities == null ? "null" : Arrays.toString(abilities.toArray()))//
				+ ",\n attrMods=\n\t" + Arrays.toString(attrMods) + "]";
	}

	//

	public static class AbilityData {
		public int level;
		public String name;

		@Override
		public String toString() { return "AbilityData [level=" + level + ", name=" + name + "]"; }
	}
}