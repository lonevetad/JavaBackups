package tools.impl;

import tools.ObjectWithID;

public class OWIDLongImpl implements ObjectWithID {

	private static final long serialVersionUID = -6546852002000L;

	public OWIDLongImpl() { this.ID = null; }

	protected Long ID = null;

	@Override
	public final Long getID() { return this.ID; }

	@Override
	public boolean setID(Long newID) {
		if (this.ID != null || newID == null) { return false; }
		this.ID = newID;
		return true;
	}
}