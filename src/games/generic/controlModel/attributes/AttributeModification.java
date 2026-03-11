package games.generic.controlModel.attributes;

import java.util.Comparator;
import java.util.function.Function;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.AttributesHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.misc.AmountNamed;
import games.generic.controlModel.misc.CreatureAttributes;
import tools.Comparators;
import tools.ObjectNamedID;

/**
 * Each {@link EquipmentItem} could modify the {@link AttributesHolder}'s
 * attributes (i.e. {@link CreatureAttributes}) through applying this class's
 * instances by invoking
 * {@link CreatureAttributes#applyAttributeModifier(EquipmentAttributeModifier)}.
 */
public class AttributeModification extends AmountNamed {
	private static final long serialVersionUID = -88782140147L;
	public static final Function<AttributeModification, String> KEY_EXTRACTOR = eu -> eu.getAttributeModified()
			.getName();
	public static final Comparator<AttributeModification> COMPARATOR = (am1, am2) -> {
		if (am1 == am2) {
			return 0;
		}
		if (am1 == null) {
			return -1;
		}
		if (am2 == null) {
			return 1;
		}
		return Comparators.STRING_COMPARATOR.compare(KEY_EXTRACTOR.apply(am1), KEY_EXTRACTOR.apply(am2));
	};

	public static <AM extends AttributeModification> AttributeModification[] newEmptyArray(
			AttributeIdentifier[] attributesModified, Function<AttributeIdentifier, AM> attrModConstructor) {
		int n;
		AttributeModification[] r;
		r = new AttributeModification[n = attributesModified.length];
		while (--n >= 0) {
			r[n] = attrModConstructor.apply(attributesModified[n]);
		}
		return r;
	}

	//

	public AttributeModification(AttributeIdentifier attributeModified, int value) {
		super(attributeModified, value);
	}

	public AttributeIdentifier getAttributeModified() {
		return (AttributeIdentifier) super.type;
	}

	public void setAttributeModified(AttributeIdentifier attributeModified) {
		super.setType(attributeModified);
	}

	@Override
	public void setType(ObjectNamedID type) {
		if (!(type instanceof AttributeIdentifier)) {
			throw new IllegalArgumentException("Can't set the Attribute through an instance of "
					+ (type == null ? "null" : type.getClass().getName()) + "\n\t-: " + type.toString());
		}
		super.setType(type);
	}

	@Override
	public String toString() {
		return "AttributeModification: (attr=" + getAttributeModified() + "; value=" + value + ")";
	}

	@Override
	protected void loadInnerObjectNamedID(GModality gm, String name) {
		throw new UnsupportedOperationException(
				"Method not implemented in a general game: override in the game-specific environment");
	}
}