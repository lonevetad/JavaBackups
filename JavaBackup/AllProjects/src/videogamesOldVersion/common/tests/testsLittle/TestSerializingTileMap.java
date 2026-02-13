package common.tests.testsLittle;

import java.util.Scanner;

import common.removed.TileMap;
import common.removed.TileMapFactory;
import common.removed.TileMapFactoryWithShapeSpec;
import tools.FileUtilities;
import videogamesOldVersion.common.MainControllerEmpty;
import videogamesOldVersion.common.tests.LoaderTests;

public class TestSerializingTileMap {

	public TestSerializingTileMap() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		MainControllerEmpty mce;
		LoaderGeneric lg;
		TileMap tm;
		TileMapFactory tmf;
		String error, path;
		Object o;
		Scanner scan;

		lg = LoaderTests.getInstance();
		mce = new MainControllerEmpty(lg, null, null);
		tmf = TileMapFactoryWithShapeSpec.newTileMapInstancerRectangle(4, 4);
		tm = tmf.newTileMapInstance(mce, "img");
		System.out.println("Write \'y\' to create the file, anything else to read");

		scan = new Scanner(System.in);
		error = scan.nextLine();
		path = lg.getPath() + "ObjectTest" + lg.getSavesExtension();

		if ("y".equals(error)) {
			error = FileUtilities.writeObject(tm, path);

			if (error == null)
				System.out.println("write done");
			else
				System.err.println(error);
		} else {
			System.out.println("start reading");
			o = FileUtilities.readObject(path);
			if (o != null) {
				if (o instanceof TileMap) {
					tm = (TileMap) o;
					System.out.println("got: " + tm.getName());
				} else
					System.err.println("WTH i've read? " + o.getClass().getName());
			} else
				System.err.println("Null read");
		}

		scan.close();
	}

}