package games.generic.controlModel.misc.uidp;

import java.util.Objects;

import games.generic.controlModel.loaders.LoaderUniqueIDProvidersState;
import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import tools.UniqueIDProvider;

/**
 * A way to define both a
 * <code>public static final {@link UniqueIDProvider}</code> and a
 * <code>public static final {@link UIDProviderLoadedListener}</code> for
 * interfaces (which can't define a non-final static field). <br>
 * The static {@link UniqueIDProvider} can then be used in each subclasses and
 * subinterfaces. With this class
 * <p>
 * For example, as shown in
 * {@link LoaderUniqueIDProvidersState#enrichAllKnownUIDPLoadedListenerList(java.util.List)}:
 *
 * <pre>
	<code>
	public interface MyFancyBarInterface {

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
 *
 * @author ottin
 *
 * @param <C>
 *
 */
public final class UIDPLoadableFacade<C> implements UniqueIDProvider {

	public UIDPLoadableFacade(Class<C> classBasedOn) {
		this.classBasedOn = Objects.requireNonNull(classBasedOn);
		this.delegate = null;
		this.uidpLoaderListener = uidp -> { if (uidp != null) { delegate = uidp; } };
	}

	protected final Class<C> classBasedOn;
	protected UniqueIDProvider delegate;
	protected final UIDProviderLoadedListener uidpLoaderListener;

	@Override
	public Long getNewID() { return this.delegate.getNewID(); }

	public UniqueIDProvider getDelegate() { return this.delegate; }

	public Class<C> getClassBasedOn() { return classBasedOn; }

	public UIDProviderLoadedListener getUidpLoaderListener() { return this.uidpLoaderListener; }

}