package games.generic.controlModel.misc.uidp;

import java.util.Map;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.loaders.LoaderUniqueIDProvidersState.UIDPLoadable;
import games.generic.controlModel.loaders.LoaderUniqueIDProvidersState.UIDPState;
import tools.Comparators;
import tools.UniqueIDProvider;
import tools.UniqueIDProvider.BaseUniqueIDProvider;

public class UIDPCollector {

	/**
	 * Defines a listener of a {@link UniqueIDProvider} that listens when the
	 * provided is loaded.
	 *
	 * May behave like a "setter"
	 *
	 * @author ottin
	 *
	 */
	public static interface UIDProviderLoadedListener extends Consumer<UniqueIDProvider> {
		@Override
		public default void accept(UniqueIDProvider t) { this.notifyLoadedProvidedForClass(t); }

		public void notifyLoadedProvidedForClass(UniqueIDProvider loadedProvider);
	}

	protected static final Map<String, UIDProviderLoadedListener> provider_initializers_after_loading = MapTreeAVL
			.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.STRING_COMPARATOR);
	protected static final Map<String, UniqueIDProvider> providers = MapTreeAVL
			.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);

	//

	public static <E> void registerForProvider(Class<E> clazz, UIDProviderLoadedListener loadListener) {
		registerForProvider(UIDPState.CLASS_TO_NAME.apply(clazz), loadListener);
	}

	public static <E> void registerForProvider(String clazzName, UIDProviderLoadedListener loadListener) {
		synchronized (provider_initializers_after_loading) {
			provider_initializers_after_loading.put(clazzName, loadListener);
		}
	}

	public static <E> UniqueIDProvider getProvider(Class<E> clazz) {
		return getProvider(UIDPState.CLASS_TO_NAME.apply(clazz));
	}

	public static <E> UniqueIDProvider getProvider(String clazzName) {
		UniqueIDProvider uidp;
		synchronized (providers) {
			uidp = providers.get(clazzName);
			if (uidp == null) { providers.put(clazzName, uidp = new BaseUniqueIDProvider()); }
		}
		return uidp;
	}

	public static <E> void loadProvider(Class<E> clazz, UIDPState state) {
		loadProvider(UIDPState.CLASS_TO_NAME.apply(clazz), state);
	}

	public static <E> void loadProvider(String clazzName, UIDPState state) {
		UIDPLoadable uidp;
		synchronized (providers) {
			UIDProviderLoadedListener loadListener;
			uidp = (UIDPLoadable) providers.get(clazzName);
			if (uidp == null) { providers.put(clazzName, uidp = new UIDPLoadable()); }
			uidp.loadState(state);
			loadListener = provider_initializers_after_loading.get(clazzName);
			if (loadListener != null) { loadListener.accept(uidp); }
		}
	}

	public static <E> UIDPState getProviderState(Class<E> clazz) {
		UIDPLoadable uidp;
		UIDPState state;
		state = null;
		synchronized (providers) {
			uidp = (UIDPLoadable) providers.get(UIDPState.CLASS_TO_NAME.apply(clazz));
			if (uidp != null) { state = uidp.getState(clazz); }
		}
		return state;
	}

	//

	//
}