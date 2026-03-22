package games.generic.controlModel.loaders;

import java.io.File;

import games.generic.controlModel.GController;
import tools.LoggerMessages;

/**
 * Class that loads something, usually from file
 */
public abstract class LoaderGeneric {
//	implements ObjectWithID{
//	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @author ottin
	 *
	 */
	public static enum LoadStatusResult {
		Success, CriticalFail, MinorFail;

		public boolean isFailed() { return this.ordinal() != 0; }
	}

	public static final char sc = File.separatorChar;
	public static final String startPath = LoggerMessages.CONSOLE_BASE_PATH + sc + "resources" + sc;
//	public static UniqueIDProvider UIDP_LOADER = null;
//	public static final UIDProviderLoadedListener UIDP_LOAD_LISTENER_LoaderGeneric = uidp -> UIDP_LOADER = uidp;

	static {
//		UIDPCollector.registerForProvider(LoaderGeneric.class, );
		System.out.println("starting path:");
		System.out.println(startPath);
	}

	//

	public abstract LoadStatusResult loadInto(GController gc);

	// abstract
	public LoadStatusResult saveFrom(GController gc) {

		return LoadStatusResult.Success;
	}
}