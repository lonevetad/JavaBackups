package games.generic.controlModel;

import java.util.Comparator;

import tools.Comparators;

public interface ObjectNamed {
	public static final Comparator<ObjectNamed> COMPARATOR_OBJECT_NAMED = (o1, o2) -> {
		if (o1 == o2) { return 0; }
		if (o1 == null) { return -1; }
		if (o2 == null) { return 1; }
		return Comparators.STRING_COMPARATOR.compare(o1.getName(), o2.getName());
	};

	/**
	 * The "name" of this instance, that is a String identifier that should be
	 * unique.
	 */
	public String getName();

}