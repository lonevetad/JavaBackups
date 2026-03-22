package games.old;

import java.util.Set;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.FactoryObjGModalityBased;

public class ObjGModalityBasedProvider_OLD<DesiredType>
		extends ObjNamedIDProvider<FactoryObjGModalityBased<DesiredType>> {
//	extends ObjNamedIDProvider<FactoryObjGModalityBased<DesiredType>>

	public void addAbility(FactoryObjGModalityBased<DesiredType> a) { addObjIdentified(a); }

	/**
	 * Should be avoided, should then prefer
	 * {@link #getObjIdentifiedByName(String)}.
	 */
	public DesiredType getObjByID(GModality gm, Integer id) {
		if (gm == null || id == null)
			return null;
		return getObjIdentifiedByID(id).newInstance(gm);
	}

	/** Should be preferred over {@link #getObjIdentifiedByID(Integer)}. */
	public DesiredType getAbilityByName(GModality gm, String name) {
		if (gm == null || name == null)
			return null;
		return getObjIdentifiedByName(name).newInstance(gm);
	}

	public Set<FactoryObjGModalityBased<DesiredType>> getObjects() { return getObjectsIdentified(); }

	public int getAbilitiesCount() { return getObjectsIdentifiedCount(); }
}