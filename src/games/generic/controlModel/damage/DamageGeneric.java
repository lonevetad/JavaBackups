package games.generic.controlModel.damage;

import games.generic.controlModel.misc.AmountNamed;

/**
 * Describes a generic type of damage (physical, fire, poison, ranged, critical,
 * dark, holy, cold, lightning, etc...), making event deliberation general
 * (lighter in terms of memory but a bit heavier in terms of execution time).
 * Use {@link #getDamageType()} to distinguish the type.
 */
public class DamageGeneric extends AmountNamed {
	private static final long serialVersionUID = 894363018L;

	public DamageGeneric(int damageAmount, DamageTypeGeneric damageType) {
		super(damageType, damageAmount);
	}

	public int getDamageAmount() {
		return super.value; // super.getValue();
	}

	public DamageTypeGeneric getDamageType() {
		return (DamageTypeGeneric) super.type;// super.getType();
	}

	public void setDamageAmount(int damageAmount) {
//		super.setValue(damageAmount);
		super.value = damageAmount;
	}

	public void setDamageType(DamageTypeGeneric damageType) {
//		super.setType(damageType);
		super.type = damageType;
	}

	@Override
	public String toString() {
		return "DamageGeneric [damageAmount=" + getDamageAmount() + ", damageType=" + getDamageType() + "]";
	}
}