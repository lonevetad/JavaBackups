package games.theRisingAngel.creatures;

import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.damage.DamageTypeGeneric;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventDestructionObj;
import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.items.EquipmentSet;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;
import games.generic.controlModel.rechargeable.resources.RechargableResource;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.holders.StaminaHavingObject;
import games.generic.controlModel.rechargeable.resources.impl.RechargableResourceWithDelay;
import games.generic.controlModel.subimpl.BaseCreatureRPGImpl;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.GameOptionsTRAn;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.DamageTypesTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.enums.RechargeableResourcesTRAn;
import games.theRisingAngel.inventory.EquipmentSetTRAn;
import games.theRisingAngel.misc.CreatureAttributesTRAn;
import tools.Comparators;
import tools.ObjectNamedID;

public abstract class BaseCreatureTRAn extends BaseCreatureRPGImpl implements StaminaHavingObject {
	private static final long serialVersionUID = -34551879021102L;
	protected static boolean setRRTypesNeverFilled;
	protected static final MapTreeAVL<Long, RechargeableResourceType> BACKMAP_DEFAULT_SET_RECHARGEABLE_RESOURCE_TYPE;
	protected static final Set<RechargeableResourceType> DEFAULT_SET_RECHARGEABLE_RESOURCE_TYPE;

	protected static final void checkSetRRTypes() {
		if (setRRTypesNeverFilled) {
			for (RechargeableResourceType r : RechargeableResourcesTRAn.ALL_RECHARGEABLE_RESOURCES_TRAn) {
				if (!BACKMAP_DEFAULT_SET_RECHARGEABLE_RESOURCE_TYPE.containsKey(r.getID())) {
					BACKMAP_DEFAULT_SET_RECHARGEABLE_RESOURCE_TYPE.put(r.getID(), r);
				}
			}
			setRRTypesNeverFilled = false;
		}
	}

