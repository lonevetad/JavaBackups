package tools.json.OLD;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONArray;
import tools.json.types.JSONBoolean;
import tools.json.types.JSONDouble;
import tools.json.types.JSONInt;
import tools.json.types.JSONLong;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * Parse a JSON String into an object
 */
public class JSONParser_V1 {

	/**
	  */
	public static JSONValue parse(String input) {
		Objects.requireNonNull(input);
		if ((input = input.trim()).length() == 0) { return new JSONObject(); }

		// TODO PARSING
		int[] indexLineColumn = { 0, 0, 0 };
		/**
		 * holds:
		 * <ol>
		 * <li>index in input</li>
		 * <li>line count (starts at 0)</li>
		 * <li>column count (starts at 0)</li>
		 */
		jumpWhitespaces(input, indexLineColumn);
		try {
			return parseValue(input, indexLineColumn);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"Unexpected end of input at line " + indexLineColumn[1] + " column " + indexLineColumn[2]);
		}
	}

	protected static void checkRightToken(String input, int[] indexLineColumn, char c) {
		if (input.charAt(indexLineColumn[0]) != c) {
			throw new IllegalArgumentException("Wrong character '" + input.charAt(indexLineColumn[0]) + "' at line "
					+ indexLineColumn[1] + ", column " + indexLineColumn[2] + ", expected: --" + c + "--");
		}
	}

	protected static boolean isWhitespace(char c) {
		return (8 <= c // backspace, horizontal tab, new line, vert. tab, new page,
				&& c <= 15) // carriage return, shift out, shift in
				|| c == ' ' || c == '\n' || c == '\r' || c == '\t' //
				|| Character.isWhitespace(c);
	}

	protected static void jumpWhitespaces(String input, int[] indexLineColumn) {
		boolean canJump;
		char c;
		int index = indexLineColumn[0], len = input.length();
		if (index >= len) { return; }
		do {
			c = input.charAt(index);
			if (c == '\n' || c == '\r' || c == 13) {
				indexLineColumn[1]++;
				indexLineColumn[2] = 0;
			}
			canJump = ++index < len && isWhitespace(c);
			if (canJump) {
				indexLineColumn[2]++;
			} else {
				index--;
			}
		} while (canJump);
		indexLineColumn[0] = index;
	}

	protected static String extractString(String input, int[] indexLineColumn) {
		int end, len;
		String text;
		len = input.length();
		end = indexLineColumn[0];
		while (++end < len && (input.charAt(end)) != '\"') //
			// && (!isWhitespace(c)))
			;
		text = input.substring(indexLineColumn[0], end);
		indexLineColumn[2] += end - indexLineColumn[0];
		indexLineColumn[0] = end;
		jumpWhitespaces(input, indexLineColumn);
		return text;
	}

	/**
	 * Parse the next value and delegates to the specific parser token. <br>
	 * It assumes that the "index" parameter points to a non-whitespace character.
	 */
	protected static JSONValue parseValue(String input, int[] indexLineColumn) {
		char c;
		JSONValue val;
		val = null;
		c = input.charAt(indexLineColumn[0]);
		if (c == '{') {
			indexLineColumn[0]++;
			indexLineColumn[2]++;
			jumpWhitespaces(input, indexLineColumn);
			val = parseObj(input, indexLineColumn);
			checkRightToken(input, indexLineColumn, '}');
			indexLineColumn[0]++;
			indexLineColumn[2]++;
			jumpWhitespaces(input, indexLineColumn);
		} else if (c == '[') {
			indexLineColumn[0]++;
			indexLineColumn[2]++;
			jumpWhitespaces(input, indexLineColumn);
			val = parseArray(input, indexLineColumn);
			// try to see if the next token is ']'
			checkRightToken(input, indexLineColumn, ']');
			// skip that character
			indexLineColumn[0]++;
			indexLineColumn[2]++;
			jumpWhitespaces(input, indexLineColumn);
		} else if (c == '\"') {
			indexLineColumn[0]++;
			indexLineColumn[2]++;
			jumpWhitespaces(input, indexLineColumn);
			val = parseString(input, indexLineColumn);
			// try to see if the next token is '}'
			checkRightToken(input, indexLineColumn, '\"');
			// skip that character
			indexLineColumn[0]++;
			indexLineColumn[2]++;
			jumpWhitespaces(input, indexLineColumn);
		} else if (c == '+' || c == '-' || Character.isDigit(c) || c == '.') {
			val = parseNumber(input, indexLineColumn);
		}
		if (val != null) { return val; }

		val = parseBoolean(input, indexLineColumn);
		if (val != null) { return val; }

		// simple string
		val = parseString(input, indexLineColumn);

		return val;
	}

	protected static JSONValue parseBoolean(String input, int[] indexLineColumn) {
		int index = indexLineColumn[0];
		char c = input.charAt(index);
		if (c == 't' || c == 'T') {
			c = input.charAt(index + 1);
			if (c == 'r' || c == 'R') {
				c = input.charAt(index + 2);
				if (c == 'u' || c == 'U') {
					c = input.charAt(index + 3);
					if (c == 'e' || c == 'E') {
						indexLineColumn[0] += 4;
						indexLineColumn[2] += 4;
						return new JSONBoolean(true);
					}
				}
			}
		} else if (c == 'f' || c == 'F') {
			c = input.charAt(index + 1);
			if (c == 'a' || c == 'A') {
				c = input.charAt(index + 2);
				if (c == 'l' || c == 'L') {
					c = input.charAt(index + 3);
					if (c == 's' || c == 'S') {
						c = input.charAt(index + 4);
						if (c == 'e' || c == 'E') {
							indexLineColumn[0] += 5;
							indexLineColumn[2] += 5;
							return new JSONBoolean(false);
						}
					}
				}
			}
		}
		return null;
	}

	protected static JSONValue parseString(String input, int[] indexLineColumn) {
		boolean hasHypens;
		JSONValue stringVal;
		if (hasHypens = input.charAt(indexLineColumn[0]) == '\"') {
			indexLineColumn[0]++;
			indexLineColumn[2]++;
		}
		stringVal = new JSONString(extractString(input, indexLineColumn));
		if (hasHypens) {
			checkRightToken(input, indexLineColumn, '\"');
			indexLineColumn[0]++;
			indexLineColumn[2]++;
		}
		return stringVal;
	}

	protected static JSONValue parseNumber(String input, int[] indexLineColumn) {
		boolean hasExp, hasDot, canContinue, hasError;
		char c;
		int start, end, len;
		len = input.length();
		start = indexLineColumn[0];
		end = start;
		c = input.charAt(start);
		if (c == '-' || c == '+') { end++; }
		canContinue = true;
		hasError = hasExp = hasDot = false;
		do {
			c = input.charAt(end);
			if (Character.isDigit(c)) {
				canContinue = true;
				end++;
			} else if (c == '.') {
				canContinue = !hasDot;
				hasError = hasDot;
				hasDot = true;
				if (canContinue) { end++; }
			} else if (c == 'e' || c == 'E') {
				canContinue = !hasExp;
				hasError = hasExp;
				hasExp = true;
				if (canContinue) { end++; }
				c = input.charAt(end);
				if (c == '-' || c == '+') { end++; }
			} else {
				canContinue = false;
				// check for next probable tokens
				if (c != ',' && c != ']' && c != '}' && (!isWhitespace(c))) { hasError = true; }
			}
		} while (canContinue && end < len);

		int charsConsumed = end - start;
		indexLineColumn[0] += charsConsumed;
		indexLineColumn[2] += charsConsumed;
		if (hasError) {
			throw new IllegalArgumentException("Wrong number format at line " + indexLineColumn[1] + ", column "
					+ indexLineColumn[2] + ":\n\t at start(" + start + "), end (" + end + ") we have text:"
					+ input.substring(start, end));
		}

		String numText = input.substring(start, end);
		try {
			long value = Long.parseLong(numText);
			if (value <= (Integer.MAX_VALUE) || value >= (Integer.MIN_VALUE)) {
				return new JSONInt((int) value);
			} else {
				return new JSONLong(value);
			}
		} catch (Exception e) {
			try {
				double value = Double.parseDouble(numText);
				return new JSONDouble(value);
			} catch (Exception e2) {
				e2.printStackTrace();
				throw e2;
			}
		}
	}

	protected static JSONValue parseObj(String input, int[] indexLineColumn) {
		boolean canGo;
		char c;
		JSONObject obj;
		JSONValue fVal, field;
		obj = new JSONObject();
		c = input.charAt(indexLineColumn[0]);
		canGo = c != '}';
		while (canGo) {
			jumpWhitespaces(input, indexLineColumn);
			field = parseString(input, indexLineColumn);
			jumpWhitespaces(input, indexLineColumn);
			checkRightToken(input, indexLineColumn, ':');
			indexLineColumn[0]++;
			indexLineColumn[2]++;

			jumpWhitespaces(input, indexLineColumn);
			fVal = parseValue(input, indexLineColumn);
			obj.addField(field.asString(), fVal);

			jumpWhitespaces(input, indexLineColumn);
			c = input.charAt(indexLineColumn[0]);

			if (c == ',') {
				indexLineColumn[0]++;
				indexLineColumn[2]++;
				jumpWhitespaces(input, indexLineColumn);
				c = input.charAt(indexLineColumn[0]);
				canGo = c != '}';
			} else {
				canGo = false;
			}
		}
		return obj;
	}

	protected static JSONValue parseArray(String input, int[] indexLineColumn) {
		boolean canContinue, arrayOk;
		char c;
		JSONValue e = null;
		JSONTypes firstElementType, arrayType;
		List<JSONValue> values;
		firstElementType = JSONTypes.Object;
		arrayType = JSONTypes.ArrayHomogeneousType;
		values = new LinkedList<>();
		c = input.charAt(indexLineColumn[0]);
		arrayOk = true; // hypotesis: empty array is always ok
		canContinue = c != ']';
		while (canContinue) {
			jumpWhitespaces(input, indexLineColumn);
			c = input.charAt(indexLineColumn[0]);

			if (c != ']') {
				e = parseValue(input, indexLineColumn);
				if (e == null) {
					arrayOk = false;
				} else {
					if (values.isEmpty()) {
						firstElementType = e.getType();
					} else if (firstElementType != e.getType()) { arrayType = JSONTypes.ArrayMiscTypes; }
					values.add(e);

					jumpWhitespaces(input, indexLineColumn);
					c = input.charAt(indexLineColumn[0]);

					if (c == ',') {
						canContinue = true;
						indexLineColumn[0]++;
						indexLineColumn[2]++;
						jumpWhitespaces(input, indexLineColumn);
					}

					if (c == ']') {
						canContinue = false;
						arrayOk = true;
					}
				}
			} else {
				canContinue = false;
				arrayOk = true;
			}
		}
		if (arrayOk) {
			// creation of the array
			if (values.size() == 0) {
				e = new JSONArray(JSONTypes.ArrayHomogeneousType, new JSONValue[0], JSONTypes.Object);
			} else {
				e = new JSONArray(arrayType, values.toArray(new JSONValue[values.size()]), firstElementType);
			}
		}
		return e;
	}
}