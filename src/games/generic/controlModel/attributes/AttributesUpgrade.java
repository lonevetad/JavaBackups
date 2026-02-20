package games.generic.controlModel.attributes;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;

import dataStructures.minorUtils.SortedSetEnhancedDelegating;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EssenceStorage;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONObject;
import tools.json.types.JSONObject;

/**
 * Simply consists in a named collection of {@link AttributeModification}.<br>
 * They differs from them because they are part of a definition of an
 * {@link EquipmentItem} (talking about "static definition", like those provided
 * in a database-like) while this class is just an "upgrade" provided randomly
 * to increase (or decrease) the quality (and the value, maybe) of the equipment
 * having it. <br>
 * (Also, in some game this upgrade could be extracted in some kind of
 * potion-essence like {@link EssenceStorage} and applied to another equipment,
 * that is a very useful and cool feature).
 */
public interface AttributesUpgrade
		extends RarityHolder, ObjectNamed, SortedSetEnhancedDelegating<AttributeModification> {

	public static final String FIELD_ATTRIBUTE_MODIFIERS = "attributeModifiers";

	/** BEWARE: do not modify the set. */
	public SortedSet<AttributeModification> getAttributeModifiers();

	@Override
	public default SortedSet<AttributeModification> getDelegator() {
		return this.getAttributeModifiers();
	}

	@Override
	public default Comparator<AttributeModification> getKeyComparator() {
		return AttributeModification.COMPARATOR;
	}

	//

	public default AttributesUpgrade addAttributeModifier(AttributeModification am) {
		if (am == null) {
			return null;
		}
		getAttributeModifiers().add(am);
		return this;
	}

	/** Should call {@link #addAttributeModifier(AttributeModification)}. */
	public default AttributesUpgrade addAttributeModifier(AttributeIdentifier ai, int value) {
		return this.addAttributeModifier(new AttributeModification(ai, value));
	}

	//

	// JSON-related

	// TODO

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		ObjectNamed.super.toJSONValue(wrapper);
		RarityHolder.super.toJSONValue(wrapper);
		final JSONObject attributesJsoned = new JSONObject();
		this.getAttributeModifiers().forEach(am -> {
			JSONObject amJSONed = new JSONObject();
			am.toJSONValue(amJSONed);
			attributesJsoned.addField(am.getName(), amJSONed);
		});
		wrapper.addField(FIELD_ATTRIBUTE_MODIFIERS, attributesJsoned);
	}

	// TODO IL RESTO

	@Override
	public default void loadFromJSONObject(JSONObject wrapper) {
		ObjectNamed.super.loadFromJSONObject(wrapper);
		RarityHolder.super.loadFromJSONObject(wrapper);
		// index
		// get the field
		if (!wrapper.hasField(FIELD_ATTRIBUTE_MODIFIERS)) {
			this.raiseExceptionMissingField(FIELD_ATTRIBUTE_MODIFIERS, JSONTypes.Object);
		}
		JSONOValue jsonedAttributeModifiers_value = wrapper.getFieldValue(FIELD_ATTRIBUTE_MODIFIERS);
		// now de-serialize it
		if (!jsonedAttributeModifiers_value.isType(JSONTypes.Object)) {
			this.raiseExceptionIllegalTypeField(FIELD_ATTRIBUTE_MODIFIERS, JSONTypes.Object,
					jsonedAttributeModifiers_value);
		}
		JSONObject jsonedAttributeModifiers = (JSONObject) jsonedAttributeModifiers_value;
		jsonedAttributeModifiers.forEachField((name, jsonedAttributeModifier_value) -> {
			int value;
			AttributeModification am = new AttributeModification();
		});
		// TODO
	}

	@Override
	public default void loadFromJSONMap(Map<String, Object> jsonMap) {
		ObjectNamed.super.loadFromJSONMap(jsonMap);
		RarityHolder.super.loadFromJSONMap(jsonMap);
		//
	}
}