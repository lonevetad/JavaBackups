package srl;

import java.io.Serializable;

import srl.impl.SRLBody;
import srl.parsers.SRLParser;

/** Interpreter of a SRL program. Built via {@link SRLParser}. */
public class SRLProgram implements Runnable, Serializable, Cloneable {
	private static final long serialVersionUID = -7563214708L;

	public SRLProgram(SRLBody prog) { this.prog = prog; }

	protected final SRLBody prog;
	protected SRLRegistersCollection registers;

	public SRLBody getProg() { return prog; }

	public SRLRegistersCollection getRegisters() { return registers; }

	public void setRegisters(SRLRegistersCollection registers) { this.registers = registers; }

	public void addStatement(SRLCodeStatement s) { prog.addStatement(s); }

	/** See {@link SRLRegistersCollection#resetRegisters()}. */
	public void resetRegisters() {
		this.registers.resetRegisters();
	}

	@Override
	public SRLProgram clone() {
		SRLProgram p;
		p = new SRLProgram(prog.clone());
		p.setRegisters(registers.clone());
		return p;
	}

	//

	@Override
	public void run() { this.execute(); }

	public void execute() { prog.runCode(registers, true); }

	public void executeInverse() { prog.runCode(registers, false); }
}