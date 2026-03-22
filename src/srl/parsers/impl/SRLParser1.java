package srl.parsers.impl;

import java.util.Map;
import java.util.TreeMap;

import srl.SRLProgram;
import srl.SRLRegistersCollection;
import srl.impl.SRLBody;
import srl.impl.SRLFor;
import srl.impl.SRLIncrDecrOnRegister;
import srl.impl.SRLSwap;
import srl.parsers.SRLParseException;
import srl.parsers.SRLParser;
import srl.parsers.SRLToken;
import srl.parsers.SRLTokenName;
import srl.parsers.SRLTokenStream;
import tools.Comparators;

/**
 * A simple parser for simple SRL programs.
 * <p>
 * N.B.: the parser is flexible to the parenthesis used in everything, even in a
 * <i>"body"</i> statement (a <i>"body"</i> is the sub-program executed by a
 * <i>"for"</i> cycle, or the whole program itself). It's intentional.
 */
public class SRLParser1 implements SRLParser {

	public SRLParser1() {}

	@Override
	public SRLProgram parseAndGetProgram(SRLTokenStream stream) {
		SRLProgram prog;
		SRLRegistersCollection registers;
//		ForbiddenRegisters forbiddenVariablesOfForAndCounts;
		registers = new SRLRegistersCollection();
		prog = new SRLProgram(parseBody(stream, registers, new ForbiddenRegisters()));
		prog.setRegisters(registers);
		return prog;
	}

	protected void excUnwantedToken(SRLTokenStream stream, SRLTokenName expected, SRLToken got) {
		throw new SRLParseException("UNWANTED token: At " + stream.currentLineColumn() + ", expected " + expected.name()
				+ ", got: " + got.toString());
	}

	protected void excRegisterNotDeclared(SRLTokenStream stream, String regName) {
		throw new SRLParseException("Variable " + regName + " not already declared at: " + stream.currentLineColumn());
	}

	protected void checkDeclaredVar(SRLTokenStream stream, SRLRegistersCollection registers, String regName) {
		if (!registers.containsRegister(regName)) { excRegisterNotDeclared(stream, regName); }
	}

	protected void checkUnusedVar(SRLTokenStream stream, ForbiddenRegisters forbiddenVariablesOfForAndCounts,
			String regName) {
		if (forbiddenVariablesOfForAndCounts.containsRegister(regName)) {
			throw new SRLParseException("Cannot alter the register: " + regName
					+ ", because is fixed (in some cycle), at " + stream.currentLineColumn());
		}

	}

	protected void checkWantedToken(SRLTokenStream stream, SRLTokenName expected, SRLToken got) {
		if (expected != got.getTokenName()) { excUnwantedToken(stream, expected, got); }
	}

