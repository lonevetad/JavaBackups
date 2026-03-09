package games.theRisingAngel.enums;

import games.generic.controlModel.damage.DamageTypeGeneric;

public enum DamageTypesTRAn implements DamageTypeGeneric {
	Physical, Magical;

	public static final DamageTypesTRAn[] VALUES;
	public static final IndexToObjectBackmapping INDEX_TO_DAMAGE_TRYPE_TRAn;
	public static final String NAME;
	static {
		VALUES = DamageTypesTRAn.values();
		INDEX_TO_DAMAGE_TRYPE_TRAn = (int i) -> DamageTypesTRAn.VALUES[i];
		NAME = DamageTypesTRAn.class.getName();
	}

	@Override
	public Long getID() {
		return (long) ordinal();
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() {
		return INDEX_TO_DAMAGE_TRYPE_TRAn;
	}
}