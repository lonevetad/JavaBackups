package games.theRisingAngel.loaders.factories;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.misc.FactoryObjPrototypedGModalityBased;
import games.generic.controlModel.providers.AbilitiesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentTypesTRAn;

public class FactoryEquip implements FactoryObjPrototypedGModalityBased<EquipmentItem> {
	public final FactoryItems fi;
	// public EquipmentTypesTRAn type;
	public List<AbilityData> abilities = null;
	protected AttributeModification[] attrMods = null;
	/*
	*/

	public FactoryEquip() {
		super();
		this.fi = new FactoryItems();
	}

	@Override
	public EquipmentItem getPrototype() {
		return (EquipmentItem) fi.getPrototype();
	}

	@Override
	public void setPrototype(EquipmentItem prototype) {
		this.fi.setPrototype(prototype);
	}

	@Override
	public EquipmentItem newInstance(GModality gm) {
		EquipmentItem ei;
//	ei = new EquipmentItemImpl((GModalityRPG) gm, type, name);
		if (this.attrMods == null) {
			this.attrMods = this.getPrototype().getBaseAttributesModifiers()
					.toArray(new AttributeModification[this.getPrototype().getBaseAttributesModifiers().size()]);
		}
		try {
			EquipmentTypesTRAn type = this.getType();
			ei = type.getFactory().newEquipItem( //
					(GModalityRPG) gm, //
					type, //
					getFactoryItem().getName(), //
					attrMods //
			);
		} catch (Exception e) {
			e.printStackTrace();
			gm.getLogger().logAndPrint("ERROR on equipment with name: " + this.fi.getName() + "\n");
			throw e;
		}
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

	public FactoryItems getFactoryItem() {
		return fi;
	}

	public int getRarity() {
		return fi.getRarity();
	}

	public String getName() {
		return fi.getName();
	}

	public String getDescription() {
		return fi.getDescription();
	}

	public Dimension getDimensionInInventory() {
		return fi.getDimensionInInventory();
	}

	public int[] getPrice() {
		return fi.getPrice();
	}

	public InventoryItemFactory getInventoryItemFactory() {
		return fi.getInventoryItemFactory();
	}

	public EquipmentTypesTRAn getType() {
		return (EquipmentTypesTRAn) this.getPrototype().getEquipmentType();
	}

	public List<AbilityData> getAbilities() {
		return abilities;
	}

	public AttributeModification[] getAttrMods() {
		return this.attrMods;
	}

	@Override
	public String toString() {
		return "FactoryEquip [\n name=" + getName() + ", type=" + this.getType() + //
				",\n description: " + getDescription() + ",\n rarity=" + getRarity() + ", sell price: "
				+ Arrays.toString(getPrice())//
				+ ",\n dimensions in inventory: " + getFactoryItem().getDimensionInInventory() + ",\n abilities=\n\t"
				+ (abilities == null ? "null" : Arrays.toString(abilities.toArray()))//
				+ ",\n attrMods=\n\t" + Arrays.toString(attrMods) + "]";
	}

	//

	public static class AbilityData {
		public int level;
		public String name;

		@Override
		public String toString() {
			return "AbilityData [level=" + level + ", name=" + name + "]";
		}
	}
}