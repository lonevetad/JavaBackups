package common.abstractCommon;

import java.util.LinkedList;
import java.util.Set;

/**
 * Class designed to load a set of a single tasks.<br>
 * Examples:
 * <ul>
 * <li>Images</li>
 * <li>Map</li>
 * <li>Saves</li>
 * <li>Sounds</li>
 * <li>Settings</li>
 * <li>Etc ...</li>
 * </ul>
 * <br>
 * All tasks are read in a row through the method {@link #loadAll()}.
 * <p>
 * Subclasses of this class may specify some different methods to load tasks,
 * like the ones shown in the example. TO make it more automatic, make thos
 * mathod having the same signature of the functional interface
 * {@link SingleTaskLoader} so the <i>method reference</i> can be used.
 * <p>
 * Example:
 *
 * <pre>
 * <code>
 * public class ResourcesLoader extends AllLoader{
 * 	public ResourcesLoader(){
 * 		super();
 * 		addTask(this::loadImages);
 * 		addTask(this::loadSounds);
 * 		addTask(this::loadMaps);
 * 	}
 * 	public void loadImages(){
 * 		// code omitted
 * 	}
 * 	public void loadSounds(){
 * 		// code omitted
 * 	}
 * 	public void loadMaps(){
 * 		// code omitted
 * 	}
 * }
 * </code>
 * </pre>
 */
public abstract class AllLoader {
	/**
	 * Represent a loader of a single one task, as exlained in the class
	 * {@link AllLoader} docs.
	 */
	public interface SingleTaskLoader {
		public void loadTask();
	}

	public AllLoader() {
		this(new LinkedList<>());
	}

	public AllLoader(java.util.List<SingleTaskLoader> tasks) {
		this.tasks = tasks;
	}

	java.util.List<SingleTaskLoader> tasks;

	//

	// TODO GETTER

	public java.util.List<SingleTaskLoader> getTasks() {
		return tasks;
	}

	//

	// TODO SETTER

	public AllLoader setTasks(java.util.List<SingleTaskLoader> tasks) {
		this.tasks = tasks;
		return this;
	}

	//

	// TODO OTHER

	/**
	 * Returns the list of all tasks to be loaded. Each of them is an instance of
	 * {@link SingleTaskLoader}.
	 */
	public abstract java.util.List<SingleTaskLoader> getAllTasks();

	/**
	 * Add the specified {@link SingleTaskLoader} to the list.<br>
	 * No repetitions are allowed, acting as a {@link Set}.
	 */
	public void addTask(SingleTaskLoader stl) {
		java.util.List<SingleTaskLoader> tasks;
		tasks = getAllTasks();
		if (stl == null || tasks == null || tasks.isEmpty())
			return;
		if (!tasks.contains(stl))
			tasks.add(stl);
	}

	/**
	 * Performs the loading of all loading tasks specified in
	 * {@link #getAllTasks()}.
	 *
	 * @return true if all tasks have been loaded without any axceptions
	 */
	public boolean loadAll() {
		java.util.List<SingleTaskLoader> tasks;
		tasks = getAllTasks();
		if (tasks == null || tasks.isEmpty())
			return false;
		for (SingleTaskLoader stl : tasks) {
			stl.loadTask();
		}
		return true;
	}
}