package games.generic.controlModel.enums;

import games.generic.controlModel.misc.IndexableObject;

/**
 * Marker-like interface to define instances of an enumeration ({@link Enum}).
 * Instances can than be loaded and allocated dinamically (it's preferred to be
 * performed just on load time).<br>
 * <p>
 * Instances should be grouped in {@link GEnumeration}
 *
 * @author ottin
 *
 */
public interface GEnumInstance extends IndexableObject {

	/**
	 * Set the ID of this enumeration instance. Should be invoked only by the
	 * {@link EnumsManager}.
	 *
	 * <p>
	 * Inherited documentation:
	 * <p>
	 * {@inheritDoc}
	 *
	 * @param ID
	 */
	@Override
	public boolean setID(Long ID);

	/**
	 * Get the {@link Enum}-like instance which this instance is belonging to.
	 *
	 * @return
	 */
	public GEnumeration getGroupBelonging();

	/**
	 * Implementation note: should be set once only.
	 *
	 * @param groupBelonging
	 */
	public void setGroupBelonging(GEnumeration groupBelonging);
}