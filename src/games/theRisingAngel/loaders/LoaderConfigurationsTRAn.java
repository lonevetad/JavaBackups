package games.theRisingAngel.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Objects;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GController;
import games.generic.controlModel.holders.ProbabilityOfContextesHolders;
import games.generic.controlModel.loaders.LoaderConfigurationsRPG;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.subimpl.GameOptionsRPG;
import games.theRisingAngel.GControllerTRAn;
import games.theRisingAngel.GameOptionsTRAn;
import games.theRisingAngel.enums.AllEnumsOfProbabilitiesTRAn;
import tools.Comparators;
import tools.ObjWithRarityWeight;
import tools.ObjectNamedID;
import tools.WeightedSetOfRandomOutcomes;
import tools.json.JSONParser;
import tools.json.types.JSONObject;

/**
 */
public class LoaderConfigurationsTRAn extends LoaderConfigurationsRPG {

	public static final String RESOURCE_REPOSITORY = "TheRisingAngel";
	public static final String RESOURCE_REPOSITORY_PULL_FACT = LoaderGeneric.startPath + RESOURCE_REPOSITORY
			+ File.separatorChar;

	public LoaderConfigurationsTRAn() { super(); }

	@Override
	public LoadStatusResult loadInto(GController gc) {
		GControllerTRAn gcTrar;
		GameOptionsTRAn go;
		LoadStatusResult superRes;
		gcTrar = (GControllerTRAn) gc;

		superRes = LoaderConfigurationsTRAn.super.loadInto(gc);
		if (superRes != LoadStatusResult.Success) { return superRes; }

		go = (GameOptionsTRAn) gcTrar.getGameOptions();
		loadGameOptions(go);

		loadAllRarities(gc);

		return LoadStatusResult.Success;
	}

	@Override
	public void loadGameOptions(GameOptionsRPG gopt) {
		GameOptionsTRAn got;
		Objects.requireNonNull(gopt);
		got = (GameOptionsTRAn) gopt;
		final JSONObject objJSON;
		try {
			final JSONObject subunitsEachMacrounitsRAW;

			objJSON = (JSONObject) JSONParser.parseFile(RESOURCE_REPOSITORY_PULL_FACT + "configurations.json");

			Objects.requireNonNull(objJSON);

			System.out.println(objJSON);

			got.setFactorPriceEssenceExtracting(objJSON.getFieldValue("factorPriceEssenceExtracting").asInt());
			got.setPointsAttributeUpgradeOnLeveling(objJSON.getFieldValue("pointsAttributeUpgradeOnLeveling").asInt());

			subunitsEachMacrounitsRAW = (JSONObject) objJSON.getFieldValue("subunitsEachMacrounits");
			got.setSpaceSubunitsEachUnit(subunitsEachMacrounitsRAW.getFieldValue("space").asInt());
			got.setTimeSubunitsEachUnit(subunitsEachMacrounitsRAW.getFieldValue("time").asInt());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void loadAllRarities(final GController gc) {
		final JSONObject objJSON;
		try {
			final JSONObject probabilityOfContextesHoldersRAW;
			final ProbabilityOfContextesHolders pch;

			objJSON = (JSONObject) JSONParser.parseFile(RESOURCE_REPOSITORY_PULL_FACT + "rarities.json");

			probabilityOfContextesHoldersRAW = (JSONObject) objJSON.getFieldValue("probabilityOfContextesHolders");
			pch = gc.getProbabilityOfContextesHolders();

			probabilityOfContextesHoldersRAW.forEachField((fieldName, valueRAW) -> {
				final ObjectNamedID context; // es: "ItemsRaritiesDefault"
				context = AllEnumsOfProbabilitiesTRAn.valueOf(fieldName);
				if (context != null) {
					final AllEnumsOfProbabilitiesTRAn ctxProb;
					final JSONObject probabilitiesObjRAW;
					final ObjWithRarityWeight[] weights;
					final WeightedSetOfRandomOutcomes wsro;
					final Map<String, ObjWithRarityWeight> mapNameToWeightedValue;

					ctxProb = (AllEnumsOfProbabilitiesTRAn) context;
					weights = ctxProb.getWeightedOutcomes();
					probabilitiesObjRAW = (JSONObject) valueRAW;

					mapNameToWeightedValue = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
							Comparators.STRING_COMPARATOR);
					for (ObjWithRarityWeight w : weights) {
						mapNameToWeightedValue.put(w.getName(), w);
					}
					// for each weight
					probabilitiesObjRAW.forEachField((singleValueName, singleValueRAW) -> {
						ObjWithRarityWeight weightedValue;
						weightedValue = mapNameToWeightedValue.get(singleValueName);
						if (weightedValue != null) { // LOAD IT
							weightedValue.setRarityWeight(singleValueRAW.asInt());
						}
					});

					// after load, store it
					pch.addWeightedIndexesContext(context, wsro = new WeightedSetOfRandomOutcomes(weights));

					System.out.println("in TRAn from loader config, loaded WeightedSetOfRandomOutcomes : " + wsro);
				}
			});

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}