package tools;

/**
 * Interface aimed to generate new {@link Long} <i>ID</i>.<br>
 * It's especially useful for the interface {@link ObjectWithID} (in particular
 * {@link ObjectWithID#getID()}). generation of the ID for the
 */
public interface UniqueIDProvider {

	public static final UniqueIDProvider UDIP_GENERAL = newBasicIDProvider();

	public static UniqueIDProvider newBasicIDProvider() { return new BaseUniqueIDProvider(); }

	//

	/**
	 * Generates a new, unique ID. <br>
	 * Useful to provide (and assign) the value that would be returned by
	 * {@link ObjectWithID#getID()}.
	 */
	public Long getNewID();

//

	public static class BaseUniqueIDProvider implements UniqueIDProvider {
		protected long idProgressive = 0;

		@Override
		public Long getNewID() { return idProgressive++; }
	}
}