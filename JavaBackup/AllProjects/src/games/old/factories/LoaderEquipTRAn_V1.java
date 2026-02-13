package games.old.factories;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.LoaderGeneric;
import games.generic.controlModel.providers.AbilitiesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.LoaderEquipments;
import games.theRisingAngel.enums.EquipmentTypesTRAn;
import games.theRisingAngel.inventory.equipsWithAbilities.ArmProtectionShieldingDamageByMoney;
import games.theRisingAngel.inventory.equipsWithAbilities.HelmetOfPlanetaryMeteors;
import games.theRisingAngel.inventory.equipsWithAbilities.NecklaceOfPainRinvigoring;
import games.theRisingAngel.loaders.LoaderUtilsTRAn;
import games.theRisingAngel.loaders.factories.FactoryItems;

public class LoaderEquipTRAn_V1 extends LoaderEquipments {

	public LoaderEquipTRAn_V1(GameObjectsProvider<EquipmentItem> objProvider) { super(objProvider); }

	@Override
	public void loadInto(GController gc) {
		LoaderEquipFromFile leff;
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

		leff = new LoaderEquipFromFile("", "equipItems.json");
		leff.readAllFile();
		System.out.println("total equip loaded: " + leff.factories.size());
		for (FactoryEquip fe : leff.factories) {
			objProvider.addObj(fe.fi.name, fe.fi.rarity, fe);
		}
	}

	protected static class LoaderEquipFromFile extends JSONFileConsumer {
		List<FactoryEquip> factories;

		protected LoaderEquipFromFile(String subPath, String filename) { super(subPath, filename); }

		@Override
		protected void readAllFileImpl(String line) {
			int indexComma;
			String temp;
			String[] splitted;
			FactoryEquip fe;
			factories = new LinkedList<>();
			while (line.contains("{")) { // start of an equip, must start at the previous end if any
				fe = new FactoryEquip();
				do {
					line = getLineReader().next().trim();
					indexComma = line.indexOf(':');
					if (indexComma >= 0) {
//						splitted = line.split(":"); //
						splitted = new String[] { line.substring(0, indexComma), line.substring(indexComma + 1) };
						trimAll(splitted);
						line = LoaderGeneric.removeQuotes(splitted[0]).trim();// as cache
						switch (line) {
						case "name":
							fe.fi.name = LoaderGeneric.removeQuotes(splitted[1]);
							break;
						case "description":
							fe.fi.description = LoaderGeneric.removeQuotes(splitted[1]);
							break;
						case "type":
							fe.type = EquipmentTypesTRAn
									.getEquipTypeTRArByName(LoaderGeneric.removeQuotes(splitted[1]));
							break;
						case "attributeMods":
							fe.attrMods = LoaderUtilsTRAn.extractAttributeModifications(getLineReader());
							break;
						case "abilities":
							temp = splitted[1].trim();
							fe.abilities = LoaderUtilsTRAn.extractAbilitiesName(temp);
							break;
						case "rarity":
							fe.fi.rarity = LoaderGeneric.extractIntValue(splitted[1]);
							break;
						case "dimensInvent":
							fe.fi.dimensionInInventory = LoaderUtilsTRAn
									.extractDimensionInInventory(getLineReader().next().trim());
							getLineReader().next();
							break;
						case "sellPrice":
							temp = splitted[1].trim();
							fe.fi.price = LoaderUtilsTRAn.extractSellPrices(temp);
							break;
						default:
							break;
						}
					}
				} while (!line.contains("}")); // to the end
				factories.add(fe);
			}
		}
	}

	/** Instantiator of the loaded equipment */
	protected static class FactoryEquip implements FactoryObjGModalityBased<EquipmentItem> {
		FactoryItems fi = new FactoryItems();
		EquipmentTypesTRAn type;
		List<String> abilities = null;
		List<AttributeModification> attrMods = null;

		@Override
		public EquipmentItem newInstance(GModality gm) {
			EquipmentItem ei;
//			ei = new EquipmentItemImpl((GModalityRPG) gm, type, name);
			ei = type.factory.newEquipItem((GModalityRPG) gm, type, fi.name);
			setValues(gm, ei);
			return ei;
		}

		protected void setValues(GModality gm, EquipmentItem ei) {
//			EquipmentItem ei;
//			ei = (EquipmentItem) ii;
			fi.setValues(gm, ei);
			if (attrMods != null) {
				for (AttributeModification am : attrMods)
					ei.addAttributeModifier(am);
			}
			if (abilities != null) {
				GameObjectsProvidersHolderRPG gophRpg;
				AbilitiesProvider ap;
				gophRpg = (GameObjectsProvidersHolderRPG) gm.getGameObjectsProvider();
				ap = gophRpg.getAbilitiesProvider();
				for (String an : abilities) {
					ei.addAbility(ap.getAbilityByName(gm, an));
				}
			}
		}

		@Override
		public String toString() {
			return "FactoryEquip [\n name=" + fi.name + ", type=" + type + ",\n rarity=" + fi.rarity + ", sell price: "
					+ Arrays.toString(fi.price) + ",\n dimensions in inventory: " + fi.dimensionInInventory
					+ ",\n abilities=\n\t" + abilities + ",\n attrMods=\n\t" + attrMods + "]";
		}
	}

	public static void main(String[] args) {
		int size;
		int[] rarities;
		LoaderEquipFromFile leff;
		LinkedList<FactoryEquip> factories;
		FactoryEquip fe;
		leff = new LoaderEquipFromFile("", "equipItems.json");
		leff.readAllFile();
		size = leff.factories.size();
		System.out.println("LoaderEquipFromFile we read:");
		rarities = new int[6];
		Arrays.fill(rarities, 0);
		factories = (LinkedList<FactoryEquip>) leff.factories;
		while (!factories.isEmpty()) {
			fe = factories.removeFirst();
			System.out.println();
			System.out.println(fe);
			rarities[fe.fi.rarity]++;
		}
		System.out.println("total: " + size);
		System.out.println("RARITIES proportion:");
		System.out.println(Arrays.toString(rarities));
	}
}