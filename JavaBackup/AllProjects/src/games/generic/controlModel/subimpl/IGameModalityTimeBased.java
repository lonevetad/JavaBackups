package games.generic.controlModel.subimpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.GThread;
import games.generic.controlModel.objects.TimedObject;

/**
 * Mark a {@link GModality} as a "time-based" game, maybe "real-time" based
 * game.<br>
 * For this purpose, implements the method {@link GModality#doOnEachCycle(int)}
 * so that it invokes this interface's method {@link #progressElapsedTime(int)}.
 */
public interface IGameModalityTimeBased {

	/**
	 * Override designed.
	 * <p>
	 * The game is "time based" so tons of objects (the majority or totality,
	 * probably) are implementing {@link TimedObject} (see that interface's
	 * documentation for more informations about what does this means) and this
	 * method is designed to "progress / evolve" those objects' status depending on
	 * elapsed time.
	 * <p>
	 * Method that should be invoked by {@link GModality#doOnEachCycle(int)}.
	 * <p>
	 * The parameter is just a "time unit", that could be "milliseconds" or "a
	 * single turn".
	 */
	public void progressElapsedTime(int timeUnit);

	/**
	 * Return an instance of {@link GModelTimeBased}, so that this modality is
	 * forced to define it (as this interface's name suggests).
	 */
	public GModelTimeBased getModelTimeBased();

	public void addGameThread(GThread t);

	public void removeGameThread(GThread t);

	/**
	 * See {@link GameOptionsRPG#getTimeSubunitsEachUnit()} and
	 * {@link TimedObject#getTimeSubunitsEachUnit()}.
	 *
	 * @return
	 */
	public int getTimeSubunitsEachUnit();
}