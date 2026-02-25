
package games.theRisingAngel.enums;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.currency.Currency;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.IEquipmentUpgrade;
import games.generic.controlModel.misc.IEnumAlike;
import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;
import games.generic.controlModel.subimpl.EquipmentUpgradeImpl;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.inventory.EquipItemFactory;
import games.theRisingAngel.inventory.EquipItemTRAn;
import tools.Comparators;

public class TribesTRAn {

	public static final Tribe[] ALL_TRIBES;
	public static final IndexToObjectBackmapping INDEX_TO_TRIBE_TRAn;
	public static final Map<TribeReligion, Tribe> MAP_RELIGION_TO_TRIBE;

	public static final RaritiesTRAn RARITY_TRIBE_EQUIPMENT_PIECES;
	public static final Map<RaritiesTRAn, AttributesVariationTribeEquip> MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE;
	public static final Map<EquipmentTypesTRAn, PieceOfEquipmentSetData> MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE;
	public static final Set<EquipmentTypesTRAn> ALL_EQUIP_TYPES_ON_TRIBE_SETS;
	public static final Set<RaritiesTRAn> ALL_EQUIP_UPGRADES_RARITIES;

	// TODO : static initializer
	static {
		MapTreeAVL<RaritiesTRAn, AttributesVariationTribeEquip> mrta;
		MapTreeAVL<EquipmentTypesTRAn, PieceOfEquipmentSetData> metd;
		Map<TribeReligion, Tribe> mrtt;

		ALL_TRIBES = Tribe.values();
		INDEX_TO_TRIBE_TRAn = (int i) -> TribesTRAn.ALL_TRIBES[i];

		RARITY_TRIBE_EQUIPMENT_PIECES = RaritiesTRAn.Rare;

		mrta = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, RaritiesTRAn.COMPARATOR_RARITY_TRAn);

		// TODO (2026-02-13): all of these should be put in a configuration
		mrta.put(RaritiesTRAn.Common,
				new AttributesVariationTribeEquip(VariationLevels.Memory, 3, -1, new int[] { 4 }));
		mrta.put(RaritiesTRAn.Good, new AttributesVariationTribeEquip(VariationLevels.Trade, 7, -2, new int[] { 8 }));
		mrta.put(RaritiesTRAn.Awesome,
				new AttributesVariationTribeEquip(VariationLevels.Essence, 11, -3, new int[] { 15 }));
		mrta.put(RaritiesTRAn.Rare,
				new AttributesVariationTribeEquip(VariationLevels.Original, 17, -5, new int[] { 20 }));

		ALL_EQUIP_UPGRADES_RARITIES = Collections.unmodifiableSet(mrta.keySet());
		MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE = Collections.unmodifiableMap(mrta);

		//

