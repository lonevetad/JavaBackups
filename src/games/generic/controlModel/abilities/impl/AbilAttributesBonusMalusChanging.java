package games.generic.controlModel.abilities.impl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.AttributesHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.subimpl.TimedObjectPeriodic;
import tools.ObjectWithID;

/**
 * Provides some {@link CreatureAttributes} bonus and some malus. Which is bonus
 * and which is malus changes over time and lasts for
 * {@link #getTimeThreshold()} "time units".
 * <p>
 * This class implements {@link TimedObjectPeriodic}) and the
 * {@link #executeAction(GModality)} function calls the function
 * {@link #performAbility(GModality)}). The bonus/malus application is
 * implemented as follow:
 * <ol>
 * <li>{@link #removeAttributesBonusesMaluses(GModality, CreatureAttributes, int)}:
 * first of all, all bonuses/malus are removed</li>
 * <li>{@link #updateAttributesBonusesMaluses(GModality, CreatureAttributes, int)}:
 * then the bonuses and the malus are updated.</li>
 * <li>{@link #applyAttributesBonusesMaluses(GModality, CreatureAttributes, int)}:
 * in the end, the bonues/malus are applied.</li>
 * </ol>
 */
public abstract class AbilAttributesBonusMalusChanging extends AbilityBaseWithCustomName
		implements TimedObjectPeriodic {
	private static final long serialVersionUID = -87835423666663L;

	public AbilAttributesBonusMalusChanging(GModality gameModality, String name) { this(gameModality, name, 0); }

	public AbilAttributesBonusMalusChanging(GModality gameModality, String name, int level) {
		super(name, level);
		this.gameModality = gameModality;
	}

	protected long accumulatedTimeElapsed;
	protected final GModality gameModality;

	@Override
	public long getAccumulatedTimeElapsed() { return this.accumulatedTimeElapsed; }

	@Override
	public GModality getGameModality() { return gameModality; }

	//

	@Override
	public void setGameModality(GModality gameModality) {}

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) { this.accumulatedTimeElapsed = newAccumulated; }

	//

	@Override
	public void resetAbility() { setAccumulatedTimeElapsed(0); }

	@Override
	public void onAddingToOwner(GModality gm) { performAbility(gm); }

	@Override
	public void executeAction(GModality modality) { performAbility(modality); }

	@Override
	public void performAbility(GModality gm, int largetLevel) {
		ObjectWithID ow;
//		BaseCreatureRPG ah;
		CreatureAttributes ca;
		ow = this.owner;
		if (ow == null || (!(ow instanceof AttributesHolder)))
			return;
		ca = ((AttributesHolder) ow).getAttributes();
		if (ca == null)
			return;
		removeAttributesBonusesMaluses(gm, ca, largetLevel);
		updateAttributesBonusesMaluses(gm, ca, largetLevel);
		applyAttributesBonusesMaluses(gm, ca, largetLevel);
	}

	public abstract void removeAttributesBonusesMaluses(GModality gm, CreatureAttributes ca, int largetLevel);

	public abstract void updateAttributesBonusesMaluses(GModality gm, CreatureAttributes ca, int largetLevel);

	public abstract void applyAttributesBonusesMaluses(GModality gm, CreatureAttributes ca, int largetLevel);
}