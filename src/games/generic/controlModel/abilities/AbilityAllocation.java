package games.generic.controlModel.abilities;

import java.util.function.Function;

import games.generic.controlModel.GModality;

/**
 * An ability could be "equipped" to the owner by setting a specific level it
 * has to operate on (assuming the ability can vary its behavior or values
 * depending on the level). For instance, an "healing" ability could heal more
 * life the more its level grows. The ability cost may grow in the same way.
 * <p>
 * It delegates
 *
 * @author ottin
 */
public interface AbilityAllocation {
	public static final Function<AbilityAllocation, AbilityGeneric> ABILITY_ALLOCATED_EXTRACTOR = AbilityAllocation::getAllocatedAbility;

	/**
	 * Returns the ability this allocation is meant to.
	 *
	 * @return
	 */
	public AbilityGeneric getAllocatedAbility();

	public int getAllocatedLevel();

	public void setAllocatedAbility(AbilityGeneric ability);

	public void setAllocatedLevel(int level);

	public default void increaseLevel() { this.setAllocatedLevel(getAllocatedLevel() + 1); }

	public default void decreaseLevel() { this.setAllocatedLevel(getAllocatedLevel() - 1); }

	public default void activateAbility(GModality gameModality) {
		this.getAllocatedAbility().performAbility(gameModality, getAllocatedLevel());
	}

	public default void performAbility(GModality gm) {
		this.getAllocatedAbility().performAbility(gm, getAllocatedLevel());
	}

	//

	// DELEGATION

	//

	public default void toString(StringBuilder sb) { this.getAllocatedAbility().toString(sb); }
}