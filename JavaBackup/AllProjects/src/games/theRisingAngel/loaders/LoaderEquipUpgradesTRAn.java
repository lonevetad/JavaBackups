package games.theRisingAngel.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.items.EquipmentUpgrade;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.providers.EquipmentUpgradesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.LoaderEquipUpgrades;
import games.theRisingAngel.GControllerTRAn;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.RaritiesTRAn;
import games.theRisingAngel.enums.TribesTRAn;
import games.theRisingAngel.enums.TribesTRAn.ReligionAlignment;
import games.theRisingAngel.enums.TribesTRAn.Tribe;
import games.theRisingAngel.loaders.factories.FactoryEquipUpgrade;
import games.theRisingAngel.misc.CreatureAttributesTRAn;
import tools.LoggerMessages;
import tools.impl.LoggerOnFile;
import tools.json.JSONParser;
import tools.json.types.JSONObject;

public class LoaderEquipUpgradesTRAn extends LoaderEquipUpgrades {

	// START COMBINATORIC GENERATION SETUP

	public static final boolean ADD_TRIBES_UPGRADES = true;
	// TODO: make it false and implement that case
	public static final boolean RELIGION_ALIGNMENT_CANON_ONLY = false;

	// end COMBINATORIC GENERATION SETUP

	public LoaderEquipUpgradesTRAn(GameObjectsProvider<EquipmentUpgrade> objProvider) { super(objProvider); }

	@Override
	public LoadStatusResult loadInto(GController gc) {
		int[] index = { 0 };
//		JSONArray equipments;
		final LoaderEquipUpgradesTRAn thisLoader = this;

		try {
//			equipments = (JSONArray) JSONParser
//					.parseFile(LoaderConfigurations.RESOURCE_REPOSITORY_PULL_FACT + "equipUpgrades.json");
//
//			equipments.forEach(

			JSONParser.forEachInArray(//
					JSONParser.charactersIteratorFrom(
							new File(LoaderConfigurationsTRAn.RESOURCE_REPOSITORY_PULL_FACT + "equipUpgrades.json")),
					(indexEquipUp, rawEquipUp) -> {
						FactoryEquipUpgrade factory;
						JSONObject equipEquipJSON, attributeModsJSON;
						AttributeModification[] attrMods;
						equipEquipJSON = (JSONObject) rawEquipUp;
						factory = new FactoryEquipUpgrade();
						factory.name = equipEquipJSON.getFieldValue("name").asString();
						factory.rarity = equipEquipJSON.getFieldValue("rarity").asInt();
						factory.bonusPriceSell = equipEquipJSON.getFieldValue("price").asArrayInt();

						attributeModsJSON = (JSONObject) equipEquipJSON.getFieldValue("attributeModifiers");
						attrMods = new AttributeModification[attributeModsJSON.getFieldsAmount()];
						index[0] = 0;
						attributeModsJSON.forEachField((fieldName, attrValueJSON) -> {
							AttributeIdentifier attribute;
							attribute = AttributesTRAn.valueOf(fieldName);
							attrMods[index[0]++] = new AttributeModification(attribute, attrValueJSON.asInt());
						});
						factory.attrMods = attrMods;
						if (equipEquipJSON.hasField("description")) {
							factory.description = equipEquipJSON.getFieldValue("description").asString();
						}

						thisLoader.saveObjectFactory(factory.name, factory.rarity, factory);
					});

		} catch (FileNotFoundException e) {
			gc.getLogger().logException(e);
//			e.printStackTrace();
			return LoadStatusResult.CriticalFail;
		}

		System.out.println("LoaderEquipUpgradesTRAn loader has "
				+ thisLoader.getObjProvider().getObjectsFactoriesCount() + " non tribe counters");

		if (ADD_TRIBES_UPGRADES) {
			final ReligionAlignment[] rel;
			rel = RELIGION_ALIGNMENT_CANON_ONLY ? new ReligionAlignment[] { ReligionAlignment.Canon }
					: ReligionAlignment.values();

//			for(Tribe tribe: TribesTRAn.ALL_TRIBES) {
//				tribe.newEquipmentSet(null)
//				thisLoader.saveObjectFactory(startPath, sc, null);
//			}
			TribesTRAn.MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.forEach((rarity, attrVariationForUpgrade) -> {
				if (attrVariationForUpgrade.isCanBeEquipUpgrade()) {
					for (ReligionAlignment religAlign : rel) {
						for (Tribe tribe : TribesTRAn.ALL_TRIBES) {
							thisLoader.saveObjectFactory(TribesTRAn.getNameEquipUgradeFor(tribe, rarity, religAlign),
									rarity.getIndex(), new EquipUpgradeTribeFactoryTRAn(tribe, rarity, religAlign));
						}
					}
				}
			});
		}
		return LoadStatusResult.Success;
	}

