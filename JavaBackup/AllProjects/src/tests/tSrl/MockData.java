package tests.tSrl;

import java.util.function.Supplier;

public class MockData {
	private static ISRLProgramTextSupplier[] SRL_PROGRAMS_TEST = null;

	public static ISRLProgramTextSupplier[] getProgramsExamples() {
		if (SRL_PROGRAMS_TEST == null) { SRL_PROGRAMS_TEST = srlProgEx(); }
		return SRL_PROGRAMS_TEST;
	}

	private static final ISRLProgramTextSupplier[] srlProgEx() {
		int i;
//		ArrayList<ISRLProgramTextSupplier> al;
		String fileText = null;
		int[] r0Vals = { 5, -5, 6, -6, 3, -3, 4, -4, 1, 0, -1, 2, -2, 11, -11, 12, -12, 31, -31, 32, -32 };
		String[] a, testsR0;
		String[][] fixedTests = { new String[] { //
//				"init r0 17 init r1 4", //
//				"init r0 7 init r1 4 inc r0", //
//				"init r0 7 init r1 4 inc /*commento multilinea*/ r0", //
//				"init r0 7 init r1 4 inc //commento singola \n r0", //
//				"init r0 7 init r1 4 inc r0 inc r0", //
				"init r0 7 init r1 4 inc r0 inc r0 inc r0", //
//				"init r0 -7 init r1 4 inc r0", //
//				"init r0 -7 init r1 4 inc r0 inc r0", //
//				"init r0 -7 init r1 4 inc r0 inc r0 inc r0", //
//				"init r0 7 init r1 4 for r0 { inc r1}", //
//				"init r0 -17 init r1 0 for r0 inc r1", //
//				"init r0 7 init r1 4 for r0 { incr r1 } dec r0 decr r0;", //
//				"init r0 7 init r1 4 init r2 300 for r0 { for (r1){ inc r2 } } ; dec r0", //
//				"init r0 5 init r1 10 init r2 0 init r3 2 for r0 { for (r1){ inc r2 for r3 inc r2 } } ;", //
//				"init r0 5 init r1 3 init r2 1000 init r3 2 init acaso 222 for r0 { for (r1){ inc r2 for r3 dec r2 } inc acaso } ; dec r0", //
//				"init r0 5 init r1 3 init r2 1000 init r3 2 init acaso 222 for r0 { for (r1){ inc r2 for r3 dec r2 inc acaso } inc acaso } ; dec r0", //
//				"init r0 5 init r1 4 for r0 { inc r1 inc r1 inc r1}", //
//				"init rr -3 init r2 7 ; \n for rr { inc r2 }", //
//				"init pippo 5 init pluto -88 swap(pippo, pluto)", //
//				"init dividendo 18 init quoziente 0 init tp1 1 init tp2 0 ;\n"
//						+ "for dividendo { swap(tp1 tp2) for(tp1){ inc quoziente; } }", //
//				"init dividendo 7 init quoziente 0 init tp1 1 init tp2 0 ;\n"
//						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
//				"init dividendo 1 init quoziente 0 init tp1 1 init tp2 0 ;\n"
//						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
//				"init dividendo -9 init quoziente 0 init tp1 1 init tp2 0 ;\n"
//						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
//				"init dividendo -8 init quoziente 0 init tp1 1 init tp2 0 ;\n"
//						+ "for dividendo { swap(tp1 tp2) for(tp1){ inc quoziente; } }", //
//				"init dividendo -1 init quoziente 0 init tp1 1 init tp2 0 ;\n"
//						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
//				"init dividendo 0 init quoziente 0 init tp1 1 init tp2 0 ;\n"
//						+ "for dividendo { swap(tp1, tp2) for(tp1){ inc quoziente; } }", //
//				"init a -10 init b 7 for a dec b", //
//				"init a -3 init b 7 init c 100 for a { for b { inc c}}", // , //
//				"init a -4 init b -16 init c 100 for a { for b { inc c}}", //
//				"init a 7 init b 10 init ancilla 0;" // SWAP macro in pure SRL
//						+ "for a { inc ancilla} for ancilla {dec a}\n" //
//						+ "for b { inc a} for a {dec b}\n" //
//						+ "for ancilla { inc b} for b {dec ancilla}", //
//				"init a -8 init b -4 init c -2 init d -10 init res 0;;\n" + //
//						"for a{ for(b){ for[c){ for{d}{inc res } } }}\n" + //
//						"inc d inc d;", //
//				"init a -8 init b -4 init c -2 init d -1 init res 0;;\n" + //
//						"for a{ for(b){ for[c){ for{d}{inc res } } }}\n" + //
//						"inc d inc d; for d{ for b { dec c }} ; \n" + //
//						"for d{for{c}{dec b}}" + //
//						"\n\n init eee 5 for eee dec res;",//
				}, //
				null };
		int[][] pairTestingSwap = new int[][] { { 5, 5 },//
//				{ 17, 10 }, { 8, -5 }, { -4, 16 }, { -50, -40 }//
//				, { 4, 0 }, { 0, 4 }, { -4, 0 }, { 0, -4 }//
//				, { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }//
//				, { 2, 3 }, { 3, 2 }, { -2, 3 }, { 3, -2 }, { 2, -3 }, { -3, 2 }, { -2, -3 }, { -3, -2 }//
		};
		fileText = " init ancilla 0;" + //
				"for a { inc ancilla} for ancilla {dec a}\n" + //
				"for b { inc a} for a {dec b}\n" + //
				"for ancilla { inc b} for b {dec ancilla}";
		fixedTests[1] = new String[pairTestingSwap.length];
		for (i = 0; i < pairTestingSwap.length; i++) {
			int[] pair = pairTestingSwap[i];
			fixedTests[1][i] = "init a " + pair[0] + " init b " + pair[1] + fileText;
		}

		// fixedTests continene: quelli a mano all'inizio + quelli con l'ancilla qui
		// sopra

		// testsR0
		testsR0 = new String[] { //
//				" init r1 1 init r2 0 init r3 0; \n for r0 { swap(r1, r2) ; for r1{ inc r3} }", //
//				//
//				" init isEven 1 init isOdd 0 init quot 0 init ancilla 0\n" + //
//						"for r0 { swap(isEven, isOdd) for isEven { inc quot }} ;\n" + //
//						"for quot { incr ancilla inc ancilla}", //

//				" init r1 1 init r2 0 init r3 0; \n for r1 { inc r3 } swap(r1, r2) for r1 { dec r3 }" //

//				" init r1 1 init r2 0 init r3 0; \n " + //
//						"for r0 { for r1 { inc r3 } swap(r1,r2) for r1 { dec r3 } } " //
//						"for r0 { for r1 { for r0 { inc r3 } } swap(r1,r2) for r1 { for r0 { dec r3 } } } " //

//				" init r1 1 init r2 0 init r3 0; \n " + //
//						"for r0 { for r0 { for r1 { inc r3 } swap(r1,r2) for r1 { dec r3 } } " + //
////				"for r0 { swap(r1, r2) } "+//
//						"}", // +
//				//
//				" init r1 1 init r2 0 init r3 0; \n " + //
//						"for r0 { for r0 { for r1 { inc r3 } swap(r1,r2) for r1 { dec r3 } } " + //
//						"for r0 { swap(r1, r2) } " + //
//						"}",// +
//				// cosi' r3 vale |N| e r2 contiene "N e' pari?"

//				" init r1 1 init r2 0 init r3 0 init r4 0 inc r5 ; \n " + //
//				"for r0 { for r0 { inc r3 inc r5 } inc r4 } \n"+ // r3 == r5 == N*N ; r4 == N == sign(N)*|N|
//				"dec r4 \n" + // r4 == N-1 .. se era 5, ora vale 4, se era -6 ora vale -7
//				"for r0 { for r4 { dec r3 } } \n" // r3 ora vale +|N|

//" init r1 1 init r2 0 init r3 0 for r0 { for r1 { incr r3 } swap(r1,r2) for r1 { decr r3 } swap(r1,r2) }"
//				" init r1 1 init r2 0 init r3 0 init r4 0;" //
//						+ "\n incr r3 for r0 { incr r3 incr r3 dec r4 } " //
//						+ "\nfor r3 { swap(r1,r2) for r1 inc r4 } swap(r1,r2)"
//						// /|\ the core -- ERRORE: r4 vale sempre il numero di "inc r4 che precedono questo ciclo (se assente, 0)"
//						+ "\n for r0 { dec r3 dec r3 } " // portiamo r3 ad 1
//						+ "\n for r4 { dec r3 } " // solo uno varra' 1
//						+ "\n // swap(r3,r4) // do it to ask the question isNonNegative"
//						+ "\n for r0 { dec r3 dec r4 } // vale -N " //
//						+ "\n inc r3 // il negativo, tra r0 ed r3, vale -|N|+1 ossia -(|N|-1), l'altro [il positivo] |N|+1 " //
				//
				// , " init r1 1 init r2 0 init r3 0 for r0 { for r1 { incr r3 } swap(r1,r2) for
				// r1 { decr r3 } swap(r1,r2) }"
//						// + "\n for r0 { inc r4 } " //
//						+ "\n for r3 { inc r4 } " // rimane 1
//						+ "\n inc r3 for r0 { dec r3}",//
				// vedere quanto vale r4s
				//
//				" init r1 1 init r2 0 init r3 0 init r4 0 init r5 0 init r6 0 ; +" //r5 1 init r6 0 ;\n" //
//	//let's try
////				+"for r0 { for r5 {"  //
//
//				+ "\ninc r5 for r0 { inc r5 inc r5 } ;"// odd
//				+ "\n inc r3 dec r4 for r5 { swap(r1, r2) for r1 {swap(r3, r4) }}" //
//				+ "\n for r5 { swap(r1, r2) }" // reset
//				+ "\n dec r5 for r0 { dec r5 dec r5 }" // back to zero
//				//core
////				+ "\ninc r3" //
//				+ "\n for r0 { inc r3 inc r3 }" // is odd now: (2N+1) if N>=0 ; (-(2*|N| -1)) otherwise
////				+ "\n" //
//						+ "\nfor r3 { swap(r1, r2) ; for r1{ inc r4;} }"// halve
//						+ "\nfor r4 { dec r3 } for r3 {dec r4} " //
				//
				//
				//
//				" init r1 1 init r2 0 init r3 0 init r4 0 init r5 0 init r6 0 ;"//
				//
				//
				//
//						+ "\nfor r0 { swap(r1, r2) ; for r1{ inc r3; inc r4 inc r5 } } for r0 { swap(r1, r2) ; }"// halve
//																													// in
//																													// r3
//						+ "\nfor r3 { for r3 { "// it's repeated a positive time (needed to NOT invert the code if N<0
////				+ "\n for r3 { inc r5 }"// copy r3 in r5 and reset r3
//						+ "\nfor r4 { swap(r1, r2) ; for r1{ inc r6;} } for r4 { swap(r1, r2) ; }"// half, in r5
//						+ "\nfor r5 { dec r4 }"// to zero
////						+ "\nfor r6 { dec r5 inc r4 }"// to zero
//						+ "\n } }"//
				/**
				 * old core of a double for r0 : + "\n for r3 { inc r6 }"// save old r3 // <br>
				 * + "\nfor r3 { swap(r1, r2) ; for r1{ inc r5;} } for r3 { swap(r1, r2) ; }"//
				 * half, in r5 // <br>
				 * + "\n for r6 { dec r3 }"// to zero // <br>
				 * + "\n for r5 { inc r3 dec r6 dec r6 } "// save r5 in r3, bring r6 to 1 if r5
				 * is odd // <br>
				 * // this for dec r5 only if its odd // <br>
				 * + "\n for r5 { for r1 { dec r6} swap(r1, r2) for r1{inc r6} } for r5 {
				 * swap(r1, r2) } " // <br>
				 * + "\n for r3 { dec r5 } " // set r5 to zero // <br>
				 */

//						+ "\nfor r0 { inc r3 incr r4 inc r4 } inc r4 " //
//						+ "\nfor r4 { dec r3 } "
//						+ "\n inc r0 for r0{ for r0 { inc r3 }} dec r0" //
//						+ "\n for r0{ for r0 { dec r3 } dec r3 dec r3 } " // se N >=0 -> r3 = 1

				//

				" init r1 1 init r2 0 init r3 1 init r4 0 " //
						+ "\ninc r2 ;" //
						+ "\nfor r0 {" //
						+ "\n\tfor r1 { " //
						+ "\n\t\tdec r2;\n\t\tswap(r3,r4) " //
						+ "\n\t\tfor r4 {\n\t\t\tinc r2\n\t\t} " //
						+ "\n\t} " //
						+ "\n\tfor r2 { " //
						+ "\n\t\nswap(r1,r3) " //
						+ "\n\t} " //
						+ "\n} "
//				+ "\nfor r0{ swap(r3,r4) } "
//				+ "\ninc r1 for r4 { dec r3 } "
		};

		a = new String[fixedTests[0].length + fixedTests[1].length + //
				(r0Vals.length * testsR0.length)];
		i = 0;
		for (String[] singleSource : fixedTests) {
			for (String source : singleSource) {
				a[i++] = source;
			}
		}
		pairTestingSwap = null;
		fixedTests = null;

		// test r0
		for (String tr0 : testsR0) {
			for (int r0 : r0Vals) {
				a[i++] = "init r0 " + r0 + tr0;
			}
		}

		// clean
		testsR0 = null;
		i = 0;

//		// from text
//		try {
//			StringBuilder sb;
//			sb = new StringBuilder(1024);
//			FileReader fr;
//			BufferedReader br;
//			fr = new FileReader(new File("./lessThanOne.txt"));
//			br = new BufferedReader(fr);
//			br.lines().forEach(l -> sb.append('\n').append(l));
//			fileText = sb.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (fileText == null) {
		ISRLProgramTextSupplier[] aa;
		aa = new ISRLProgramTextSupplier[i = a.length];
		while (--i >= 0) {
			aa[i] = new SRLProgramTextSupplier(a[i]);
		}
		return aa;
//		}
//		// if the file has been found ..
//		al = new ArrayList<>(a.length + r0Vals.length);
//		for (String simpleTest : a) {
//			al.add(new SRLProgramTextSupplier(simpleTest));
//		}
//		for (int r0 : r0Vals) {
//			al.add(new SRLProgramTextSupplier_ForLessThanOneFile(fileText, r0));
//		}
//		return al.toArray(new ISRLProgramTextSupplier[al.size()]);
	}

	public static interface ISRLProgramTextSupplier extends Supplier<String> {}

	public static class SRLProgramTextSupplier implements ISRLProgramTextSupplier {
		public final String program;

		public SRLProgramTextSupplier(String program) {
			super();
			this.program = program;
		}

		@Override
		public String get() { return program; }
	}

	public static class SRLProgramTextSupplier_ForLessThanOneFile implements ISRLProgramTextSupplier {
		public final int r0;
		public final String program;

		public SRLProgramTextSupplier_ForLessThanOneFile(String program, int r0) {
			super();
			this.program = program;
			this.r0 = r0;
		}

		@Override
		public String get() {
			return "init r0 " + r0 + //
					"init r1 1 init r2 0 init r3 0 init r4 0 \n" //
					+ program;
		}

	}
}