	protected SRLBody parseBody(SRLTokenStream stream, SRLRegistersCollection registers,
			ForbiddenRegisters forbiddenVariablesOfForAndCounts) {
		boolean hasParenthesis, hasMoreToken, hasClosedPar, expectArgumentParhentesis;
		SRLToken t, registerFirst;
		SRLBody body;
		String registerFirstName;
//		System.out.println("new body :D");
		t = stream.nextToken();
		hasParenthesis = t.getTokenName() == SRLTokenName.OpenParenthesis;
		if (hasParenthesis) { // take the next token
			t = stream.nextToken();
		}
//			System.out.println("\t\tnow body starts with: " + t.getTokenContent());
		hasMoreToken = stream.hasNextToken();
		body = new SRLBody();
		hasClosedPar = false;
		while (hasMoreToken) {
			if (t == null) { t = stream.nextToken(); }
			switch (t.getTokenName()) {
			case InitRegister:
				registerFirst = stream.nextToken();
				checkWantedToken(stream, SRLTokenName.Register, registerFirst);
				registerFirstName = registerFirst.getTokenContent();
				registerFirst = stream.nextToken();
				checkWantedToken(stream, SRLTokenName.Number, registerFirst);
				registers.addRegister(registerFirstName);
				registers.setRegisterValue(registerFirstName, Long.parseLong(registerFirst.getTokenContent()));
				break;
			case Increment:
				registerFirst = stream.nextToken();
				checkWantedToken(stream, SRLTokenName.Register, registerFirst);
				registerFirstName = registerFirst.getTokenContent();
				checkDeclaredVar(stream, registers, registerFirstName);
				checkUnusedVar(stream, forbiddenVariablesOfForAndCounts, registerFirstName);
				body.addStatement(new SRLIncrDecrOnRegister(true, registerFirstName));
				break;
			case Decrement:
				registerFirst = stream.nextToken();
				checkWantedToken(stream, SRLTokenName.Register, registerFirst);
				registerFirstName = registerFirst.getTokenContent();
				checkDeclaredVar(stream, registers, registerFirstName);
				checkUnusedVar(stream, forbiddenVariablesOfForAndCounts, registerFirstName);
				body.addStatement(new SRLIncrDecrOnRegister(false, registerFirstName));
				break;
			case For:
				SRLBody forBody;
				registerFirst = stream.nextToken();
				expectArgumentParhentesis = registerFirst.getTokenName() == SRLTokenName.OpenParenthesis;
				if (expectArgumentParhentesis) { registerFirst = stream.nextToken(); }
				checkWantedToken(stream, SRLTokenName.Register, registerFirst);
				registerFirstName = registerFirst.getTokenContent();
				checkDeclaredVar(stream, registers, registerFirstName);
				forbiddenVariablesOfForAndCounts.addRegisterUsage(registerFirstName);
				if (expectArgumentParhentesis) {
					registerFirst = stream.nextToken();
					checkWantedToken(stream, SRLTokenName.ClosedParenthesis, registerFirst);
				}
//				System.out.println("FORRRR over the variable:" + registerFirstName + ", is starting a body");
				forBody = parseBody(stream, registers, forbiddenVariablesOfForAndCounts);
				body.addStatement(new SRLFor(registerFirstName, forBody)); // add the for :D
				forbiddenVariablesOfForAndCounts.removeRegisterUsage(registerFirstName);
				break;
			case Swap:
				SRLToken registerSecond;
				String registerSecondName;
				registerFirst = stream.nextToken();
				expectArgumentParhentesis = registerFirst.getTokenName() == SRLTokenName.OpenParenthesis;
				if (expectArgumentParhentesis) { registerFirst = stream.nextToken(); }
				checkWantedToken(stream, SRLTokenName.Register, registerFirst);
				registerFirstName = registerFirst.getTokenContent();
				registerSecond = stream.nextToken();
				if (registerSecond.getTokenName() == SRLTokenName.Comma) {
					// skip the comma
					registerSecond = stream.nextToken();
				}
				checkWantedToken(stream, SRLTokenName.Register, registerSecond);
				registerSecondName = registerSecond.getTokenContent();
				checkDeclaredVar(stream, registers, registerFirstName);
				checkDeclaredVar(stream, registers, registerSecondName);
				checkUnusedVar(stream, forbiddenVariablesOfForAndCounts, registerFirstName);
				checkUnusedVar(stream, forbiddenVariablesOfForAndCounts, registerSecondName);
				if (expectArgumentParhentesis) {
					registerFirst = stream.nextToken();
					checkWantedToken(stream, SRLTokenName.ClosedParenthesis, registerFirst);
				}
				body.addStatement(new SRLSwap(registerFirstName, registerSecondName));
				break;
			case ClosedParenthesis:
				if (hasParenthesis) {
					hasClosedPar = true;
				} else {
					stream.pushBackToken(t);
					// do not break: the given closed parenthesis is supossed to belong to another
					// body -> E.O.Body
				}
			case EOF:
				hasMoreToken = false;
				break;
			default:
				throw new SRLParseException("What is this token? " + t.getTokenName() + ", with content: "
						+ t.getTokenContent() + ",,, at:" + stream.currentLineColumn());
			}
			hasMoreToken &= stream.hasNextToken();
			t = null;
		}
		if (hasParenthesis && (!hasClosedPar)) {
			throw new SRLParseException("Parenthesis not closed at " + stream.currentLineColumn());
		}
//		System.out.println("exiting body");
		return body;
	}

	//

	protected static class ForbiddenRegisters {
		protected final Map<String, RegisterAndUsageCounter> forbVarBack;

		public ForbiddenRegisters() {
			super();
			this.forbVarBack = new TreeMap<>(Comparators.STRING_COMPARATOR);
			// MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
			// Comparators.STRING_COMPARATOR);
		}

		public boolean containsRegister(String regName) { return this.forbVarBack.containsKey(regName); }

		public void addRegisterUsage(String regName) {
			RegisterAndUsageCounter r;
			r = this.forbVarBack.get(regName);
			if (r == null) {
				this.forbVarBack.put(regName, new RegisterAndUsageCounter(regName));
			} else {
				r.usageCounter++;
			}
		}

		public void removeRegisterUsage(String regName) {
			RegisterAndUsageCounter r;
			r = this.forbVarBack.get(regName);
			if (r != null) {
				if (r.usageCounter <= 1) {
					this.forbVarBack.remove(regName);
				} else {
					r.usageCounter--;
				}
			}
		}

		protected static class RegisterAndUsageCounter {
			protected int usageCounter;
			protected String regName;

			public RegisterAndUsageCounter(String regName) {
				super();
				this.regName = regName;
				this.usageCounter = 1;
			}
		}
	}
}