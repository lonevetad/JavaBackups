package games.old; // generic.controlModel.inventoryAbil.abilitiesImpl;

import games.generic.controlModel.gObj.creature.BaseCreatureRPG;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityBaseImpl;
import games.generic.controlModel.misc.CreatureAttributes;
import tools.ObjectWithID;

@Deprecated
public abstract class EquipmentAbilityBaseImpl extends AbilityBaseImpl implements EquipItemAbility {
	private static final long serialVersionUID = 70154894962305478L;

	public EquipmentAbilityBaseImpl() { super(); }

	public EquipmentAbilityBaseImpl(String name) { super(name); }

	protected EquipmentItem equipItem;

	@Override
	public EquipmentItem getEquipItem() { return equipItem; }

	@Override
	public ObjectWithID getOwner() {
		return owner != null ? owner : this.getEquipItem().getCreatureWearingEquipments();
	}

	@Override
	public void setEquipItem(EquipmentItem equipmentItem) { this.equipItem = equipmentItem; }

	//

	public CreatureAttributes getAttributesWearer() {
		EquipmentItem ei;
		BaseCreatureRPG ah;
		ei = this.equipItem;
		if (ei == null)
			return null;
		ah = ei.getCreatureWearingEquipments();
		return (ah == null) ? null : ah.getAttributes();
	}
}