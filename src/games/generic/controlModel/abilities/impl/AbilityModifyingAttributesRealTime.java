package games.generic.controlModel.abilities.impl;

import java.util.Arrays;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityTimedGeneric;
import games.generic.controlModel.attributes.AttributeIdentifier;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.objects.creature.CreatureSimple;
import tools.ObjectWithID;

public abstract class AbilityModifyingAttributesRealTime extends AbilityBaseWithCustomName
		implements AbilityTimedGeneric, HelperWithAttributeModifications {
	private static final long serialVersionUID = 56132035015L;
	public static final int MILLISEC_ATTRIBUTE_UPDATE = 500;

	public AbilityModifyingAttributesRealTime(GModality gameModality, String name) {
		super(name);
		this.gameModality = gameModality;
	}

	public AbilityModifyingAttributesRealTime(GModality gameModality, String name,
			AttributeIdentifier[] attributesModified) {
		this(gameModality, name);
		this.setAttributesToModify(attributesModified);
	}

	public AbilityModifyingAttributesRealTime(GModality gameModality, String name,
			AttributeModification[] attributesModifications) {
		this(gameModality, name);
		this.attributesToModify = attributesModifications;
	}

	protected long accumulatedTimeElapsedForUpdating;
	/** Attributes this ability modifies. */
	protected AttributeModification[] attributesToModify;

	public AttributeModification[] getAttributesToModify() {
		return attributesToModify;
	}

	@Override
	public long getAccumulatedTimeElapsed() {
		return accumulatedTimeElapsedForUpdating;
	}

	@Override
	public long getTimeThreshold() {
		return MILLISEC_ATTRIBUTE_UPDATE;
	}

	public void setAttributesToModify(AttributeIdentifier[] attributesModified) {
		if (attributesModified == null) {
			return;
		}
		int n;
		AttributeModification[] r;
		r = new AttributeModification[n = attributesModified.length];
		while (--n >= 0) {
			r[n] = newAttributeModification(attributesModified[n], 0);
		}
		this.setAttributesToModify(r);
	}

	@Override
	public void setAttributesToModify(AttributeModification[] attributesModified) {
		this.attributesToModify = attributesModified;
	}

	/*
	 * { attributesToModify =
	 * AttributeModification.newEmptyArray(attributesModified,
	 * this::newAttributeModification); } }/*
	 */

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) {
		this.accumulatedTimeElapsedForUpdating = newAccumulated;
	}

	//

	protected void applyAttributeModifications() {
		CreatureAttributes ca;
		ca = getOwnerAttributes();
		if (ca == null) {
			return;
		}
		for (AttributeModification am : this.attributesToModify) {
			ca.applyAttributeModifier(am);
		}
	}

	protected void removeAttributeModifications() {
		CreatureAttributes ca;
		ca = getOwnerAttributes();
		if (ca == null) {
			return;
		}
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
		}
	}

	protected void removeAndNullifyAttributeModifications() {
		CreatureAttributes ca;
		ca = getOwnerAttributes();
		if (ca == null) {
			return;
		}
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
		if (o == null) {
			return;
		}
		ah = (o instanceof BaseCreatureRPG) ? ((BaseCreatureRPG) o) : null; // ei.getCreatureWearingEquipments();
		if (ah == null) {
			return;
		}
		ca = ah.getAttributes();
		if (ca == null) {
			return;
		}
		this.updateAttributeModifications(modality, ah, ca, targetLevel);
	}

	protected void actionPreAttributeModificationUpdates() {
	}

	/**
	 * Update the values of all {@link AttributeModification} (returned by
	 * {@link #getAttributesToModify()}) applied to the {@link CreatureAttributes}
	 * of a {@link CreatureSimple}. The update is performed by the
	 * {@link #updateAttributesModifiersValues(GModality, CreatureSimple, CreatureAttributes)}
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
		updateAttributesModifiersValues(gm, ah, ca, targetLevel);
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
	public abstract void updateAttributesModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca,
			int targetLevel);

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [name=" + name + ", ID=" + ID +
		// + "\n\t equipped to: " + (this.getEquipItem() == null ? "null" :
		// this.getEquipItem().getName())
				",\n\t attributesToModify=" + Arrays.toString(attributesToModify) + "]";
	}
}