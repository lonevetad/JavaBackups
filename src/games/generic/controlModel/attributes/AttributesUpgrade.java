package games.generic.controlModel.attributes;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;

import dataStructures.minorUtils.SortedSetEnhancedDelegating;
import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EssenceStorage;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONInt;
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

	public static final String FIELD_ATTRIBUTE_MODIFIERS = "attributesModifiers";

	/** BEWARE: do not modify the set. */
	public SortedSet<AttributeModification> getAttributesModifiers();

	@Override
	public default SortedSet<AttributeModification> getDelegator() {
		return this.getAttributesModifiers();
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
		getAttributesModifiers().add(am);
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
		this.getAttributesModifiers().forEach(am -> {
			attributesJsoned.addField(am.getName(), am.toJSONValue());
		});
		wrapper.addField(FIELD_ATTRIBUTE_MODIFIERS, attributesJsoned);
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		ObjectNamed.super.loadFromJSONObject(gm, wrapper);
		RarityHolder.super.loadFromJSONObject(gm, wrapper);
		// attribute modifiers
		// get the field
		if (!wrapper.hasField(FIELD_ATTRIBUTE_MODIFIERS)) {
			this.raiseExceptionMissingField(FIELD_ATTRIBUTE_MODIFIERS, JSONTypes.Object);
		}
		JSONValue jsonedAttributesModifiers_value = wrapper.getFieldValue(FIELD_ATTRIBUTE_MODIFIERS);
		// now de-serialize it
		if (!jsonedAttributesModifiers_value.isType(JSONTypes.Object)) {
			this.raiseExceptionIllegalTypeField(FIELD_ATTRIBUTE_MODIFIERS, JSONTypes.Object,
					jsonedAttributesModifiers_value);
		}
		JSONObject jsonedAttributesModifiers = (JSONObject) jsonedAttributesModifiers_value;
		jsonedAttributesModifiers.forEachField((name, jsonedAttributeModifier_value) -> {
			int value;
			JSONObject jsonedAttributeModifier;
			// load the "attributeModifier" by-hand because it's probably an Enum instance
			if (!jsonedAttributeModifier_value.isType(JSONTypes.Object)) {
				this.raiseExceptionIllegalTypeField(FIELD_ATTRIBUTE_MODIFIERS + JSONable.SEPARATOR_FIELD + name,
						JSONTypes.Object, jsonedAttributesModifiers_value);
			}
			jsonedAttributeModifier = (JSONObject) jsonedAttributeModifier_value;
			if (!jsonedAttributeModifier.hasField("value")) {
				this.raiseExceptionMissingField(FIELD_ATTRIBUTE_MODIFIERS + JSONable.SEPARATOR_FIELD + name
						+ JSONable.SEPARATOR_FIELD + "value", null);
			}
			JSONValue valueJSONed_value = jsonedAttributeModifier.getFieldValue("value");
			if (!valueJSONed_value.isType(JSONTypes.Int)) {
				this.raiseExceptionIllegalTypeField(FIELD_ATTRIBUTE_MODIFIERS + JSONable.SEPARATOR_FIELD + name
						+ JSONable.SEPARATOR_FIELD + "value", JSONTypes.Int, valueJSONed_value);
			}
			JSONInt valueJSONed = (JSONInt) valueJSONed_value;
			value = valueJSONed.asInt();
			this.loadAttributeUpgrade(gm, name, value);

			this.getAttributesModifiers().add(new AttributeModification(gm.getFactoryByClassnameProvider()));

//			@Override
//			public void loadAttributeUpgrade(GModality gm, String attributeName, int value) {
//				this.getAttributesModifiers().add(new AttributeModification(AttributesTRAn.valueOf(attributeName), value));
//			}
		});
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		ObjectNamed.super.loadFromJSONMap(gm, jsonMap);
		RarityHolder.super.loadFromJSONMap(gm, jsonMap);
		//
		// get the field
		if (!jsonMap.containsKey(FIELD_ATTRIBUTE_MODIFIERS)) {
			this.raiseExceptionMissingField(FIELD_ATTRIBUTE_MODIFIERS, JSONTypes.Object);
		}
		Object jsonedAttributesModifiers_value = jsonMap.get(FIELD_ATTRIBUTE_MODIFIERS);
		// now de-serialize it
		if (!(jsonedAttributesModifiers_value instanceof Map<?, ?>)) {
			this.raiseExceptionIllegalTypeField(FIELD_ATTRIBUTE_MODIFIERS, JSONTypes.Object,
					jsonedAttributesModifiers_value);
		}
		Map<String, Object> jsonedAttributesModifiers = (Map<String, Object>) jsonedAttributesModifiers_value;
		jsonedAttributesModifiers.forEach((name, jsonedAttributeModifier_value) -> {
			int value;
			Map<String, Object> jsonedAttributeModifier;

			// load the "attributeModifier" by-hand because it's probably an Enum instance
			if (!(jsonedAttributeModifier_value instanceof Map<?, ?>)) {
				this.raiseExceptionIllegalTypeField(FIELD_ATTRIBUTE_MODIFIERS + JSONable.SEPARATOR_FIELD + name,
						JSONTypes.Object, jsonedAttributesModifiers_value);
			}
			jsonedAttributeModifier = (Map<String, Object>) jsonedAttributeModifier_value;
			if (!jsonedAttributeModifier.containsKey("value")) {
				this.raiseExceptionMissingField(FIELD_ATTRIBUTE_MODIFIERS + JSONable.SEPARATOR_FIELD + name
						+ JSONable.SEPARATOR_FIELD + "value", null);
			}
			Object valueJSONed_value = jsonedAttributeModifier.get("value");
			if (!(valueJSONed_value instanceof Integer)) {
				this.raiseExceptionIllegalTypeField(FIELD_ATTRIBUTE_MODIFIERS + JSONable.SEPARATOR_FIELD + name
						+ JSONable.SEPARATOR_FIELD + "value", JSONTypes.Int, valueJSONed_value);
			}
			Integer valueJSONed = (Integer) valueJSONed_value;
			value = valueJSONed;
			this.loadAttributeUpgrade(gm, name, value);
		});
	}
}