package games.generic.controlModel.enums;

import games.generic.controlModel.subimpl.GObjectsHolderImpl;
import tools.Comparators;
import tools.ObjectNamedID;

/**
 * A set of {@link GEnumInstance}, all belonging to the same "theme" or
 * sub-group (for instance, some animals, belonging to different MAP regions).
 *
 * @author ottin
 *
 */
public class GEnumeration extends GObjectsHolderImpl<String, GEnumInstance> implements ObjectNamedID {

	private static final long serialVersionUID = 56484538723587L;

	public GEnumeration(String name) {
		super(Comparators.STRING_COMPARATOR, GEnumInstance::getName);
		this.name = name;
	}

	protected Long ID;
	protected String name;

	//

	@Override
	public Long getID() { return ID; }

	@Override
	public boolean setID(Long newID) {
		if (this.ID != null || newID == null) { return false; }
		this.ID = newID;
		return true;
	}

	@Override
	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	//

}