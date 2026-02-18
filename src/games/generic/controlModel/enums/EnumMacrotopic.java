package games.generic.controlModel.enums;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.subimpl.GObjectsHolderImpl;
import tools.Comparators;
import tools.ObjectNamedID;

/**
 * Cluster / collection of {@link GEnumeration} following the same "theme" (for
 * instance, all creatures in game, which may be divided depending on the in-map
 * regions, or all spells, divided into classes and shared among classes).
 *
 * @author ottin
 *
 */
public class EnumMacrotopic extends GObjectsHolderImpl<Long, GEnumeration> implements ObjectNamedID {
	private static final long serialVersionUID = 983456907823450L;
	protected final MapTreeAVL<String, GEnumeration> subclusterByName;

	public EnumMacrotopic() {
		super(Comparators.LONG_COMPARATOR, // ObjectWithID.KEY_EXTRACTOR
				GEnumeration::getID);
		subclusterByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
	}

	protected Long ID;
	protected String name;

	public MapTreeAVL<String, GEnumeration> getSubclusterByName() { return subclusterByName; }

	@Override
	public Long getID() { return ID; }

	/**
	 * Name of the "macrotopic / theme", like "creatures", "spells", "runes", etc.
	 * <p>
	 * {@inheritDoc}
	 *
	 */
	@Override
	public String getName() { return name; }

	/**
	 * See {@link #getName()}.
	 *
	 * @param name
	 */
	public void setName(String name) { this.name = name; }

	@Override
	public boolean setID(Long iD) {
		if (iD == null || iD == this.ID) { return false; }
		ID = iD;
		return true;
	}

}