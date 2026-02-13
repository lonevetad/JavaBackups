package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.function.BiConsumer;

import tools.json.JSONParser;
import tools.json.JSONValue;

public class TestJSONParser {

	public static void main(String[] args) {
		String[] texts = { "4", //
				"  4", //
				"\"ciao\"", //
				"\"ciao mondo\"", //
				"true", //
				"3.1459264", //
				"285735823957059891022348", //
				//
				"[]", //
				"[5]", //
				"[ 6,   7,\t8] ", //
				"[ 6, \t 15, \n \"8\", -6,] ", //
				"[ 6, \t 15, \n \"8\", -6, false] ", //
				//
				"{}", //
				"{ \"phy\" : 1.61680339}", //
				"{ \"phi\" : 1.61680339 }", //
				"{ \"pi\" : 3.14159265, }", //
				"{ \"name\" : \"Mario\", \"lastname\": \"ROssi\" }", //
				"{ \"hotel ID\" : \"sdg4s8g54g84w0g\", \"description\": {\"it\": \"molto brutto\" , \"en\": \"ugly hotel\", } , \"price\": 7e2}", //
				"{    \"name\": \"of the Hurry one\", \"rarity\": 1, \"price\": [4\n ],  \"attributeModifiers\": {    \"Strength\": 3,    \"Precision\": -2,   \"ProbabilityPerThousandAvoidPhysical\": 15, \"ProbabilityPerThousandAvoidMagical\": 15,    \"ProbabilityPerThousandHitPhysical\": -20, \"ProbabilityPerThousandHitMagical\": -20, \"CriticalProbabilityPerThousand\": 10 } }", //
				//
				"[ {\"name\": \"meow\",\"animal type\":\"cat\"} ,\n {\"name\": \"bau\",\"animal type\":\"dog\", \"age\":7 } , {\"name\": \"chip\",\n \"animal type\":\"bird\", \"color\": \"blue\"}  ]" //
		};

		JSONValue o;
		for (String t : texts) {
			System.out.println("\n\nparsing: ----" + t + "----");
			o = JSONParser.parse(t);
			System.out.println("obtained:");
			System.out.println(o);
			System.out.println("type: " + o.getType().name());
		}

		System.out.println("\n\n FINE test stringhe statiche");

		String basePath, fullPath, outputPath;
		basePath = FileSystems.getDefault().getPath(".").toAbsolutePath().toString();
		basePath = basePath.substring(0, basePath.length() - 2);
		fullPath = basePath + File.separatorChar + "resources" + File.separatorChar + "TheRisingAngel"
				+ File.separatorChar + "equipUpgrades.json";

		outputPath = basePath + File.separatorChar + "testsManualOutput" + File.separatorChar + "testJsonParser.json";
		try {
			System.out.println("opening file at folder:");
			System.out.println(basePath);
			System.out.println("file: " + fullPath);
			o = JSONParser.parseFile(fullPath);
			System.out.println(o.getType());
//			System.out.println(Arrays.toString(o.asArrayObject()));

			FileWriter wr;
			wr = new FileWriter(outputPath);
			wr.append(o.toString());
			wr.flush();
			wr.close();
			wr = null;
			System.out.println("saved into file");

			o = JSONParser.parseFile(fullPath);

			o = JSONParser.parse(o.toString());
			System.out.println(o.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n\n\nFINE");

		String arrayExample = texts[texts.length - 1];
		BiConsumer<Integer, JSONValue> printer;

		printer = (index, element) -> {
			if (element != null) {
				System.out.print("type " + element.getType() + " --> ");
				System.out.println(element);
			} else {
				System.out.println("NULL");
			}
		};
		System.out.println("\n\n\n START TEST ARRAY & STREAM WITH:");
		System.out.println(arrayExample);
		System.out.println();
		System.out.println("\n\ntest array");
		JSONParser.forEachInArray(JSONParser.charactersIteratorFrom(arrayExample), printer);

		System.out.println("\n\n test stream");
		JSONParser.forEachInArray(
				JSONParser.charactersIteratorFrom(arrayExample.chars().mapToObj(b -> Character.valueOf((char) b))),
				printer);

		System.out.println("\n\nfor-eaching the whole file");

		try {
			JSONParser.forEachInArray(JSONParser.charactersIteratorFrom(new File(fullPath)), printer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}