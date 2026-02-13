package games.generic.controlModel.subimpl;

import java.util.List;
import java.util.Map;

import dataStructures.MapMapped;
import dataStructures.MapTreeAVL;
import dataStructures.MapTreeAVL.Optimizations;
import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityAllocation;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.abilities.impl.AbilityAllocationImpl;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.events.event.EventDamage;
import games.generic.controlModel.holders.EquipmentsHolder;
import games.generic.controlModel.items.EquipmentSet;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.GObjMovement;
import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;
import games.generic.controlModel.objects.GameObjectGeneric;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.generic.controlModel.rechargeable.resources.RechargableResource;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import games.generic.controlModel.rechargeable.resources.ResourceRechargeableStrategy;
import games.generic.controlModel.rechargeable.resources.impl.ResourceRechargeableStrategyTimeTickBased;
import geometry.AbstractShape2D;
import tools.Comparators;
import tools.ObjectNamedID;
import tools.ObjectWithID;

/**
 * Defines a default (but not mandatory) implementation of a "creature" concept,
 * especially useful for "Rule Play Game" (RPG). <br>
 * A pointer to {@link EquipmentSet} is provided both to define a base for the
 * "player" concept and, maybe, to help spawning "variable" enemies and dropping
 * items (or just <i>define</i> those enemies, recycling equipments's attributes
 * to define their attributes).
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link }></li>
 * </ul>
 */
public abstract class BaseCreatureRPGImpl implements BaseCreatureRPG {
	private static final long serialVersionUID = 1L;

	protected boolean isDestroyed;
	protected Long ID;
	protected String name;
	protected List<String> eventsWatching;
	protected EquipmentSet equipmentSet;
	protected CreatureAttributes attributes;
	protected AbstractShape2D shape;
	protected GObjMovement movementImplementation;
	protected Map<String, AbilityGeneric> abilities = null;
	protected Map<String, AbilityAllocation> abilityAllocations = null;
	protected ResourceRechargeableStrategy rechargeStrategy;
	protected Map<RechargeableResourceType, RechargableResource> rechargableResources;
	protected GModalityRPG gModalityRPG;

	public BaseCreatureRPGImpl(GModalityRPG gModRPG, String name) {
		super();
		this.gModalityRPG = gModRPG;
		this.name = name;
		this.ID = UIDP_CREATURE.getNewID();
		this.isDestroyed = false;
		initAllCreatureStuffs();
	}

	/** Override designed. */
	protected void initAllCreatureStuffs() {
		this.attributes = newAttributes();
		this.initRechargeableResourceHolderStuffs();
		this.setEquipmentSet(newEquipmentSet());
		this.equipmentSet.setCreatureWearingEquipments(this);
	}

	/**
	 * Creates a new {@link CreatureAttributes} with a fixed amounts of attributes.
	 */
	protected CreatureAttributes newAttributes(int attributesAmount, IndexToObjectBackmapping itai) {
		return new CreatureAttributesBaseAndDerivedCaching(attributesAmount, itai);
	}

	/** Must call {@link #newAttributes(int, IndexToObjectBackmapping)}. */
	protected abstract CreatureAttributes newAttributes();

	protected abstract int getDamageReductionForType(ObjectNamedID damageType);

	// TODO GETTER

	@Override
	public GModality getGameModality() {
		return gModalityRPG;
	}

	public GModalityRPG getgModalityRPG() {
		return gModalityRPG;
	}

