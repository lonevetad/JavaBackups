package games.generic.controlModel.enums;

import java.util.Map;

import games.generic.controlModel.subimpl.GObjectsHolderImpl;
import tools.Comparators;

public class EnumsManager extends GObjectsHolderImpl<String, EnumMacrotopic> {

	public EnumsManager() { super(Comparators.STRING_COMPARATOR, EnumMacrotopic::getName); }

	//

	public Map<String, EnumMacrotopic> getEnumsByName() { return this.objectsHeld; }

	public boolean containsByEnumName(String nameEnum) { return this.getEnumsByName().containsKey(nameEnum); }

	public EnumMacrotopic getByEnumName(String nameEnum) { return this.getEnumsByName().get(nameEnum); }
}