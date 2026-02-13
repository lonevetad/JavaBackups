package tools;

import java.util.Comparator;

import games.generic.controlModel.ObjectNamed;

/**
 * Mark an object as identifiable through both its numerical identifier (i.e.
 * {@link #getID()}) and its textual identifier (i.e. {@link #getName()}).
 */
public interface ObjectNamedID extends ObjectWithID, ObjectNamed {
	public static final Comparator<ObjectNamedID> COMPARATOR_OBJECT_NAMED_ID = (o1, o2) -> {
		int res;
		if (o1 == o2) { return 0; }
		if (o1 == null) { return -1; }
		if (o2 == null) { return 1; }
		res = Comparators.STRING_COMPARATOR.compare(o1.getName(), o2.getName());
		if (res != 0) { return res; }
		return Comparators.LONG_COMPARATOR.compare(o1.getID(), o2.getID());
	};
}