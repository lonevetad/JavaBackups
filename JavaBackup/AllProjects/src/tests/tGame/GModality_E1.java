package tests.tGame;

import java.util.function.Consumer;

import dataStructures.isom.MultiISOMRetangularMap;
import dataStructures.isom.matrixBased.MISOM_SingleObjInNode;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import games.generic.controlModel.CreatureAttributesBaseAndDerived;
import games.generic.controlModel.GController;
import games.generic.controlModel.GModel;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentUpgrade;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.abilities.ADamageReductionOnLifeLowerToPhysicalAttributes;
import games.theRisingAngel.abilities.ALoseManaBeforeLife;
import games.theRisingAngel.abilities.AProtectButMakesSoft;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.DamageTypesTRAn;
import games.theRisingAngel.inventory.equipsWithAbilities.ArmProtectionShieldingDamageByMoney;
import games.theRisingAngel.inventory.equipsWithAbilities.NecklaceOfPainRinvigoring;
import games.theRisingAngel.misc.CreatureAttributesTRAn;
import games.theRisingAngel.misc.PlayerCharacterTypesTRAn.PlayerCharacterTypes;
import games.theRisingAngel.providers.GameObjectsProvidersHolderTRAn;
import geometry.implementations.shapes.ShapeRectangle;
import tests.tGame.tgEvent1.GEventInterface_E1;
import tests.tGame.tgEvent1.oggettiDesempio.ObjDamageDeliverE1;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinterTO;
import tests.tGame.tgEvent1.oggettiDesempio.ObjPrinter_EventDeliver;
import tests.tGame.tgEvent1.oggettiDesempio.ObserverPrinterEvent;
import tools.NumberManager;
import tools.ObjectNamedID;

public class GModality_E1 extends GModalityTRAnBaseWorld {
	static final int STARTING_PLAYER_LIFE_MAX = 100;

	public GModality_E1(GController controller, String modalityName) { super(controller, modalityName); }

	public Player_E1 getPlayerRPG() { return (Player_E1) player; }

	@Override
	public void startGame() {
		super.startGame();
//		getPlayerRPG().onEnteringInGame(this);
	}

