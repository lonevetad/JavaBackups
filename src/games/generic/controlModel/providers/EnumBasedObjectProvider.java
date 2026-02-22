package games.generic.controlModel.providers;

import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.IndexableObject;

public abstract class EnumBasedObjectProvider<E extends Enum<E> & IndexableObject> extends GameObjectsProvider<E> {
	public EnumBasedObjectProvider() {
		super();
	}

	public abstract E[] getEnumValues();

	@Override
	public void initialize() {
		super.initialize();
		// define the factories for all enums' values
		for (E e : this.getEnumValues()) {
			this.addObj(e.getName(), gm -> e);
		}
	}

}
