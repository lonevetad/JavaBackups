package games.generic.controlModel.holders;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

import games.generic.controlModel.subimpl.GModelTimeBased;
import tools.ObjectWithID;

/**
 * Interface for classes that holds and manages in some way (at least in a
 * similar way to {@link Collection}) and provides them.<br>
 * Example of subclasses are:
 * <ul>
 * <li>{@link GModelTimeBased.TimedObjectHolder}</li>
 * <li></li>
 * <li></li>
 * </ul>
 */
public interface GObjectsHolder<T extends ObjectWithID> {

	/**
	 * Returns all {@link ObjectWithID} held by this specific holder.<br>
	 * BEWARE: In some implementations, other GObjectsHolder instances may held
	 * sub-GObjectsHolder to perform some fine-grained tasks, differentiations, etc
	 * or just hold other classes' "Models". In that case DO NOT use this method,
	 * but accessors like {@link #get(Integer)} or traversal-method like
	 * {@link #forEach(Consumer)} to assure to process the wanted object or all
	 * objects.
	 */
	public Set<T> getObjects();

	public int objectsHeldCount();

	public default int size() { return this.objectsHeldCount(); }

	/**
	 *
	 * @param o the object to being added
	 * @return wether the addition is successful (if the provided object is not
	 *         suited for this hodelr, then {@code false} has to be returned).
	 */
	public boolean add(T o);

	/***
	 *
	 * @param o
	 * @return whether the object has been removed or not.
	 */
	public boolean remove(T o);

	/** Similar to {@link Collection#clear()}. */
	public boolean removeAll();

	public boolean contains(T o);

	public T get(Long id);

	public void forEach(Consumer<T> action);
}