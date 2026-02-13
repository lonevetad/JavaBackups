package games.generic.controlModel.rechargeable.resources;

import java.util.Comparator;

import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.misc.IndexableObject;
import games.generic.controlModel.rechargeable.resources.examples.ExampleHealingType;
import tools.Comparators;
import tools.ObjectNamedID;

/**
 * Marks a resource as something that can be recharged in some way (like
 * {@link ExampleHealingType}), but it's not a {@link Currency}.<BR>
 * It also defines an upper bound and a lower bound of the value (assuming that
 * this resource is an integer).
 */
public interface RechargeableResourceType extends ObjectNamedID, IndexableObject {
	public static final Comparator<RechargeableResourceType> COMPARATOR_RECHARGEABLE_RESOURCE_TYPE = (ht1, ht2) -> {
		int c;
		if (ht1 == ht2)
			return 0;
		if (ht1 == null)
			return -1;
		if (ht2 == null)
			return 1;
		c = Comparators.LONG_COMPARATOR.compare(ht1.getID(), ht2.getID());
		return (c != 0) ? c : Comparators.STRING_COMPARATOR.compare(ht1.getName(), ht2.getName());
	};

	/**
	 * Returns the minimum value this resource can have.
	 */
	public default int getLowerBound() { return Integer.MIN_VALUE; }

	/**
	 * Returns the maximum value this resource can have.
	 */
	public default int getUpperBound() { return Integer.MAX_VALUE; }

	//

	public RechargableResource newRechargableResource(ResourceRechargeableHolder holder);
}