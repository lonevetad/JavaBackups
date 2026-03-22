package games.theRisingAngel.creatures;

import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.objects.creature.CreatureSharingAbilities;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.Comparators;
import tools.ObjectNamedID;

/** Remember to set that ability */
public class Genomorph extends BaseCreatureTRAn implements CreatureSharingAbilities {
	private static final long serialVersionUID = -93220118950L;
	public static final String GENOMORPH_CLASS = "genomorph";

	public Genomorph(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
		MapTreeAVL<String, String> m;
		m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
		sharedAbilities = m.toSetValue(s -> s); // identity function // AbilityGeneric.NAME_EXTRACTOR);
	}

	protected Set<String> sharedAbilities;
	protected String abilityToShare;

	//

	@Override
	public ObjectNamedID getCreatureGroupBelonging() { return games.theRisingAngel.enums.CreatureTypesTRAn.Genomorph; }

	@Override
	public String getAbilityToShare() { return abilityToShare; }

	@Override
	public Set<String> getSharedAbilities() { return sharedAbilities; }

	//

	@Override
	public void setSharedAbilities(Set<String> abilities) { sharedAbilities = abilities; }

	public void setAbilityToShare(String abilityToShare) { this.abilityToShare = abilityToShare; }
}