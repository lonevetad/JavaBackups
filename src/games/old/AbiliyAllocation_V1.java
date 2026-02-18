package games.old;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import tools.ObjectWithID;

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
public interface AbiliyAllocation_V1 extends AbilityGeneric {

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

	@Override
	public default void performAbility(GModality gm) {
		this.getAllocatedAbility().performAbility(gm, getAllocatedLevel());
	}

	//

	// DELEGATION

	//

	@Override
	public default void performAbility(GModality gm, int targetLevel) {
		this.getAllocatedAbility().performAbility(gm, targetLevel);
	}

	@Override
	public default boolean canBePerformed(GModality gm) { return this.getAllocatedAbility().canBePerformed(gm); }

	@Override
	public default String getName() { return this.getAllocatedAbility().getName(); }

	@Override
	public default void toString(StringBuilder sb, int tabLevel) { this.getAllocatedAbility().toString(sb, tabLevel); }

	@Override
	public default void toString(StringBuilder sb) { this.getAllocatedAbility().toString(sb); }

	@Override
	public default void addTab(StringBuilder sb) { this.getAllocatedAbility().addTab(sb); }

	@Override
	public default void addTab(StringBuilder sb, int tabLevel) { this.getAllocatedAbility().addTab(sb, tabLevel); }

	@Override
	public default void addTab(StringBuilder sb, int tabLevel, boolean newLineNeeded) {
		this.getAllocatedAbility().addTab(sb, tabLevel, newLineNeeded);
	}

	@Override
	public default void addMeToGame(GModality gm) { this.getAllocatedAbility().addMeToGame(gm); }

	@Override
	public default Integer getID() { return this.getAllocatedAbility().getID(); }

	@Override
	public default ObjectWithID getOwner() { return this.getAllocatedAbility().getOwner(); }

	@Override
	public default void removeMeToGame(GModality gm) { this.getAllocatedAbility().removeMeToGame(gm); }

	@Override
	public default void setOwner(ObjectWithID owner) { this.getAllocatedAbility().setOwner(owner); }

	@Override
	public default void onRemovingFromOwner(GModality gm) { this.getAllocatedAbility().onRemovingFromOwner(gm); }

	@Override
	public default void resetAbility() { this.getAllocatedAbility().resetAbility(); }

	@Override
	public default void resetStuffs() { this.getAllocatedAbility().resetStuffs(); }

	@Override
	public default int getRarityIndex() { return this.getAllocatedAbility().getRarityIndex(); }

	@Override
	public default RarityHolder setRarityIndex(int rarityIndex) {
		return this.getAllocatedAbility().setRarityIndex(rarityIndex);
	}

	@Override
	public default void onAddedToGame(GModality gm) { this.getAllocatedAbility().onAddedToGame(gm); }

	@Override
	public default void onAddingToOwner(GModality gm) { this.getAllocatedAbility().onAddingToOwner(gm); }

	@Override
	public default void onRemovedFromGame(GModality gm) { this.getAllocatedAbility().onRemovedFromGame(gm); }

	@Override
	public default CreatureAttributes getOwnerAttributes() {
		return this.getAllocatedAbility().getOwnerAttributes();
	}

	@Override
	public default void onRemoving(GModality gm) { this.getAllocatedAbility().onRemoving(gm); }
}