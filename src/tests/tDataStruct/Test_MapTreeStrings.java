package tests.tDataStruct;

import dataStructures.MapTreeAVL;
import tools.Comparators;

public class Test_MapTreeStrings {

	public Test_MapTreeStrings() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		MapTreeAVL<String, String> m;
		String[] strings;
		m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, MapTreeAVL.BehaviourOnKeyCollision.Replace,
				Comparators.STRING_COMPARATOR);
		m.put("ciao", "ciao");
		m.put("ciao", "ciao");
		System.out.println(m);
		System.out.println("\n\n now adding strings");
		strings = new String[] { "ciao", "mondo", "come", "ciao", "stai?" };
		for (String s : strings)
			m.put(s, s);
		System.out.println(m);
	}

}
