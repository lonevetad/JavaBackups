package games.theRisingAngel.loaders;

import java.util.LinkedList;
import java.util.List;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentUpgrade;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.LoaderGeneric;
import games.generic.controlModel.subimpl.EquipmentUpgradeImpl;
import games.generic.controlModel.subimpl.LoaderEquipUpgrades;

public class LoaderEquipUpgradesTRAn extends LoaderEquipUpgrades {

	public LoaderEquipUpgradesTRAn(GameObjectsProvider<EquipmentUpgrade> objProvider) {
		super(objProvider);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadInto(GController gc) {
		LoaderEquipUpgradeFromFile leuff;
		// TODO prendere ispirazione da LoaderEquipments

		// e poi
		leuff = new LoaderEquipUpgradeFromFile("", "equipUpgradesTRAr.json");
		leuff.readAllFile();
		System.out.println("total equip-upgrades loaded: " + leuff.factories.size());
		for (FactoryEquipUpgrade fe : leuff.factories) {
			objProvider.addObj(fe.name, fe.rarity, fe);
		}
	}

	protected static class LoaderEquipUpgradeFromFile extends JSONFileConsumer {
		List<FactoryEquipUpgrade> factories;

		protected LoaderEquipUpgradeFromFile(String subPath, String filename) { super(subPath, filename); }

		@Override
		protected void readAllFileImpl(String line) {
//			String temp;
			int indexComma;
			String[] splitted;
			FactoryEquipUpgrade fe;
			factories = new LinkedList<>();
			while (line.contains("{")) { // start of an equip, must start at the previous end if any
				fe = new FactoryEquipUpgrade();
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
							fe.name = LoaderGeneric.removeQuotes(splitted[1]);
							break;
						case "description":
							fe.description = LoaderGeneric.removeQuotes(splitted[1]);
							break;
						case "modifiers":
							fe.attrMods = LoaderFunctionsTRAn.extractAttributeModifications(getLineReader());
							break;
						case "rarity":
							fe.rarity = LoaderGeneric.extractIntValue(splitted[1]);
							break;
						case "bonusPrice":
							fe.bonusPriceSell = LoaderFunctionsTRAn.extractSellPrices(splitted[1].trim());
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

	protected static class FactoryEquipUpgrade implements FactoryObjGModalityBased<EquipmentUpgrade> {
		int rarity;
		int[] bonusPriceSell;
		String name, description;
		List<AttributeModification> attrMods = null;

		@Override
		public EquipmentUpgrade newInstance(GModality gm) {
			EquipmentUpgrade ei;
			ei = new EquipmentUpgradeImpl(rarity, name);
//			ei.description = description;
			if (attrMods != null) {
				for (AttributeModification am : attrMods)
					ei.addAttributeModifier(am);
			}
			return ei;
		}

		@Override
		public String toString() {
			return "FactoryEquipUpgrade [\n name=" + name + ",\n rarity=" + rarity + ",\n attrMods=\n\t" + attrMods
					+ "]";
		}
	}

	public static void main(String[] args) {
		LoaderEquipUpgradeFromFile leuff;
		leuff = new LoaderEquipUpgradeFromFile("", "equipUpgradesTRAr.json");
		leuff.readAllFile();
		System.out.println("LoaderEquipUpgradeFromFile we read:");
		for (FactoryEquipUpgrade feu : leuff.factories) {
			System.out.println();
			System.out.println(feu);
		}
		System.out.println("total: " + leuff.factories.size());
	}
}