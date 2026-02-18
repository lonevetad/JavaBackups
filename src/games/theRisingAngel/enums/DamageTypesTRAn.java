package games.theRisingAngel.enums;

import games.generic.controlModel.damage.DamageTypeGeneric;

public enum DamageTypesTRAn implements DamageTypeGeneric {
	Physical, Magical;

	@Override
	public Long getID() { return (long) ordinal(); }

	@Override
	public String getName() { return name(); }
}