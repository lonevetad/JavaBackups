package tools.minorTools;

import java.util.Arrays;

public class StringSorter {

	private StringSorter() {
	}

	public static final char lastCharInAlphabeth = '\n';

	@SuppressWarnings("unused")
	private static final char[] partOne = { ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' },
			partTwo = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
					'T', 'U', 'V', 'X', 'Y', 'W', 'Z' },
			partThree = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
					't', 'u', 'v', 'x', 'y', 'w', 'z' },
			partFour = { '(', ')', '[', ']', '{', '}', '.', ',', '_', '\'', '\\', '!', '&', '$', '\u00E0', '\u00E8',
					'\u00CC', '\u00F2', '\u00F2', '\u00F9', '@', '#', '\t', lastCharInAlphabeth },

			raccoltaCaratteri = merge(partOne, partThree, partFour),
			raccoltaCaratteriCaseSensitive = merge(partOne, partTwo, partThree, partFour);

	static {
		System.out.println("raccoltaCaratteri\n\t:" + Arrays.toString(raccoltaCaratteri));
	}

	public static int compareStr(String s1, String s2) {
		boolean notFinished, /* canRead1, canRead2, */ isDigit1, isDigit2, isLetter1, isLetter2;
		char c1, c2;
		int i, j, l1, l2, l1_1, l2_1, startIndex, endIndex;
		long n1, n2, p;
		if (s1 == s2)
			return 0;
		if (s1 == null)
			return -1;
		if (s2 == null)
			return 1;

		i = j = -1;
		notFinished = /* canRead1 = canRead2 = */true;
		l1_1 = (l1 = s1.length()) - 1;
		l2_1 = (l2 = s2.length()) - 1;
		while (notFinished && (i < l1_1) && (j < l2_1)) {
			c1 = s1.charAt(++i);
			c2 = s2.charAt(++j);
			/**
			 * casistiche:
			 * <ul>
			 * <li>se sono entrambi "digit", compongo il numero di entrambi e li
			 * confronto</li>
			 * <li>se sono entrambi Letter, li confronto banalmente</li>
			 * <li>Se sono un mix di questi due, quello con il "digit" ha il valore
			 * minore</li>
			 * <li>Se non sono nulla di tutto ciò (caratteri strani ASCII), allora li
			 * confronto banalmente</li>
			 * <li></li>
			 * </ul>
			 */
			isDigit1 = Character.isDigit(c1);
			isDigit2 = Character.isDigit(c2);
			isLetter1 = Character.isLetter(c1);
			isLetter2 = Character.isLetter(c2);
			if (isDigit1 && isDigit2) {
				// conversione in cifre e confronto
				p = 1;
				n1 = 0;
				startIndex = i;
				while (++i < l1 && Character.isDigit(s1.charAt(i)))
					;
				endIndex = i;
				// adesso, da "start" a "end-1" ci sono solo digit
				while (--endIndex >= startIndex) {
					n1 += (s1.charAt(endIndex) - '0') * p;
					p *= 10;
				}
				p = 1;
				n2 = 0;
				startIndex = j;
				while (++j < l2 && Character.isDigit(s2.charAt(j)))
					;
				endIndex = j;
				// adesso, da "start" a "end-1" ci sono solo digit
				while (--endIndex >= startIndex) {
					n2 += (s2.charAt(endIndex) - '0') * p;
					p *= 10;
				}
				if (n1 != n2) {
					// TROVATO DIFFERENZE; FINITO
					return n1 > n2 ? 1 : -1;
				}
			} else if (isLetter1 && isLetter2) {
				// easy, sono caratteri letterali
				if (c1 != c2) {
					// FINISH
					return c1 > c2 ? 1 : -1;
				}
			} else {
				// è un mix ? .. vediamo
				if (isDigit1 && isLetter2) {
					return -1;
				} else if (isDigit2 && isLetter1) {
					return 1;
				} else {
					// CHAOS ... non sono ne cifre ne lettere
					if (c1 != c2) {
						// FINISH
						return c1 > c2 ? 1 : -1;
					}
				}
			}
			if (notFinished) {
				// se non ci sono stati confronti tali
				notFinished = ((i < l1) && (j < l2));
			}
		}
		// NULLA DI UTILE, le stringhe sono finite .. comparo le lunghezze
		return l1 == l2 ? 0 : (l1 > l2 ? 1 : -1);
	}

	private static final char[] merge(char[]... charlist) {
		int i, j, l = 0;
		char[] ret, temp;
		for (i = 0; i < charlist.length; i++) {
			temp = charlist[i];
			l += temp.length;
		}
		ret = new char[l];
		l = 0;
		for (i = 0; i < charlist.length; i++) {
			temp = charlist[i];
			for (j = 0; j < temp.length; j++) {
				ret[l++] = temp[j];
			}
		}
		return ret;
	}

	public static /* String[] */ void sortGrowing(String[] a) {
		if (a != null) {
			// ret = new String[ a.length ];
			int j = 0, min = 0;
			String t;
			for (int i = 0; i < a.length; i++) {
				min = i;
				for (j = i + 1; j < a.length; j++) {
					if (isStringUnoMinoreDiStringDue(a[j], a[min])) {
						min = j;
					}
				}
				// scambio
				if (min != i) {
					t = a[i];
					a[i] = a[min];
					a[min] = t;
				}
			}
		}
		// return ret;
	}

	public static boolean isStringUnoMinoreDiStringDue(String sj, String smin) {
		boolean sminNull = (smin == null), sjNull = (sj == null);
		boolean ret = sminNull && (sjNull ^ sminNull);
		if ((!ret) && (!sjNull) && (!sminNull)) {
			int iDict = 0, is = 0, iCharSmin = -1, iCharSj = -1;
			char charSmin, charSj;
			boolean devoAncoraCercareCharSj, devoAncoraCercareCharSmin;
			boolean tuttiICaratteriUguali = true; // assumo per ipotesi
			while (tuttiICaratteriUguali && (is < smin.length()) && (is < sj.length())) {
				charSmin = Character.toLowerCase(smin.charAt(is));
				charSj = Character.toLowerCase(sj.charAt(is));
				if (charSmin != charSj) { // se si entra in questo if, il while
											// precedente dovrebbe terminare
					// ricerca nel raccoltaCaratteri di entrambi
					tuttiICaratteriUguali = false;
					iDict = 0;
					iCharSmin = iCharSj = -1; // indici della posizione del
												// carattere nel
												// raccoltaCaratteri
					devoAncoraCercareCharSj = devoAncoraCercareCharSmin = true;
					while ((iDict < raccoltaCaratteri.length)
							&& (devoAncoraCercareCharSj || devoAncoraCercareCharSmin)) {
						devoAncoraCercareCharSmin = (iCharSmin < 0);
						devoAncoraCercareCharSj = (iCharSj < 0); //
						if (devoAncoraCercareCharSmin) {
							if (raccoltaCaratteri[iDict] == charSmin) {
								iCharSmin = iDict;
							}
						}
						if (devoAncoraCercareCharSj) {
							if (raccoltaCaratteri[iDict] == charSj) {
								iCharSj = iDict;
							}
						}
						iDict++;
					}
					// ora verifico se sj � "minore" di smin
					if (iCharSmin < 0) {
						iCharSmin = raccoltaCaratteri.length;
					}
					if (iCharSj < 0) {
						iCharSj = raccoltaCaratteri.length;
					}
					ret = iCharSj < iCharSmin
							&& (iCharSmin != raccoltaCaratteri.length && iCharSj != raccoltaCaratteri.length);
				} // else {
					// tuttiICaratteriUguali &= true;
					// }
				is++;
			}
			if (tuttiICaratteriUguali) {
				if (smin != null && sj != null && (smin.length() > sj.length())) {
					ret = true;
				}
			}
		}
		return ret;
	}

	public static /* String[] */ void sortUnGrowing(String[] a) { // uso il
																	// selection
																	// sort
		// String[] ret = null;
		if (a != null) {
			// ret = new String[ a.length ];
			int j = 0, max = 0;
			String t;
			for (int i = 0; i < a.length; i++) {
				max = i;
				for (j = i + 1; j < a.length; j++) {
					if (isStringUnoMaggioreDiStringDue(a[j], a[max])) {
						max = j;
					}
				}
				// scambio
				if (max != i) {
					t = a[i];
					a[i] = a[max];
					a[max] = t;
				}
			}
		}
		// return ret;
	}

	public static boolean isStringUnoMaggioreDiStringDue(String sj, String smax) {
		boolean smaxNull = (smax == null), sjNull = (sj == null);
		boolean ret = smaxNull && (sjNull ^ smaxNull);
		if ((!ret) && (!sjNull) && (!smaxNull)) {
			int iDict = 0, is = 0, iCharSmax = -1, iCharSj = -1;
			char charSmax, charSj;
			boolean devoAncoraCercareCharSj, devoAncoraCercareCharSmax; // per
																		// la
																		// ricerca
																		// nel
																		// raccoltaCaratteri
			boolean tuttiICaratteriUguali = true; // assumo per ipotesi
			while (tuttiICaratteriUguali && (is < smax.length()) && (is < sj.length())) {
				charSmax = Character.toLowerCase(smax.charAt(is));
				charSj = Character.toLowerCase(sj.charAt(is));
				if (charSmax != charSj) { // se si entra in questo if, il while
											// precedente dovrebbe termaxare
					// ricerca nel raccoltaCaratteri di entrambi
					tuttiICaratteriUguali = false;
					iDict = 0;
					iCharSmax = iCharSj = -1; // indici della posizione del
												// carattere nel
												// raccoltaCaratteri
					devoAncoraCercareCharSj = devoAncoraCercareCharSmax = true;
					while ((iDict < raccoltaCaratteri.length)
							&& (devoAncoraCercareCharSj || devoAncoraCercareCharSmax)) {// (
																						// ||
																						// (iCharSmax
																						// <
																						// 0)
																						// )
																						// )
																						// {
						devoAncoraCercareCharSmax = (iCharSmax < 0) /*
																	 * && ( iDict < smax. length() )
																	 */ ;
						devoAncoraCercareCharSj = (iCharSj < 0) /*
																 * && ( iDict < sj.length() )
																 */ ; //
						if (devoAncoraCercareCharSmax) {
							if (raccoltaCaratteri[iDict] == charSmax) {
								iCharSmax = iDict;
							}
						}
						if (devoAncoraCercareCharSj) {
							if (raccoltaCaratteri[iDict] == charSj) {
								iCharSj = iDict;
							}
						}
						iDict++;
					}
					// ora verifico se sj � "maggiore" di smax
					// rimuovo i seguenti controlli per fare in modo che tutti
					// gli elementi che contengono un carattere che sarebbe
					// dovuto esere
					// if ( iCharSmax < 0 ) { iCharSmax =
					// raccoltaCaratteri.length; }
					// if ( iCharSj < 0 ) { iCharSj = raccoltaCaratteri.length;
					// }
					ret = iCharSj > iCharSmax && (iCharSmax != -1 && iCharSj != -1);
				} // else {
					// tuttiICaratteriUguali &= true;
					// }
				is++;
			}
			if (tuttiICaratteriUguali) { // se si � usciti dal while per la
											// lunghezza delle parole, che erano
											// inizialmente uguali ( acqua e
											// acquamarina )
				if (smax != null && sj != null && (smax.length() < sj.length())) { // ,
																					// la
																					// pi�
																					// lunga
																					// �
																					// la
																					// "maggiore"
					ret = true;
				}
			}
		}
		return ret;
	}

}