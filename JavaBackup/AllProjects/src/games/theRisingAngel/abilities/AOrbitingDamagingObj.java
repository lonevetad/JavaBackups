package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.abilities.impl.AOrbitingSpawningBlobs;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageTypeGeneric;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import geometry.AbstractShape2D;
import geometry.ObjectShaped;
import geometry.implementations.shapes.ShapeCircle;
import tools.UniqueIDProvider;

/**
 * Spawns orbs rotating over a point and dealing damage. Adds a 10% of
 * probability of hitting.
 */
public abstract class AOrbitingDamagingObj extends AOrbitingSpawningBlobs {
	private static final long serialVersionUID = 1L;
	protected static final int HIT_PROBAB_PER_THOUSAND_BONUS = 750;
	protected static final UniqueIDProvider UIDP_ORBITING_DAMAGING_OBJS = UniqueIDProvider.newBasicIDProvider();

	public AOrbitingDamagingObj(GModality gameModality, String name) { super(gameModality, name); }

	protected DamageGeneric damageToDeal;

	public DamageGeneric getDamageToDeal() { return damageToDeal; }

	public void setDamageToDeal(DamageGeneric damageToDeal) { this.damageToDeal = damageToDeal; }

	//

	@Override
	protected ObjectShaped newOrbitingObj() { return new OrbDamaging(); }

	@Override
	protected boolean isValidTarget(ObjectShaped possibleTarget) { return possibleTarget instanceof CreatureSimple; }

	@Override
	protected void interactWith(GModality modality, int index, ObjectShaped os, ObjectShaped target) {
		GModalityTRAnBaseWorld gmtrar;
		GameObjectsManager gom;
		gmtrar = (GModalityTRAnBaseWorld) modality;
		gom = gmtrar.getGameObjectsManager();
		gom.dealsDamageTo((DamageDealerGeneric) os, (CreatureSimple) target, damageToDeal);
	}

	//

	//

	protected class OrbDamaging implements ObjectShaped, DamageDealerGeneric {
		private static final long serialVersionUID = 1L;
		protected final Long ID;
		protected AbstractShape2D shape;

		public OrbDamaging() {
			super();
			ID = UIDP_ORBITING_DAMAGING_OBJS.getNewID();
			shape = new ShapeCircle(0, 0, true, getGameModality().getSpaceSubunitsEachMacrounits());
		}

		@Override
		public Long getID() { return ID; }

		@Override
		public String getName() { return null; }

		@Override
		public int getProbabilityPerThousandHit(DamageTypeGeneric damageType) {
			int p;
			DamageDealerGeneric caster;
			p = HIT_PROBAB_PER_THOUSAND_BONUS;
			if (owner instanceof DamageDealerGeneric) {
				caster = (DamageDealerGeneric) owner;
				p += caster.getProbabilityPerThousandHit(damageType);
			}
			return p;
		}

		@Override
		public int getProbabilityPerThousandCriticalStrike(DamageTypeGeneric damageType) { return 0; }

		@Override
		public int getPercentageCriticalStrikeMultiplier(DamageTypeGeneric damageType) { return 0; }

		@Override
		public AbstractShape2D getShape() { return null; }

		@Override
		public void setShape(AbstractShape2D shape) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getDamageBonus(DamageTypeGeneric damageType) { return 0; }

		@Override
		public int getDamageBonusPercentage(DamageTypeGeneric damageType) { return 0; }

		@Override
		public boolean setID(Long newID) { return false; }
	}
}