		metd = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, EquipmentTypesTRAn.COMPARATOR_EQUIP_TYPES_TRAn);

		metd.put(EquipmentTypesTRAn.Head,
				new PieceOfEquipmentSetData("Hat", EquipmentTypesTRAn.Head, new Dimension(2, 2), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 35),
								new AttributeModification(AttributesTRAn.ManaMax, 20),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 3),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 3),
								new AttributeModification(AttributesTRAn.PhysicalProbabilityPerThousandHit, -8),
								new AttributeModification(AttributesTRAn.MagicalProbabilityPerThousandHit, -8) }));
		metd.put(EquipmentTypesTRAn.Shoulder,
				new PieceOfEquipmentSetData("Cloak", EquipmentTypesTRAn.Shoulder, new Dimension(2, 2), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 15),
								new AttributeModification(AttributesTRAn.ManaMax, 40),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.PhysicalProbabilityPerThousandAvoid, -12),
								new AttributeModification(AttributesTRAn.MagicalProbabilityPerThousandAvoid, -12) }));
		metd.put(EquipmentTypesTRAn.Chest,
				new PieceOfEquipmentSetData("Jacket", EquipmentTypesTRAn.Chest, new Dimension(3, 3), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 50),
								new AttributeModification(AttributesTRAn.ManaMax, 5),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 5),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 5),
								new AttributeModification(AttributesTRAn.Velocity, -15) }));
		metd.put(EquipmentTypesTRAn.Hands,
				new PieceOfEquipmentSetData("Gloves", EquipmentTypesTRAn.Hands, new Dimension(1, 1), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 10),
								new AttributeModification(AttributesTRAn.ManaMax, 45),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.VelocityAttackStrikePercentage, 10) }));
		metd.put(EquipmentTypesTRAn.Arms,
				new PieceOfEquipmentSetData("Sleeve", EquipmentTypesTRAn.Arms, new Dimension(1, 2), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 45),
								new AttributeModification(AttributesTRAn.ManaMax, 10),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.VelocitySpellCastPercentage, 10) }));
		metd.put(EquipmentTypesTRAn.Legs,
				new PieceOfEquipmentSetData("Pants", EquipmentTypesTRAn.Legs, new Dimension(2, 3), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 30),
								new AttributeModification(AttributesTRAn.ManaMax, 25),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 3),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 3),
								new AttributeModification(AttributesTRAn.StaminaMax, 5),
								new AttributeModification(AttributesTRAn.StaminaRegen, 1) }));
		metd.put(EquipmentTypesTRAn.Feet,
				new PieceOfEquipmentSetData("Boots", EquipmentTypesTRAn.Feet, new Dimension(2, 2), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 35),
								new AttributeModification(AttributesTRAn.ManaMax, 20),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.Velocity, 10) }));

		ALL_EQUIP_TYPES_ON_TRIBE_SETS = Collections.unmodifiableSet(metd.keySet());
		MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE = Collections.unmodifiableMap(metd);

		//

		mrtt = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, TribeReligion.COMPARATOR_TRIBE_RELIGION);
		for (Tribe t : ALL_TRIBES) {
			mrtt.put(t.religion, t);
		}
		MAP_RELIGION_TO_TRIBE = Collections.unmodifiableMap(mrtt);
	}

	//

	/**
	 * All tribes in this game. Each of them venerates a base Attribute and hates
	 * another one. Being devoted to an Attribute gives more bonus than the malus
	 * provided by the hating.
	 */
	public static enum Tribe implements IEnumAlike {
		Apebz, Asexiso, Buavoj, Bifod, Cobahir, Cugujab, Dokosok, Domonoleg, Drovovomir, Epewuv, Equaks, Fidnox,
		Folottuj, Gamaskov, Gizix, Gokuq, Guwaddarg, Hansyn, Hitifa, Hugupad, Icibiup, Innizay, Jaonuvup, Jawueqk,
		Jikitiwumo, Kantiw, Kefmah, Kemaf, Lamahaya, Lezerto, Lijislaer, Liwed, Maxibos, Mijumey, Muffinex, Muvuduk,
		Nangeco, Noguev, Ojenaff, Onuhzum, Poltodka, Pomaquox, Puwazok, Quapaq, Quazahnbokt, Ruhujiqk, Ruwizodup,
		Soxoxzeo, Suzurt, Tamawat, Telleno, Tevesimag, Tocomh, Udigutun, Ujuymey, Ulubaw, Vajyrfo, Viwuahz, Warfays,
		Weakimit, Wujoweg, Wunocogdi, Xakonokork, Xayewi, Xecadyma, Xenagon, Yaexuf, Yaqenohz, Yikipq, Zanomozosya,
		Zasitecyo, Zuwidon;

		static {
			Tribe[] tribes = Tribe.values();
			int indexTribe = 0;
			for (int praiseIndex = AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE; praiseIndex <= AttributesTRAn.LAST_INDEX_ATTRIBUTE_UPGRADABLE; praiseIndex++) {
				for (int hateIndex = AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE; hateIndex <= AttributesTRAn.LAST_INDEX_ATTRIBUTE_UPGRADABLE; hateIndex++) {
					if (praiseIndex != hateIndex) {
						tribes[indexTribe].religion = new TribeReligion(tribes[indexTribe],
								AttributesTRAn.ALL_ATTRIBUTES[praiseIndex], AttributesTRAn.ALL_ATTRIBUTES[hateIndex]);
						indexTribe++;
					}
				}
			}
		}

		//

		// Tribe(TribeReligion religion) { this.religion = religion; }

		protected TribeReligion religion;

		@Override
		public int getIndex() {
			return this.ordinal();
		}

		@Override
		public String getName() {
			return this.name();
		}

		@Override
		public Long getID() {
			return (long) this.ordinal();
		}

		public TribeReligion getReligion() {
			return this.religion;
		}

		@Override
		public IndexToObjectBackmapping getFromIndexBackmapping() {
			return INDEX_TO_TRIBE_TRAn;
		}

		public String stringify() {
			StringBuilder sb;
			String n;
			sb = new StringBuilder(64);
			n = this.name();
			sb.append("Tribe:\t").append(super.toString());
			for (int rep = 3 - (n.length() >> 2); rep >= 0; rep--) {
				sb.append('\t');
			}
			return sb.append(", religion: ").append(this.getReligion().toString()).toString();
		}

		// GENERATORS

		public IEquipmentUpgrade newEquipmentUpgradeForRarity(RaritiesTRAn rar, GModalityRPG gmrpg) {
			return this.newEquipmentUpgradeForRarity(rar, gmrpg, ReligionAlignment.Canon);
		}

		public IEquipmentUpgrade newEquipmentUpgradeForRarity(RaritiesTRAn rar, GModalityRPG gmrpg,
				ReligionAlignment religAlign) {
			int n, addedPrice;
			IEquipmentUpgrade eu;
			AttributesVariationTribeEquip variation;
			CurrencySet cs;
			Currency[] currencies;
			TribeReligion rel;
			TribeReligionAlignmentModification relAlMod;

			rel = this.religion;
			variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(rar);
			eu = new EquipmentUpgradeImpl(rar.getIndex(), getNameEquipUgradeFor(this, rar, religAlign));

			relAlMod = new TribeReligionAlignmentModification(variation, religAlign);
			eu.addAttributeModifier(new AttributeModification(rel.religionDevotedTo, relAlMod.getBonus()));
			eu.addAttributeModifier(new AttributeModification(rel.religionHated, relAlMod.getMalus()));

			cs = gmrpg.getGameObjectsProvider().newCurrencyHolder();
			currencies = cs.getCurrencies();
			cs.setGameModaliy(gmrpg);
			n = variation.addedPrices.length;
			while (--n >= 0) { // for each type of currencies ...
				addedPrice = variation.addedPrices[n];
				if (relAlMod.isIsnegativePriceChanging()) {
					addedPrice = -addedPrice;
				}
				cs.setCurrencyAmount(currencies[n], addedPrice);
			}
			eu.setPricesModifications(cs);

			return eu;
		}

		// TODO : a parameters refactoring may be needed in case of new ones (explosion
		// of combinatorics)

		protected static EquipItemTRAn newEquipItemFor(GModalityRPG gmrpg, Tribe tribe, EquipmentTypesTRAn pieceType) {
			return newEquipItemFor(gmrpg, tribe, pieceType, null, null, ReligionAlignment.Canon);
		}

		protected static EquipItemTRAn newEquipItemFor(GModalityRPG gmrpg, Tribe tribe, EquipmentTypesTRAn pieceType,
				ReligionAlignment religAlign) {
			return newEquipItemFor(gmrpg, tribe, pieceType, null, null);
		}

		protected static EquipItemTRAn newEquipItemFor(GModalityRPG gmrpg, Tribe tribe, EquipmentTypesTRAn pieceType,
				PieceOfEquipmentSetData ped, AttributesVariationTribeEquip variation) {
			return newEquipItemFor(gmrpg, tribe, pieceType, ped, variation, ReligionAlignment.Canon);
		}

		protected static EquipItemTRAn newEquipItemFor(GModalityRPG gmrpg, Tribe tribe, EquipmentTypesTRAn pieceType,
				PieceOfEquipmentSetData ped, AttributesVariationTribeEquip variation, ReligionAlignment religAlign) {
			int n, price;
			AttributeModification[] allAttributes;
			EquipItemTRAn equipPiece;
			EquipItemFactory equipFactory;
			CurrencySet cs;
			Currency[] currencies;
			TribeReligion rel;
			TribeReligionAlignmentModification relAlMod;

			Objects.requireNonNull(gmrpg);

			if (variation == null) {
				variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(RARITY_TRIBE_EQUIPMENT_PIECES);
			}
			if (ped == null) {
				ped = MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE.get(pieceType);
			}

			rel = tribe.religion;
			allAttributes = new AttributeModification[2 + ped.additionalAttributeModifiers.length];
			relAlMod = new TribeReligionAlignmentModification(variation, religAlign);
			allAttributes[0] = new AttributeModification(rel.religionDevotedTo, relAlMod.getBonus());
			allAttributes[1] = new AttributeModification(rel.religionHated, relAlMod.getMalus());
			System.arraycopy(ped.additionalAttributeModifiers, 0, allAttributes, 2,
					ped.additionalAttributeModifiers.length);

			equipFactory = EquipItemFactory.getDefaultFactoryFor(pieceType);
			equipPiece = equipFactory.newEquipItem(gmrpg, pieceType, getNameEquipFor(tribe, pieceType, religAlign),
					allAttributes);

			equipPiece.setDimensionInInventory(ped.dimensionInventory);

			cs = gmrpg.getGameObjectsProvider().newCurrencyHolder();
			currencies = cs.getCurrencies();
			cs.setGameModaliy(gmrpg);
			n = variation.addedPrices.length;
			while (--n >= 0) {
				price = variation.addedPrices[n];
				if (!relAlMod.isIsnegativePriceChanging()) {
					price <<= 1;
				}
				cs.setCurrencyAmount(currencies[n], price);
			}
			equipPiece.setSellPrice(cs);
			return equipPiece;
		}

		public EquipItemTRAn newEquipmentPiece(GModalityRPG gmrpg, EquipmentTypesTRAn pieceType) {
			return newEquipItemFor(gmrpg, this, pieceType);
		}

		public EquipmentItem newEquipmentPiece(GModalityRPG gmrpg, EquipmentTypesTRAn pieceType,
				ReligionAlignment religAlign) {
			return newEquipItemFor(gmrpg, this, pieceType, religAlign);
		}

		public MapTreeAVL<EquipmentTypesTRAn, EquipItemTRAn> newEquipmentSet(GModalityRPG gmrpg) {
			return this.newEquipmentSet(gmrpg, ReligionAlignment.Canon);
		}

		public MapTreeAVL<EquipmentTypesTRAn, EquipItemTRAn> newEquipmentSet(GModalityRPG gmrpg,
				ReligionAlignment religAlign) {
			final MapTreeAVL<EquipmentTypesTRAn, EquipItemTRAn> m;
			final AttributesVariationTribeEquip variation;
			final Tribe thisTribe;

			m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
					EquipmentTypesTRAn.COMPARATOR_EQUIP_TYPES_TRAn);

			thisTribe = this;
			variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(RARITY_TRIBE_EQUIPMENT_PIECES);

			MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE.forEach( //
					(eqType, ped) -> m.put(eqType,
							newEquipItemFor(gmrpg, thisTribe, eqType, ped, variation, religAlign)) //
			);

			return m;
		}

		public TribeWarStatus getWarsStatusWith(TribesTRAn.Tribe otherTribe) {
			TribeReligion tr, or;
			if (otherTribe == null || otherTribe == this) {
				return TribeWarStatus.Neutral;
			}
			tr = this.religion;
			or = otherTribe.religion;
			if (isWorstEnemy(otherTribe) || tr.religionDevotedTo == or.religionHated
					|| tr.religionHated == or.religionDevotedTo) {
				return TribeWarStatus.War;
			} else if (tr.religionDevotedTo == or.religionDevotedTo || tr.religionHated == or.religionHated) {
				return TribeWarStatus.Ally;
			}
			return TribeWarStatus.Neutral;
		}

		public boolean isWorstEnemy(TribesTRAn.Tribe otherTribe) {
			TribeReligion tr, or;
			tr = this.religion;
			or = otherTribe.religion;
			return (tr.religionDevotedTo == or.religionHated) && (tr.religionHated == or.religionDevotedTo);
		}

		@Override
		public boolean setID(Long newID) {
			return false;
		}
	}

	//

	// TODO : TribeWarStatus enum
	public static enum TribeWarStatus implements IEnumAlike {
		Neutral, War, Ally;

		public static final TribeWarStatus[] ALL_TRIBE_WAR_STATUS;
		public static final IndexToObjectBackmapping INDEX_TO_TRIBE_WAR_STATUS;

		static {
			ALL_TRIBE_WAR_STATUS = TribeWarStatus.values();
			INDEX_TO_TRIBE_WAR_STATUS = (int i) -> ALL_TRIBE_WAR_STATUS[i];
		}

		@Override
		public Long getID() {
			return (long) this.ordinal();
		}

		@Override
		public String getName() {
			return this.name();
		}

		@Override
		public int getIndex() {
			return this.ordinal();
		}

		@Override
		public IndexToObjectBackmapping getFromIndexBackmapping() {
			return INDEX_TO_TRIBE_WAR_STATUS;
		}

		@Override
		public boolean setID(Long ID) {
			return false;
		}
	}

	/**
	 * A {@link TribeReligion} worship level may vary.
	 */
	public static enum ReligionAlignment {
		/**
		 * Usual values, no changes in amounts and naming
		 */
		Canon(""),
		/**
		 * Amplify the bonus at the cost of turning the price to negative:
		 * <ul>
		 * <li>Flip the prices modifications to negative</li>
		 * <li>swap bonus (which is greater than malus) with malus</li>
		 * <li>flips the sign of the new bonus and malus</li>
		 * </ul>
		 */
		Fanatic,
		/**
		 * Turns malus into positive values. In order to balance the alteration, the
		 * bonus is subtracted by twice the "new malus" (the bonus amount is more than
		 * twice the absolute value of the malus)
		 */
		Heretic;

		private ReligionAlignment() {
			this(null);
		}

		private ReligionAlignment(String na) {
			this.nameAlteration = (na != null) ? na : //
					(this.name() + ' ');
		}

		public final String nameAlteration;
	}

	/**
	 * The greater (their ordinal), the more intense is their bonuses
	 */
	public static enum VariationLevels {
		Original(false), Memory, Trade, Essence;

		public final boolean canProduceEquipUpgrade;

		VariationLevels() {
			this(true);
		}

		VariationLevels(boolean canProduceEquipUpgrade) {
			this.canProduceEquipUpgrade = canProduceEquipUpgrade;
		}
	}

	// TODO : TribeWarStatusTRAn enum

	//

	// TODO : other stuffs

	public static Tribe getTribeByReligion(TribeReligion religion) {
		if (religion == null) {
			return null;
		}
		return MAP_RELIGION_TO_TRIBE.get(religion);
	}

	public static String getNameEquipUgradeFor(Tribe tribe, RaritiesTRAn rarity) {
		return getNameEquipUgradeFor(tribe, rarity, null);
	}

	public static String getNameEquipUgradeFor(Tribe tribe, RaritiesTRAn rarity, ReligionAlignment religAlign) {
		AttributesVariationTribeEquip variation;
		if (religAlign == null) {
			religAlign = ReligionAlignment.Canon;
		}
		variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(rarity);
		if (variation == null) {
			throw new IllegalArgumentException("Not accepted rarity: " + rarity);
		}
		return "of " + religAlign.nameAlteration + tribe.getName() + " Tribe " + variation.getVariationName();
	}

	public static String getNameEquipFor(Tribe tribe, EquipmentTypesTRAn equipType) {
		return getNameEquipFor(tribe, equipType, null);
	}

	public static String getNameEquipFor(Tribe tribe, EquipmentTypesTRAn equipType, ReligionAlignment religAlign) {
		PieceOfEquipmentSetData ped;
		if (religAlign == null) {
			religAlign = ReligionAlignment.Canon;
		}
		ped = MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE.get(equipType);
		if (ped == null) {
			throw new IllegalArgumentException("Not accepted equipment type: " + equipType);
		}
		return ped.namePrefix + " Cosplay of " + religAlign.nameAlteration + tribe.getName() + " Tribe";
	}

	//

	/**
	 * Represent the set of bonuses and malus a religion (linked with a Tribe) gives
	 * to.
	 */
	public static final class TribeReligion {
		public static final Comparator<TribeReligion> COMPARATOR_TRIBE_RELIGION = (r1, r2) -> {
			int res;
			if (r1 == r2) {
				return 0;
			}
			if (r1 == null) {
				return -1;
			}
			if (r2 == null) {
				return 1;
			}
			res = Comparators.LONG_COMPARATOR.compare(r1.religionDevotedTo.getID(), r2.religionDevotedTo.getID());
			if (res != 0) {
				return res;
			}
			return Comparators.LONG_COMPARATOR.compare(r1.religionHated.getID(), r2.religionHated.getID());
		};

		public TribeReligion(Tribe tribeVeneratingIt, AttributesTRAn religionDevotedTo, AttributesTRAn religionHated) {
			super();
			if (religionDevotedTo == null || religionHated == null || religionDevotedTo == religionHated) {
				throw new IllegalArgumentException("Can't praise and hate a null religion or the same religions: <"
						+ religionDevotedTo + ", " + religionHated + ">");
			}
			this.tribeVeneratingIt = tribeVeneratingIt;
			this.religionDevotedTo = religionDevotedTo;
			this.religionHated = religionHated;
		}

		protected final Tribe tribeVeneratingIt;
		protected final AttributesTRAn religionDevotedTo, religionHated;

		//

		public Tribe getTribeVeneratingIt() {
			return tribeVeneratingIt;
		}

		public AttributesTRAn getReligionDevotedTo() {
			return religionDevotedTo;
		}

		public AttributesTRAn getReligionHated() {
			return religionHated;
		}

		@Override
		public String toString() {
			return "TribeReligion [tribeVeneratingIt=" + tribeVeneratingIt.getName() + ", religionDevotedTo="
					+ religionDevotedTo + ", religionHated=" + religionHated + "]";
		}
	}

	public static class TribeReligionAlignmentModification {
		public TribeReligionAlignmentModification(AttributesVariationTribeEquip variation,
				ReligionAlignment religAlign) {
			if (religAlign == null) {
				religAlign = ReligionAlignment.Canon;
			}
			switch (religAlign) {
				case Canon: {
					isnegativePriceChanging = false;
					bonus = variation.bonus;
					malus = variation.malus;
					break;
				}
				case Fanatic: {
					// OLD: false
					isnegativePriceChanging = true;
					/*
					 * OLD swap both bonus/malus and signs bonus = -variation.malus; malus =
					 * -variation.bonus;
					 */
					bonus = variation.bonus << 1;
					malus = variation.malus * 2;
					break;
				}
				case Heretic: {
					isnegativePriceChanging = true;
					// turns positive
					malus = -variation.malus;
					/*
					 * then balance: remove the "new malus" and how the bonus have balanced the
					 * original malus; in total, it's twice the Math.abs of the original malus
					 */
					bonus = variation.bonus - (malus << 1);
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected religion: " + religAlign);
			}
		}

		final boolean isnegativePriceChanging;
		final int bonus, malus;

		public boolean isIsnegativePriceChanging() {
			return isnegativePriceChanging;
		}

		public int getBonus() {
			return bonus;
		}

		public int getMalus() {
			return malus;
		}

	}

	public static class AttributesVariationTribeEquip {
		public AttributesVariationTribeEquip(VariationLevels variationLevel, int bonus, int malus, int[] addedPrices) {
			super();
			this.variationName = variationLevel.name();
			this.bonus = bonus;
			this.malus = malus;
			this.addedPrices = addedPrices;
			this.canBeEquipUpgrade = variationLevel.canProduceEquipUpgrade;
		}

		protected boolean canBeEquipUpgrade;
		protected int bonus;
		protected int malus;
		protected int[] addedPrices;
		protected String variationName;

		//
		public String getVariationName() {
			return variationName;
		}

		public int getBonus() {
			return bonus;
		}

		public int getMalus() {
			return malus;
		}

		public int[] getAddedPrices() {
			return addedPrices;
		}

		public boolean isCanBeEquipUpgrade() {
			return canBeEquipUpgrade;
		}
		//
		// public void setVariationName(String variationName) { this.variationName =
		// variationName; }
		// public void setBonus(int bonus) { this.bonus = bonus; }
		// public void setMalus(int malus) { this.malus = malus; }
		// public void setAddedPrices(int[] addedPrices) { this.addedPrices =
		// addedPrices; }
		// public void setCanBeEquipUpgrade(boolean canBeEquipUpgrade) {
		// this.canBeEquipUpgrade = canBeEquipUpgrade; }

		@Override
		public String toString() {
			return "AttributesVariationTribeEquip [variationName=" + variationName + ", addedPrices="
					+ Arrays.toString(addedPrices) + ", bonus=" + bonus + ", malus=" + malus + "]";
		}
	}

	//

	public static class PieceOfEquipmentSetData {
		public PieceOfEquipmentSetData(String namePrefix, EquipmentTypesTRAn equipType, Dimension dimensionInventory,
				AttributeModification[] additionalAttributeModifiers) {
			super();
			this.namePrefix = namePrefix;
			this.equipType = equipType;
			this.dimensionInventory = dimensionInventory;
			this.additionalAttributeModifiers = additionalAttributeModifiers;
		}

		protected String namePrefix;
		protected EquipmentTypesTRAn equipType;
		protected Dimension dimensionInventory;
		protected AttributeModification[] additionalAttributeModifiers;

		//
		public String getNamePrefix() {
			return namePrefix;
		}

		public EquipmentTypesTRAn getEquipType() {
			return equipType;
		}

		public Dimension getDimensionInventory() {
			return dimensionInventory;
		}

		public AttributeModification[] getAdditionalAttributeModifiers() {
			return additionalAttributeModifiers;
		}
		//
		// public void setNamePrefix(String namePrefix) { this.namePrefix = namePrefix;
		// }
		// public void setEquipType(EquipmentTypesTRAn equipType) { this.equipType =
		// equipType; }
		// public void setDimensionInventory(Dimension dimensionInventory) {
		// this.dimensionInventory = dimensionInventory; }
		// public void setAdditionalAttributeModifiers(AttributeModification[]
		// additionalAttributeModifiers) { this.additionalAttributeModifiers =
		// additionalAttributeModifiers; }

		@Override
		public String toString() {
			return "PieceOfEquipmentSetData [namePrefix=" + namePrefix + ", equipType=" + equipType
					+ ", dimensionInventory=" + dimensionInventory + ", additionalAttributeModifiers="
					+ Arrays.toString(additionalAttributeModifiers) + "]";
		}

	}

	public static void main(String[] args) {
		int i = 0;
		for (Tribe t : Tribe.values()) {
			System.out.println(i++ + "\t-> " + t.stringify());
		}

		System.out.println("\n\n");
		MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE.forEach((et, poesd) -> {
			System.out.print("equip type ");
			System.out.print(et.getName());
			System.out.print(", -> ");
			System.out.println(poesd);
		});

		System.out.println("\n\n");
		MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.forEach((et, av) -> {
			System.out.print("rarity ");
			System.out.print(et.getName());
			System.out.print(", -> ");
			System.out.println(av);
		});

	}
}