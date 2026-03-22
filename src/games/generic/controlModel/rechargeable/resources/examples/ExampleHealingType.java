package games.generic.controlModel.rechargeable.resources.examples;

import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.rechargeable.resources.RechargableResource;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.impl.RechargableResourceImpl;

/** Provides a set of examples of resources that could be healed. */
public enum ExampleHealingType implements RechargeableResourceType {
	Life, Mana, Shield, Stamina, Inspiration, Energy, Darkness, Fury;

	public static final ExampleHealingType[] VALUES;
	public static final IndexToObjectBackmapping BACKMAPPING;
	static {
		VALUES = ExampleHealingType.values();
		BACKMAPPING = (int i) -> VALUES[i];
	}

	@Override
	public String getName() { return name(); }

	@Override
	public Long getID() { return (long) this.ordinal(); }

	@Override
	public int getIndex() { return ordinal(); }

	@Override
	public RechargableResource newRechargableResource(ResourceRechargeableHolder holder) {
		return new RechargableResourceImpl(holder, this);
	}

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() { return BACKMAPPING; }

	@Override
	public boolean setID(Long newID) { return false; }
}