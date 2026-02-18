package games.theRisingAngel.abilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityBaseImpl;
import games.generic.controlModel.abilities.impl.AbilityTargetingGObjInMap;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageReceiverGeneric;
import games.generic.controlModel.misc.GObjMovement;
import games.generic.controlModel.objects.MovingObject;
import games.generic.controlModel.objects.MovingObjectDelegatingMovement;
import games.generic.controlModel.objects.OrbitingInteractiveObject;
import games.generic.controlModel.objects.TimedObject;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.generic.controlModel.subimpl.movements.GObjLinearMovement;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.implementations.shapes.ShapeCircle;
import tools.Comparators;
import tools.MathUtilities;
import tools.UniqueIDProvider;

// emula il "Storm Burst" di "path of Exile"
/**
 * Generates a series of orbs that scatters and deals damage upon hitting
 * someone while moving or exploding.
 */
public abstract class ARandomScatteringOrbs extends AbilityBaseImpl implements AbilityTargetingGObjInMap, TimedObject {
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Scattering Bombs";
	protected static final boolean STATUS_TRAVELLING = true, STATUS_STATIONARY_FOR_EXPLOSION = !STATUS_TRAVELLING;
	protected static final int MILLIS_STATIONARY_BEFORE_EXPLOSION = 500, MILLIS_TRAVELLING = 1000;
	protected static final UniqueIDProvider UIDP_SCATTERING_ORBS = UniqueIDProvider.newBasicIDProvider();

	public ARandomScatteringOrbs(GModalityTRAnBaseWorld gameModality) {
		super();
		this.gameModality = gameModality;
		random = gameModality.getRandom();
		timeSpawningOrbs = tempSpawning = 0;
		this.targetsFilter = this.newTargetsFilter();
	}

	protected transient int tempSpawning;
	protected int timeSpawningOrbs;
	protected transient Random random;
	protected GModalityTRAnBaseWorld gameModality;
	protected Set<ScatteringOrb> orbs;
	protected Predicate<ObjectLocated> targetsFilter;

	@Override
	public String getName() { return NAME; }

	public int getTimeSpawningOrbs() { return timeSpawningOrbs; }

	public Random getRandom() { return random; }

	public Set<ScatteringOrb> getOrbs() { return orbs; }

	@Override
	public Predicate<ObjectLocated> getTargetsFilter() { return this.targetsFilter; }

	//

	public void setTimeSpawningOrbs(int timeSpawningOrbs) { this.timeSpawningOrbs = timeSpawningOrbs; }

	public void setRandom(Random random) { this.random = random; }

	@Override
	public GModality getGameModality() { return gameModality; }

	@Override
	public void setGameModality(GModality gameModality) { this.gameModality = (GModalityTRAnBaseWorld) gameModality; }

	//

	/** Get the radius of each single orb */
	public int getRadiusOrbs() {
		return gameModality.getSpaceSubunitsEachUnit(); // just 1 unit
	}

	/**
	 * Returns an upper bound of amount of scattering orbs. A negative or zero
	 * number is considered as no upper bound.
	 */
	public abstract int getMaxAmountScatteringOrbs();

	public abstract DamageGeneric getDamageOrbs(ScatteringOrb orb, DamageReceiverGeneric target);

	/**
	 * Orbs scatters randomly in a circle as large as this value. Greater-than-zero
	 * values are strongly recommended.<br>
	 */
	public abstract int getRadiusScattering();

	/**
	 * Get the center at which orbs are scattering.<br>
	 * Could follow the user's mouse's pointer.
	 */
	public abstract Point getCenterOfScattering();

	protected abstract ScatteringOrb newOrb();

	protected Predicate<ObjectLocated> newTargetsFilter() { return ol -> this.owner != ol; }

//

	@Override
	public void resetAbility() {
		this.tempSpawning = 0;
		this.orbs.forEach(so -> this.gameModality.removeGameObject(so));
		this.orbs.clear();
	}

	@Override
	public void performAbility(GModality gm, int level) {
// TODO WHAT TO DO? NOTHING?
	}

	@Override
	public void act(GModality modality, int timeUnits) {
		int tso;
		tso = getTimeSpawningOrbs();
		if (this.orbs.size() < getMaxAmountScatteringOrbs()) {
			if ((this.tempSpawning += timeUnits) >= tso) {
				do {
					tempSpawning -= tso;
					this.orbs.add(newOrb()); // spawn
				} while (tempSpawning >= tso && this.orbs.size() < getMaxAmountScatteringOrbs());
			}
		}
		orbs.forEach(so -> so.act(modality, timeUnits));
	}

