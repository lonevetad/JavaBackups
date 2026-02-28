package tests.tUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestExpr {
	public static enum Operator {
		AND, OR, NOT, EXPR, TERMINAL;
	}

	static List<Character> union(Character[]... charss) {
		List<Character> l = new java.util.ArrayList<>();
		for (Character[] chars : charss) {
			for (Character c : chars) {
				l.add(c);
			}
		}
		return l;
	}

	static final Character[] OPERATORS = new Character[] { '.', '|', '-' };
	static final Character[] WHITE_CHARS = new Character[] { ' ', '\t', '\n' };
	static final Set<Character> WHITE_CHARS_SET = new HashSet<>(List.of(WHITE_CHARS));
	static final Set<Character> UNALLOWED_CHARS_TERMINAL = new HashSet<>(
			union(OPERATORS, WHITE_CHARS, new Character[] { '(', ')', ',' }//
			));
	static final String[] expressions = { "a", "a.b", // and
			"a|b", // or
			"-a", // not
			"--a", // not not
			"-(-a)", // not expr not
			"(a)", // expression
			"-(a)", // not expression
			"-(a.b)", // not and
			"-(a|b)", // not or
			"a.b.c", // and and
			"a|b|c", // or or
			"a.-b", // and not
			"a|-b", // or not
			"a.b|c", // order precedence
			"a|b.c", //
			"a.b.c|d.e.f", //
			"a.b|c.d|e.f", //
			"a.b.g|c.d.h|e.f.i", //
			"a.(b|c)", //
			"a|b.-c", //
			"a|b.(-c|-d)", //
			"(a|b).(c|-d)", //
			"a.b|c|d|-e|f|(g|h)|---i.-j|k", //
			"(a.b)|(c.d)", //
			"(a.b)|(c.d).(-e|f)", //
			"(a.b)|-(-c.d)|(-e.f)", //
			"(a.-b).(c.d)|(------------e.-f|g)", //
			"(a.-b).(c.d)|(------------e.-f|g).h"

	};

	public static void main(String[] args) {
		String folderPath;
		// folderPath = "C:\\Users\\marco\\";
		folderPath = "C:\\Users\\ottin\\";
		try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
				new java.io.FileWriter(folderPath + "Desktop\\prog\\Java\\JavaBackups\\src\\tests\\tUtils\\te.txt"))) {
			System.out.println("START");
			for (String s : expressions) {
				String startText = "\n\nParsing expression: " + s;
				System.out.println(startText);
				writer.write(startText);
				writer.newLine();
				writer.flush();
				try {
					ParserNode node = parse(s);
					writer.write(node.toString());
					writer.newLine();
					writer.flush();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					writer.write(e.getMessage());
					writer.newLine();
					writer.flush();
					// System.out.println("Error parsing expression: " + s + " - " +
					// e.getMessage());
				}
			}
			System.out.println("END");
		} catch (java.io.IOException ex) {
			ex.printStackTrace();
		}
	}

	//

	static ParserNode parse(String expression) { // START
		int[] index = { 0 }; // to allow for modification inside parseExpr
		return parseOr(expression.trim(), index, 0);
	}

	static void skipWhiteChars(String expression, int[] index) {
		int len = expression.length();
		while (index[0] < len && WHITE_CHARS_SET.contains(expression.charAt(index[0]))) {
			index[0]++;
		}
	}

	static ParserNode parseOr(String expression, int[] index, int depth) {
		return parseOr(null, expression, index, depth);
	}

	static ParserNode parseOr(ParserNode left, String expression, int[] index, int depth) {
		// int startIndex = index[0];
		if (left == null) {
			left = parseAnd(expression, index, depth);
			skipWhiteChars(expression, index);
			if (index[0] >= expression.length() || expression.charAt(index[0]) != '|') {
				return left;
			}
		}
		left.increaseDepth();
		index[0]++; // consume the OR
		skipWhiteChars(expression, index);
		ParserNode orNode = new ParserNode(null, Operator.OR, depth);
		orNode.left = left;
		orNode.right = parseOr(null, expression, index, depth + 1);
		return orNode;
	}

	static ParserNode parseAnd(String expression, int[] index, int depth) {
		return parseAnd(null, expression, index, depth);
	}

	static ParserNode parseAnd(ParserNode left, String expression, int[] index, int depth) {
		// int startIndex = index[0];
		if (left == null) {
			left = parseExpr(expression, index, depth);
			skipWhiteChars(expression, index);
			if (index[0] >= expression.length() || expression.charAt(index[0]) != '.') {
				return left;
			}
		}
		left.increaseDepth();
		index[0]++; // consume the AND
		skipWhiteChars(expression, index);
		ParserNode andNode = new ParserNode(null, Operator.AND, depth);
		andNode.left = left;
		andNode.right = parseAnd(null, expression, index, depth + 1); // Expr
		return andNode;
	}

	static ParserNode parseExpr(String expression, int[] index, int depth) { // TODO: MOVE IT TO THE LAST, JUST BEFORE
																				// THE TERMINAL
		if (expression.charAt(index[0]) == '(') {
			// int originalIndex = index[0];c
			index[0]++;
			skipWhiteChars(expression, index);
			ParserNode expr = new ParserNode(null, Operator.EXPR, depth);
			ParserNode result = parseOr(expression, index, depth + 1); // restart the tree evaluation, but deeper
			skipWhiteChars(expression, index);
			if (index[0] >= expression.length() || expression.charAt(index[0]) != ')') {
				throw new IllegalArgumentException("Expected ')' at index " + index[0] + ", but got :'"
						+ (index[0] >= expression.length() ? "EOF" : expression.charAt(index[0])) + "' in expression: "
						+ expression);
			}
			index[0]++; // consume the closing parenthesis
			skipWhiteChars(expression, index);
			expr.left = result;
			// is this part of a sub-expression?
			/*
			 * if (index[0] < expression.length()) { if (expression.charAt(index[0]) == '.')
			 * { ParserNode currentExpr = expr; expr = parseAnd(expr, expression, index,
			 * depth); // was absorbed by a OR? ParserNode orExpr = expr; ParserNode prev =
			 * expr; while (expr.operator == Operator.OR) { prev = expr; expr = expr.left; }
			 * expr.left = currentExpr; return orExpr; } else if
			 * (expression.charAt(index[0]) == '|') { expr = parseOr(expr, expression,
			 * index, depth); } }
			 */
			return expr;
		}
		return parseNot(expression, index, depth);
	}

	static ParserNode parseNot(String expression, int[] index, int depth) {
		if (expression.charAt(index[0]) == '-') {
			index[0]++;
			skipWhiteChars(expression, index);
			ParserNode notNode = new ParserNode(null, Operator.NOT, depth);
			// only terminals and expressions can be negated
			notNode.left = parseExpr(expression, index, depth + 1);
			return notNode;
		}
		return parseTerminal(expression, index, depth);
	}

	static ParserNode parseTerminal(String expression, int[] index, int depth) {
		int startIndex = index[0];
		skipWhiteChars(expression, index);
		if (index[0] >= expression.length()) {
			throw new RuntimeException("EOF");
		}
		if (expression.charAt(index[0]) == '(') {
			return parseExpr(expression, index, depth);
		}
		while (index[0] < expression.length() && (!UNALLOWED_CHARS_TERMINAL.contains(expression.charAt(index[0])))) {
			index[0]++;
		}
		// index[0]--;
		if (startIndex >= index[0]) {
			throw new IllegalArgumentException(
					"Expected terminal at index " + startIndex + " in expression: " + expression);
		}
		String terminalText = expression.substring(startIndex, index[0]);
		System.out.println("terminalText: " + terminalText);
		ParserNode terminal = new ParserNode(terminalText, Operator.TERMINAL, depth);
		return terminal;
	}

	//

	public static class ParserNode {
		int depth;
		String value;
		Operator operator;
		ParserNode left;
		ParserNode right;

		public ParserNode(String value, Operator operator, int depth) {
			this.value = value;
			this.operator = operator;
			this.depth = depth;
		}

		public void increaseDepth() {
			this.depth++;
			if (this.left != null) {
				this.left.increaseDepth();
			}
			if (this.right != null) {
				this.right.increaseDepth();
			}
		}

		static void addTabs(StringBuilder sb, int d) {
			while (d-- > 0) {
				sb.append("  ");
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			toString(sb);
			return sb.toString();
		}

		public void toString(StringBuilder sb) {
			addTabs(sb, depth);
			switch (operator) {
			case AND:
				sb.append("AND (").append(this.depth).append(")\n");
				this.left.toString(sb);
				this.right.toString(sb);
				break;
			case OR:
				sb.append("OR (").append(this.depth).append(")\n");
				this.left.toString(sb);
				this.right.toString(sb);
				break;
			case NOT:
				sb.append("NOT (").append(this.depth).append(")\n");
				this.left.toString(sb);
				break;
			case EXPR:
				sb.append("( (").append(this.depth).append(")\n");
				this.left.toString(sb);
				addTabs(sb, depth);
				sb.append(")\n");
				break;
			case TERMINAL:
				sb.append("T: (").append(this.depth).append(")").append(value).append("\n");
				break;
			}
		}

	}
}
