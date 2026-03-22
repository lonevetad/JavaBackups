package srl.parsers;

import srl.SRLProgram;

/** Define and build an interpeter of SRL programs */
public interface SRLParser {
	public SRLProgram parseAndGetProgram(SRLTokenStream stream);
}