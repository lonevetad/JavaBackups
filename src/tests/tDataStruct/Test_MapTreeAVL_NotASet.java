package tests.tDataStruct;

import dataStructures.MapTreeAVL;

public class Test_MapTreeAVL_NotASet {

	public Test_MapTreeAVL_NotASet() {
	}

	public static void main(String[] args) {
		MapTreeAVL<Integer, String> m;
		Integer sameKey;
		m = MapTreeAVL.newMap(MapTreeAVL.BehaviourOnKeyCollision.AddItsNotASet, Integer::compare);
		sameKey = 7;
		System.out.println("Start:");
		System.out.println(m.toString());
		System.out.println("++++++++++++++++++++++");
		for (int i = 0; i < 16; i++) {
			m.put(sameKey, "test+" + i);
			System.out.println("--------------------");
			System.out.println(m.toString());
		}
		System.out.println("#######################");
		System.out.println("now get: " + m.apply(sameKey));
		System.out.println("END");
	}

}
