package games.generic.controlModel.misc;

import java.util.Map;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.objects.creature.CreatureSimple;
import tools.Comparators;
import tools.WeightedSetOfRandomOutcomes;

/**
 * Defines a "database-like" holder of a specific hierarchy of game objects (all
 * of them implementing {@link ObjectNamed}), so that their definition can be
 * stored and new instances can be allocated when needed.<br>
 * For this purpose, taking inspiration from the Factory Pattern, those game
 * objects' definitions are provided using instances of
 * {@link FactoryObjGModalityBased}. Important to be noted: those factories
 * requires an instance of {@link GModality} as a parameter, because it could be
 * require, but it's NOT mandatory.<br>
 * Examples of classes of objects that could be defined here:
 * <ul>
 * <li>Creatures and other characters to be put into the game map (i.e.:
 * {@link CreatureSimple})</li>
 * <li>Equipment: {@link EquipmentItem}</li>
 * <li>abilities (no distincion from equipments ones and skills:
 * {@link AbilityGeneric})</li>
 * <li>Tiles for maps</li>
 * <li>Maps</li>
 * </ul>
 * Randomness can be manipulated through {@link WeightedSetOfRandomOutcomes}.
 */
public class GameObjectsProvider<E extends ObjectNamed> {
	protected final MapTreeAVL<String, FactoryObjGModalityBased<E>> objsByName;

	public GameObjectsProvider() {
		this.objsByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.STRING_COMPARATOR);
	}

	public void addObj(String name, FactoryObjGModalityBased<E> gm) {
//		System.out.println("\n\n------------original name: " + name + " in class: " + this.getClass().getSimpleName());
		this.objsByName.put(name, gm);
		/*
		 * if (objsByName.get(name) == null) {
		 * System.err.println("WTF not present after putting?"); System.exit(1); } else
		 * { System.out.println("sucessfully added for ++" + name + "--: " +
		 * objsByName.get(name)); }
		 */
	}

	/**
	 * Added as a workaround, it just calls
	 * {@link #addObj(String, FactoryObjGModalityBased)}.
	 */
	public void addObj(String name, int rarityIndex, FactoryObjGModalityBased<E> gm) { this.addObj(name, gm); }

	public FactoryObjGModalityBased<E> getObjByName(String name) { return this.objsByName.get(name); }

	public E getNewObjByName(GModality gm, String name) {
		FactoryObjGModalityBased<E> e;
		e = this.objsByName.get(name);
		return e == null ? null : e.newInstance(gm);
	}

	public FactoryObjGModalityBased<E> getAtIndex(int index) { return this.objsByName.getAt(index).getValue(); }

	/**
	 * Returns a {@link Map} that holds every stored object, which are identified by
	 * their name.
	 */
	public Map<String, FactoryObjGModalityBased<E>> getObjectsIdentified() { return this.objsByName; }

	public void forEachFactory(BiConsumer<String, FactoryObjGModalityBased<E>> action) {
		this.objsByName.forEach(action);
	}

	public int getObjectsFactoriesCount() { return this.objsByName.size(); }

	public void removeAll() { this.objsByName.clear(); }
}