package games.generic.controlModel.subimpl;

import java.util.List;
import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.EventDamage;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.creature.BaseCreatureRPG;
import games.generic.controlModel.heal.HealAmountInstance;
import games.generic.controlModel.heal.HealableResourcesHolderStrategy;
import games.generic.controlModel.heal.IHealableResourceType;
import games.generic.controlModel.heal.IHealableResourcesHolder;
import games.generic.controlModel.heal.IHealingResourcesOverTimeStrategy;
import games.generic.controlModel.heal.resExample.ExampleHealingType;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.inventoryAbil.EquipmentSet;
import games.generic.controlModel.inventoryAbil.EquipmentsHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.GObjMovement;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.CreatureUIDProvider;
import games.theRisingAngel.misc.DamageTypesTRAn;
import geometry.AbstractShape2D;
import tools.Comparators;
import tools.ObjectNamedID;

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
	protected Integer ID;
	protected String name;
	protected List<String> eventsWatching;
	protected EquipmentSet equipmentSet;
	protected CreatureAttributes attributes;
	protected IHealableResourcesHolder healableResourcesHolder;
	protected IHealingResourcesOverTimeStrategy healerStrategy;
	protected AbstractShape2D shape;
	protected GObjMovement movementImplementation;
	protected Map<String, AbilityGeneric> abilities = null;

	protected GModalityRPG gModalityRPG;

	public BaseCreatureRPGImpl(GModalityRPG gModRPG, String name) {
		super();
		this.gModalityRPG = gModRPG;
		this.name = name;
		initializeID();
		this.isDestroyed = false;
		initAllCreatureStuffs();
	}

	protected void initializeID() { this.ID = CreatureUIDProvider.newID(); }

	/** Override designed. */
	protected void initAllCreatureStuffs() {
		HealableResourcesHolderStrategy hrh;
		this.attributes = newAttributes();
		hrh = new HealableResourcesHolderStrategy(this);
		this.setHealableResourcesHolder(hrh);
		this.setHealingStrategyHelper(hrh);
		defineAllHealableResources();
		this.setEquipmentSet(newEquipmentSet());
		this.equipmentSet.setCreatureWearingEquipments(this);
	}

	@Override
	public void defineAllHealableResources() {
		this.addHealableResourceType(ExampleHealingType.Mana);
		this.addHealableResourceType(ExampleHealingType.Life);
		this.addHealableResourceType(ExampleHealingType.Shield);
	}

	/**
	 * Creates a new {@link CreatureAttributes} with a fixed amounts of attributes.
	 */
	protected CreatureAttributes newAttributes(int attributesAmount) {
		return new CreatureAttributesBaseAndDerivedCaching(attributesAmount);
	}

	/** Must call {@link #newAttributes(int)}. */
	protected abstract CreatureAttributes newAttributes();

	//

	@Override
	public GModality getGameModality() { return gModalityRPG; }

	public GModalityRPG getgModalityRPG() { return gModalityRPG; }

	@Override
	public Integer getID() { return ID; }

	@Override
	public String getName() { return name; }

	@Override
	public List<String> getEventsWatching() { return eventsWatching; }

	@Override
	public CreatureAttributes getAttributes() { return attributes; }

	@Override
	public Map<String, AbilityGeneric> getAbilities() {
		checkAbilitiesSet();
		return abilities;
	}

	@Override
	public AbstractShape2D getShape() { return shape; }

	@Override
	public boolean isDestroyed() { return this.isDestroyed; }

	@Override
	public EquipmentSet getEquipmentSet() { return equipmentSet; }

	@Override
	public IHealableResourcesHolder getHealableResourcesHolder() { return healableResourcesHolder; }

	@Override
	public IHealingResourcesOverTimeStrategy getHealingStrategyHelper() { return healerStrategy; }
	//

	@Override
	public void setGameModality(GModality gameModality) { this.gModalityRPG = (GModalityRPG) gameModality; }

	public void setName(String name) { this.name = name; }

	@Override
	public void setEquipmentSet(EquipmentSet equips) {
		if (this.equipmentSet != null) { this.equipmentSet.setCreatureWearingEquipments(null); }
		this.equipmentSet = equips;
		if (equips != null) { equips.setCreatureWearingEquipments(this); }
	}

	@Override
	public void setAttributes(CreatureAttributes attributes) { this.attributes = attributes; }

	@Override
	public void setShape(AbstractShape2D shape) { this.shape = shape; }

	public void setDestroyed(boolean isDestroyed) { this.isDestroyed = isDestroyed; }

	public void setMovementImplementation(GObjMovement movementImplementation) {
		this.movementImplementation = movementImplementation;
	}

	@Override
	public void setHealableResourcesHolder(IHealableResourcesHolder healableResourcesHolder) {
		this.healableResourcesHolder = healableResourcesHolder;
	}

	@Override
	public void setHealingStrategyHelper(IHealingResourcesOverTimeStrategy healingStrategyHelper) {
		this.healerStrategy = healingStrategyHelper;
	}

	/*
	 * public void setLifeMax(int lifeMax) { if (lifeMax > 0) {
	 * this.getAttributes().setOriginalValue(AttributesTRAr.LifeMax.getIndex(),
	 * lifeMax); if (this.getLife() > lifeMax) this.setLife(lifeMax); } }
	 */

	/*
	 * public void setLifeRegenation(int lifeRegenation) { if (lifeRegenation > 0) {
	 * this.getAttributes().setOriginalValue(AttributesTRAr.RigenLife.getIndex(),
	 * lifeRegenation); } }
	 */

	//

	//

	// TODO VERY USEFULL STUFFS

	//

	//

	protected void checkAbilitiesSet() {
		if (this.abilities == null) {
			MapTreeAVL<String, AbilityGeneric> m;
			m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, MapTreeAVL.BehaviourOnKeyCollision.Replace,
					Comparators.STRING_COMPARATOR);
			this.abilities = m; // m.toSetValue(AbilityGeneric.NAME_EXTRACTOR);
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
		System.out.println("\n\n\n\n " + name + " I'M DEEEEEAAAAAADDDDDD \n\n\n");
		return true;
	}

	protected int getDamageReductionForType(ObjectNamedID damageType) {
		// damageType == DamageTypesTRAr.Physical
		return this.getAttributes().getValue(AttributesTRAn.damageReductionByType((DamageTypesTRAn) damageType));
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

	@Override
	public HealAmountInstance newHealInstance(IHealableResourceType healType, int healAmount) {
		return new HealAmountInstance(healType, healAmount);
	}

	//

	// TODO FIRE EVENTS

	// TODO MANA
}