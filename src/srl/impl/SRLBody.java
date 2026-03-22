package srl.impl;

import java.util.List;

import dataStructures.LinkedListLightweight;
import srl.SRLCodeStatement;
import srl.SRLRegistersCollection;

/** Just a scope, useful for {@link SRLFor}.S */
public class SRLBody implements SRLCodeStatement {
	private static final long serialVersionUID = 55027084952165L;
	/** If false, then the order of the body's instruction MUST be kept */
	public static boolean SHOULD_INVERT_STATEMENTS_ON_INVERSE = true;

	public SRLBody() { this.body = new LinkedListLightweight<>(); }

	protected LinkedListLightweight<SRLCodeStatement> body;

	public List<SRLCodeStatement> getBody() { return body; }

	public void addStatement(SRLCodeStatement s) { body.add(s); }

	@Override
	public void runCode(SRLRegistersCollection registers, boolean isNOTInverse) {
		if (this.body.isEmpty()) { System.out.println("SRLBody empty"); }
		if (isNOTInverse || (!SHOULD_INVERT_STATEMENTS_ON_INVERSE)) {
			this.body.forEach(o -> o.runCode(registers, isNOTInverse));
		} else { // since List has not a natural/native "reverse order iteration", I made my own
			// V1
//			LinkedList<SRLCodeStatement> reverseList;
//			reverseList = new LinkedList<>();
//			this.body.forEach(reverseList::addFirst); // STACK
			// V2
//			int i;
//			SRLCodeStatement[] stack;
//			stack = new SRLCodeStatement[i = this.body.size()];
//			for (SRLCodeStatement s : this.body) {
//				stack[--i] = s;
//			}
//			for (SRLCodeStatement s : stack) {
//				s.runCode(registers, isNOTInverse);
//			}
			// V3
			this.body.forEachReverseOrder(o -> o.runCode(registers, isNOTInverse));
		}
	}

	@Override
	public SRLBody clone() {
		SRLBody b;
		b = new SRLBody();
		this.body.forEach(stat -> b.addStatement(stat.clone()));
		return b;
	}
}