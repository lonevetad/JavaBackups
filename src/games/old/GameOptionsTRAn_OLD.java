package games.old;

import games.generic.GameOptions;
import games.generic.controlModel.GController;
import games.generic.controlModel.holders.ProbabilityOfContextesHolders;
import games.generic.controlModel.misc.LoaderGeneric;
import games.generic.controlModel.misc.LoaderGeneric.JSONFileConsumer;
import games.theRisingAngel.enums.AllEnumsOfProbabilitiesTRAn;
import tools.WeightedSetOfRandomOutcomes;

public class GameOptionsTRAn_OLD extends GameOptions {
	protected ProbabilityOfContextesHolders probabilitiesContexes;
	protected JSONFileConsumer jsonReader;
//	JSONLineReader lr;

	protected GameOptionsTRAn_OLD() {
		jsonReader = new JSONFileConsumer("", "configurationsTRAr.json") {

			@Override
			protected void readAllFileImpl(String line) { readAllFile_GOT(line); }
		};
		probabilitiesContexes = new ProbabilityOfContextesHolders();
	}

	protected void readAllFile_GOT(String line) {
		int indexComma;
		String nameWeightedIndexes, weightsString;
		int[] weights;
		String splitted[];
		System.out.println("read line: " + line);
		if (line.contains("probabilityOfContextesHolders")) {
			do {
				line = jsonReader.getLineReader().next().trim();
				indexComma = line.indexOf(':');
				if (indexComma >= 0) {
//						splitted = line.split(":"); //
					splitted = new String[] { line.substring(0, indexComma), line.substring(indexComma + 1) };
					LoaderGeneric.trimAll(splitted);

					// recycle "line"
					nameWeightedIndexes = splitted[0];
					nameWeightedIndexes = nameWeightedIndexes
							.substring(nameWeightedIndexes.indexOf('"') + 1, nameWeightedIndexes.lastIndexOf('"'))
							.trim();
					weightsString = splitted[1].trim();
					splitted = weightsString.substring(1, weightsString.lastIndexOf(']')).split(",");

					weights = new int[splitted.length];
					for (int i = 0, n = splitted.length; i < n; i++) {
						weights[i] = Integer.parseInt(splitted[i].trim());
					}
					probabilitiesContexes.addWeightedIndexesContext(
							AllEnumsOfProbabilitiesTRAn.valueOf(nameWeightedIndexes),
							new WeightedSetOfRandomOutcomes(weights));
				}
			} while (!line.contains("]"));
		}
		// else .. other options
	}

	@Override
	public void loadConfig(GController gc) { jsonReader.readAllFile(); }
}