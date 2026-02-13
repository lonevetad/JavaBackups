package tools.impl;

import java.util.Comparator;

import tools.Comparators;
import tools.ObjWithIDGeneric;

/** Not deprecated but should not be used. */
public class ObjectIDInteger implements ObjWithIDGeneric<Integer> {
	private static final long serialVersionUID = -70321584892380L;
	private static int progressiveID = 0;
	public static final Comparator<ObjectIDInteger> COMPARATOR_OWID = (o1, o2) -> {
		if (o1 == o2)
			return 0;
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		return Comparators.INTEGER_COMPARATOR.compare(o1.ID, o2.ID);
	};

	public static int getProgressiveID() { return progressiveID; }

	//

	//

	public ObjectIDInteger() {
		super();
		this.ID = ++progressiveID;
	}

	protected Integer ID;

	@Override
	public Integer getID() { return ID; }

	@Override
	public boolean setID(Integer newID) {
		if (this.ID != null || newID == null) { return false; }
		this.ID = newID;
		return true;
	}
}