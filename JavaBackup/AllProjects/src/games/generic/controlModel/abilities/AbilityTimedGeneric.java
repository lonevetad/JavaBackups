package games.generic.controlModel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.subimpl.TimedObjectPeriodic;

/**
 * Ability that is automatically activated as the time goes by.
 */
public interface AbilityTimedGeneric extends AbilityGeneric, TimedObjectPeriodic {

	@Override
	public default void executeAction(GModality gmodality) { performAbility(gmodality); }

	@Override
	public default void resetAbility() { setAccumulatedTimeElapsed(0); }
}