	static {
		MapTreeAVL<Long, RechargeableResourceType> m;
		setRRTypesNeverFilled = true;
		m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.LONG_COMPARATOR);
		BACKMAP_DEFAULT_SET_RECHARGEABLE_RESOURCE_TYPE = m;
		DEFAULT_SET_RECHARGEABLE_RESOURCE_TYPE = m.toSetValue(RechargeableResourceType::getID);
	}

	public BaseCreatureTRAn(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
		this.life = this.mana = this.shield = this.stamina = 0;
	}

	protected int life, mana, shield, stamina; // current values, to optimize

	//

	@Override
	public EquipmentSet newEquipmentSet() {
		return new EquipmentSetTRAn();
	}

	@Override
	protected CreatureAttributes newAttributes() {
		return newAttributes(AttributesTRAn.ALL_ATTRIBUTES.length, AttributesTRAn.INDEX_TO_ATTRIBUTE_TRAn);
	}

	@Override
	protected CreatureAttributes newAttributes(int attributesAmount, IndexToObjectBackmapping itai) {
		return new CreatureAttributesTRAn();
	}

	@Override
	public Set<RechargeableResourceType> getRechargableResourcesType() {
		checkSetRRTypes();
		return DEFAULT_SET_RECHARGEABLE_RESOURCE_TYPE;
	}

	// TODO GETTER

	@Override
	public int getLife() {
		return this.life;
	}

	@Override
	public int getLifeMax() {
		return this.getAttributes().getValue(AttributesTRAn.LifeMax);
	}

	@Override
	public int getLifeRegeneration() {
		return this.getAttributes().getValue(AttributesTRAn.LifeRegen);
	}

	@Override
	public int getMana() {
		return this.mana;
	}

	@Override
	public int getManaMax() {
		return this.getAttributes().getValue(AttributesTRAn.ManaMax);
	}

	@Override
	public int getManaRegeneration() {
		return this.getAttributes().getValue(AttributesTRAn.ManaRegen);
	}

	@Override
	public int getShield() {
		return this.shield;
	}

	@Override
	public int getShieldMax() {
		return this.getAttributes().getValue(AttributesTRAn.ShieldMax);
	}

	@Override
	public int getShieldRegeneration() {
		return this.getAttributes().getValue(AttributesTRAn.ShieldRegen);
	}

	@Override
	public int getStamina() {
		return this.stamina;
	}

	@Override
	public int getStaminaMax() {
		return this.getAttributes().getValue(AttributesTRAn.StaminaMax);
	}

	@Override
	public int getStaminaRegeneration() {
		return this.getAttributes().getValue(AttributesTRAn.StaminaRegen);
	}

	@Override
	public RechargeableResourceType getLifeResourceType() {
		return RechargeableResourcesTRAn.Life;
	}

	@Override
	public RechargeableResourceType getManaResourceType() {
		return RechargeableResourcesTRAn.Mana;
	}

	@Override
	public RechargeableResourceType getShieldResourceType() {
		return RechargeableResourcesTRAn.Shield;
	}

	@Override
	public RechargeableResourceType getStaminaResourceType() {
		return RechargeableResourcesTRAn.Stamina;
	}

	@Override
	public int getLuckPerThousand() {
		return this.getAttributes().getValue(AttributesTRAn.Luck);
	}

	@Override
	public AttributeIdentifier getShieldDelayReductionAttribute() {
		return AttributesTRAn.ShieldDelayReduction;
	}

	// TODO damage dealer receiver

	/**
	 * Returns the raw/absolute amount of damage bonus.
	 */
	@Override
	public int getDamageBonus(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.PhysicalDamageBonus
				: AttributesTRAn.MagicalDamageBonus;
		return this.getAttributes().getValue(ai);
	}

	/**
	 * Returns the relative (percentage) amount of damage bonus.
	 */
	@Override
	public int getDamageBonusPercentage(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.PhysicalDamageMultiplierPercentageBonus
				: AttributesTRAn.MagicalDamageMultiplierPercentageBonus;
		return this.getAttributes().getValue(ai);
	}

	/**
	 * Returns the raw/absolute amount of damage reduction.
	 */
	@Override
	public int getDamageReduction(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.PhysicalDamageReduction
				: AttributesTRAn.MagicalDamageReduction;
		return this.getAttributes().getValue(ai);
	}

	/**
	 * Returns the relative (percentage) amount of damage reduction.
	 */
	@Override
	public int getDamageReductionPercentage(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.PhysicalDamageMultiplierPercentageReduction
				: AttributesTRAn.MagicalDamageMultiplierPercentageReduction;
		return this.getAttributes().getValue(ai);
	}

	@Override
	public int getProbabilityPerThousandAvoid(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.PhysicalProbabilityPerThousandAvoid
				: AttributesTRAn.MagicalProbabilityPerThousandAvoid;
		return this.getAttributes().getValue(ai);
	}

	@Override
	public int getProbabilityPerThousandHit(DamageTypeGeneric damageType) {
		AttributeIdentifier ai;
		ai = damageType == DamageTypesTRAn.Physical ? AttributesTRAn.PhysicalProbabilityPerThousandHit
				: AttributesTRAn.MagicalProbabilityPerThousandHit;
		return this.getAttributes().getValue(ai);
	}

	@Override
	public int getProbabilityPerThousandCriticalStrike(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalProbabilityPerThousandHit);
	}

	@Override
	public int getPercentageCriticalStrikeMultiplier(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalMultiplierPercentage);
	}

	@Override
	public int getProbabilityPerThousandAvoidCritical(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalProbabilityPerThousandAvoid);
	}

	/** Percentage amount of multiplication of the damage. Should be positive. */
	@Override
	public int getPercentageCriticalStrikeReduction(DamageTypeGeneric damageType) {
		return this.getAttributes().getValue(AttributesTRAn.CriticalMultiplierPercentageReduction);
	}

	//

	// TODO SETTER

	@Override
	public void setLife(int life) {
		if (life < this.getLifeResourceType().getLowerBound()) {
			life = this.getLifeResourceType().getLowerBound();
		}
		if (life > this.getLifeMax()) {
			life = this.getLifeMax();
		}
		this.life = life;

		if (life <= 0) {
			EventDestructionObj de;
			de = fireDestructionEvent(getgModalityRPG());
			if (de == null || de.isDestructionValid()) {
				destroy();
			}
		}
	}

	@Override
	public void setLifeMax(int lifeMax) {
		RechargeableResourceType type;
		type = this.getLifeResourceType();
		if (lifeMax < type.getLowerBound()) {
			lifeMax = type.getLowerBound();
		}
		if (lifeMax > type.getUpperBound()) {
			lifeMax = type.getUpperBound();
		}
		if (this.life > lifeMax) {
			this.life = lifeMax;
		}
		this.getAttributes().setOriginalValue(AttributesTRAn.LifeMax, lifeMax);
	}

	@Override
	public void setLifeRegeneration(int lifeRegenation) {
		if (lifeRegenation >= 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.LifeRegen, lifeRegenation);
		}
	}

	@Override
	public void setMana(int mana) {
		if (mana < this.getManaResourceType().getLowerBound()) {
			mana = this.getManaResourceType().getLowerBound();
		}
		if (mana > this.getManaMax()) {
			mana = this.getManaMax();
		}
		this.mana = mana;
	}

	@Override
	public void setManaMax(int manaMax) {
		RechargeableResourceType type;
		type = this.getManaResourceType();
		if (manaMax < type.getLowerBound()) {
			manaMax = type.getLowerBound();
		}
		if (manaMax > type.getUpperBound()) {
			manaMax = type.getUpperBound();
		}
		if (this.mana > manaMax) {
			this.mana = manaMax;
		}
		this.getAttributes().setOriginalValue(AttributesTRAn.ManaMax, manaMax);
	}

	@Override
	public void setManaRegeneration(int manaRegeneration) {
		if (manaRegeneration >= 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.ManaRegen, manaRegeneration);
		}
	}

	@Override
	public void setShield(int shield) {
		if (shield < this.getShieldResourceType().getLowerBound()) {
			shield = this.getShieldResourceType().getLowerBound();
		}
		if (shield > this.getShieldMax()) {
			shield = this.getShieldMax();
		}
		this.shield = shield;
	}

	@Override
	public void setShieldMax(int shieldMax) {
		RechargeableResourceType type;
		type = this.getShieldResourceType();
		if (shieldMax < type.getLowerBound()) {
			shieldMax = type.getLowerBound();
		}
		if (shieldMax > type.getUpperBound()) {
			shieldMax = type.getUpperBound();
		}
		if (this.shield > shieldMax) {
			this.shield = shieldMax;
		}
		this.getAttributes().setOriginalValue(AttributesTRAn.ShieldMax, shieldMax);
	}

	@Override
	public void setShieldRegeneration(int shieldRegenation) {
		if (shieldRegenation >= 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.ShieldRegen, shieldRegenation);
		}
	}

	@Override
	public void setStamina(int stamina) {
		if (stamina < this.getStaminaResourceType().getLowerBound()) {
			stamina = this.getStaminaResourceType().getLowerBound();
		}
		if (stamina > this.getStaminaMax()) {
			stamina = this.getStaminaMax();
		}
		this.stamina = stamina;
	}

	@Override
	public void setStaminaMax(int staminaMax) {
		RechargeableResourceType type;
		type = this.getStaminaResourceType();
		if (staminaMax < type.getLowerBound()) {
			staminaMax = type.getLowerBound();
		}
		if (staminaMax > type.getUpperBound()) {
			staminaMax = type.getUpperBound();
		}
		if (this.stamina > staminaMax) {
			this.stamina = staminaMax;
		}
		this.getAttributes().setOriginalValue(AttributesTRAn.StaminaMax, staminaMax);
	}

	@Override
	public void setStaminaRegeneration(int staminaRegeneration) {
		if (staminaRegeneration >= 0) {
			this.getAttributes().setOriginalValue(AttributesTRAn.StaminaRegen, staminaRegeneration);
		}
	}

	//

	//

	// TODO OTHER METHODS

	@Override
	public void initSetRechargeableResources() {
		RechargableResource res;
		this.addRechargableResource(new RechResourceDelayAware(getLifeResourceType(), this) {
			private static final long serialVersionUID = 4444444444L;

			@Override
			public void setRechargeAmount(int regenerationAmount) {
				setLifeRegeneration(regenerationAmount);
			}

			@Override
			public void setAmountMax(int resourceAmountMax) {
				setLifeMax(resourceAmountMax);
			}

			@Override
			public void setAmount(int resourceAmount) {
				setLife(resourceAmount);
			}

			@Override
			public int getRechargeAmount() {
				return getLifeRegeneration();
			}

			@Override
			public int getMaxAmount() {
				return getLifeMax();
			}

			@Override
			public int getAmount() {
				return getLife();
			}

			@Override
			public void advanceElapseTime(int timeUnits) {
			}

			@Override
			public void stopRechargeStartDelaying() {
			}
		});

		this.addRechargableResource(new RechResourceDelayAware(getManaResourceType(), this) {
			private static final long serialVersionUID = 55555555555555L;

			@Override
			public void setRechargeAmount(int regenerationAmount) {
				setManaRegeneration(regenerationAmount);
			}

			@Override
			public void setAmountMax(int resourceAmountMax) {
				setManaMax(resourceAmountMax);
			}

			@Override
			public void setAmount(int resourceAmount) {
				setMana(resourceAmount);
			}

			@Override
			public int getRechargeAmount() {
				return getManaRegeneration();
			}

			@Override
			public int getMaxAmount() {
				return getManaMax();
			}

			@Override
			public int getAmount() {
				return getMana();
			}

			@Override
			public void advanceElapseTime(int timeUnits) {
			}

			@Override
			public void stopRechargeStartDelaying() {
			}
		});

		res = new RechResourceDelayAware(getShieldResourceType(), this) {
			private static final long serialVersionUID = 6666666666L;

			@Override
			public void setRechargeAmount(int regenerationAmount) {
				setShieldRegeneration(regenerationAmount);
			}

			@Override
			public void setAmountMax(int resourceAmountMax) {
				setShieldMax(resourceAmountMax);
			}

			@Override
			public void setAmount(int resourceAmount) {
				setShield(resourceAmount);
			}

			@Override
			public int getRechargeAmount() {
				return getShieldRegeneration();
			}

			@Override
			public int getMaxAmount() {
				return getShieldMax();
			}

			@Override
			public int getAmount() {
				return getShield();
			}

			@Override
			public int getDelayBeforeRecharge() {
				int sd, d;
				d = super.getDelayBeforeRecharge();
				sd = getAttributes().getValue(getShieldDelayReductionAttribute());
				return sd > d ? 0 : (d - sd);
			}

		};
		res.setDelayBeforeRecharge(((GameOptionsTRAn) this.gModalityRPG.getGameController().getGameOptions())
				.getShieldDelayMilliseconds());
		this.addRechargableResource(res);
		super.initSetRechargeableResources();
	}

	@Override
	public void initSetRechargeableResourcesType() {
		checkSetRRTypes();
		// NOTHING: DEFAULT_SET_RECHARGEABLE_RESOURCE_TYPE is already set
	}

	@Override
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent) {
		return maybeDestructionEvent.getName() == EventsTRAn.Destroyed.getName();
	}

	@Override
	protected int getDamageReductionForType(ObjectNamedID damageType) {
		return this.getAttributes().getValue(AttributesTRAn.damageReductionByType((DamageTypesTRAn) damageType));
	}

	// TODO FIRE EVENTS

	@Override
	public EventDestructionObj fireDestructionEvent(GModality gm) {
		GModalityET gmet;
		GEventInterfaceRPG gei;
		if (gm == null || (!(gm instanceof GModalityET)))
			return null;
		gmet = (GModalityET) gm;
		gei = (GEventInterfaceRPG) gmet.getEventInterface();
		return gei.fireDestructionObjectEvent(gmet, this);
	}

	//

	// TODO CLASS

	protected static abstract class RechResourceDelayAware extends RechargableResourceWithDelay {
		private static final long serialVersionUID = -475214589669875230L;

		public RechResourceDelayAware(RechargeableResourceType resourceType,
				ResourceRechargeableHolder resourceHolder) {
			super();
			this.resourceType = resourceType;
			this.resourceHolder = resourceHolder;
		}

		protected final RechargeableResourceType resourceType;
		/** The one who owns this resource. */
		protected final ResourceRechargeableHolder resourceHolder;

		/**
		 * @return the resourceType
		 */
		@Override
		public RechargeableResourceType getResourceType() {
			return resourceType;
		}

		/**
		 * @return the resourceHolder
		 */
		@Override
		public ResourceRechargeableHolder getResourceHolder() {
			return resourceHolder;
		}
	}

}