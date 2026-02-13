package games.theRisingArmy.main;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import common.utilities.Methods;
import common.utilities.SerializableStuff;
import games.theRisingArmy.main.SharedStuffs_TRAr.ManaTypes;

public class ManaGroup implements SerializableStuff {

	private static final long serialVersionUID = 84887626002030L;

	public ManaGroup() {
		cacheBi = null;
		totalManaCost = totalManaColored = 0;
		manaQuantity = new int[SharedStuffs_TRAr.MANA_TYPES_LENGTH];
	}

	protected transient int totalManaCost, totalManaColored;
	protected int manaQuantity[];
	protected transient BufferedImage cacheBi;

	//

	// TODO GETTER

	public int getMana(SharedStuffs_TRAr.ManaTypes manaType) {
		return (manaType == null) ? -1 : manaQuantity[manaType.index];
	}

	public int getTotalManaCost() {
		return totalManaCost;
	}

	public int getTotalManaColored() {
		return totalManaColored;
	}

	//

	// TODO SETTER
	public boolean setMana(SharedStuffs_TRAr.ManaTypes manaType, int quantity) {
		if (manaType == null || quantity < 0) return false;
		deltaMana(manaType, quantity - manaQuantity[manaType.index]);
		return true;
	}

	//

	// TODO OTHE METHODS

	public boolean increaseMana(SharedStuffs_TRAr.ManaTypes manaType, int quantity) {
		if (manaType == null || quantity < 1) return false;
		deltaMana(manaType, quantity);
		return true;
	}

	public boolean decreaseMana(SharedStuffs_TRAr.ManaTypes manaType, int quantity) {
		if (manaType == null || quantity < 1) return false;
		deltaMana(manaType, -quantity);
		return true;
	}

	private void deltaMana(SharedStuffs_TRAr.ManaTypes manaType, int delta) {
		boolean iscolored;
		int prevQuantity;

		if (delta != 0) {
			cacheBi = null;
			prevQuantity = manaQuantity[manaType.index];
			if (delta < 0 && prevQuantity < (-delta)) throw new RuntimeException(
					"Not enought mana " + manaType.colorName + " stored to decrease  it by " + (-delta));

			iscolored = manaType != ManaTypes.Undefined;
			// applico il delta
			manaQuantity[manaType.index] += delta;
			// aggiorno le cache
			totalManaCost += delta;
			if (iscolored) {
				totalManaColored += delta;
			}
		}
	}

	public void reset() {
		int i, len;
		i = -1;
		len = SharedStuffs_TRAr.MANA_TYPES_LENGTH;
		while (++i < len) {
			manaQuantity[i] = 0;
		}
		cacheBi = null;
		totalManaColored = totalManaCost = 0;
	}

	public int recalculateTotalManaCost() {
		return recalculateManaAmout(SharedStuffs_TRAr.MANA_TYPES_LENGTH);
	}

	public int recalculateColoredManaCost() {
		return recalculateManaAmout(SharedStuffs_TRAr.MANA_TYPES_LENGTH - 1);
	}

	protected int recalculateManaAmout(int len) {
		int t; // , i;
		t = 0;
		// i = -1;
		// while (++i < len) {
		while (len-- > 0) {
			t += manaQuantity[len];
		}
		return t;
	}

