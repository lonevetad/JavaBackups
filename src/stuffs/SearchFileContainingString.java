package stuffs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeMap;

public class SearchFileContainingString {

	public static final String[] defaultExtension = { "fileList", "txt", "c", "lua", "htm", "html", "jsDiameter", "py", "php",
			"h", "yml", "config", "java", "go", "golang", "sh", "log" };
	public static final Comparator<String> stringComparator = (String s1, String s2) -> {
		if (s1 == s2)
			return 0;
		if (s1 == null)
			return -1;
		if (null == s2)
			return 1;

		return s1.compareTo(s2);
	};

	private static final String syes = "y";

	public SearchFileContainingString() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		boolean mustContinue, mustClose;
		int i, l, sl;
		long w;
		Scanner scan;
		String text, extList;
		File f;
		String[] userExt;
		TreeMap<String, String> extensions;
		PrintStream ps;

		// StringTokenizer st;

		scan = new Scanner(System.in);
		mustContinue = true;
		mustClose = false;
		w = 0;
		ps = null;

		do {
			System.out.println("Insert the text you are looking for:");
			text = scan.nextLine();

			extensions = generateFullRBTExtension();
			System.out.println("\n These are the allowed extension used for filtering files:\n\t");
			System.out.println(Arrays.toString(defaultExtension));
			System.out.println("\n Would you like to ADD other EXTENSION? Insert \"" + syes
					+ "\" to confirm, everything else to use the list shown above.");

			if (userInseredYes(scan)) {
				// st = new StringTokenizer();
				System.out.println("insert the whole list of extension, separeted with a single space");
				extList = scan.nextLine();

				userExt = extList.split(" ");
				l = userExt.length;
				i = -1;
				while (++i < l) {
					extList = userExt[i];
					if ((sl = extList.length()) > 0 && extList.charAt(sl - 1) != '.') {
						extensions.put(extList, extList);
						// System.out.println("-----added: " + extList);
					}
				}
			}

			System.out.println("\n Insert the filename of the folder to start the search");
			f = new File(scan.nextLine());

			if (f.exists()) {

				System.out.println("\n Last question: output on CONSOLE? Insert \"" + syes
						+ "\" to confirm, everything else to a write on a file in the same starting folde insered above");

				if (!userInseredYes(scan)) {
					System.out.println("insert the name of the file (will end with \"" + defaultExtension[0]
							+ "\" as extension):");
					try {
						ps = new PrintStream(new File(f.getAbsolutePath() + File.separatorChar + scan.nextLine() + '.'
								+ defaultExtension[0]));
						mustClose = true;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						ps = System.out;
					}
				} else {
					ps = System.out;
				}

				System.out.println("Your list of file containing the required text: " + text + '\n');

				w = System.currentTimeMillis();
				searchFileContainingString(text, extensions, f, ps);
				w = System.currentTimeMillis() - w;

				System.out.println("\n\n DONE, tooks " + w + " milliseconds.");

				System.out.println(
						"\n\n ANOTHER SEARCH? Insert \"" + syes + "\" to continue, everything else otherwise.");
				mustContinue = userInseredYes(scan);
			} else {
				System.err.println("File did not exists: " + f.toString());
				mustContinue = false;
			}

		} while (mustContinue);

		if (mustClose && ps != null) {
			ps.close();
		}
		scan.close();
		System.out.println("\n\nFINISH");
	}

	private static TreeMap<String, String> generateFullRBTExtension() {
		int i, l;
		String s;
		TreeMap<String, String> t;

		t = new TreeMap<String, String>(stringComparator);
		l = defaultExtension.length;
		i = -1;
		while (++i < l) {
			t.put(s = defaultExtension[i], s);
		}
		return t;
	}

	private static final boolean userInseredYes(Scanner scan) {
		return syes.equalsIgnoreCase(scan.nextLine());
	}

	//

	public static final void searchFileContainingString(String text, TreeMap<String, String> extensions,
			File fileStart) {
		// System.out.println("\n\n\nAAAAAHHHH\n\n\n");
		searchFileContainingString(text, extensions, fileStart, System.out);
	}

	public static final void searchFileContainingString(String text, TreeMap<String, String> extensions, File fileStart,
			PrintStream ps) {
		boolean notContaining;
		int i, l, dotIndex;
		String s, contenuto;
		BufferedReader br;
		File f;
		File[] fl;

		fl = fileStart.listFiles();
		if (fl != null && (l = fl.length) > 0) {
			i = -1;
			while (++i < l) {
				f = fl[i];
				if (f != null && f.exists()) {
					if (f.isDirectory()) {
						// ricorsione
						searchFileContainingString(text, extensions, f, ps);
					} else {
						// the core
						s = f.getName();
						dotIndex = s.lastIndexOf('.');
						if (dotIndex >= 0 && dotIndex < (s.length() - 1)
								&& extensions.containsKey(s.substring(dotIndex + 1))) {
							// estensione accettata, check del contenuto
							try {
								br = new BufferedReader(new FileReader(f));
								notContaining = true;

								while (((contenuto = br.readLine()) != null)
										&& (notContaining = (!contenuto.contains(text))))
									;
								if (!notContaining) {
									ps.println(f.getAbsolutePath());
									ps.flush();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

}