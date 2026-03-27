package games.generic.controlModel.loaders;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GController;
import games.generic.controlModel.GMap;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.misc.uidp.UIDPCollector;
import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import games.generic.controlModel.misc.uidp.UIDPState;
import games.generic.controlModel.objects.OrbitingInteractiveObject;
import games.generic.view.dataProviders.DrawableObjProviderEventsObserver;
import tools.Comparators;
import tools.ObjectWithID;
import tools.UniqueIDProvider;
import tools.UniqueIDProvider.BaseUniqueIDProvider;
import tools.log.LoggerMessages;

/**
 * Class that loads every {@link UIDPState} in order to load the state of the
 * {@link LoaderGeneric} that can be loaded (using the
 * {@link UIDProviderLoadedListener} list defined in
 * {@link LoaderUniqueIDProvidersState#enrichAllKnownUIDPLoadedListenerList(List)}.
 * <br>
 * This class may simple initialize new ones (NOT recommended if any
 * {@link ObjectWithID} is saved and restored from somewhere, like a file, a
 * database, etc, because its ID may be generated again)
 * <p>
 * TODO : all usage/reference of
 * {@link UIDPCollector#registerForProvider(Class, UIDProviderLoadedListener)}
 * should be refactored from a call in the static initalizer of some classes
 * (like {@link AbilityGeneric}, {@link GEvent}, {@link GMap}, etc) to the
 * definition of a static but non-final {@link UniqueIDProvider}, initialized to
 * null, and a {@link UIDProviderLoadedListener} static final instance
 *
 * <p>
 * NOTE: this class is a {@link LoaderGeneric} but it HAS to be invoked as the
 * FIRST loader EVER
 *
 * @author ottin
 *
 */
public abstract class LoaderUniqueIDProvidersState extends LoaderGeneric {

	//

