package games.generic.controlModel.objects.creature;

import java.util.function.BiConsumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.holders.EquipmentsHolder;
import games.generic.controlModel.objects.InteractingObj;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.holders.ManaHavingObject;
import games.generic.controlModel.rechargeable.resources.holders.ShieldHavingObject;

public interface BaseCreatureRPG
		extends EquipmentsHolder, CreatureSimple, ManaHavingObject, ShieldHavingObject, InteractingObj {

	public RechargeableResourceType getManaResourceType();

	public RechargeableResourceType getShieldResourceType();

	@Override
	public default int getMana() { return this.getAmount(getManaResourceType()); }

	@Override
	public default int getManaMax() { return this.getMaxAmount(getManaResourceType()); }

	@Override
	public default int getManaRegeneration() { return this.getRechargeAmount(getManaResourceType()); }

	@Override
	public default int getShield() { return this.getAmount(getShieldResourceType()); }

	@Override
	public default int getShieldMax() { return this.getMaxAmount(getShieldResourceType()); }

	@Override
	public default int getShieldRegeneration() { return this.getRechargeAmount(getShieldResourceType()); }

	//

	@Override
	public default void setMana(int mana) { this.setAmount(getManaResourceType(), mana); }

	@Override
	public default void setManaMax(int manaMax) { this.setMaxAmount(getManaResourceType(), manaMax); }

	@Override
	public default void setManaRegeneration(int manaRegenation) {
		this.setRechargeAmount(getManaResourceType(), manaRegenation);
	}

	@Override
	public default void setShield(int shield) { this.setAmount(getShieldResourceType(), shield); }

	@Override
	public default void setShieldMax(int shieldMax) { this.setMaxAmount(getShieldResourceType(), shieldMax); }

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
		this.getEquipmentSet().forEachEquipment((e, i) -> { if (e != null) { e.onAddedToGame(gm); } });
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
//		this.addRechargableResource(new RechargableResource(this, getLifeResourceType()) {
//			private static final long serialVersionUID = 4444444444L;
//
//			@Override
//			public void setRechargeAmount(int regenerationAmount) { setLifeRegeneration(regenerationAmount); }
//
//			@Override
//			public void setAmountMax(int resourceAmountMax) { setLifeMax(resourceAmountMax); }
//
//			@Override
//			public void setAmount(int resourceAmount) { setLife(resourceAmount); }
//
//			@Override
//			public int getRechargeAmount() { return getLifeRegeneration(); }
//
//			@Override
//			public int getMaxAmount() { return getLifeMax(); }
//
//			@Override
//			public int getAmount() { return getLife(); }
//		});
//
//		this.addRechargableResource(new RechargableResource(this, getManaResourceType()) {
//			private static final long serialVersionUID = 55555555555555L;
//
//			@Override
//			public void setRechargeAmount(int regenerationAmount) { setManaRegeneration(regenerationAmount); }
//
//			@Override
//			public void setAmountMax(int resourceAmountMax) { setManaMax(resourceAmountMax); }
//
//			@Override
//			public void setAmount(int resourceAmount) { setMana(resourceAmount); }
//
//			@Override
//			public int getRechargeAmount() { return getManaRegeneration(); }
//
//			@Override
//			public int getMaxAmount() { return getManaMax(); }
//
//			@Override
//			public int getAmount() { return getMana(); }
//		});
//
//		this.addRechargableResource(new RechargableResource(this, getShieldResourceType()) {
//			private static final long serialVersionUID = 6666666666L;
//
//			@Override
//			public void setRechargeAmount(int regenerationAmount) { setShieldRegeneration(regenerationAmount); }
//
//			@Override
//			public void setAmountMax(int resourceAmountMax) { setShieldMax(resourceAmountMax); }
//
//			@Override
//			public void setAmount(int resourceAmount) { setShield(resourceAmount); }
//
//			@Override
//			public int getRechargeAmount() { return getShieldRegeneration(); }
//
//			@Override
//			public int getMaxAmount() { return getShieldMax(); }
//
//			@Override
//			public int getAmount() { return getShield(); }
//		});
		CreatureSimple.super.initSetRechargeableResources();
	}

	@Override
	public default int getLuckPerThousand() { return CreatureSimple.super.getLuckPerThousand(); }
}