	@Override
	public void onCreate() {
		GModel_E1 gmodel;
		Player_E1 p;
		ObjDamageDeliverE1 odd;
		ObserverPrinterEvent ope;
		NecklaceOfPainRinvigoring necklace_opr;
		ArmProtectionShieldingDamageByMoney armProtection_sdbm;
		CreatureAttributesBaseAndDerived ca;
		CreatureAttributesBonusesCalculator cabc;
		EquipmentItem equip;
//		GC_E1 contr;
		GameObjectsProvidersHolderTRAn goph;
		ObjPrinter_EventDeliver printerPlayer;
		String equipmentName;
		GObjectsInSpaceManager goism;
		MultiISOMRetangularMap<Double> isom;
		MatrixInSpaceObjectsManager<Double> matrix;

		super.onCreate();
		super.setRandomSeed(0);

		System.out.println("\n\n\n MY NAME: " + getModalityName() + "\n\n");

		// create a fake ISOMMatrix to add the player in
		goism = this.getGameObjectsManager().getGObjectInSpaceManager();
		isom = (MultiISOMRetangularMap<Double>) goism.getOIMManager();
		matrix = new MISOM_SingleObjInNode<>(false, 20, 20, NumberManager.getDoubleManager());
		isom.addMap(matrix, 0, 0);

		//
//		contr = (GC_E1) controller;
		gmodel = (GModel_E1) this.getModel();
		goph = (GameObjectsProvidersHolderTRAn) this.getGameObjectsProvider();

		// TODO add all stuffs .. qui è il posto in cui dovrebbero stare gli oggetti
		// strani che inserisco
		p = (Player_E1) newPlayerInGame(null, PlayerCharacterTypes.Human); // new Player_E1(this);
		p.setName("Lonevetad");
		p.setShape(new ShapeRectangle(0.0, 0, 0, true, 10, 10));
		p.getShape().setLeftTopCorner(3, 3);
		this.setPlayer(p);
		p.setLifeMax((int) (p.getLife() * 1.5));
		p.setGameModality(this);
//		p.getCurrencies().setMoneyAmount(0, 100);

		this.addGameObject(p);
		this.addGameObject(new ObjPrinterTO(this, 1250, "LongWaiting"));
//		this.addGameObject(new ObjPrinterTO(333, "Short") {
//
//			@Override
//			public void executeAction(GModality modality) {
//				System.out.println("player's life: " + p.getLife() + ", life regen: " + p.getLifeRegenation());
//			}
//		});
//		this.addGameObject(new ObjPrinter_EventDeliver(250, "Tiny"));
		// gModelE.addTimeProgressingObject(odd);
		printerPlayer = new ObjPrinter_EventDeliver(this, 2000, "HAKUNA MATATA") {
			private static final long serialVersionUID = 1L;

			@Override
			public String getText() {
//				return p.getAttributes().toString();
				StringBuilder sb;
				CreatureAttributes ca;
				sb = new StringBuilder(127);
				ca = p.getAttributes();
				sb.append("Player life ").append(p.getLife()).append(", current values: ");
				for (int i = 0; i < ca.getAttributesCount(); i++)
					sb.append(ca.getValue(AttributesTRAn.ALL_ATTRIBUTES[i])).append(", ");
				return sb.toString();
			}
		};
//		this.addGameObject(printerPlayer);
		ope = new ObserverPrinterEvent();
//		this.addEventObserver(ope);
		this.addGameObject(ope);

//		necklace_opr = (NecklaceOfPainRinvigoring) goph.getEquipmentsProvider()//
//				.getNewObjByName(this, NecklaceOfPainRinvigoring.NAME);
//		p.equip(necklace_opr);
//
//		armProtection_sdbm = (ArmProtectionShieldingDamageByMoney) goph.getEquipmentsProvider().getNewObjByName(this,
//				ArmProtectionShieldingDamageByMoney.NAME);
//		p.equip(armProtection_sdbm);

		// TODO aggiungere gli esempi pensati negli Appunti e esempio
		// first make the player, then the damager, the healer, the fairy, the
		// money-maker, etc

		int[][] damageDealers = { //
//				 milliseconds (ms), damage, starting time (ms), damage index
				{ 6000, 300, 5000, 0 }, //
				{ 4000, 125, 2500, 0 }, //
				{ 12000, 650, 125, 1 }, //
//				{ 17000, 700, -5333, 0 }, //
//				{ 1200, 20, 15, 0 }, //
		};
		for (int[] damageData : damageDealers) {
			odd = new ObjDamageDeliverE1(damageData[0]);
			odd.setTarget(p);
			odd.setDamageAmount(damageData[1]);
			odd.setAccumulatedTimeElapsed(damageData[2]);
			odd.setDamageType(damageData[3] == 0 ? DamageTypesTRAn.Physical : DamageTypesTRAn.Magical);
			this.addGameObject(odd);
		}
		damageDealers = null;

		//

		equipmentName = "Plated Armor of Stonefying Skin";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		if (equip != null) {
			equip.addAbility( //
					goph.getAbilitiesProvider().//
							getAbilityByName(this, ADamageReductionOnLifeLowerToPhysicalAttributes.NAME));
		}
		System.out.println("\n\n equipping: " + equipmentName);
		System.out.println(equip.toString());
		System.out.println("\n\n");
		p.equip(equip);
		System.out.println("Plated Armor of Stonefying Skin has " + equip.getAbilities().size() + " abilities !!!");

		equipmentName = "Belt with Limph-made Walled";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		System.out.println("\n\n equipping: " + equipmentName);
		System.out.println(equip.toString());
		System.out.println("\n\n");
		p.equip(equip);

		for (String en : new String[] { "Cloth Hat", "Ring of rusted plate", "Sunstone Ring", "Triphane Ring",
				"Amazonite Ring", "Amazonite Ring", "Moonstone Ring", "Gloves of the mad hunter",
				"Ferromagnetic Earrings", "Ferromagnetic Bracelet", "Ferromagnetic Chocker", "Crystal armguard" }) {
			equipmentName = en;
			equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
			p.equip(equip);
		}

		equipmentName = "Lapis Lazuli Ring";
		System.out.println("Now adding an artificially manipulated " + equipmentName);
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		System.out.println("Original equip");
		System.out.println(equip);
		for (String eu : new String[] { "Stones Intarsed", "Yellow colored", "Mana to Blood" }) {
			equip.addUpgrade(goph.getEquipUpgradesProvider().getNewObjByName(this, eu));
		}
		equip.addAbility(goph.getAbilitiesProvider().getNewObjByName(this, ALoseManaBeforeLife.NAME));
		System.out.println("--------------------------..........Altered equip");
		System.out.println(equip);
		p.equip(equip);

		// another custom equip
//		equipmentName = "Cloth Shoes";
//		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
//		equip.addAbility(goph.getAbilitiesProvider().getNewObjByName(this, AMeditationMoreRegen.NAME));
//		equip.addAbility(goph.getAbilitiesProvider().getNewObjByName(this, "Mag(ic)netic Dynamo"));
//		equip.addUpgrade(goph.getEquipUpgradesProvider().getNewObjByName(this, "Of Diet"));
//		p.equip(equip);

		equipmentName = "Snake Belt";
		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
		equip.addAbility(goph.getAbilitiesProvider().getNewObjByName(this, AProtectButMakesSoft.NAME));
		p.equip(equip);

//		equipmentName = "Snake Belt";
//		equip = goph.getEquipmentsProvider().getNewObjByName(this, equipmentName);
//		equip.addAbility(goph.getAbilitiesProvider().getNewObjByName(this, AProtectButMakesSoft.NAME));
//		p.equip(equip);

		//

		System.out.println("\n\n\nGModalit_E1#onCreate .. quanti oggetti ho?");
		this.forEachGameObject(o -> System.out.println(o));
		System.out.println("\n\n and timed objects?");
		gmodel.forEachTimedObject(o -> System.out.println(o));
		System.out.println("timed objects ended\n\n");

		gmodel.forEachObjHolder((gohname, goh) -> {
			System.out.println("\n\nGOH " + gohname + " has: ");
			goh.forEach(o -> System.out.println("\t - " + o));
		});
		System.out.println("and then\n\n\n equipment set:");
		p.getEquipmentSet().forEachEquipment((e, i) -> {
			if (e != null)
				System.out.println(i + "-> " + e);
		});
		System.out.println("\n\n at the end, the player looks like:");
		ca = (CreatureAttributesBaseAndDerived) p.getAttributes();
		System.out.println(printerPlayer.getText());
		System.out.println(ca);
		System.out.println("\n\n player attributes without bonuses");
		cabc = ca.getBonusCalculator();
		ca.setBonusCalculator(null);
		System.out.println(ca);
		System.out.println("\n\n\n");
		ca.setBonusCalculator(cabc);

		// then ...
//		checkAndRebuildThreads();

		// let's try to sum up ALL equip upgrades
		{
			final Consumer<AttributeModification> amApplier;
			final CreatureAttributesTRAn caa;
			caa = new CreatureAttributesTRAn();
			p.setAttributes(caa);
			amApplier = am -> { caa.applyAttributeModifier(am); };
			goph.getEquipUpgradesProvider().forEachFactory((euName, f) -> {
				EquipmentUpgrade eu;
				eu = f.newInstance(this);
				eu.getAttributeModifiers().forEach(amApplier);
			});
			System.out.println("\n\n All Attribute modifications all together would apply this modifications: ");
			System.out.println(caa.toString());
			System.out.println("\n\n and without bonuses");
			caa.setBonusCalculator(null); // no bonus no cry
			System.out.println(caa.toString());
			// at the end, reset original attributes
			p.setAttributes(ca);
		}
	}

	@Override
	public GModel newGameModel() { return new GModel_E1(); }

	@Override
	public GEventInterface newEventInterface() {
		GEventInterface_E1 gei;
		gei = new GEventInterface_E1();
		gei.setNewGameEventManager(this);
		return gei;
	}

	@Override
	protected PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer, ObjectNamedID playerType) {
		Player_E1 p;
		p = new Player_E1(this, (PlayerCharacterTypes) playerType);
		setStartingBaseAttributes(p);
		p.setLifeMax(STARTING_PLAYER_LIFE_MAX);
		p.setLife(STARTING_PLAYER_LIFE_MAX);
		p.setCurrencies(newCurrencyHolder());
		return p;
	}
}