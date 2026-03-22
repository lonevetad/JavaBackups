package srl.impl;

import srl.SRLCodeStatement;
import srl.SRLRegistersCollection;
import srl.SRLRegistersCollection.Register;

public class SRLSwap implements SRLCodeStatement {
	private static final long serialVersionUID = 89003304115L;

	public SRLSwap(String registerFirst, String registerSecond) {
		super();
		this.registerFirst = registerFirst;
		this.registerSecond = registerSecond;
	}

	protected final String registerFirst, registerSecond;

	public String getRegisterFirst() { return registerFirst; }

	public String getRegisterSecond() { return registerSecond; }

	@Override
	public void runCode(SRLRegistersCollection registers, boolean isNOTInverse) {
		long temp;
		Register r1, r2;
		r1 = registers.getRegister(registerFirst);
		r2 = registers.getRegister(registerSecond);
		temp = r1.value;
		r1.value = r2.value;
		r2.value = temp;
	}

	@Override
	public SRLSwap clone() { return new SRLSwap(registerFirst, registerSecond); }
}