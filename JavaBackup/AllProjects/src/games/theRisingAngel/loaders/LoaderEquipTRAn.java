package games.theRisingAngel.loaders;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.loaders.ObjectLoadable;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.providers.EquipItemProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.LoaderEquipments;
import games.theRisingAngel.GControllerTRAn;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EquipmentTypesTRAn;
import games.theRisingAngel.enums.RaritiesTRAn;
import games.theRisingAngel.enums.TribesTRAn;
import games.theRisingAngel.enums.TribesTRAn.ReligionAlignment;
import games.theRisingAngel.enums.TribesTRAn.Tribe;
import games.theRisingAngel.inventory.EquipItemTRAn;
import games.theRisingAngel.inventory.equipsWithAbilities.ArmProtectionShieldingDamageByMoney;
import games.theRisingAngel.inventory.equipsWithAbilities.HelmetOfPlanetaryMeteors;
import games.theRisingAngel.inventory.equipsWithAbilities.NecklaceOfPainRinvigoring;
import games.theRisingAngel.loaders.factories.FactoryEquip;
import games.theRisingAngel.loaders.factories.FactoryItems;
import tools.LoggerMessages;
import tools.impl.LoggerOnFile;
import tools.json.JSONParser;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONArray;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

public class LoaderEquipTRAn extends LoaderEquipments implements ObjectLoadable {
	private static final long serialVersionUID = 1L;

	// START COMBINATORIC GENERATION SETUP
	public static enum TribeEquipLoadMode {
		Ignore, SetGrouped, EachItemsIndividually
	}

	public static final TribeEquipLoadMode TRIBE_EQUIP_LOAD_MODE = TribeEquipLoadMode.EachItemsIndividually;
	// TODO: make it false and implement that case
	public static final boolean RELIGION_ALIGNMENT_CANON_ONLY = false;
	protected static final ReligionAlignment[] RELIGION_ALIGMENTS_TO_LOAD;
	static {
		RELIGION_ALIGMENTS_TO_LOAD = RELIGION_ALIGNMENT_CANON_ONLY ? new ReligionAlignment[] { ReligionAlignment.Canon }
				: ReligionAlignment.values();
	}

	// END COMBINATORIC GENERATION SETUP

	public LoaderEquipTRAn(GameObjectsProvider<EquipmentItem> objProvider) {
		super(objProvider);
		this.tribesFullSetDropStates = new EnumMap<>(Tribe.class);
		neverLoadedTribeSets = true;
	}

	protected boolean neverLoadedTribeSets;
	protected Map<Tribe, EquipTRAnFullSetFactory> tribesFullSetDropStates;

