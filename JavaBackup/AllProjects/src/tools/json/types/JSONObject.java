package tools.json.types;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import tools.Comparators;
import tools.json.JSONTypes;
import tools.json.JSONValue;

public class JSONObject extends JSONValue {
	private static final long serialVersionUID = -5641554230425406L;
	protected Map<String, JSONValue> fields = null;

	@Override
	public JSONTypes getType() { return JSONTypes.Object; }

	public int getFieldsAmount() { return (this.fields == null) ? 0 : this.fields.size(); }

	public int size() { return this.getFieldsAmount(); }

	public boolean hasField(String name) { return (this.fields != null) && this.fields.containsKey(name); }

	public void addField(String name, JSONValue v) {
		if (this.fields == null) { this.fields = MapTreeAVL.newMap(Comparators.STRING_COMPARATOR); }
		this.fields.put(name, v);
	}

	/**
	 * Returns the value if the item exists, {@code null} otherwise.
	 */
	public JSONValue getFieldValue(String name) { return this.hasField(name) ? this.fields.get(name) : null; }

	public void forEachField(BiConsumer<? super String, ? super JSONValue> action) {
		if (this.fields == null || this.fields.isEmpty()) { return; }
		this.fields.forEach(action);
	}

	@Override
	public Object asObject() { return java.util.Collections.unmodifiableMap(this.fields); }

	@Override
	public void toString(StringBuilder sb) {
		boolean[] isNotFirst = { false };
		sb.append('{');
		if (this.fields != null && (!this.fields.isEmpty())) {
			this.forEachField((name, val) -> {
				if (isNotFirst[0]) {
					sb.append(',');
				} else {
					isNotFirst[0] = true;
				}
				sb.append('\"').append(name).append('\"').append(':');
				val.toString(sb);
			});
		}
		sb.append('}');
	}

	public <E> E toClassInstance(Class<E> clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		E instance;
		instance = clazz.getDeclaredConstructor().newInstance();
		this.forEachField((fieldName, fieldValue) -> {
			try {
				Field field = instance.getClass().getField(fieldName);
				switch (fieldValue.getType()) {
				case Boolean: {
					field.setBoolean(instance, fieldValue.asBoolean());
					break;
				}
				case Int: {
					field.setInt(instance, fieldValue.asInt());
					break;
				}
				case Long: {
					field.setLong(instance, fieldValue.asLong());
					break;
				}
				case Double: {
					field.setDouble(instance, fieldValue.asDouble());
					break;
				}
				case String: {
					field.set(instance, fieldValue.asString());
					break;
				}
				case ArrayMiscTypes: {
					field.set(instance, fieldValue.asArrayObject());
					break;
				}
				case ArrayHomogeneousType: {
					JSONArray fieldValueAsArray = (JSONArray) fieldValue;
					switch (fieldValueAsArray.elementsTypes) {
					case Boolean: {
						field.set(instance, fieldValueAsArray.asArrayBoolean());
						break;
					}
					case Int: {
						field.set(instance, fieldValueAsArray.asArrayInt());
						break;
					}
					case Long: {
						field.set(instance, fieldValueAsArray.asArrayLong());
						break;
					}
					case Double: {
						field.set(instance, fieldValueAsArray.asArrayDouble());
						break;
					}
					case String: {
						field.set(instance, fieldValueAsArray.asArrayString());
						break;
					}
					case ArrayMiscTypes:
					case ArrayHomogeneousType: {
						field.set(instance, fieldValueAsArray.asArrayObject());
						break;
					}
					default:
						throw new IllegalArgumentException(
								"Unexpected array homogeneous type upon setting it for the field: " + fieldName);
					}
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected field type: " + fieldValue.getType().name());
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		});
		return instance;
	}
}