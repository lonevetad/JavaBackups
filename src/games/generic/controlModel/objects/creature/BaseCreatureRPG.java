package games.generic.controlModel.objects.creature;

import java.util.Map;
import java.util.function.BiConsumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.holders.EquipmentsHolder;
import games.generic.controlModel.objects.InteractingObj;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.holders.ManaHavingObject;
import games.generic.controlModel.rechargeable.resources.holders.ShieldHavingObject;
import tools.json.types.JSONObject;

public interface BaseCreatureRPG
		extends EquipmentsHolder, CreatureSimple, ManaHavingObject, ShieldHavingObject, InteractingObj {

	public RechargeableResourceType getManaResourceType();

	public RechargeableResourceType getShieldResourceType();

	@Override
	public default int getMana() {
		return this.getAmount(getManaResourceType());
	}

	@Override
	public default int getManaMax() {
		return this.getMaxAmount(getManaResourceType());
	}

	@Override
	public default int getManaRegeneration() {
		return this.getRechargeAmount(getManaResourceType());
	}

	@Override
	public default int getShield() {
		return this.getAmount(getShieldResourceType());
	}

	@Override
	public default int getShieldMax() {
		return this.getMaxAmount(getShieldResourceType());
	}

	@Override
	public default int getShieldRegeneration() {
		return this.getRechargeAmount(getShieldResourceType());
	}

	//

	@Override
	public default void setMana(int mana) {
		this.setAmount(getManaResourceType(), mana);
	}

	@Override
	public default void setManaMax(int manaMax) {
		this.setMaxAmount(getManaResourceType(), manaMax);
	}

	@Override
	public default void setManaRegeneration(int manaRegenation) {
		this.setRechargeAmount(getManaResourceType(), manaRegenation);
	}

	@Override
	public default void setShield(int shield) {
		this.setAmount(getShieldResourceType(), shield);
	}

	@Override
	public default void setShieldMax(int shieldMax) {
		this.setMaxAmount(getShieldResourceType(), shieldMax);
	}

	@Override
	public default void setShieldRegeneration(int shieldRegenation) {
		this.setRechargeAmount(getShieldResourceType(), shieldRegenation);
	}

	//

	@Override
	default void addMeToGame(GModality gm) {
		// implemented to let "this class implementor" to redefine and call it"
		EquipmentsHolder.super.addMeToGame(gm);
		CreatureSimple.super.addMeToGame(gm);
	}

	@Override
	default void onAddedToGame(GModality gm) {
		// nothing to do here righ now
		BiConsumer<String, AbilityGeneric> abilityAdderToGModality;
		// Add equips and abilities on GMod
		abilityAdderToGModality = (n, ab) -> ab.onAddedToGame(gm);
		this.getAbilities().forEach(abilityAdderToGModality);
		this.getEquipmentSet().forEachEquipment((e, i) -> {
			if (e != null) {
				e.onAddedToGame(gm);
			}
		});
	}

	@Override
	default void removeMeToGame(GModality gm) {
		EquipmentsHolder.super.removeMeToGame(gm);
		CreatureSimple.super.removeMeToGame(gm);
	}

	@Override
	default void onRemovedFromGame(GModality gm) {
		// nothing to do here right now
	}

	@Override
	public default void initSetRechargeableResources() {
		CreatureSimple.super.initSetRechargeableResources();
	}

	@Override
	public default int getLuckPerThousand() {
		return CreatureSimple.super.getLuckPerThousand();
	}

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		CreatureSimple.super.toJSONValue(wrapper);
		EquipmentsHolder.super.toJSONValue(wrapper);
		CreatureSimple.super.toJSONValue(wrapper);
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		CreatureSimple.super.loadFromJSONObject(gm, wrapper);
		EquipmentsHolder.super.loadFromJSONObject(gm, wrapper);
		CreatureSimple.super.loadFromJSONObject(gm, wrapper);
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		CreatureSimple.super.loadFromJSONMap(gm, jsonMap);
		EquipmentsHolder.super.loadFromJSONMap(gm, jsonMap);
		CreatureSimple.super.loadFromJSONMap(gm, jsonMap);
	}
}