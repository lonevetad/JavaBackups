package dataStructures.tupleSpace;

import java.util.Arrays;

public class TestTuplesSpace {

	public TestTuplesSpace() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		TuplesSpace ts;
		Tuple t;
		TypesDataTupla[] query;
		ts = new TuplesSpace();

		System.out.println("START");
		t = new Tuple(new Tuple.PairTypeValue[] { //
				new Tuple.PairTypeValue(TypesDataTupla.IntNumber, 7), //
				new Tuple.PairTypeValue(TypesDataTupla.String, "test"), //
				new Tuple.PairTypeValue(TypesDataTupla.Bool, false)//
		});
		System.out.println(t.toString());
		System.out.println("adding successfull?: " + ts.addTupla(t));
		ts.forEach((index, tupla) -> {
			if (tupla != null)
				System.out.println(tupla);
			else
				System.out.println("tupla null at: " + index);
		}, true);
		System.out.println("now, real to-string:");
		System.out.println(ts.toString());

		//
		System.out.println("\n\n multiple adds in a row\n");

		t = new Tuple(new Tuple.PairTypeValue[] { //
				new Tuple.PairTypeValue(TypesDataTupla.IntNumber, -3) });
		ts.addTupla(t);
		t = new Tuple(new Tuple.PairTypeValue[] { //
				new Tuple.PairTypeValue(TypesDataTupla.IntNumber, 8) });
		ts.addTupla(t);
		t = new Tuple(new Tuple.PairTypeValue[] { //
				new Tuple.PairTypeValue(TypesDataTupla.IntNumber, 14) });
		ts.addTupla(t);

		t = new Tuple(new Tuple.PairTypeValue[] { //
				new Tuple.PairTypeValue(TypesDataTupla.String, "Hello"), //
				new Tuple.PairTypeValue(TypesDataTupla.String, "World!") //
		});
		ts.addTupla(t);
		t = new Tuple(new Tuple.PairTypeValue[] { //
				new Tuple.PairTypeValue(TypesDataTupla.String, "I'm old: "), //
				new Tuple.PairTypeValue(TypesDataTupla.IntNumber, 23) //
		});
		ts.addTupla(t);

		t = new Tuple(new Tuple.PairTypeValue[] { //
				new Tuple.PairTypeValue(TypesDataTupla.String, "Java"), //
				new Tuple.PairTypeValue(TypesDataTupla.String, "is"), //
				new Tuple.PairTypeValue(TypesDataTupla.String, "a"), //
				new Tuple.PairTypeValue(TypesDataTupla.String, "nice"), //
				new Tuple.PairTypeValue(TypesDataTupla.String, "language") //
		});
		ts.addTupla(t);

		t = new Tuple(new Tuple.PairTypeValue[] { //
				new Tuple.PairTypeValue(TypesDataTupla.Char, '$'), //
				new Tuple.PairTypeValue(TypesDataTupla.IntNumber, (int) '$'), //
				new Tuple.PairTypeValue(TypesDataTupla.String, Integer.toBinaryString('$'))//
		});
		ts.addTupla(t);

		t = new Tuple(new Tuple.PairTypeValue[] { //
				new Tuple.PairTypeValue(TypesDataTupla.IntNumber, 345), //
				new Tuple.PairTypeValue(TypesDataTupla.String, "same type, different vals"), //
				new Tuple.PairTypeValue(TypesDataTupla.Bool, true)//
		});
		ts.addTupla(t);

		//
		System.out.println("\n now, another to-string:");
		System.out.println(ts.toString());

		//
		System.out.println("\n\n now search");
		query = new TypesDataTupla[] { TypesDataTupla.String, TypesDataTupla.IntNumber, TypesDataTupla.Bool };
		System.out.println("has " + Arrays.toString(query) + " ? ");
		System.out.println("tupla found: " + ts.peekTuplaOf(query));
		query = new TypesDataTupla[] { TypesDataTupla.IntNumber, TypesDataTupla.String, TypesDataTupla.Bool };
		System.out.println("has " + Arrays.toString(query) + " ? ");
		System.out.println("maybe yes");
		System.out.println("tupla found: " + ts.peekTuplaOf(query));
		query = new TypesDataTupla[] { TypesDataTupla.Char };
		System.out.println("has " + Arrays.toString(query) + " ? ");
		System.out.println("tupla found: " + ts.peekTuplaOf(query));

		//
		System.out.println("\n\n-----------------------------removing !!");

		query = new TypesDataTupla[] { TypesDataTupla.String, TypesDataTupla.IntNumber, TypesDataTupla.Bool };
		System.out.println("remove " + Arrays.toString(query) + " ? ");
		System.out.println("tupla found: " + ts.removeTupla(query));
		query = new TypesDataTupla[] { TypesDataTupla.IntNumber, TypesDataTupla.String, TypesDataTupla.Bool };
		System.out.println("remove " + Arrays.toString(query) + " ? ");
		System.out.println("maybe yes");
		System.out.println("tupla found: " + ts.removeTupla(query));
		query = new TypesDataTupla[] { TypesDataTupla.Char };
		System.out.println("removes " + Arrays.toString(query) + " ? ");
		System.out.println("tupla found: " + ts.removeTupla(query));
		query = new TypesDataTupla[] { TypesDataTupla.IntNumber };
		System.out.println("removes " + Arrays.toString(query) + " ? ");
		System.out.println("tupla found: " + ts.removeTupla(query));

		System.out.println("\n now, print:");
		System.out.println(ts.toString());
		//
		System.out.println("END");
	}

}
