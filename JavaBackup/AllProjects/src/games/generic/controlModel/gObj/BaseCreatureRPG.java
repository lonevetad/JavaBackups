package games.generic.controlModel.gObj;

import java.util.List;

import games.generic.ObjectWithID;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.EquipmentSet;
import games.generic.controlModel.inventoryAbil.EquipmentsHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subImpl.GModalityRPG;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.events.GEventInterfaceTRAr;
import geometry.AbstractShape2D;

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
public abstract class BaseCreatureRPG implements EquipmentsHolder, CreatureSimple {

	private static final long serialVersionUID = 1L;
	protected boolean isDestroyed;
	protected int life, ticks;
	protected long accumulatedTimeLifeRegen;
	protected Integer ID;
	protected String name;
	protected List<String> eventsWatching;
	protected EquipmentSet equipmentSet;
	protected CreatureAttributes attributes;
	protected AbstractShape2D shape;

	protected GModalityRPG gModalityRPG;

	public BaseCreatureRPG(GModalityRPG gModRPG, String name) {
		super();
		this.gModalityRPG = gModRPG;
		this.name = name;
	}

	//

	@Override
	public GModality getGameModality() {
		return gModalityRPG;
	}

	public GModalityRPG getgModalityRPG() {
		return gModalityRPG;
	}

	@Override
	public Integer getID() {
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
	public AbstractShape2D getShape() {
		return shape;
	}

	@Override
	public EquipmentSet getEquipmentSet() {
		return equipmentSet;
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public int getLife() {
		return life;
	}

	@Override
	public int getLifeMax() {
		return this.getAttributes().getValue(AttributesTRAr.LifeMax.getIndex());
	}

	@Override
	public int getLifeRegenation() {
		return this.getAttributes().getValue(AttributesTRAr.RigenLife.getIndex());
	}

	@Override
	public int getTicks() {
		return ticks;
	}

	@Override
	public long getAccumulatedTimeLifeRegen() {
		return accumulatedTimeLifeRegen;
	}

	// SETTER

	public void setgModalityRPG(GModalityRPG gModalityRPG) {
		this.gModalityRPG = gModalityRPG;
	}

	@Override
	public void setGameModality(GModality gameModality) {
		this.gModalityRPG = (GModalityRPG) gameModality;
	}

	@Override
	public void setEquipmentSet(EquipmentSet equips) {
		this.equipmentSet = equips;
	}

	public void setEventsWatching(List<String> eventsWatching) {
		this.eventsWatching = eventsWatching;
	}

	@Override
	public void setAttributes(CreatureAttributes attributes) {
		this.attributes = attributes;
	}

	@Override
	public void setShape(AbstractShape2D shape) {
		this.shape = shape;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	@Override
	public void setLife(int life) {
		this.life = life;
	}

	@Override
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	@Override
	public void setAccumulatedTimeLifeRegen(long accumulatedTimeLifeRegen) {
		this.accumulatedTimeLifeRegen = accumulatedTimeLifeRegen;
	}

	@Override
	public void setLifeMax(int lifeMax) {
		if (lifeMax > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAr.LifeMax.getIndex(), lifeMax);
			if (this.getLife() > lifeMax)
				this.setLife(lifeMax);
		}
	}

	@Override
	public void setLifeRegenation(int lifeRegenation) {
		if (lifeRegenation > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAr.RigenLife.getIndex(), lifeRegenation);
		}
	}

	//

	//

	// TODO VERY USEFULL STUFFS

	//

	//

	@Override
	public void fireLifeHealingReceived(GModality gm, int originalHealing) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean destroy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void fireDestructionEvent(GModality gm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void move(long milliseconds) {
		// TODO Auto-generated method stub

	}

	public void equip(EquipmentItem equipment) {
		EquipmentSet es;
		es = this.getEquipmentSet();
		if (es != null) {
			es.addEquipmentItem(getGameModality(), equipment);
		}
	}

	@Override
	public void receiveDamage(GModality gm, DamageGeneric originalDamage, ObjectWithID source) {
		if (originalDamage.getDamageAmount() <= 0)
			return;
		int dr;
		// check the type
		dr = this.getAttributes().getValue(AttributesTRAr.DamageReductionPhysical.getIndex());
		if (dr < 0)
			dr = 0;
		setLife(getLife() - (originalDamage.getDamageAmount() - dr));
		fireDamageReceived(gm, originalDamage, source);
	}
	//

	// TODO FIRE EVENTS

	@Override
	public void fireDamageReceived(GModality gm, DamageGeneric originalDamage, ObjectWithID source) {
		GModalityRPG gmodrpg;
		GEventInterfaceRPG geie1;
		if (gm == null || (!(gm instanceof GModalityRPG)))
			return;
		gmodrpg = (GModalityRPG) gm;
		geie1 = (GEventInterfaceTRAr) gmodrpg.getEventInterface();
		geie1.fireDamageReceivedEvent(gmodrpg, source, this, originalDamage);
	}
}