	public LoaderUniqueIDProvidersState() {
//		this.ID = UIDP_LOADER.getNewID();
		this.classesUsingUIDPLoadableBackmap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.STRING_COMPARATOR);
		this.classesUsingUIDPLoadable = Collections.unmodifiableSet(this.classesUsingUIDPLoadableBackmap.entrySet());
	}

	protected final MapTreeAVL<String, UIDProviderLoadedListener> classesUsingUIDPLoadableBackmap;
	protected final Set<Map.Entry<String, UIDProviderLoadedListener>> classesUsingUIDPLoadable;

	/**
	 * Returns a set of ALL classes (their name, to be precise, depending on
	 * {@link UIDPCollector#CLASS_TO_NAME}) that requires to use a
	 * {@link UniqueIDProvider} for them (and their subclasses) AND the
	 * {@link UniqueIDProvider} state (a {@link UIDPState} has to be saved and
	 * restored between game runs.
	 *
	 * It can also be used by the {@link ModGa}
	 *
	 * @return
	 */
	public Set<Map.Entry<String, UIDProviderLoadedListener>> getClassesUsingUIDPLoadable() {
		return classesUsingUIDPLoadable;
	}

	//

	/**
	 * Read all {@link UIDPState} stored somewhere
	 *
	 * @return
	 */
	protected abstract Iterable<UIDPState> readSavedUIDPStates();

	/**
	 * NOTE FOR OVERRIDING:
	 *
	 * <pre>
	<code>
	protected void enrichAllKnownUIDPLoadedListenerList(
	Map&#60;Class&#60;?&#62;, UIDProviderLoadedListener&#62; list) {
	
		// MANDATORY
		super.enrichAllKnownUIDPLoadedListenerList(list);
	
		// ADD HERE THE NEW ONES , like :
		list.put(MyFancyFooClass.class, MyFancyFooClass.UIDP_LOADED_LISTENER_MFFC));
		list.put(MyFancyBarInterface.class, MyFancyBarInterface.UIDP_LOADED_LISTENER_MFBI));
	}
	</code>
	
	where
	
	<code>
	public class MyFancyFooClass {
		private static UniqueIDProvider UIDP_MFFC = null;
	
		public static final UIDProviderLoadedListener UIDP_LOADED_LISTENER_MFFC =
			uidp -> { if(uidp != null) { UIDP_MFFC = uidp; } };
	
		// since the provider is not final, it's not advised to make it public:
		// this function makes it accessible without exposing the non-final pointer
		public static UniqueIDProvider getUniqueIDProvider_MyFancyFooClass(){
			return UIDP_MFFC;
		}
	
		// other stuffs
	}
	</code>
	
	and
	
	<code>
	public interface MyFancyBarInterface {
		// suggested implementation: using UIDPLoadableFromCollector
	
		public static final UniqueIDProvider UIDP_MFBI = new UIDPLoadableFromCollector&#60;&#62;(MyFancyBarInterface.class);
	
		&#64;SuppressWarnings("unchecked")
		public static final UIDProviderLoadedListener UIDP_LOADED_LISTENER_MFBI = ((UIDPLoadableFromCollector&#60;MyFancyBarInterface&#62;) UIDP_MyFancyBarInterface)
			.getUidpLoaderListener();
	
		public static UniqueIDProvider getUniqueIDProvider_MyFancyBarInterface(){
			return UIDP_MFBI;
		}
	
		// other stuffs
	}
	</code>
	 * </pre>
	 * <p>
	 * Add new {@link UIDProviderLoadedListener} taken somewhere, probably as a
	 * static field of some class
	 *
	 * @param list the list to be enriched using {@link List#add(Object)}.
	 */
	public void enrichAllKnownUIDPLoadedListenerList(Map<Class<?>, UIDProviderLoadedListener> list) {
		//
		list.put(AbilityGeneric.class, AbilityGeneric.UIDP_LOADED_LISTENER_ABILITY);
		list.put(GEvent.class, GEvent.UIDP_LOADED_LISTENER_EVENT);
		list.put(GMap.class, GMap.UIDP_LOADED_LISTENER_GMAP);
		list.put(InventoryItem.class, InventoryItem.UIDP_LOADED_LISTENER_INVENTORY);
		// special cases / subclasses
		list.put(OrbitingInteractiveObject.class,
				OrbitingInteractiveObject.UIDP_LOADED_LISTENER_ORBITING_INTERACTIVE_OBJECT);
		// GUI
		list.put(DrawableObjProviderEventsObserver.class, DrawableObjProviderEventsObserver.UIDP_LOADED_LISTENER_DOPEO);

	}

	/***
	 * NOTE FOR OVERRIDING: <code>
	protected List<UIDProviderLoadedListener> getAllKnownUIDPLoadedListener() {
		List<UIDProviderLoadedListener> list;
		list = super.getAllKnownUIDPLoadedListener();
		// ADD HERE THE NEW ONES
		return list;
	}
	 *
	 *
	 * </code>
	 *
	 * @return
	 */
	protected final Map<Class<?>, UIDProviderLoadedListener> getAllKnownUIDPLoadedListener() {
		Map<Class<?>, UIDProviderLoadedListener> list;
		list = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.CLASS_COMPARATOR);
		this.enrichAllKnownUIDPLoadedListenerList(list);
		return list;
	}

	//

	public <E> void registerClassAsUsingUIDP(Class<E> clazz, UIDProviderLoadedListener loadListener) {
		this.classesUsingUIDPLoadableBackmap.put(UIDPState.CLASS_TO_NAME.apply(clazz), loadListener);
	}

	@Override
	public LoadStatusResult loadInto(GController gc) {
		Map<Class<?>, UIDProviderLoadedListener> knownUIDPLoadedListenerMap = this.getAllKnownUIDPLoadedListener();
		final Map<String, Class<?>> classByNameNotLoaded = new HashMap<>();
		knownUIDPLoadedListenerMap.forEach((clazz, u) -> {
			this.registerClassAsUsingUIDP(clazz, u);
			classByNameNotLoaded.put(UIDPState.CLASS_TO_NAME.apply(clazz), clazz);
		});
		// check what is saved and create default ones for the absent ones
		LoggerMessages log = gc.getLogger();
		final Iterable<UIDPState> iterUIDPState = this.readSavedUIDPStates();
		final Consumer<UIDPState> stateFullLoader = (state) -> {
			// first of all, load the provider into the collector
			log.logAndPrint("Loading UIDPState of '" + state.getClassIdentifier() + "' with state: " + state.getState()
					+ " ...");
			UIDPCollector.loadProvider(state.getClassIdentifier(), state);
			log.logAndPrint("loaded!\n");
			// then, notify that the provider has been loaded
			this.classesUsingUIDPLoadableBackmap //
					.get(state.getClassIdentifier()) //
					.accept( // (note: retrieve back the provider stored in the collector)
							UIDPCollector.getProvider(state.getClassIdentifier()) //
			);
			log.logAndPrint("UIDPState of '" + state.getClassIdentifier() + "' notified.\n");
		};
		if (iterUIDPState != null) {
			try {
				for (UIDPState state : iterUIDPState) {
					try {
						stateFullLoader.accept(state);
						classByNameNotLoaded.remove(state.getClassIdentifier()); // mark it as loaded by removing it
					} catch (Exception e2) {
						gc.getLogger().logException(e2);
					}
				}
			} catch (Exception e) {
				log.logAndPrint("\n");
				gc.getLogger().logException(e);
				return LoadStatusResult.MinorFail;
			}
		}

		log.logAndPrint("Loading " + classByNameNotLoaded.size() + " default UIDP\n");
		classByNameNotLoaded.forEach((className, clazz) -> {
			log.logAndPrint("Loading default UIDP of class: " + className + "\n");
			UIDPState state = UIDPState.newEmpty(className);
			stateFullLoader.accept(state);
		});

		return LoadStatusResult.Success;
	}

	//
	//

	public static class UIDPLoadable extends BaseUniqueIDProvider {

		public UIDPState getState() {
			return this.getState(getClass());
		}

		public void loadState(UIDPState state) {
			this.idProgressive = state.getState();
		}

		public UIDPState getState(Class<?> clazz) {
			return new UIDPState(clazz, idProgressive);
		}

	}
}