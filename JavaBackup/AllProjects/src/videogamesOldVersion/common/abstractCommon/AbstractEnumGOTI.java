package videogamesOldVersion.common.abstractCommon;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.function.BiConsumer;

import videogamesOldVersion.common.EnumGameObjectTileImage;
import videogamesOldVersion.common.EnumGameObjectTileImageCollection;
import videogamesOldVersion.common.GameObjectInMap;
import videogamesOldVersion.common.gui.GameObjectInMapView;
import videogamesOldVersion.common.gui.TileImage;

/**
 * "GOTI" stands for "GameObjectTileImage"<br>
 * Simple interface emulating an enumeration of {@link AbstractEnumElementGOTI},
 * which can instantiate {@link TileImage}.<br>
 * This enum is meant to store and retrieve images (held by {@link TileImage})
 * associated with a {@link GameObjectInMap} to create a subcomponent of the
 * View part of a game (if that game use the MVC pattern) : an instance of
 * {@link GameObjectInMapView}.
 */
public interface AbstractEnumGOTI extends Iterable<AbstractEnumElementGOTI>, Serializable {

	public static AbstractEnumGOTI newDefaultInstance(String nameEnum) {
		return new EnumGameObjectTileImage(nameEnum);
	}

	public static AbstractEnumElementGOTI newDefaultElement() {
		return newDefaultElement(AbstractEnumElementGOTI.newDefaultShapeSpec());
	}

	public static AbstractEnumElementGOTI newDefaultElement(ShapeSpecification ss) {
		return AbstractEnumElementGOTI.newDefaultInstance().setShapeSpecification(ss);
	}

	//

	public String getNameEnum();

	/**
	 * Similar to {@link AbstractEnumGOTI#getAllElementsByImageName()}, but using a
	 * {@link Integer} key, obtained through
	 * {@link AbstractEnumElementGOTI#getID()}.
	 */
	public SortedMap<Integer, AbstractEnumElementGOTI> getAllElementsByID();

	/**
	 * All {@link AbstractEnumElementGOTI} are stored using a {@link String} key,
	 * obtained through {@link AbstractEnumElementGOTI#getName()}.<br>
	 * See the {@link EnumGameObjectTileImageCollection} documentation's note.
	 */
	public SortedMap<String, AbstractEnumElementGOTI> getAllElementsByImageName();

	/** Returns -1 in case of error. */
	public default int size() {
		// SortedMap<Integer, AbstractEnumElementGOTI> mi;
		SortedMap<String, AbstractEnumElementGOTI> ms;
		// mi = getAllElementsByID();
		ms = getAllElementsByImageName();
		return ms != null ? ms.size() : -1;
	}

	//

	/**
	 * Search a {@link TileImage}'s factory (more precisely, a object able to
	 * produce {@link TileImage}) using a {@link Integer} "ID" as key. This should
	 * be used over {@link #fetchElementGOTI(String)} because of the Integer
	 * comparer is faster than String comparer.
	 * <p>
	 * Have a look of the {@link EnumGameObjectTileImageCollection} documentation's
	 * note.
	 *
	 * @return: <code>null</code> if the entry is not present.
	 */
	public default AbstractEnumElementGOTI fetchElementGOTI(Integer id) {
		SortedMap<Integer, AbstractEnumElementGOTI> m;
		m = getAllElementsByID();
		return m == null ? null : m.get(id);
	}

	/**
	 * See {@link #fetchElementGOTI(Integer)}, but using the {@link String} image's
	 * name as key.<br>
	 * According to the {@link EnumGameObjectTileImageCollection} documentation's
	 * note, this method is preferred but it's slower.
	 */
	public default AbstractEnumElementGOTI fetchElementGOTI(String imageName) {
		SortedMap<String, AbstractEnumElementGOTI> m;
		m = getAllElementsByImageName();
		return m == null ? null : m.get(imageName);
	}

	/**
	 * Add the given {@link AbstractEnumElementGOTI} on this collection, through its
	 * {@link Integer} ID ({@link AbstractEnumElementGOTI#getID()}) AND its
	 * {@link String} Name ({@link AbstractEnumElementGOTI#getName()}).<br>
	 * Returns <code>true</code> if the given value was added in BOTH of the
	 * previous ways, false if the storing operation has failed.
	 */
	public default boolean store(AbstractEnumElementGOTI eetif) {
		boolean b;
		Integer id;
		String name;
		SortedMap<Integer, AbstractEnumElementGOTI> mi;
		SortedMap<String, AbstractEnumElementGOTI> ms;
		b = false;
		if (eetif != null) {
			mi = getAllElementsByID();
			ms = getAllElementsByImageName();
			if (mi != null && (id = eetif.getID()) != null //
					&& ms != null && (name = eetif.getName()) != null) {
				mi.put(id, eetif);
				ms.put(name, eetif);
				b = true;
			}
		}
		return b;
	}

	/**
	 * {@link #store(AbstractEnumElementGOTI)} all elements contained in the given
	 * collections
	 */
	public default AbstractEnumGOTI addAll(Iterable<AbstractEnumElementGOTI> groupOfEnumElement) {
		if (groupOfEnumElement != null) {
			for (AbstractEnumElementGOTI eetif : groupOfEnumElement)
				store(eetif);
		}
		return this;
	}

	public default <T extends Enum<T> & AbstractEnumElementGOTI> AbstractEnumGOTI addAll(T[] groupOfEnumElement) {
		int i, len;
		if (groupOfEnumElement != null && ((len = groupOfEnumElement.length) > 0)) {
			i = -1;
			while(++i < len)
				store(groupOfEnumElement[i]);
		}
		return this;
		// return this.addAll((Iterable<AbstractEnumElementTileImageFactory>)
		// groupOfEnumElement);
	}

	public default void forEachEnumElement(BiConsumer<String, AbstractEnumElementGOTI> toDo) {
		SortedMap<String, AbstractEnumElementGOTI> r;
		r = getAllElementsByImageName();
		if (toDo != null && r != null && (!r.isEmpty())) {
			r.forEach(toDo);
		}
	}

	//

	public default AbstractEnumElementGOTI fetchEnumElement(Integer id, String imageName) {
		AbstractEnumElementGOTI eetif;
		eetif = null;
		if (id != null)
			eetif = fetchElementGOTI(id);
		if (eetif == null && imageName != null)
			eetif = fetchElementGOTI(imageName);
		return eetif;
	}

	public default TileImage newTileImage(MainController main, Integer id) {
		return newTileImage(main, id, null);
	}

	public default TileImage newTileImage(MainController main, String imageName) {
		return newTileImage(main, null, imageName);
	}

	public default TileImage newTileImage(MainController main, Integer id, String imageName) {
		AbstractEnumElementGOTI eetif;
		eetif = fetchEnumElement(id, imageName);
		return (eetif == null) ? null : eetif.newTileImage(main);
	}

	public default GameObjectInMap newGameObjectInMap(MainController main, Integer id) {
		return newGameObjectInMap(main, id, null);
	}

	public default GameObjectInMap newGameObjectInMap(MainController main, String imageName) {
		return newGameObjectInMap(main, null, imageName);
	}

	public default GameObjectInMap newGameObjectInMap(MainController main, Integer id, String imageName) {
		AbstractEnumElementGOTI eetif;
		eetif = fetchEnumElement(id, imageName);
		return (eetif == null) ? null : eetif.newGameObjectInMap(main);
	}

}