	public static class EquipUpgradeTribeFactoryTRAn implements FactoryObjGModalityBased<EquipmentUpgrade> {
		protected final Tribe tribe;
		protected final RaritiesTRAn rarity;
		protected final ReligionAlignment religionAlignment;

		public EquipUpgradeTribeFactoryTRAn(Tribe tribe, RaritiesTRAn rarity, ReligionAlignment religionAlignment) {
			super();
			this.tribe = tribe;
			this.rarity = rarity;
			this.religionAlignment = religionAlignment;
		}

		public Tribe getTribe() { return tribe; }

		public RaritiesTRAn getRarity() { return rarity; }

		@Override
		public EquipmentUpgrade newInstance(GModality gm) {
			return tribe.newEquipmentUpgradeForRarity(rarity, (GModalityRPG) gm, religionAlignment);
		}
	}

	public static List<String> factoryToLinesString(GModality gm, FactoryObjGModalityBased<EquipmentUpgrade> factory) {
		int rarity, price;
		final List<String> l;
		String name, description;
		AttributeModification[] attrMods;
		l = new LinkedList<>();
		if (factory instanceof FactoryEquipUpgrade) {
			FactoryEquipUpgrade fe;
			fe = (FactoryEquipUpgrade) factory;
			name = fe.name;
			description = fe.description;
			rarity = fe.rarity;
			price = fe.bonusPriceSell[0];
			attrMods = fe.attrMods;
		} else {
			EquipmentUpgrade eu;
			CurrencySet curr;
			eu = factory.newInstance(gm);
			name = eu.getName();
			description = eu.getDescription();
			rarity = eu.getRarityIndex();
			curr = eu.getPricesModifications();
			price = curr.getCurrencyAmount(curr.getCurrencies()[0]);
			attrMods = eu.getAttributeModifiers().toArray(new AttributeModification[eu.getAttributeModifiers().size()]);
		}
		l.add("name:" + name);
		l.add("\trarity :" + rarity);
		l.add("\tprice :" + price);
		l.add("\tdescription: " + description);
		l.add("\tattribute modifications :");
		for (AttributeModification am : attrMods) {
			l.add("\t\t" + am.getName() + " -> " + am.getValue());
		}

		if (factory instanceof EquipUpgradeTribeFactoryTRAn) {
			EquipUpgradeTribeFactoryTRAn fa;
			fa = (EquipUpgradeTribeFactoryTRAn) factory;
			l.add("\ttribe: " + fa.tribe.name());
			l.add("\treligionAlignment: " + fa.religionAlignment.name());
		}
		return l;
	}

