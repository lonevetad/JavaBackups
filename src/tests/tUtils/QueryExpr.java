package tests.tUtils;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import jakarta.json.stream.JsonGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.StringWriter;

public class QueryExpr {
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
			"---a", // not not not
			"------a", // 6*not
			"-------a", // 7*not
			"-(-a)", // not expr not
			"(a)", // expression
			"-(a)", // not expression
			"-(a.b)", // not and
			"-(a|b)", // not or
			"a.(((b|c)))|d", // nested expressions
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
		folderPath = "C:\\Users\\marco\\";
		// folderPath = "C:\\Users\\ottin\\";
		try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
				new java.io.FileWriter(folderPath + "Desktop\\prog\\Java\\JavaBackups\\src\\tests\\tUtils\\te.txt"))) {
			System.out.println("START");
			for (String s : expressions) {
				String startText = "\n\n--------------------------------------\n\nParsing expression: " + s;
				System.out.println(startText);
				writer.write(startText);
				writer.newLine();
				writer.flush();
				try {
					ParserBooleanExprNode node = parse(s);
					writer.write(node.toString());
					writer.newLine();
					writer.flush();
					writer.write("Optimized: ");
					node.optimize();
					writer.newLine();
					writer.flush();
					writer.write(node.toString());
					writer.newLine();
					writer.newLine();

					writer.write("\n: ");
					writer.write("Parsing query ...");
					writer.newLine();
					writer.flush();
					Query q = parseToQuery(s);
					writer.write("Parsed! query to json...");
					writer.newLine();
					writer.flush();
					String jsoned = queryToJson(q);
					writer.write("jsoned! :");
					writer.newLine();
					writer.flush();
					writer.write(jsoned);
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
			System.out.println("END QUERY EXPR");
		} catch (java.io.IOException ex) {
			ex.printStackTrace();
		}
	}

	//

	//

	//

	// Assuming 'query' is your complex Query object
	public static String queryToJson(Query query) {
		StringBuilder sb = new StringBuilder();
		JacksonJsonpMapper mapper = new JacksonJsonpMapper();
		StringWriter writer = new StringWriter();

		try (JsonGenerator generator = mapper.jsonProvider().createGenerator(writer)) {
			query.serialize(generator, mapper);
		}

		return writer.toString();
	}

	public static Query parseToQuery(String expression) { // TODO : TO BE USED IN PLACE OF "buildBoolQuery"
		ParserBooleanExprNode root = parse(expression);
		BoolQuery.Builder rootQuery = new BoolQuery.Builder();
		return toQuery(root, rootQuery);
	}

	protected static Query toQuery(ParserBooleanExprNode node, BoolQuery.Builder rootQuery) {
		Query q = null;
		switch (node.operator) {
			case AND -> {
				// just propagate the rootQuery, which is the "AND-in-progress"
				final BoolQuery.Builder andQuery = (rootQuery == null) ? new BoolQuery.Builder() : rootQuery;
				collectChainedAnds(andQuery, node, node.left);
				collectChainedAnds(andQuery, node, node.right);
				q = Query.of(qb -> qb.bool(andQuery.build()));
			}
			case OR -> {
				BoolQuery.Builder innerOr = new BoolQuery.Builder();
				collectChainedOrs(innerOr, node, node.left);
				collectChainedOrs(innerOr, node, node.right);
				rootQuery.filter(b -> b.bool(innerOr.build())); // append all OR clauses as a single one
				// no setting ot the "q" since the ORs will be just appended in AND to the
				// rootQuery as a filter
			}
			case NOT -> { // should not be there -> AND and OR clauses will handle it
				boolean isThereARootQuery = (rootQuery != null);
				final BoolQuery.Builder rootQueryToPutIn = isThereARootQuery ? rootQuery : new BoolQuery.Builder();

				q = (node.left.operator == Operator.TERMINAL || node.value != null) ? // is the argument a terminal ?
						buildTermQuery(node.value.strip()) // build it
						: toQuery(node.left, rootQueryToPutIn) // otherwise, it must be computed
				;
				// are we part of a super-expression? if so -> just append it
				if (isThereARootQuery) {
					rootQueryToPutIn.mustNot(q);
				} else {
					// if not, WE are the root: create the proper, actual query to be returned
					rootQueryToPutIn.mustNot(q);
					q = Query.of(qb -> qb.bool(rootQueryToPutIn.build()));
				}
			}
			case EXPR -> {
				node.optimize();
				q = toQuery(node, rootQuery);
			}
			case TERMINAL -> {
				q = buildTermQuery(node.value.strip());
				if (rootQuery != null) { // are we part of a super-expression? if so -> append it
					rootQuery.should(q); // "must(..)" ?
				}
			}
			default -> throw new IllegalArgumentException("Unrecognized operator: " + node.operator);
		}
		return q;
	}

	protected static void collectChainedOrs(BoolQuery.Builder innerOr, ParserBooleanExprNode subrootNode,
			ParserBooleanExprNode currentNode) {
		switch (currentNode.operator) {
			case OR -> {
				// continue the chain:
				collectChainedOrs(innerOr, subrootNode, currentNode.left);
				collectChainedOrs(innerOr, subrootNode, currentNode.right);
			}
			case TERMINAL, NOT -> {
				toQuery(currentNode, innerOr); // the "append" will be already performed there
				/*
				 * Query termQuery = toQuery(currentNode, rootQuery);
				 * if(termQuery != null){
				 * innerOr.should(termQuery);
				 * }
				 * 
				 */
			}
			case AND -> {
				// TERMINAL will parse the terminal, while AND will build a new sub-query to
				// append
				// Query termQuery = toQuery(currentNode, innerOr); // "null" because it's
				// unused;
				// innerOr.mustNot(termQuery);
				Query q = toQuery(currentNode, new BoolQuery.Builder());
				innerOr.should(q); // "should" since we are OR-ing
			}
			case null, default ->
				throw new IllegalArgumentException(
						"Unrecognized operator since no other cases should be managed there: " + currentNode.operator);
		}
	}

	protected static void collectChainedAnds(BoolQuery.Builder innerAnd, ParserBooleanExprNode subrootNode,
			ParserBooleanExprNode currentNode) {
		switch (currentNode.operator) {
			case OR, TERMINAL, NOT -> {
				Query q = toQuery(currentNode, null);
				innerAnd.filter(q);
			}
			case AND -> {
				// continue the chain:
				collectChainedAnds(innerAnd, subrootNode, currentNode.left);
				collectChainedAnds(innerAnd, subrootNode, currentNode.right);
			}
			case null, default ->
				throw new IllegalArgumentException(
						"Unrecognized operator since no other cases should be managed there: " + currentNode.operator);
		}
	}

	//

	//

	//

	static ParserBooleanExprNode parse(String expression) { // START
		int[] index = { 0 }; // to allow for modification inside parseExpr
		return parseOr(expression.trim(), index, 0);
	}

	static void skipWhiteChars(String expression, int[] index) {
		int len = expression.length();
		while (index[0] < len && WHITE_CHARS_SET.contains(expression.charAt(index[0]))) {
			index[0]++;
		}
	}

	static ParserBooleanExprNode parseOr(String expression, int[] index, int depth) {
		return parseOr(null, expression, index, depth);
	}

	static ParserBooleanExprNode parseOr(ParserBooleanExprNode left, String expression, int[] index, int depth) {
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
		ParserBooleanExprNode orNode = new ParserBooleanExprNode(null, Operator.OR, depth);
		orNode.left = left;
		orNode.right = parseOr(null, expression, index, depth + 1);
		return orNode;
	}

	static ParserBooleanExprNode parseAnd(String expression, int[] index, int depth) {
		return parseAnd(null, expression, index, depth);
	}

	static ParserBooleanExprNode parseAnd(ParserBooleanExprNode left, String expression, int[] index, int depth) {
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
		ParserBooleanExprNode andNode = new ParserBooleanExprNode(null, Operator.AND, depth);
		andNode.left = left;
		andNode.right = parseAnd(null, expression, index, depth + 1); // Expr
		return andNode;
	}

	static ParserBooleanExprNode parseExpr(String expression, int[] index, int depth) { // TODO: MOVE IT TO THE LAST,
																						// JUST BEFORE
		// THE TERMINAL
		if (expression.charAt(index[0]) == '(') {
			// int originalIndex = index[0];c
			index[0]++;
			skipWhiteChars(expression, index);
			ParserBooleanExprNode expr = new ParserBooleanExprNode(null, Operator.EXPR, depth);
			ParserBooleanExprNode result = parseOr(expression, index, depth + 1); // restart the tree evaluation, but
																					// deeper
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
			 * { ParserBooleanExprNode currentExpr = expr; expr = parseAnd(expr, expression,
			 * index,
			 * depth); // was absorbed by a OR? ParserBooleanExprNode orExpr = expr;
			 * ParserBooleanExprNode prev =
			 * expr; while (expr.operator == Operator.OR) { prev = expr; expr = expr.left; }
			 * expr.left = currentExpr; return orExpr; } else if
			 * (expression.charAt(index[0]) == '|') { expr = parseOr(expr, expression,
			 * index, depth); } }
			 */
			return expr;
		}
		return parseNot(expression, index, depth);
	}

	static ParserBooleanExprNode parseNot(String expression, int[] index, int depth) {
		if (expression.charAt(index[0]) == '-') {
			index[0]++;
			skipWhiteChars(expression, index);
			ParserBooleanExprNode notNode = new ParserBooleanExprNode(null, Operator.NOT, depth);
			// only terminals and expressions can be negated
			notNode.left = parseExpr(expression, index, depth + 1);
			return notNode;
		}
		return parseTerminal(expression, index, depth);
	}

	static ParserBooleanExprNode parseTerminal(String expression, int[] index, int depth) {
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
		ParserBooleanExprNode terminal = new ParserBooleanExprNode(terminalText, Operator.TERMINAL, depth);
		return terminal;
	}

	//

	public static class ParserBooleanExprNode {
		int depth;
		String value;
		Operator operator;
		ParserBooleanExprNode left;
		ParserBooleanExprNode right;

		public ParserBooleanExprNode(String value, Operator operator, int depth) {
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

		public void optimize() {
			if (this.left != null) {
				this.left.optimize();
			}
			if (this.right != null) {
				this.right.optimize();
			}
			if (this.operator == Operator.EXPR) {
				this.operator = this.left.operator;
				this.value = this.left.value;
				this.right = this.left.right;
				this.left = this.left.left;
			}
			if (this.operator == Operator.NOT && this.left.operator == Operator.NOT) {
				ParserBooleanExprNode notNode = this.left;
				this.operator = notNode.left.operator;
				this.value = notNode.left.value;
				this.right = notNode.left.right;
				this.left = notNode.left.left;
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
