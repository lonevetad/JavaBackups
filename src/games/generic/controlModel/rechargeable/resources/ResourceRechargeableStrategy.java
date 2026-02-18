package games.generic.controlModel.rechargeable.resources;

import java.util.Map;

import games.generic.controlModel.objects.TimedObject;
import tools.ObjectWithID;

/**
 * Defines a way to recharge some rechargeable resource
 * ({@link RechargeableResourceType}) as time gose by (since this interface
 * extends {@link TimedObject})
 */
public interface ResourceRechargeableStrategy extends ObjectWithID {

	public void rechargeResources(Map<RechargeableResourceType, RechargableResource> resources);
}