	protected void performDamage(GModality gm, ScatteringOrb orb, CreatureSimple target) {
		GModalityTRAnBaseWorld gmtran;
		gmtran = (GModalityTRAnBaseWorld) gm;
		gmtran.dealsDamageTo(orb, target, getDamageOrbs(orb, target));
	}

	//

	//

	protected abstract class ScatteringOrb
			implements MovingObject, ObjectShaped, MovingObjectDelegatingMovement, DamageDealerGeneric {
		private static final long serialVersionUID = 1L;
		protected transient boolean status; // if false, then it's stationary
		protected transient int tempTime;
		protected transient final Long id;
		protected GObjLinearMovement movementLinear;
		protected AbstractShape2D shape;
		protected transient Set<ObjectLocated> targetsDamagedOnTravel;
		protected transient List<Point> path;

		protected ScatteringOrb() {
			Point p;
			MapTreeAVL<Long, ObjectLocated> backmap;
			tempTime = 0;
			id = UIDP_SCATTERING_ORBS.getNewID();
			p = getCenterOfScattering();
			shape = new ShapeCircle(p.x, p.y, true, getRadiusOrbs());
			movementLinear = new GObjLinearMovement();
			movementLinear.setObjectToMove(this);
			movementLinear.resetStartingPoint();
			status = STATUS_STATIONARY_FOR_EXPLOSION;
			backmap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR);
			targetsDamagedOnTravel = backmap.toSetValue(ObjectLocated.KEY_EXTRACTOR);
			path = new ArrayList<>(2);
		}

		@Override
		public Long getID() { return id; }

		@Override
		public GObjMovement getMovementImplementation() { return movementLinear; }

		@Override
		public void setMovementImplementation(GObjMovement movementImplementation) {
			this.movementLinear = (GObjLinearMovement) movementImplementation;
		}

		@Override
		public AbstractShape2D getShape() { return shape; }

		@Override
		public void setShape(AbstractShape2D shape) { this.shape = shape; }

		/**
		 * Perform blast explosion, calling
		 * {@link ARandomScatteringOrbs#performDamage(GModality, ScatteringOrb, CreatureSimple)}.
		 */
		public abstract void explode(GModality modality);

		/**
		 * SHOULD call GModality methods for handling damages, i.e.
		 * {@link ARandomScatteringOrbs#performDamage(GModality, ScatteringOrb, CreatureSimple)}.
		 */
		public abstract void damageOnTraveling(GModality modality, ObjectLocated target);

		@Override
		public void act(GModality modality, int timeUnits) {
			if (status) {
				// traveling
				move(modality, timeUnits);
				if ((tempTime += timeUnits) >= MILLIS_TRAVELLING) {
					tempTime = 0;
					status = STATUS_STATIONARY_FOR_EXPLOSION;
				}
			} else if ((tempTime += timeUnits) >= MILLIS_TRAVELLING) {
				int radius;
				double ang;
				Point dest, centre;
				tempTime = 0;
				status = STATUS_TRAVELLING;
				targetsDamagedOnTravel.clear();
				explode(modality);
				// set next destination
				ang = random.nextDouble() * OrbitingInteractiveObject.RHO;
				radius = random.nextInt(getRadiusScattering());
				centre = getCenterOfScattering();
				dest = new Point(0, 0);
				ang = Math.cos(ang);// recycle it
				dest.x = centre.x * ((int) ang * radius);
				ang = 1.0 - ang * ang;// == sin
				dest.y = centre.y * ((int) ang * radius);
				// must cover the whole distance within time
				movementLinear
						.setVelocity((int) (MathUtilities.distance(dest, getLocation()) / getTimeSubUnitsEachUnit()));
				movementLinear.setDestination(dest);
				path.clear();
				path.add(shape.getCenter());
				path.add(dest);// just fill the hole
			}
		}

		@Override
		public void move(GModality modality, int timeUnits) {
			Point start;
			Set<ObjectLocated> collected;
			start = new Point(this.shape.getCenter());
			this.movementLinear.act(modality, timeUnits);
			path.set(0, start);
			path.set(1, this.shape.getCenter());
			collected = modality.getGameObjectsManager().getGObjectInSpaceManager().findInPath(shape,
					getTargetsFilter(), path);
			// damages everyone
			collected.forEach(ol -> {
				if (targetsDamagedOnTravel.contains(ol))
					return;
				damageOnTraveling(modality, ol);
				targetsDamagedOnTravel.add(ol);
			});
		}
	}
}