package games.generic.controlModel.abilities.impl;

import games.generic.controlModel.abilities.AbilityAllocation;
import games.generic.controlModel.abilities.AbilityGeneric;

/**
 * See {@link AbilityAllocation}.
 */
public class AbilityAllocationImpl implements AbilityAllocation {
	public AbilityAllocationImpl(AbilityGeneric ability) {
		super();
		this.ability = ability;
		this.level = 0;
	}

	protected int level;
	protected AbilityGeneric ability;

	@Override
	public AbilityGeneric getAllocatedAbility() { return this.ability; }

	@Override
	public int getAllocatedLevel() { return this.level; }

	@Override
	public void setAllocatedAbility(AbilityGeneric ability) { this.ability = ability; }

	@Override
	public void setAllocatedLevel(int level) {
		if (level < 0) { level = 0; }
		this.level = level;
	}
}