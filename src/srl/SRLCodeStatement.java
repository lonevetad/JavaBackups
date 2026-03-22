package srl;

import java.io.Serializable;

public interface SRLCodeStatement extends Serializable, Cloneable {

	public default void runCode(SRLRegistersCollection registers) { runCode(registers, true); }

	public void runCode(SRLRegistersCollection registers, boolean isNOTInverse);

	public SRLCodeStatement clone();
}