	public BufferedImage getBufferedImageManaCost(int sizeSquareSymbol) {
		int manaUncolored, digitsManaUncolored, numberOfSymbols, i, xx, len, howMuchMana;
		int[] digitsUncolored = null;
		SharedStuffs_TRAr.ManaTypes mana;
		BufferedImage lastColored;

		if (totalManaCost == 0) return cacheBi = SharedStuffs_TRAr.ManaTypes.Undefined.getImage(sizeSquareSymbol);
		if (sizeSquareSymbol < 1) return null;
		if (cacheBi == null) {
			manaUncolored = totalManaCost - totalManaColored;
			digitsManaUncolored = 0;
			if (manaUncolored > 0) {
				digitsUncolored = Methods.separateDigits(manaUncolored); // digitsBase10(manaUncolored);
				digitsManaUncolored = digitsUncolored.length;
			}

			numberOfSymbols = digitsManaUncolored + totalManaColored;

			xx = 0;
			cacheBi = new BufferedImage(sizeSquareSymbol * numberOfSymbols, sizeSquareSymbol,
					BufferedImage.TYPE_4BYTE_ABGR);

			// i = 0;
			while (digitsManaUncolored-- > 0) {
				Methods.writeOn(SharedStuffs_TRAr.ManaTypes.Undefined.getImage(sizeSquareSymbol,
						digitsUncolored[digitsManaUncolored]), cacheBi, xx, 0);
				xx += sizeSquareSymbol;
			}

			// ora il colore

			i = -1;
			len = SharedStuffs_TRAr.MANA_COLORED_IMAGES_ORIGINAL.length;
			while (++i < len) {
				mana = SharedStuffs_TRAr.MANA_TYPES[i];
				howMuchMana = getMana(mana);
				lastColored = mana.getImage(sizeSquareSymbol);
				while (howMuchMana-- > 0) {
					Methods.writeOn(lastColored, cacheBi, xx, 0);
					xx += sizeSquareSymbol;
				}
			}
			//

			//
		}
		if (cacheBi == null) {
			// throw new IOException(
			System.err.println(
					"BufferedImage returned is null, probably because of one of icons' files has not been found");
		}
		return cacheBi;
	}

	@Override
	public void doAfterUnserialization() {
		totalManaCost = recalculateTotalManaCost();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if ((obj == null) || (getClass() != obj.getClass())) return false;
		ManaGroup other = (ManaGroup) obj;
		if (totalManaColored != other.totalManaColored) return false;
		if (totalManaCost != other.totalManaCost) return false;
		if (!Arrays.equals(manaQuantity, other.manaQuantity)) return false;
		return true;
	}

	//

	//

	public static void main(String[] args) {
		int i;
		String prefix;
		ManaGroup mg;
		mg = new ManaGroup();
		prefix = "resources/";
		i = 0;

		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "empty", mg.getBufferedImageManaCost(50));

		mg.setMana(ManaTypes.Fire, 1);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "1 fire", mg.getBufferedImageManaCost(50));

		mg.setMana(ManaTypes.Bio, 2);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "2 bio", mg.getBufferedImageManaCost(50));

		mg.setMana(ManaTypes.Water, 1);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "and 1 water", mg.getBufferedImageManaCost(50));

		mg.setMana(ManaTypes.Undefined, 3);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "and 3 undefinied", mg.getBufferedImageManaCost(50));

		mg.increaseMana(ManaTypes.Undefined, 9);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "increased 9 undefinied", mg.getBufferedImageManaCost(50));

		mg.increaseMana(ManaTypes.Undefined, 97);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "increased 97 undefinied", mg.getBufferedImageManaCost(50));

		mg.increaseMana(ManaTypes.Undefined, 2000);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "increased 2000 undefinied", mg.getBufferedImageManaCost(50));

		mg.decreaseMana(ManaTypes.Undefined, 999);
		mg.decreaseMana(ManaTypes.Bio, 1);
		mg.decreaseMana(ManaTypes.Water, 1);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "minus 999 undefinied and 1 bio and water",
				mg.getBufferedImageManaCost(50));

		mg.reset();
		System.out.println("RESETTED");
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "reset", mg.getBufferedImageManaCost(50));

		mg.setMana(ManaTypes.Undefined, 7);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "only 7 undefinied", mg.getBufferedImageManaCost(50));

		mg.setMana(ManaTypes.Undefined, 13);
		System.out.println("total mana: " + mg.getTotalManaCost() + ", mana colored: " + mg.getTotalManaColored());
		Methods.writeImage(prefix + (i++) + "__" + "only 13 undefinied", mg.getBufferedImageManaCost(50));

	}

}
