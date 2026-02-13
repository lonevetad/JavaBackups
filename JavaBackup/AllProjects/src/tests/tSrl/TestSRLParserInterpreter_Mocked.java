package tests.tSrl;

import java.io.IOException;

import srl.SRLProgram;
import srl.SRLRegistersCollection;
import srl.parsers.SRLParser;
import srl.parsers.impl.CharStreamByString;
import srl.parsers.impl.SRLParser1;
import srl.parsers.impl.SRLTokenStream1;
import tests.tSrl.MockData.ISRLProgramTextSupplier;
import tools.LoggerMessages;
import tools.impl.LoggerOnFile;

public class TestSRLParserInterpreter_Mocked extends TestSRLParserInterpreter {

	public static void main(String[] args) throws IOException {
		LoggerMessages log;
		SRLProgram prog;
		SRLParser parser;
		String source;
		SRLRegistersCollection originalRegisters;
		log = LoggerMessages.LOGGER_DEFAULT;
		System.out.println("Start");
		log = new LoggerOnFile("srl_parser.txt");
		log.log("START TEST SRL PARSER");
		parser = new SRLParser1();
		for (ISRLProgramTextSupplier supp : MockData.getProgramsExamples()) {
			source = supp.get();
			log.log("\n\n\n ");
			for (int i = 0; i < 5; i++) {
				log.log("\n_________________________________");
			}
			log.logAndPrint("\n\n\n\nusing the source:");
			if (source.length() < 1024) {
				log.logAndPrint(source);
			} else {
				log.logAndPrint("Sorgente TROPPO grande");
			}
			log.log("\nparse it");
			prog = parser.parseAndGetProgram(new SRLTokenStream1(new CharStreamByString(source)));
			log.log("\n parsed, the registers are:\n\n");
			log.log(prog.getRegisters().toString());
			originalRegisters = prog.getRegisters().clone();

			log.log("\n execute it:\n\n");
			prog.execute();
			log.log("\n in the end, the registers are:\n\n");
			log.log(prog.getRegisters().toString());

			log.log("\n run inverse:\n\n");
			System.out.println("inverse");
			prog.executeInverse();
			log.log("\n in the end, the registers are:\n\n");
			log.log(prog.getRegisters().toString());
			log.log("\n are the same as original? ");
			log.log(Boolean.toString(areSameReg(originalRegisters, prog.getRegisters())));
		}
	}

	static boolean areSameReg(SRLRegistersCollection originalRegisters, SRLRegistersCollection actualRegisters) {
		boolean[] b = { true };
		originalRegisters.forEachRegister(r -> {
			if (r.value != actualRegisters.getRegisterValue(r.name))
				b[0] = false;
		});
		return b[0];
	}
}