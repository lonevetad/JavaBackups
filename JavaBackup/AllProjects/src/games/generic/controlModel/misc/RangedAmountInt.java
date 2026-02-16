package games.generic.controlModel.misc;

import java.util.Map;

import dataStructures.MapTreeAVL;
import tools.Comparators;
import tools.JSONable;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONInt;


public class RangedAmountInt implements JSONable {
	private static final long serialVersionUID = 245247402524100085L;

	protected int min, max, current;
	
	public RangedAmountInt(int min, int max) {
		super();
		this.min = min;
		this.max = max;
		if(min > max) {
			throw new IllegalArgumentException("The minimum amount (" + min + ") is lower than the maximum (" + max + ").");
		}
		this.current = min;
	}
	public RangedAmountInt(int max) {
		this(0, max);
	}

	//

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public int getCurrent() {
		return current;
	}
	
	//

	public void setMin(int min) {
		this.min = min;
		if(this.max < min) {
			this.setMax(min);
		}
		if(this.current < min) {
			this.setCurrent(min);
		}
	}

	public void setMax(int max) {
		this.max = max;
		if(this.min > max) {
			this.setMin(max);
		}
	}

	public void setCurrent(int current) {
		if(current < this.getMin()) {
			throw new IllegalArgumentException("Given current (" + current + ") is lower than the minimum (" + this.getMin() + ")");
		}
		if(current > this.getMax()) {
			throw new IllegalArgumentException("Given current (" + current + ") is greater than the maximum (" + this.getMax() + ")");
		}
		this.current = current;
	}
	@Override
	public String toJSON(StringBuilder sb, int tabLevel) {
		sb.append("{\n");
		addTab(sb, tabLevel + 1);
		sb.append("\"min\": ").append(this.min).append(",\n");
		addTab(sb, tabLevel + 1);
		sb.append("\"max\": ").append(this.max).append(",\n");
		addTab(sb, tabLevel + 1);
		sb.append("\"current\": ").append(this.current).append("\n");
		addTab(sb, tabLevel);
		sb.append("}");
		return sb.toString();
	}
	@Override
	public void loadFromJSON(String jsonString) {
		JSONValue read = tools.json.JSONParser.parse(jsonString);
		if(read instanceof tools.json.types.JSONObject json) {
			JSONValue maybeMin = json.getFieldValue("min");
			if(maybeMin.isType(JSONTypes.Int)) {
				this.setMin(((JSONInt) maybeMin).asInt());
			} else {
				throw new IllegalArgumentException("Invalid JSON value for field 'min' in RangedAmountInt: " + maybeMin);
			}
			JSONValue maybeMax = json.getFieldValue("max");
			if(maybeMax.isType(JSONTypes.Int)) {
				this.setMax(((JSONInt) maybeMax).asInt());
			} else {
				throw new IllegalArgumentException("Invalid JSON value for field 'max' in RangedAmountInt: " + maybeMax);
			}
			if(json.hasField("current")) {
				JSONValue maybeCurrent = json.getFieldValue("current");
				if(maybeCurrent.isType(JSONTypes.Int)) {
					this.setCurrent(((JSONInt) maybeCurrent).asInt());
				} else {
					throw new IllegalArgumentException("Invalid JSON value for field 'current' in RangedAmountInt: " + maybeCurrent);
				}
			} else {
				this.setCurrent(this.getMin());
			}
		} else {
			throw new IllegalArgumentException("Invalid JSON string for RangedAmountInt (not a JSONObject): " + jsonString);
		}
	}

	@Override
	public Map<String, Object> toJSONMap() {
		Map<String, Object> map =  MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
		map.put("min", this.getMin());
		map.put("max", this.getMax());
		map.put("current", this.getCurrent());
		return map;
	}

	@Override
	public void loadFromJSONMap(Map<String, Object> jsonMap) {
		if(jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}
		if(!jsonMap.containsKey("min") || !(jsonMap.get("min") instanceof Integer)) {
			throw new IllegalArgumentException("Invalid JSON map for RangedAmountInt: missing or invalid 'min' field");
		}
		if(!jsonMap.containsKey("max") || !(jsonMap.get("max") instanceof Integer)) {
			throw new IllegalArgumentException("Invalid JSON map for RangedAmountInt: missing or invalid 'max' field");
		}
		this.setMin((Integer) jsonMap.get("min"));
		this.setMax((Integer) jsonMap.get("max"));
		if(jsonMap.containsKey("current")) {
			if(!(jsonMap.get("current") instanceof Integer)) {
				try{
					throw new IllegalArgumentException("Invalid JSON map for RangedAmountInt: invalid 'current' field");
				} finally {
					this.setCurrent(this.getMin());
				}
			}
			this.setCurrent((Integer) jsonMap.get("current"));
		} else {
			this.setCurrent(this.getMin());
		}
	}	
}
