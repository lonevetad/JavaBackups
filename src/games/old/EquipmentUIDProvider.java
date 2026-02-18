package games.old;

import tools.UniqueIDProvider;

public class EquipmentUIDProvider implements UniqueIDProvider {
	private static final UniqueIDProvider singleton = UniqueIDProvider.newBasicIDProvider();

	public static Integer newID() {
		return singleton.getNewID();
	}

	protected final UniqueIDProvider.BaseUniqueIDProvider equipBase;

	public EquipmentUIDProvider() {
		equipBase = new BaseUniqueIDProvider();
	}

	@Override
	public Integer getNewID() {
		return equipBase.getNewID();
	}
}