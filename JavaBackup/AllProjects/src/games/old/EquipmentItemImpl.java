package games.old;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentType;
import games.generic.controlModel.items.EquipmentUpgrade;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.subimpl.GModalityRPG;

/**
 * Used during loading: instances of {@link AttributeModification} and
 * {@link EquipmentUpgrade} does not need a reference of {@link GModality} so
 * they can be instantiated on loading time. As opposite, instances of
 * {@link EquipItemAbility} could require that {@link GModality} instance, so
 * just the abilities' names are saved and lazily loaded.
 */
@Deprecated
public class EquipmentItemImpl extends EquipmentItem {
	private static final long serialVersionUID = -56956980L;

	public EquipmentItemImpl(GModalityRPG gmrpg, EquipmentType equipmentType, String name) { // , List<String>
																								// abilitiesName
		super(gmrpg, equipmentType, name);
//		this.abilitiesName = abilitiesName;
		onCreateAfterAssigningAbilitiesName(gmrpg);
	}

//	protected List<String> abilitiesName; // TODO what about
//
//	public List<String> getAbilitiesName() {
//		return abilitiesName;
//	}
//
//	public void setAbilitiesName(List<String> abilitiesName) {
//		this.abilitiesName = abilitiesName;
//	}

	@Override
	protected void onCreate(GModality gm) {
		// nullify the super.onCreate(gm);
	}

	protected void onCreateAfterAssigningAbilitiesName(GModality gm) { super.onCreate(gm); }

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
//		AbilityGeneric a;
//		AbilitiesProvider ap;
//		GameObjectsProvidersHolderRPG prpg;
//		prpg = (GameObjectsProvidersHolderRPG) providersHolder;
////		super.enr
//		ap = prpg.getAbilitiesProvider();
//		if (this.abilitiesName != null) {
//			for (String abilityName : this.abilitiesName) {
//				a = ap.getNewObjByName(gm, abilityName);
//				this.addAbility((EquipItemAbility) a);
//			}
//			this.abilitiesName = null;
//		}
	}
}