	@Override
	public LoadStatusResult loadInto(GController gc) {
		final LoaderEquipTRAn thisLoader = this;
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Physical));
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Magical));
//		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, gmm -> new AMoreDamageReceivedMoreLifeRegen());
		objProvider.addObj(ArmProtectionShieldingDamageByMoney.NAME,
				(gm) -> new ArmProtectionShieldingDamageByMoney((GModalityRPG) gm));
		objProvider.addObj(NecklaceOfPainRinvigoring.NAME, //
				(gm) -> new NecklaceOfPainRinvigoring((GModalityRPG) gm));

		objProvider.addObj(HelmetOfPlanetaryMeteors.NAME, (gm) -> new HelmetOfPlanetaryMeteors((GModalityRPG) gm));

		try {
			final int[] index = { 0 };
//			JSONArray equips;
//			equips = (JSONArray) JSONParser
//					.parseFile(LoaderConfigurations.RESOURCE_REPOSITORY_PULL_FACT + "equipItems.json");
//
//			equips.forEach(
			JSONParser.forEachInArray(//
					JSONParser.charactersIteratorFrom(
							new File(LoaderConfigurationsTRAn.RESOURCE_REPOSITORY_PULL_FACT + "equipItems.json")),
					(indexEquip, rawEquip) -> {
						// TODO
						FactoryEquip factory;
						FactoryItems factoryItem;
						JSONObject equipEquipJSON, attributeModsJSON;
						AttributeModification[] attrMods;
						equipEquipJSON = (JSONObject) rawEquip;
						factory = new FactoryEquip();
						factoryItem = factory.getFactoryItem(); // it's a delegator that recycles code
						factoryItem.name = equipEquipJSON.getFieldValue("name").asString();
						factoryItem.rarity = equipEquipJSON.getFieldValue("rarity").asInt();
						factoryItem.price = equipEquipJSON.getFieldValue("price").asArrayInt();

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
							factoryItem.description = equipEquipJSON.getFieldValue("description").asString();
						}

						{ // abilities
							JSONValue a;
							JSONArray abilArray;
							a = equipEquipJSON.getFieldValue("abilities");
							if (a != null) {
								if (a.isType(JSONTypes.ArrayHomogeneousType)
										&& (abilArray = (JSONArray) a).length() > 0) {
									factory.abilities = new ArrayList<>(abilArray.length());
									abilArray.forEach((indexAbil, abilityDataRaw) -> {
										JSONObject ao;
										ao = (JSONObject) abilityDataRaw;
										FactoryEquip.AbilityData data;
										data = new FactoryEquip.AbilityData();
										data.name = ao.getFieldValue("name").asString();
										if (ao.hasField("level")) {
											data.level = ao.getFieldValue("level").asInt();
										} else {
											data.level = 0;
										}
										factory.abilities.add(data);
									});
								}
							}
						}
						{
							JSONObject dimensionRaw = (JSONObject) equipEquipJSON.getFieldValue("dimensionInventory");
							factoryItem.dimensionInInventory = new Dimension(
									dimensionRaw.getFieldValue("width").asInt(),
									dimensionRaw.getFieldValue("height").asInt());
						}
						try {
							factory.type = EquipmentTypesTRAn.valueOf(equipEquipJSON.getFieldValue("type").asString());
						} catch (Exception e) {
							System.out.println("Unknown type for equip ++" + factoryItem.name + "++ : --"
									+ equipEquipJSON.getFieldValue("type") + "--");
							e.printStackTrace();
							factory.type = EquipmentTypesTRAn.Special;
						}
						thisLoader.saveObjectFactory(factoryItem.name, factory);
					});

		} catch (FileNotFoundException e) {
			gc.getLogger().logException(e);
			return LoadStatusResult.CriticalFail;
		}

