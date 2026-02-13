package srl.impl;

import srl.SRLCodeStatement;
import srl.SRLRegistersCollection;

/**
 * Single operation of increment or decrement (it depends on
 * {@link #isIncrement()}} on a register (which is {@link #getRegisterName()}).
 */
public class SRLIncrDecrOnRegister implements SRLCodeStatement {
	private static final long serialVersionUID = -32025517961902L;

	public SRLIncrDecrOnRegister(boolean isIncrement, String registerName) {
		super();
		this.isIncrement = isIncrement;
		this.registerName = registerName;
	}

	protected final boolean isIncrement;
	protected final String registerName;

	public boolean isIncrement() { return isIncrement; }

	public String getRegisterName() { return registerName; }

	@Override
	public void runCode(SRLRegistersCollection registers, boolean isNOTInverse) {
		if (isNOTInverse == this.isIncrement) {
			registers.incrementRegister(this.registerName);
		} else {
			registers.decrementRegister(this.registerName);
		}
	}

	@Override
	public SRLIncrDecrOnRegister clone() { return new SRLIncrDecrOnRegister(isIncrement, registerName); }
}