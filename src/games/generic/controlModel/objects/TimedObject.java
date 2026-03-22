package games.generic.controlModel.objects;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.subimpl.GameOptionsRPG;
import games.generic.controlModel.subimpl.IGameModalityTimeBased;

/**
 * Mark an object as having a "status" (or at least a non empty set of actions)
 * that depends on the concept of time or, in particular, <i>evolves</i> as the
 * time goes on.<br>
 * The "time unit" could be everything that's needed: seconds, milliseconds,
 * "turns" (i.e., a single unit amount of time), etc.<br>
 * It differs to the concept expressed in {@link #getTimeSubUnitsEachUnit()},
 * see it for further details.
 * <p>
 * Used by {@link GModality}es that are using some concept of "time" (real time
 * or turn-based) to describe objects performing actions over time.
 * <p>
 * This interface could have been a special case of {@link GEventObserverb}, but
 * it would have been heavily inefficient. It's a "hard-code / embedded"
 * solution for listening the time progression.
 */
public interface TimedObject extends GameObjectGeneric, GModalityHolder {

	/**
	 * Evolve the status of this object depending on the elapsed time denoted by the
	 * second parameter.<br>
	 * As yet said, the "time unit" could be everything that's needed: seconds,
	 * milliseconds, "turns" (i.e., a single unit amount of time), etc. The second
	 * parameter just express the quantity, not the "measurement unit".
	 * <p>
	 * Probably, this method's name is not the best clear and fitting one.
	 **/
	public void act(GModality modality, int timeUnits);

	/**
	 * As said in this class ({@link TimedObject}) and in this method
	 * {@link #act(GModality, int)}, the "time" unit could be anything. There still
	 * could be a "greater order unit":
	 * <ul>
	 * <li>In case of real-time games, the "sub-unit" is "millisecond" and the
	 * "unit" is "a whole second", so 1000 milliseconds.</li>
	 * <li>For turn based games, both the time unit and the "superscale unit" are
	 * jus <i>one single turn</i></li>
	 * </ul>
	 * <p>
	 * See also <i>{@link GameOptionsRPG#getSpaceSubunitsEachUnit()}
	 * milliseconds</i>.
	 */
	// {@link}
	public default int getTimeSubUnitsEachUnit() {
		return ((IGameModalityTimeBased) this.getGameModality()).getTimeSubunitsEachUnit();
	}
}