//		leff = new LoaderEquipFromFile("", "equipItems.json");
//		leff.readAllFile();
//		log.logAndPrint("total equip loaded: " + leff.factories.size());
//		for (FactoryEquip fe : leff.factories) {
//			objProvider.addObj(fe.getFactoryItem().name, fe.getFactoryItem().rarity, fe);
//		}

		if (neverLoadedTribeSets) {
			switch (TRIBE_EQUIP_LOAD_MODE) {
			case EachItemsIndividually: {
				int rarityIndex;
				rarityIndex = TribesTRAn.RARITY_TRIBE_EQUIPMENT_PIECES.getIndex();
				TribesTRAn.ALL_EQUIP_TYPES_ON_TRIBE_SETS.forEach(equipType -> {
					for (Tribe tribe : TribesTRAn.ALL_TRIBES) {
						for (ReligionAlignment relAl : RELIGION_ALIGMENTS_TO_LOAD) {
							thisLoader.saveObjectFactory(TribesTRAn.getNameEquipFor(tribe, equipType), rarityIndex,
									new EquipTRAnFactoryTribeBased(tribe, equipType, relAl));
						}
					}
				});
				break;
			}
			case SetGrouped: {
				int rarityIndex;
				rarityIndex = TribesTRAn.RARITY_TRIBE_EQUIPMENT_PIECES.getIndex();
				for (Tribe tribe : TribesTRAn.ALL_TRIBES) {
					EquipTRAnFullSetFactory fullSetFactory;
					fullSetFactory = new EquipTRAnFullSetFactory(tribe);
					tribesFullSetDropStates.put(tribe, fullSetFactory);
					thisLoader.saveObjectFactory(tribe.getName(), rarityIndex, fullSetFactory);
				}
				break;
			}
			case Ignore: {
				break;
			}
			default:
				if (TRIBE_EQUIP_LOAD_MODE != null) {
					throw new IllegalArgumentException("Unexpected value: " + TRIBE_EQUIP_LOAD_MODE);
				}
			}
			neverLoadedTribeSets = false;
		}
		System.out.println("::: LOADED " + thisLoader.objProvider.getObjectsFactoriesCount() + " equip factories");
		return LoadStatusResult.Success;
	}

	@Override
	public JSONValue toJSON() {
		final JSONObject o;
		Function<EquipmentTypesTRAn, JSONString> equipToJSONStringConverter;

		o = new JSONObject();
		equipToJSONStringConverter = (eqType) -> { return new JSONString(eqType.getName()); };

		// produces all amount of pieces lef
		tribesFullSetDropStates.forEach((t, f) -> {

			JSONObject objSingleTribe;
			objSingleTribe = new JSONObject();

			for (ReligionAlignment relAl : RELIGION_ALIGMENTS_TO_LOAD) {

				objSingleTribe.addField(relAl.name(),
						// f.piecesNotYetDropped.toSetValue(EquipmentItem::getEquipmentType)
						new JSONArray( // ARRAY OF ALL OF THE EQUIPMENT's NAMES LEFT
								JSONTypes.ArrayHomogeneousType, //
								new SetMapped<EquipmentTypesTRAn, JSONString>(//
										f.piecesNotYetDroppedByReligionAlign[relAl.ordinal()].keySet(),
										equipToJSONStringConverter)//
												.toArray(new JSONString[f.piecesNotYetDroppedByReligionAlign[relAl
														.ordinal()].size()]), //
								JSONTypes.String)//
				);
			}
			o.addField(t.getName(), objSingleTribe);
		});
		return o;
	}

	//

	public static class EquipTRAnFullSetFactory implements FactoryObjGModalityBased<EquipmentItem> {
		protected final Tribe tribe;
		protected final static Random rand = GController.RANDOM;
		protected final MapTreeAVL<EquipmentTypesTRAn, EquipItemTRAn>[] piecesNotYetDroppedByReligionAlign;
		protected final int[] piecesLeftCacheByReligionAlign; // stores the caches

		@SuppressWarnings("unchecked")
		public EquipTRAnFullSetFactory(Tribe tribe) {
			super();
			this.tribe = tribe;
//			piecesNotYetDropped = null;
			this.piecesNotYetDroppedByReligionAlign = new MapTreeAVL[RELIGION_ALIGMENTS_TO_LOAD.length];
			Arrays.fill(this.piecesNotYetDroppedByReligionAlign, null);
			this.piecesLeftCacheByReligionAlign = new int[RELIGION_ALIGMENTS_TO_LOAD.length];
			Arrays.fill(this.piecesLeftCacheByReligionAlign, 0);
		}

//		ReligionAlignment
		protected void checkAndRefill(GModalityRPG gmrpg, ReligionAlignment relAl) {
//			MapTreeAVL<EquipmentTypesTRAn, EquipmentItem> pnyd = this.piecesNotYetDropped;
//			if (pnyd.isEmpty()) {
//				TribesTRAn.ALL_EQUIP_TYPES_ON_TRIBE_SETS.forEach(
//						(EquipmentTypesTRAn equipTypePiece) -> { pnyd.put(equipTypePiece.getID(), equipTypePiece); });
//			}

			final int indexReligionAlignment;
			final MapTreeAVL<EquipmentTypesTRAn, EquipItemTRAn> newSetOfPieces;
			indexReligionAlignment = relAl.ordinal();
			if (this.piecesNotYetDroppedByReligionAlign[indexReligionAlignment] == null || //
					this.piecesLeftCacheByReligionAlign[indexReligionAlignment] <= 0 // piecesNotYetDropped.isEmpty()
			) {
				newSetOfPieces = tribe.newEquipmentSet(gmrpg, relAl);
				this.piecesNotYetDroppedByReligionAlign[indexReligionAlignment] = newSetOfPieces;
				this.piecesLeftCacheByReligionAlign[indexReligionAlignment] = newSetOfPieces.size();
			}
		}

		protected void checkAndRefill(GModalityRPG gmrpg) {
			int sizeTotal;
			sizeTotal = 0;
			for (int sizeCached : this.piecesLeftCacheByReligionAlign) {
				sizeTotal += sizeCached;
			}
			if (sizeTotal <= 0) { // empty
				// refill all
				for (ReligionAlignment relAl : RELIGION_ALIGMENTS_TO_LOAD) {
					this.checkAndRefill(gmrpg, relAl);
				}
			}
		}

		@Override
		public EquipmentItem newInstance(GModality gm) {
			int size, indexReligAlign;
			MapTreeAVL<EquipmentTypesTRAn, EquipItemTRAn> pnyd;
			Entry<EquipmentTypesTRAn, EquipItemTRAn> e;
//			EquipmentTypesTRAn equipType;

			this.checkAndRefill((GModalityRPG) gm);

			// try to provide the most uniform distribution possible
			size = 0;
			for (ReligionAlignment relAl : RELIGION_ALIGMENTS_TO_LOAD) {
				size += this.piecesLeftCacheByReligionAlign[relAl.ordinal()];
			}
			/**
			 * Recycle "size" as the random index, if we imagine to align all pieces left
			 * and assign them an index from 0 to total-1.<br>
			 * "piecesLeftCacheByReligionAlign" are then the "index offset" where the next
			 * group of pieces will begin (starting right after the previous one, if any).
			 */
			if (size < 1) {
				// pick the only one item left, no need to use the random
				size = 0; // the only available index
			} else {
				size = rand.nextInt(size);
			}
			e = null;
			indexReligAlign = 0;
			while (e == null) {
				if (size < this.piecesLeftCacheByReligionAlign[indexReligAlign]) {
					pnyd = this.piecesNotYetDroppedByReligionAlign[indexReligAlign];
					e = pnyd.getAt(size);
					this.piecesLeftCacheByReligionAlign[indexReligAlign]--;
					if (pnyd.size() == 1) {
						pnyd.clear();
						this.piecesNotYetDroppedByReligionAlign[indexReligAlign] = null;
					} else {
						pnyd.remove(e.getKey());
					}
				} else {
					indexReligAlign++; // try the next religion alignment (and offset)
					// remove the offset
					size -= this.piecesLeftCacheByReligionAlign[indexReligAlign];
				}
			}

			return e.getValue();
		}

	}

	public static class EquipTRAnFactoryTribeBased implements FactoryObjGModalityBased<EquipmentItem> {
		protected final Tribe tribe;
		protected final EquipmentTypesTRAn equipType;
		protected final ReligionAlignment religionAlignment;

		public EquipTRAnFactoryTribeBased(Tribe tribe, EquipmentTypesTRAn equipType,
				ReligionAlignment religionAlignment) {
			super();
			this.tribe = tribe;
			this.equipType = equipType;
			this.religionAlignment = religionAlignment;
		}

		@Override
		public EquipmentItem newInstance(GModality gm) {
			return this.tribe.newEquipmentPiece((GModalityRPG) gm, equipType);
		}
	}

	//

	public static void main(String[] args) {
		int size, raritiesAmount;
		int[] rarities, raritiesEachEquipType; // let's collect some statistics
		RaritiesTRAn[] allRarities;
//		LoaderEquipFromFile leff;
//		LinkedList<FactoryEquip> factories;
//		FactoryEquip fe;
		LoaderEquipTRAn loader;
		EquipItemProvider equipItemProvider;
		Map<String, FactoryObjGModalityBased<EquipmentItem>> allObjectFactories;
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
								+ File.separatorChar + "outputLoaderEquipTRAn.txt");
				logTemp = LoggerMessages.loggerOrDefault(logOnFile);
			} catch (IOException e) {
				logTemp = LoggerMessages.LOGGER_DEFAULT;
				e.printStackTrace();
			}
			log = logTemp;
		}

		allRarities = RaritiesTRAn.values();

		gc = new GControllerTRAn();
		gm = (GModalityTRAnBaseWorld) gc.newModalityByName(GModalityTRAnBaseWorld.NAME);
		Objects.requireNonNull(gm);

		equipItemProvider = new EquipItemProvider();
		loader = new LoaderEquipTRAn(equipItemProvider);

		log.logAndPrint("Starting reading Equip TRAn");
		loader.loadInto(gc);
		allObjectFactories = loader.objProvider.getObjectsIdentified();
		size = allObjectFactories.size();

		raritiesAmount = allRarities.length;
		rarities = new int[raritiesAmount];
		raritiesEachEquipType = new int[ //
		/**
		 * All rarities plus the total amount of times an equip type is found
		 */
		(raritiesAmount + 1) //
				* EquipmentTypesTRAn.ALL_EQUIP_TYPES_TRAn.length //
		];
		Arrays.fill(rarities, 0);
		Arrays.fill(raritiesEachEquipType, 0);

		allObjectFactories.forEach((name, factoryEquip) -> {
			try {
				int rarity;
				EquipmentTypesTRAn eqType;
				rarity = -1;
				eqType = null;
				if (factoryEquip instanceof FactoryEquip) {
					FactoryEquip fe;
					FactoryItems fi;
					fe = (FactoryEquip) factoryEquip;
//				log.logAndPrint(fe.toString());
//				log.logAndPrint("\n");
					fi = fe.getFactoryItem();
					rarity = fi.rarity;
					eqType = fe.type;
				} else {
					try {
						EquipmentItem ei;
						ei = factoryEquip.newInstance(gm);
						rarity = ei.getRarityIndex();
						eqType = (EquipmentTypesTRAn) ei.getEquipmentType();
						// log.logAndPrint(ei.toString());
					} catch (Exception e) {
						log.logAndPrint("ERROR WITH EQUIP: ");
						log.logAndPrint(name);
						log.logAndPrint("\n");
						log.logException(e);
						log.logAndPrint("\n");
					}
				}
				if (eqType != null) {
					int equipIndexOnMatrix;

					rarities[rarity]++;

					equipIndexOnMatrix = eqType.getIndex() * (raritiesAmount + 1);
					raritiesEachEquipType[equipIndexOnMatrix + rarity]++; // trace the rarity
					raritiesEachEquipType[equipIndexOnMatrix + raritiesAmount]++; // trace the equip type itself
				}
			} catch (Exception e) {
				log.logAndPrint("ERROR WITH EQUIP: ");
				log.logAndPrint(name);
				log.logAndPrint("\n");
				log.logException(e);
				log.logAndPrint("\n");
			}
		});

		//

		log.logAndPrint("total: " + size);
		log.logAndPrint("\n");
		log.logAndPrint("RARITIES proportion:");
		log.logAndPrint("\n");
		log.logAndPrint(Arrays.toString(rarities));

		log.logAndPrint(
				"\n\n\n-----------------\ntable of equipment types X [rarities + total of usage of equip type]\n\t\t\t");
		for (RaritiesTRAn r : allRarities) {
			log.logAndPrint("|");
			log.logAndPrint(r.getName());
			log.logAndPrint("\t");
		}
		log.logAndPrint("||TOTAL\n");

		int rarityGrainedIndexIterator = 0;
		for (EquipmentTypesTRAn e : EquipmentTypesTRAn.ALL_EQUIP_TYPES_TRAn) {
			int tabsPadding;
			tabsPadding = 3 - ((e.getName().length() - 4) >> 2);
			log.logAndPrint(e.getName());
			while (tabsPadding-- > 0) {
				log.logAndPrint("\t");
			}
			for (int i = raritiesAmount + 1; i > 0; i--) {
				log.logAndPrint("|\t");
				log.logAndPrint(Integer.toString(raritiesEachEquipType[rarityGrainedIndexIterator++]));
				log.logAndPrint("\t");
			}
			log.logAndPrint("\n");
		}
	}
}