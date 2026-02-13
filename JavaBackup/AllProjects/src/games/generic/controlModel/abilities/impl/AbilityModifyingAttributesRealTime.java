package games.generic.controlModel.abilities.impl;

import java.util.Arrays;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityTimedGeneric;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.objects.creature.CreatureSimple;
import tools.ObjectWithID;

public abstract class AbilityModifyingAttributesRealTime extends AbilityBaseWithCustomName
		implements AbilityTimedGeneric {
	private static final long serialVersionUID = 56132035015L;
	public static final int MILLISEC_ATTRIBUTE_UPDATE = 500;

	public AbilityModifyingAttributesRealTime(GModality gameModality, String name,
			AttributeIdentifier[] attributesModified) {
		this(gameModality, name, AttributeModification.newEmptyArray(attributesModified));
	}

	public AbilityModifyingAttributesRealTime(GModality gameModality, String name,
			AttributeModification[] attributesModifications) {
		super(name);
		this.gameModality = gameModality;
		this.attributesToModify = attributesModifications;
	}

	protected long accumulatedTimeElapsedForUpdating;
	protected final GModality gameModality;
	/** Attributes this ability modifies. */
	protected AttributeModification[] attributesToModify;

	public AttributeModification[] getAttributesToModify() { return attributesToModify; }

	@Override
	public long getAccumulatedTimeElapsed() { return accumulatedTimeElapsedForUpdating; }

	@Override
	public long getTimeThreshold() { return MILLISEC_ATTRIBUTE_UPDATE; }

	public void setAttributesToModify(AttributeIdentifier[] attributesModified) {
		if (attributesModified != null) {
			attributesToModify = AttributeModification.newEmptyArray(attributesModified);
		}
	}

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) {
		this.accumulatedTimeElapsedForUpdating = newAccumulated;
	}

	@Override
	public GModality getGameModality() { return null; }

	//

	@Override
	public void setGameModality(GModality gameModality) {}

	protected void applyAttributeModifications() {
		CreatureAttributes ca;
		ca = getOwnerAttributes();
		if (ca == null)
			return;
		for (AttributeModification am : this.attributesToModify) {
			ca.applyAttributeModifier(am);
		}
	}

	protected void removeAttributeModifications() {
		CreatureAttributes ca;
		ca = getOwnerAttributes();
		if (ca == null)
			return;
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
		}
	}

	protected void removeAndNullifyAttributeModifications() {
		CreatureAttributes ca;
		ca = getOwnerAttributes();
		if (ca == null)
			return;
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
			am.setValue(0);
		}
	}

	@Override
	public void onAddingToOwner(GModality gm) {
		AbilityTimedGeneric.super.onAddingToOwner(gm);
		applyAttributeModifications();
	}

	@Override
	public void performAbility(GModality modality, int targetLevel) {
		BaseCreatureRPG ah;
		CreatureAttributes ca;
		ObjectWithID o;
		o = this.getOwner();
		if (o == null)
			return;
		ah = (o instanceof BaseCreatureRPG) ? ((BaseCreatureRPG) o) : null; // ei.getCreatureWearingEquipments();
		if (ah == null)
			return;
		ca = ah.getAttributes();
		if (ca == null)
			return;
		this.updateAttributeModifications(modality, ah, ca, targetLevel);
	}

	protected void actionPreAttributeModificationUpdates() {}

	/**
	 * Update the values of all {@link AttributeModification} (returned by
	 * {@link #getAttributesToModify()}) applied to the {@link CreatureAttributes}
	 * of a {@link CreatureSimple}. The update is performed by the
	 * {@link #updateAttributeModifiersValues(GModality, CreatureSimple, CreatureAttributes)}
	 * method.<br>
	 * This method is called by the {@link #performAbility(GModality, int)}
	 * function.
	 * <p>
	 * Note: It's not advised to override this implementation .
	 */
	protected final void updateAttributeModifications(GModality gm, CreatureSimple ah, CreatureAttributes ca,
			int targetLevel) {
		this.actionPreAttributeModificationUpdates();
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
		}
		updateAttributeModifiersValues(gm, ah, ca, targetLevel);
		for (AttributeModification am : this.attributesToModify) {
			ca.applyAttributeModifier(am);
		}
	}

//

	/**
	 * Should alter the {@link AttributeModification}s returned by
	 * {@link #getAttributesToModify()}, updating the value. The alteration can be
	 * based on the last parameter, which is the {@code targetLevel}.
	 */
	public abstract void updateAttributeModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca,
			int targetLevel);

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [name=" + name + ", ID=" + ID +
//			+	"\n\t equipped to: " + (this.getEquipItem() == null ? "null" : this.getEquipItem().getName())
				",\n\t attributesToModify=" + Arrays.toString(attributesToModify) + "]";
	}
}