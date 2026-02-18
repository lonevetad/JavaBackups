package games.theRisingAngel.enums;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.rechargeable.resources.RechargableResource;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.impl.RechargableResourceImpl;
import tools.UniqueIDProvider;

/**
 * <b>NOTE: use {@link #ALL_RECHARGEABLE_RESOURCES_TRAn} used instead of
 * {@link #values()} </b>
 */
public enum RechargeableResourcesTRAn implements RechargeableResourceType {
	Life, Mana, Shield, Stamina;

	public static final UniqueIDProvider RECHARGEABLE_RESOURCES_TRAn_UID_PROVIDER = UniqueIDProvider
			.newBasicIDProvider();
	/**
	 * It's a mutable list because it's designed to be expanded (<b>and used instead
	 * of {@link #values()} </b>)
	 */
	public static final List<RechargeableResourceType> ALL_RECHARGEABLE_RESOURCES_TRAn;
	public static final IndexToObjectBackmapping INDEX_TO_RECHARGEABLE_RESOURCES_TRAn;
	static {
		RechargeableResourcesTRAn[] vals = values();
		ALL_RECHARGEABLE_RESOURCES_TRAn = new ArrayList<RechargeableResourceType>(vals.length);
		for (RechargeableResourcesTRAn resource : vals) {
			resource.ID = RECHARGEABLE_RESOURCES_TRAn_UID_PROVIDER.getNewID();
			ALL_RECHARGEABLE_RESOURCES_TRAn.add(resource);
		}
		INDEX_TO_RECHARGEABLE_RESOURCES_TRAn = ALL_RECHARGEABLE_RESOURCES_TRAn::get;
	}

	//

	protected Long ID;

	@Override
	public Long getID() { return ID; }

	@Override
	public String getName() { return name(); }

	@Override
	public int getIndex() { return ordinal(); }

	@Override
	public RechargableResource newRechargableResource(ResourceRechargeableHolder holder) {
		return new RechargableResourceImpl(holder, this);
	}

	public static final void addRechargeableResourceType(RechargeableResourceType res) {
		ALL_RECHARGEABLE_RESOURCES_TRAn.add(res);
	}

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() { return INDEX_TO_RECHARGEABLE_RESOURCES_TRAn; }

	@Override
	public boolean setID(Long newID) {
		if (newID == null || newID == this.ID || (this.ID != null && this.ID.longValue() == newID.longValue())) {
			return false;
		}
		this.ID = newID;
		return true;
	}
}