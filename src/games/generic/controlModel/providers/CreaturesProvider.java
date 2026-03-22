package games.generic.controlModel.providers;

import games.generic.controlModel.GModality;
import games.generic.controlModel.objects.creature.CreatureSimple;

public class CreaturesProvider<C extends CreatureSimple> extends GObjProviderRarityPartitioning<C> {
	public static final String NAME = "CreatP";
	// old:
//	ObjGModalityBasedProvider<CreatureSimple>

	public C getCreatureByName(GModality gm, String name) {
		if (gm == null || name == null)
			return null;
//		return getObjByName(name).newInstance(gm);
		return getNewObjByName(gm, name);
	}
}