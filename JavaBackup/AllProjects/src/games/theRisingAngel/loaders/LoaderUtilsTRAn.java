package games.theRisingAngel.loaders;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.AttributesUpgrade;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.loaders.LoaderUtilsTRAn.OLD_LOADERS_JSON.JSONLineReader;

/** Set of utilities for loading stuffs on TRAr. */
public class LoaderUtilsTRAn {

	public static final String SAVES = LoaderGeneric.startPath + LoaderGeneric.sc + "saves" + LoaderGeneric.sc;

	//

	//

	//

	//

	//

	@Deprecated
	public static List<String> extractAbilitiesName(String temp) {
		String[] splitted;
		List<String> l;
		temp = temp.trim();
		temp = temp.substring(temp.indexOf('[') + 1, temp.lastIndexOf(']')).trim();
		if (temp.length() > 0) {
			splitted = temp.split(",");
			trimAll(splitted);
			for (int i = 0; i < splitted.length; i++) {
//				temp = splitted[i];
				splitted[i] = removeQuotes(splitted[i]);
			}
			l = Arrays.asList(splitted);
		} else {
			l = null; // new LinkedList<>();
		}
		return l;
	}

	@Deprecated
	public static int[] extractSellPrices(String temp) {
		String[] splitted;
		int[] l;
		temp = temp.trim();
		temp = temp.substring(temp.indexOf('[') + 1, temp.lastIndexOf(']')).trim();
		if (temp.length() > 0) {
			splitted = temp.split(",");
			trimAll(splitted);
			l = new int[splitted.length];
			for (int i = 0; i < splitted.length; i++) {
				l[i] = Integer.parseInt(splitted[i]);
			}
		} else {
			l = null; // new LinkedList<>();
		}
		return l;
	}

	@Deprecated
	public static Dimension extractDimensionInInventory(String temp) {
		Dimension d;
		String[] splitted;
		d = new Dimension();
		splitted = temp.split(",");
		for (int i = splitted.length - 1; i >= 0; i--) {
			temp = splitted[i];
			if (temp.contains("width")) {
				d.width = extractIntValue(temp.split(":")[1].trim());
			} else if (temp.contains("height")) { d.height = extractIntValue(temp.split(":")[1].trim()); }
		}
		return d;
	}

	/**
	 * This function was extracted and generalized because the "equipment system"
	 * could provide a set of {@link AttributesUpgrade} (that is a parallel of
	 * {@link AttributeModification} because it's a named collection of them) AND
	 * the set of those "equip-mods/upgrades" could be saved in a JSON format,
	 * similar to the one used in {@link LoaderEquipTRAn}
	 */
	@Deprecated
	public static AttributeModification[] extractAttributeModifications(JSONLineReader lr) {
		int v;
		AttributesTRAn attr;
		List<AttributeModification> attrMods;
		String splitted[], temp;
		attrMods = new LinkedList<>();
		while ((temp = lr.next().trim()).contains("{")) { // read the line and
			splitted = temp.split(",");
			trimAll(splitted);
			temp = trimAll(splitted[0].split(":"))[1].trim();// name of attribute
			temp = removeQuotes(temp); // remove double quotes
			attr = AttributesTRAn.getAttributeTRArByName(temp);
			temp = trimAll(splitted[1].split(":"))[1].trim();// value of attribute
			v = extractIntValue(temp);
			attrMods.add(new AttributeModification(attr, v));
		}
		AttributeModification[] a;
		a = new AttributeModification[attrMods.size()];
		return attrMods.toArray(a);
	}

	//

	// TODO MINOR UTILITIES

	public static String[] trimAll(String[] a) {
		int i;
		i = a.length;
		while (--i >= 0)
			a[i] = a[i].trim();
		return a;
	}

	public static String removeQuotes(String s) {
		int i;
		i = s.indexOf('\"');
		if (i < 0)
			return s;
		return s.substring(i + 1, s.lastIndexOf('\"'));
	}

	public static int extractIntValue(String s) {
		boolean isNeg;
		int i, pow, res, len;
		char c;
		len = s.length();
		pow = 1;
		res = 0;
		isNeg = (c = s.charAt(0)) == '-';
		if (isNeg || c == '+') {
			i = 0;
			while (++i < len && ((c = s.charAt(i)) >= '0') && c <= '9')
				;
			while (--i >= 1) {
				res += (s.charAt(i) - '0') * pow;
				pow *= 10;
			}
			if (isNeg)
				res = -res;
		} else {
			i = -1;
			while (++i < len && ((c = s.charAt(i)) >= '0') && c <= '9')
				;
			while (--i >= 0) {
				res += (s.charAt(i) - '0') * pow;
				pow *= 10;
			}
		}
		return res;
	}

	//

	//

	//

	static class OLD_LOADERS_JSON {

		/**
		 * Reads lines by lines, providing each of them for each invocation of
		 * {@link #next()}.
		 */
		public static class JSONLineReader implements Iterator<String> {
			String path, filename, line;
			BufferedReader reader;

			public JSONLineReader(String path, String filename) throws IOException {
				super();
				this.path = path;
				this.filename = filename;
				reader = new BufferedReader(new FileReader(path + LoaderGeneric.sc + filename));
				line = reader.readLine(); // read the '{' or '[' at the start
			}

			@Override
			public boolean hasNext() { return line != null && (!"}".equals(line)) && (!"]".equals(line)); }

			@Override
			public String next() {
				try {
					return line = reader.readLine().trim();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}

		public abstract static class JSONFileConsumer {
			protected JSONLineReader lr;

			/**
			 * The first string "subPath" must succeeds after {@link startPath} is setted
			 * and could be a blank string.
			 */
			protected JSONFileConsumer(String subPath, String filename) {
				try {
					lr = new JSONLineReader(LoaderGeneric.startPath, subPath + filename); // "configurationsTRAr.json");
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}

			public void readAllFile() {
				String line;// , splitted[];
				while (getLineReader().hasNext()) {
					line = getLineReader().next();
					readAllFileImpl(line);
				}
			}

			protected abstract void readAllFileImpl(String line);

			public JSONLineReader getLineReader() { return lr; }
		}
	}
}