package games.generic.controlModel;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.inventoryAbil.CreaturesProvider;
import games.generic.controlModel.inventoryAbil.EquipItemProvider;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.InventoryItem;
import games.generic.controlModel.inventoryAbil.InventoryItemNotEquippable;
import games.generic.controlModel.misc.GameObjectsProvider;
import tools.Comparators;

/**
 * Huge class acting as a "database for instantiation" collecting a series of
 * useful game objects. In particular, it holds ("provides" through
 * {@link GameObjectsProvidersHolderRPG}) the list below of superclasses and
 * manages them, like calculating the drops and spawning inside the game.
 * <ul>
 * <li>{@link InventoryItem} (More precisely, {@link EquipmentItem} and
 * {@link InventoryItemNotEquippable}).</li>
 * <li>{@link AbilityGeneric}</li>
 * <li>{@link CreatureSimple}</li>
 * </ul>
 * Manages: dropping items (for instance, upon killing an enemy or opening a
 * chest), defining abilities, etc.
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link EquipmentItem}</li>
 * <li>{@link EquipItemProvider}</li>
 * <li>{@link AbilityGeneric}</li>
 * <li>{@link AbilitiesProvider}</li>
 * <li>{@link CreatureSimple}</li>
 * <li>{@link CreaturesProvider}</li>
 * </ul>
 */
public abstract class GameObjectsProvidersHolderRPG implements GameObjectsProvidersHolder {

	public GameObjectsProvidersHolderRPG() {
		this.providers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
		this.abilitiesProvider = newAbilitiesProvider();
		this.equipmentsProvider = newEquipItemProvider();
		this.creaturesProvider = newCreatureProvider();
		this.providers.put(abilitiesProvider.getClass().getSimpleName(), abilitiesProvider);
		this.providers.put(equipmentsProvider.getClass().getSimpleName(), equipmentsProvider);
		this.providers.put(creaturesProvider.getClass().getSimpleName(), creaturesProvider);
	}

	protected Map<String, GameObjectsProvider<? extends ObjectNamed>> providers;
	protected AbilitiesProvider abilitiesProvider;
	protected EquipItemProvider equipmentsProvider;
	protected CreaturesProvider<BaseCreatureRPG> creaturesProvider;

	//
	@Override
	public Map<String, GameObjectsProvider<? extends ObjectNamed>> getProviders() {
		return providers;
	}

	public AbilitiesProvider getAbilitiesProvider() {
		return abilitiesProvider;
	}

	public EquipItemProvider getEquipmentsProvider() {
		return equipmentsProvider;
	}

	public CreaturesProvider<BaseCreatureRPG> getCreaturesProvider() {
		return creaturesProvider;
	}

//

	public void setAbilitiesProvider(AbilitiesProvider ap) {
		this.abilitiesProvider = ap;
	}

	public void setEquipmentsProvider(EquipItemProvider equipmentsProvider) {
		this.equipmentsProvider = equipmentsProvider;
	}

	public void setCreaturesProvider(CreaturesProvider<BaseCreatureRPG> creaturesProvider) {
		this.creaturesProvider = creaturesProvider;
	}

	//

	public AbilitiesProvider newAbilitiesProvider() {
		return new AbilitiesProvider();
	}

	public abstract EquipItemProvider newEquipItemProvider();

	/**
	 * Should return something based on {@link BaseCreatureRPG}.
	 */
	public abstract CreaturesProvider<BaseCreatureRPG> newCreatureProvider();
//	public abstract CreaturesProvider<? extends CreatureSimple> newCreatureProvider();
}