	@Override
	public Long getID() {
		return ID;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public CreatureAttributes getAttributes() {
		return attributes;
	}

	@Override
	public Map<String, AbilityGeneric> getAbilities() {
		checkAbilitiesSet();
		return abilities;
	}

	@Override
	public AbstractShape2D getShape() {
		return shape;
	}

	@Override
	public boolean isDestroyed() {
		return this.isDestroyed;
	}

	@Override
	public EquipmentSet getEquipmentSet() {
		return equipmentSet;
	}

	@Override
	public Map<String, AbilityAllocation> getAbilitiesAllocations() {
		return this.abilityAllocations;
	}

	@Override
	public Map<RechargeableResourceType, RechargableResource> getRechargableResources() {
		return this.rechargableResources;
	}

	@Override
	public ResourceRechargeableStrategy getResourceRechargeableStrategy() { // TODO Auto-generated method stub
		return this.rechargeStrategy;
	}

	// TODO SETTER

	@Override
	public void setGameModality(GModality gameModality) {
		this.gModalityRPG = (GModalityRPG) gameModality;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setEquipmentSet(EquipmentSet equips) {
		if (this.equipmentSet != null) {
			this.equipmentSet.setCreatureWearingEquipments(null);
		}
		this.equipmentSet = equips;
		if (equips != null) {
			equips.setCreatureWearingEquipments(this);
		}
	}

	@Override
	public void setAttributes(CreatureAttributes attributes) {
		this.attributes = attributes;
	}

	@Override
	public void setShape(AbstractShape2D shape) {
		this.shape = shape;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public void setMovementImplementation(GObjMovement movementImplementation) {
		this.movementImplementation = movementImplementation;
	}

	@Override
	public void setResourceRechargeableStrategy(ResourceRechargeableStrategy resourceRechargeableStrategy) {
		this.rechargeStrategy = resourceRechargeableStrategy;
	}

	@Override
	public boolean setID(Long newID) {
		if (newID == null || newID == this.ID || (this.ID != null && this.ID.longValue() == newID.longValue())) {
			return false;
		}
		this.ID = newID;
		return true;
	}

	//

	//

	// TODO VERY USEFULL STUFFS

	//

	//

	@Override
	public void initRechargeableResourceHolderStuffs() {
		MapTreeAVL<RechargeableResourceType, RechargableResource> backmapRechRes;
//		this.rechargeStrategy = new ResourceRechargeableStrategyTimeTickBased<ObjectWithID>(this, this);
		this.setResourceRechargeableStrategy(
				new ResourceRechargeableStrategyTimeTickBased<BaseCreatureRPGImpl>(this, this));
		backmapRechRes = MapTreeAVL.newMap(Optimizations.MinMaxIndexIteration,
				RechargeableResourceType.COMPARATOR_RECHARGEABLE_RESOURCE_TYPE);
		this.rechargableResources = backmapRechRes;

		//
		BaseCreatureRPG.super.initRechargeableResourceHolderStuffs();
	}

	protected void checkAbilitiesSet() {
		if (this.abilities == null) {
			Map<String, AbilityAllocation> me = null;
			MapMapped<String, AbilityAllocation, AbilityGeneric> abil;
			me = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, MapTreeAVL.BehaviourOnKeyCollision.Replace,
					Comparators.STRING_COMPARATOR);
			this.abilityAllocations = me;
			abil = new MapMapped<String, AbilityAllocation, AbilityGeneric>(me,
					AbilityAllocation.ABILITY_ALLOCATED_EXTRACTOR);

			abil.setReverseMapper(ag -> this.abilityAllocations.get(ag.getName()));
			this.abilities = abil;
		}
	}

	//

	// TODO PUBLIC METHODS

	@Override
	public void act(GModality modality, int timeUnits) {
		if (isDestroyed())
			return;
		BaseCreatureRPG.super.act(modality, timeUnits);
	}

	@Override
	public void move(GModality modality, int timeUnits) {
		if (movementImplementation != null)
			movementImplementation.act(modality, timeUnits);
	}

	@Override
	public BaseCreatureRPG addAbility(AbilityGeneric ability) {
		if (ability == null) {
			return this;
		}
		this.getAbilitiesAllocations().put(ability.getName(), new AbilityAllocationImpl(ability));
		return this;
	}

	@Override
	public BaseCreatureRPG removeAbilityByName(String abilityName) {
		if (abilityName == null) {
			return this;
		}
		this.getAbilitiesAllocations().remove(abilityName);
		return this;
	}

	@Override
	public void rechargeResources() {
		// does nothing:
	}

	@Override
	public void addMeToGame(GModality gm) {
		BaseCreatureRPG.super.addMeToGame(gm);
		if (this.rechargeStrategy instanceof GameObjectGeneric) {
			gm.addGameObject((GameObjectGeneric) this.rechargeStrategy);
		}
	}

	@Override
	public void removeMeToGame(GModality gm) {
		BaseCreatureRPG.super.removeMeToGame(gm);
		if (this.rechargeStrategy instanceof GameObjectGeneric) {
			gm.removeGameObject((GameObjectGeneric) this.rechargeStrategy);
		}
	}

	/**
	 * No clean-up performed in this class: yet performed by super-interfaces
	 * {@link CreatureSimple} and {@link EquipmentsHolder}.
	 * <p>
	 * Inherit documentation:
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean destroy() {
		this.gModalityRPG.removeGameObject(this);
		this.setDestroyed(true);
		System.out.println(
				"\n\n\n\n " + name + " I'M DEEEEEAAAAAADDDDDD\n i'm getting destroyed on BaseCreatureRPGImpl \n\n\n");
		return true;
	}

	@Override
	public <Source extends ObjectWithID> void performRechargeOf(ResourceAmountRecharged recharge,
			Source whoIsPerformingTheRecharge) {
		RechargableResource res;
		res = this.rechargableResources.get(recharge.getRechargedResource());
		if (res == null) {
			throw new IllegalArgumentException("Resource to recharge not found: " + recharge);
		}
		res.performRechargeBy(recharge.getRechargedAmount());
		this.fireRechargeEvent(recharge, whoIsPerformingTheRecharge);
	}

	// TODO fire damage
	@Override
	public void receiveDamage(GModality gm, DamageGeneric originalDamage, DamageDealerGeneric source) {
		int damageAmountToBeApplied, damageReallyReceived, shield; // originalDamageAmount
//		GModalityRPG gmrpg;
		EventDamage eventDamageProcessed;
		if (originalDamage.getDamageAmount() <= 0)
			return;
//		gmrpg = (GModalityRPG) gm;
//		// check the type
//		gmrpg.getGameObjectsManager().dealsDamageTo(source, this, originalDamage);
		damageAmountToBeApplied = originalDamage.getDamageAmount()
				- getDamageReductionForType(originalDamage.getType());
		// no negativity check: could there be a malus
		eventDamageProcessed = fireDamageReceived(gm, originalDamage, source, damageAmountToBeApplied);
		// after notifying everyone, the damage could have changed: check it again
		damageReallyReceived = (eventDamageProcessed == null ? damageAmountToBeApplied //
				: eventDamageProcessed.getDamageAmountToBeApplied());
		if (damageReallyReceived > 0) {
			System.out.println("-_-''-BaseCreatureRPGImpl then the damage received is: " + damageReallyReceived
					+ " and i had " + getLife() + " life, shield: " + getShield());
			shield = getShield();
			if (shield > 0) {
				if (shield >= damageReallyReceived) {
					setShield(shield - damageReallyReceived);
					originalDamage.setDamageAmount(0);
					return;
				} else {
					setShield(0);
					damageReallyReceived -= shield;
					originalDamage.setDamageAmount(damageReallyReceived);
				}
			}
			// then execute the damage, after ALL reductions and malus by listeners
			setLife(getLife() - damageReallyReceived);
		}
	}

	//

	// TODO FIRE EVENTS

	// TODO MANA
}