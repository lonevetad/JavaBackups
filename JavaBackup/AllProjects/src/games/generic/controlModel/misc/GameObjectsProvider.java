package games.generic.controlModel.misc;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import tools.Comparators;

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
 */
public class GameObjectsProvider<E extends ObjectNamed> {
	protected MapTreeAVL<String, FactoryObjGModalityBased<E>> objsByName;

	public GameObjectsProvider() {
		this.objsByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.STRING_COMPARATOR);
	}

	public void addObj(String name, FactoryObjGModalityBased<E> gm) {
		this.objsByName.put(name, gm);
	}

	public FactoryObjGModalityBased<E> getObjByName(String name) {
		return this.objsByName.get(name);
	}

	public E getNewObjByName(GModality gm, String name) {
		return this.objsByName.get(name).newInstance(gm);
	}

	public FactoryObjGModalityBased<E> getAtIndex(int index) {
		return this.objsByName.getAt(index).getValue();
	}

	public Map<String, FactoryObjGModalityBased<E>> getObjectsIdentified() {
		return this.objsByName;
	}

	public int getObjectsIdentifiedCount() {
		return this.objsByName.size();
	}

	public void removeALl() {
		this.objsByName.clear();
	}
}