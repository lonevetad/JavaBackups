package games.generic.controlModel.items;
 
import games.generic.controlModel.misc.IndexableObject;
import tools.Comparators;
import tools.Comparators.MyComparator;

/**
 * A marker-interface for {@link IEquipmentUpgrade} to define the category that Upgrade belongs to.
 * This interface might be incorporated in an {@link Enum}-
 * */
public interface IEquipmentUpgradeCategory extends IndexableObject {
	public static final MyComparator<IEquipmentUpgradeCategory> COMPARATOR_IEQUIPMENT_UPGRADE_CATEGORY = (ec1,ec2) -> {
		if (ec1 == ec2) { return 0; }
		if (ec1 == null) { return -1; }
		if (ec2 == null) { return 1; }		
		return Comparators.STRING_COMPARATOR.compare(ec1.getName(), ec2.getName());
	};
}