	public static void main(String[] args) {
		int size;
		int[] rarities, priceStatistics;
//		LoaderEquipUpgradeFromFile leuff;
//		LinkedList<FactoryEquipUpgrade> factories;
//		FactoryEquipUpgrade fe;
		LoaderEquipUpgradesTRAn loader;
		EquipmentUpgradesProvider equipUpgradeProvider;
		Map<String, FactoryObjGModalityBased<EquipmentUpgrade>> allObjectFactories;
		final CreatureAttributes caTotal, caEachRarity[];
		final RaritiesTRAn[] RARITIES;
		final LoggerMessages log;

		GControllerTRAn gc;
		GModalityTRAnBaseWorld gm;

		{
			/*
			 * this temporary variable required because the compiler thinks that the
			 * assignement in the "catch" branch could follow a preceeding assigmento to
			 * "log", even if the exception rises, in case of error, some lines before it.
			 */
			LoggerMessages logTemp;
			try {
				LoggerOnFile logOnFile;
				logOnFile = new LoggerOnFile(//
						LoggerMessages.CONSOLE_BASE_PATH + File.separatorChar + "testsManualOutput" //
								+ File.separatorChar + "outputLoaderEquipUpgradeTRAn.txt");
				logTemp = LoggerMessages.loggerOrDefault(logOnFile);
			} catch (IOException e) {
				logTemp = LoggerMessages.LOGGER_DEFAULT;
				e.printStackTrace();
			}
			log = logTemp;
		}

		RARITIES = RaritiesTRAn.values();

		gc = new GControllerTRAn();
		gm = (GModalityTRAnBaseWorld) gc.newModalityByName(GModalityTRAnBaseWorld.NAME);
		Objects.requireNonNull(gm);

		equipUpgradeProvider = new EquipmentUpgradesProvider();
		loader = new LoaderEquipUpgradesTRAn(equipUpgradeProvider);
		caTotal = new CreatureAttributesTRAn();

		log.logAndPrint("Starting reading Equip Upgrades TRAn");
		log.logAndPrint("\n");

		loader.loadInto(gc);
		allObjectFactories = loader.objProvider.getObjectsIdentified();
		size = allObjectFactories.size();
		rarities = new int[RARITIES.length];
		Arrays.fill(rarities, 0);
		caEachRarity = new CreatureAttributesTRAn[rarities.length];
		for (int i = caEachRarity.length - 1; i >= 0; i--) {
			caEachRarity[i] = new CreatureAttributesTRAn();
		}
		priceStatistics = new int[] { 0, 0, 0 };

		allObjectFactories.forEach((name, factoryEquip) -> {
			int rarity, price;
			AttributeModification[] attrMods;
			CreatureAttributes caTempRarity;

			if (factoryEquip instanceof FactoryEquipUpgrade) {
				FactoryEquipUpgrade fe;
				fe = (FactoryEquipUpgrade) factoryEquip;
				/*
				 * log.logAndPrint("\n"); log.logAndPrint(fe.toString()); log.logAndPrint("\n");
				 */
				rarity = fe.rarity;
				price = fe.bonusPriceSell[0];
				attrMods = fe.attrMods;
			} else {
				EquipmentUpgrade eu;
				CurrencySet curr;
				eu = factoryEquip.newInstance(gm);
				rarity = eu.getRarityIndex();
				curr = eu.getPricesModifications();
				price = curr.getCurrencyAmount(curr.getCurrencies()[0]);

				attrMods = eu.getAttributeModifiers()
						.toArray(new AttributeModification[eu.getAttributeModifiers().size()]);
			}

			rarities[rarity]++;
			priceStatistics[2] += price;
			if (price >= 0) {
				priceStatistics[0]++;
			} else {
				priceStatistics[1]++;
			}

			caTempRarity = caEachRarity[rarity];
			for (AttributeModification am : attrMods) {
				caTotal.applyAttributeModifier(am);
				caTempRarity.applyAttributeModifier(am);
			}
		});

		log.logAndPrint("total equip upgrades: " + size);
		log.logAndPrint("\n");
		log.logAndPrint("Which " + priceStatistics[0] + " are positive, and " + priceStatistics[1] + " are negative.");
		log.logAndPrint("\n");
		log.logAndPrint("Price total: " + priceStatistics[2]);
		log.logAndPrint("\n");
		log.logAndPrint("RARITIES proportion:");
		log.logAndPrint("\n");
		log.logAndPrint(Arrays.toString(rarities));
		log.logAndPrint("\n");

		CreatureAttributes caTempRarity;
		for (int i = 0; i < RARITIES.length; i++) {
			log.logAndPrint("\n\n------\nTotal attributes, summing all upgrades' modifications, for rarity "
					+ RARITIES[i].name().toUpperCase() + " :");
			log.logAndPrint("\n");
			caTempRarity = caEachRarity[i];
			for (AttributesTRAn attTRan : AttributesTRAn.ALL_ATTRIBUTES) {
				log.logAndPrint(attTRan.getName() + " -> " + caTempRarity.getValue(attTRan));
				log.logAndPrint("\n");
			}
		}

		log.logAndPrint("\n\n\n----------------------\nTotal attributes, summing all upgrades' modifications:\n");
		for (AttributesTRAn attTRan : AttributesTRAn.ALL_ATTRIBUTES) {
			log.logAndPrint(attTRan.getName() + " -> " + caTotal.getValue(attTRan));
			log.logAndPrint("\n");
		}
	}
}