package tools;

import java.util.Map;

public interface JSONable extends tools.Stringable {

	public String toJSON(StringBuilder sb, int tabLevel);
	public default String toJSON(int tabLevel) {
		StringBuilder sb = new StringBuilder();
		toJSON(sb, tabLevel);
		return sb.toString();
	}

	public void loadFromJSON(String jsonString);

	//

	public Map<String, Object> toJSONMap();

	public void loadFromJSONMap(Map<String, Object> jsonMap);
}