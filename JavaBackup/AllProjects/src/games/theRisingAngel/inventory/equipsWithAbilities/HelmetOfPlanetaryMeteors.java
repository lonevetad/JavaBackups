package games.theRisingAngel.inventory.equipsWithAbilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.providers.AbilitiesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.abilities.AFireShpereOrbiting;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.DamageTypesTRAn;
import games.theRisingAngel.enums.EquipmentTypesTRAn;
import games.theRisingAngel.inventory.EINotJewelry;

public class HelmetOfPlanetaryMeteors extends EINotJewelry {
	private static final long serialVersionUID = 922120283L;

	public static final String NAME = "Helmet Of Planetary Meteors";

	public HelmetOfPlanetaryMeteors(GModalityRPG gmrpg) {
		super(gmrpg, EquipmentTypesTRAn.Head, NAME, //
				new AttributeModification[] { new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 4),
						new AttributeModification(AttributesTRAn.MagicalDamageReduction, 4),
						new AttributeModification(AttributesTRAn.Defense, 4),
						new AttributeModification(AttributesTRAn.Intelligence, 6) });
	}

	protected AFireShpereOrbiting abilitySphereOrbiting;

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
		AbilitiesProvider ap;
		ap = ((GameObjectsProvidersHolderRPG) providersHolder).getAbilitiesProvider();

		this.abilitySphereOrbiting = (AFireShpereOrbiting) ap.getAbilityByName(gm, AFireShpereOrbiting.NAME);
		this.abilitySphereOrbiting.setOwner(this);
		abilitySphereOrbiting.setDamageToDeal(new DamageGeneric(15, DamageTypesTRAn.Magical));
		super.addAbility(this.abilitySphereOrbiting);
	}

}
