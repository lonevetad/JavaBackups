package tests.tDataStruct;

import java.util.Arrays;

import dataStructures.EditCosts;
import tools.Comparators;
import tools.EditDistance;
import tools.EditDistance.EqualityChecker;
import tools.impl.EditDistanceLevenshtein;

public class TestEditDistance {
	public static class S {
		public final int distExpected;
		public final String s1, s2;

		public S(String s1, String s2, int dist) {
			super();
			this.s1 = s1;
			this.s2 = s2;
			this.distExpected = dist;
		}
	}

	public static S s(String s1, String s2, int d) { return new S(s1, s2, d); }

	public static Character[] c(String s) {
		int len;
		Character[] cc;
		cc = new Character[len = s.length()];
		while (--len >= 0)
			cc[len] = Character.valueOf(s.charAt(len));
		return cc;
	}

	public static int d(S s) { return ED.editDistance(c(s.s1), c(s.s2), EC_CHAR, CAC); }

	public static void test(S s) {
		int diff;
//		System.out.println();
		diff = d(s);
		if (diff != s.distExpected) {
			System.out.println("ERROR: expected distance " + s.distExpected + ", got " + diff + " over:\n\t<< " + s.s1
					+ " >> &\n\t<< " + s.s2 + " >>");
		}
	}

	//

	public static final EditDistance ED = new EditDistanceLevenshtein();
	public static final EditCosts<Character> CAC = EditCosts.newDefaultCosts();
	public static final EqualityChecker<Character> EC_CHAR = EqualityChecker
			.fromComparator(Comparators.CHAR_COMPARATOR);

	public static final S[] S_TO_TEST = { //
			s("ciao", "ciao", 00000), //
			s("", "", 0), //
			s("ciao", "", 4), //
			s("", "ciao", 4), //
			s("ciao", "f", 4), //
			s("f", "ciao", 4), //
			s("a", "", 1), //
			s("", "a", 1), //
			s("a", "a", 0), //
			s("ab", "", 2), //
			s("", "ab", 2), //
			s("ab", "ab", 0), //
			//
			s("ciao", "casa", 3), //
			s("ciao", "ciai", 1), //
			s("ciao", "miao", 1), //
			s("cioo", "ciao", 1), //
			s("ciao", "ceao", 1), //
			s("ciao", "ciaoo", 1), //
			s("ciao", "iao", 1), //
			s("ci", "ciao", 2), //
			s("ciao", "ciaociao", 4), //
			s("iao", "cio", 2), //
			s("sunday", "saturday", 3), //
			s("java", "c", 4), //
			s("java", "python", 6), //
			s("java", "lava", 1), //
			s("java", "avaj", 2), //
			s("onetest", "testone", 6), //
			s("one test", "test one", 7), //
			s("industry", "interest", 6), //
			s("sdgslkbshbca<fbvj", "afv nhgnjksnf", 16), //
			s("wqekwiojwrioqwkekqiwqiqwoje", "qwoekqiorkqwpoekwqepqqpoeksod", 19), //

	};

	public static void main(String[] args) {
		System.out.println("start test");
		for (S stt : S_TO_TEST)
			test(stt);
		System.out.println("end test");

		System.out.println(ED.editDistance(//
				Arrays.asList(8, 5, 2, 4, 86, 0, 6), //
				Arrays.asList(2, 1, 8, 5, 2, 4, 8, 6, 0, 7, 6),
				// == 5
				EditDistance.EqualityChecker.fromComparator(Comparators.INTEGER_COMPARATOR)));
	}
}