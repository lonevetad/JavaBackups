package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.controlModel.GModality;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageTypeGeneric;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.subimpl.TimedObjectPeriodic;
import games.theRisingAngel.GameObjectsManagerTRAn;
import games.theRisingAngel.enums.DamageTypesTRAn;
import tests.tGame.GModality_E1;
import tools.UniqueIDProvider;

// TODO fare con GUI e affini
public class ObjDamageDeliverE1 implements TimedObjectPeriodic, DamageDealerGeneric {
	private static final long serialVersionUID = 4741714L;
	static final int MILLIS_EACH__DAMAGE = 1500;
	protected static final UniqueIDProvider UIDP_OBJ_DAMAGE_DELIVER_E1 = UniqueIDProvider.newBasicIDProvider();

	public int c, damageAmount;
	public long timeElapsed, timeThreshold;
	public final Long ID;
	public BaseCreatureRPG target;
	public DamageTypesTRAn damageType;

	public ObjDamageDeliverE1(long timeThreshold) {
		ID = UIDP_OBJ_DAMAGE_DELIVER_E1.getNewID();
		timeElapsed = 0;
		this.timeThreshold = timeThreshold;
		c = 0;
		this.damageType = DamageTypesTRAn.Physical;
	}

	@Override
	public Long getID() { return ID; }

	@Override
	public long getAccumulatedTimeElapsed() { return timeElapsed; }

	@Override
	public long getTimeThreshold() { return timeThreshold; }

	public BaseCreatureRPG getTarget() { return target; }

	public int getDamageAmount() { return damageAmount; }

	public DamageTypesTRAn getDamageType() { return damageType; }

	//

	public void setDamageType(DamageTypesTRAn damageType) { this.damageType = damageType; }

	public void setDamageAmount(int damageAmount) { this.damageAmount = damageAmount; }

	public void setTarget(BaseCreatureRPG target) { this.target = target; }

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) { this.timeElapsed = newAccumulated; }

	//

	@Override
	public void executeAction(GModality modality) {
		GModality_E1 gmodtrar;
		GameObjectsManagerTRAn gomTrar;
		DamageGeneric d;
		d = new DamageGeneric(damageAmount, this.damageType);
		System.out.println("Damage fired this amont of times: " + c++ + ", dealing " + damageAmount + " "
				+ this.damageType.name());
		gmodtrar = (GModality_E1) modality;
		gomTrar = (GameObjectsManagerTRAn) gmodtrar.getGameObjectsManager();
		gomTrar.dealsDamageTo(this, target, d);
	}

	@Override
	public String getName() { return "Obj damage dealer"; }

	@Override
	public int getProbabilityPerThousandHit(DamageTypeGeneric damageType) { return 250; }

	@Override
	public int getProbabilityPerThousandCriticalStrike(DamageTypeGeneric damageType) { return 0; }

	@Override
	public int getPercentageCriticalStrikeMultiplier(DamageTypeGeneric damageType) { return 0; }

	@Override
	public void onAddedToGame(GModality gm) {}

	@Override
	public void onRemovedFromGame(GModality gm) {}

	@Override
	public GModality getGameModality() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGameModality(GModality gameModality) { // TODO Auto-generated method stub
	}
}