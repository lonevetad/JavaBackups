package common.utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import common.mainTools.dataStruct.MyLinkedList;

public class ImageDisassembler {
	public static enum JTA_Meaning {
		xStart, yStart, width, height, xOffset, yOffset;
		final int index;

		JTA_Meaning() {
			index = SF.n++;
		}

		static final class SF {
			static int n = 0;
		}
	}

	static final JTA_Meaning[] valuesJTA_Meaning = JTA_Meaning.values();

	public static void main(String[] args) {
		ImageDisassembler id;
		id = new ImageDisassembler();
		id.inizializza();
	}

	public static void startTest() {
		System.out.println("Started");
		String path, name, pathDestination;
		BufferedImage bi;
		BufferedImage[] a;
		Scanner scan;

		scan = new Scanner(System.in);
		// path = "/home/marco/Scrivania/Marco/Immagini/";
		pathDestination = "/home/marco/Scrivania/Marco/Programmazione/Progetti Java/TestSalvataggio/";
		name = "Cat Run Cycle";
		System.out.println("Insert the folder's path to take the image ");

		path = scan.nextLine();

		bi = ImageUtilities.readImage(path, name);
		if (bi != null) {
			a = dissasembleImage(bi, 0, 0, 193, 100, 10, 8);
			if (a != null) {
				if (a.length > 0) {
					System.out.print("salvo le sottoimmagini :");
					System.out.println(ImageUtilities.writeImages(a, pathDestination, "cat disassembled",
							ImageUtilities.extensionImageFile[0], true));
				} else
					System.err.println("a's length on main is zero");
			} else
				System.err.println("a is null on main");
		} else
			System.err.println("bi is null");
		scan.close();
		System.out.println("finished");
	}

	public ImageDisassembler() {
		initializeFinished = false;
	}

	JFrame fin;
	JLabel jl, jlImageShow, jlImage, jlFolderDest, jlNomeImmagine, jlWidth, jlHeight, jlStartXOffset, jlStartYOffset,
			jlXOffset, jlYOffset;
	JScrollPane jspImageShow;
	JTextArea jtaImage, jtaFolderDest, jtaNomeImmagine, jtaWidth, jtaHeight, jtaStartXOffset, jtaStartYOffset,
			jtaXOffset, jtaYOffset;
	JButton jbImage, jbFolderDest, jbReloadImage, jbDisassemble;
	JCheckBox jcbCounterAtEnd;
	JFileChooser chooser;
	transient JComponent[] allJComponent;
	JTextArea allJTA[];
	transient BufferedImage orig;
	String path;
	File imageFile, folderOutputFile, imageFileFolder;
	boolean initializeFinished;
	int[] intValues;

	//

	// metodi utili

	void inizializza() {
		int i, s;
		JComponent jcTemp;
		if (!initializeFinished) {
			fin = new JFrame("Image Disassembler 2.0");
			fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			fin.setSize(50, 50);
			fin.setVisible(true);
			fin.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					adaptSize();
				}
			});
			jl = new JLabel();
			jl.setVisible(true);
			fin.add(jl);
			/* sono pigro, quindi compatto tutto in un array */
			allJComponent = new JComponent[] { jlImageShow = new JLabelImageShow(), jlImage = new JLabel("Image Path"),
					jlFolderDest = new JLabel("Folder output"), jlNomeImmagine = new JLabel("Image name"),
					jlWidth = new JLabel("Width Rectangle"), jlHeight = new JLabel("Height Rectangle"),
					jlStartXOffset = new JLabel("X start offset"), jlStartYOffset = new JLabel("Y start offset"),
					jlXOffset = new JLabel("X inner offset"), jlYOffset = new JLabel("Y inner offset") //
					, jtaImage = new JTextArea(), jtaFolderDest = new JTextArea(), jtaNomeImmagine = new JTextArea(),
					jtaWidth = new JTextArea("0"), jtaHeight = new JTextArea("0"), jtaStartXOffset = new JTextArea("0"),
					jtaStartYOffset = new JTextArea("0"), jtaXOffset = new JTextArea("0"),
					jtaYOffset = new JTextArea("0") //
					, jbImage = new JButton("Choose Image"), jbFolderDest = new JButton("Choose Folder Output"),
					jbReloadImage = new JButton("Reload Selected Image"),
					jbDisassemble = new JButton("Disassemble Image")//
					, jcbCounterAtEnd = new JCheckBox("V= end, else start")//
			};
			i = -1;
			s = allJComponent.length;
			while (++i < s) {
				jcTemp = allJComponent[i];
				jcTemp.setVisible(true);
				jl.add(jcTemp);
			}
			allJComponent = null;

			allJTA = new JTextArea[] { jtaStartXOffset, jtaStartYOffset, jtaWidth, jtaHeight, jtaXOffset, jtaYOffset };
			intValues = new int[allJTA.length];
			i = -1;
			s = allJTA.length;
			while (++i < s) {
				allJTA[i].addKeyListener(new JTA_KeyListener(allJTA[i], i, intValues));
			}
			allJTA = null;

			jspImageShow = new JScrollPane(jlImageShow);
			jspImageShow.setViewportView(jlImageShow);
			jspImageShow.setVisible(true);
			jl.add(jspImageShow);

			jbFolderDest.addActionListener((l) -> {
				resetPathFolderOuput();
			});

			jbImage.addActionListener((l) -> {
				resetPathImage();
			});

			jbReloadImage.addActionListener((l) -> {
				reloadImage();
			});

			jbDisassemble.addActionListener((e) -> {
				disassembleImage();
			});
			fin.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			initializeFinished = true;
		}
	}

	void adaptSize() {
		int w, h, hw, h_8;
		if (!initializeFinished) {
			return;
		}
		w = fin.getWidth();
		h = fin.getHeight();
		h_8 = h >> 4;

		jlImage.setSize(w >> 3, h_8);
		jlImage.setLocation(0, 0);
		jlFolderDest.setSize(jlImage.getSize());
		jlFolderDest.setLocation(jlImage.getX(), jlImage.getY() + jlImage.getHeight() + 2);
		jbImage.setSize(Math.min(200, w >> 2), jlImage.getHeight());
		jbImage.setLocation(w - ((jbImage.getWidth() << 1) + 2), jlImage.getY());
		jbFolderDest.setSize(jbImage.getSize());
		jbFolderDest.setLocation(jbImage.getX() + jbImage.getWidth(), jlImage.getY());
		jbReloadImage.setLocation(jbImage.getX(), jbImage.getY() + jbImage.getHeight() + 2);
		jbReloadImage.setSize(jbImage.getSize());
		jbDisassemble.setSize(jbImage.getSize());
		jbDisassemble.setLocation(jbFolderDest.getX(), jbFolderDest.getY() + jbFolderDest.getHeight() + 2);

		jtaImage.setLocation(jlImage.getX() + jlImage.getWidth() + 2, jlImage.getY());
		jtaImage.setSize(jbImage.getX() - (jtaImage.getX() + 2), jlImage.getHeight());
		jtaFolderDest.setLocation(jtaImage.getX(), jlFolderDest.getY());
		jtaFolderDest.setSize(jtaImage.getSize());
		jlNomeImmagine.setLocation(jlFolderDest.getX(), jlFolderDest.getY() + jlFolderDest.getHeight() + 2);
		jlNomeImmagine.setSize(jlImage.getSize());
		jtaNomeImmagine.setLocation(jtaFolderDest.getX(), jtaFolderDest.getY() + jtaFolderDest.getHeight() + 2);
		jtaNomeImmagine.setSize(jtaImage.getSize());

		jspImageShow.setLocation(0, jtaNomeImmagine.getY() + jtaNomeImmagine.getHeight() + 2);
		h -= (jspImageShow.getY() + 32);
		jspImageShow.setSize(h, h);

		w -= h;
		hw = w >> 1;

		jlStartXOffset.setLocation(h, jspImageShow.getY());
		jlStartXOffset.setSize(hw - 8, h_8);
		jtaStartXOffset.setLocation(jlStartXOffset.getX(), jlStartXOffset.getY() + jlStartXOffset.getHeight());
		jtaStartXOffset.setSize(jlStartXOffset.getSize());
		jlStartYOffset.setLocation(jlStartXOffset.getWidth() + jlStartXOffset.getX() + 8, jlStartXOffset.getY());
		jlStartYOffset.setSize(jlStartXOffset.getSize());
		jtaStartYOffset.setLocation(jlStartYOffset.getX(), jlStartYOffset.getY() + jlStartYOffset.getHeight());
		jtaStartYOffset.setSize(jlStartYOffset.getSize());

		jlWidth.setLocation(jlStartXOffset.getX(), jtaStartXOffset.getY() + jtaStartXOffset.getHeight() + 8);
		jlWidth.setSize(jlStartXOffset.getSize());
		jtaWidth.setLocation(jlWidth.getX(), jlWidth.getY() + jlWidth.getHeight());
		jtaWidth.setSize(jlStartXOffset.getSize());
		jlHeight.setLocation(jlWidth.getWidth() + jlWidth.getX() + 8, jlWidth.getY());
		jlHeight.setSize(jlStartXOffset.getSize());
		jtaHeight.setLocation(jlHeight.getX(), jlHeight.getY() + jlHeight.getHeight());
		jtaHeight.setSize(jlStartYOffset.getSize());

		jlXOffset.setLocation(jtaWidth.getX(), jtaWidth.getY() + jtaWidth.getHeight() + 8);
		jlXOffset.setSize(jlStartXOffset.getSize());
		jtaXOffset.setLocation(jlXOffset.getX(), jlXOffset.getY() + jlXOffset.getHeight());
		jtaXOffset.setSize(jlStartXOffset.getSize());
		jlYOffset.setLocation(jlXOffset.getWidth() + jlXOffset.getX() + 8, jlXOffset.getY());
		jlYOffset.setSize(jlStartXOffset.getSize());
		jtaYOffset.setLocation(jlYOffset.getX(), jlYOffset.getY() + jlYOffset.getHeight());
		jtaYOffset.setSize(jlStartYOffset.getSize());

		jcbCounterAtEnd.setLocation(jbReloadImage.getX(), jbReloadImage.getY() + jbReloadImage.getHeight() + 2);
		jcbCounterAtEnd.setSize(fin.getWidth() - (2 + jcbCounterAtEnd.getX()), jbImage.getHeight());
	}

	void disassembleImage() {
		boolean ok;
		if (jtaFolderDest.getText().length() > 0) {
			ok = ImageUtilities.writeImages(dissasembleImage(orig, intValues[JTA_Meaning.xStart.index]//
					, intValues[JTA_Meaning.yStart.index]//
					, intValues[JTA_Meaning.width.index]//
					, intValues[JTA_Meaning.height.index]//
					, intValues[JTA_Meaning.xOffset.index]//
					, intValues[JTA_Meaning.yOffset.index]//
			), jtaFolderDest.getText(), this.jtaNomeImmagine.getText(), ImageUtilities.extensionImageFile[0],
					jcbCounterAtEnd.isSelected());
			JOptionPane.showMessageDialog(fin, "Done ? " + ok, "Image disassembled", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	void reloadImage() {
		if (path != null /* && jtaNomeImmagine.getText() != null */) {
			try {
				orig = ImageUtilities.readImage(path, jtaNomeImmagine.getText());
				if (orig != null) {
					jlImageShow.setIcon(new ImageIcon(orig));
					jlImageShow.setSize(orig.getWidth(), orig.getHeight());
					jlImageShow.setPreferredSize(jlImageShow.getSize());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void resetPathImage() {
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(imageFileFolder == null ? new java.io.File(".") : imageFileFolder);
		chooser.setDialogTitle("Choose image to disassemble");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// disable the "All files" option.
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Images", ImageUtilities.extensionImageFile));
		if (chooser.showOpenDialog(fin) == JFileChooser.APPROVE_OPTION) {
			imageFile = chooser.getSelectedFile();
			try {
				jtaImage.setText(path = imageFile.getCanonicalPath());
				jtaNomeImmagine.setText(removeExtension(imageFile.getName()));
				path = path.substring(0, path.lastIndexOf(File.separatorChar)) + File.separatorChar;
				imageFileFolder = new File(path);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void resetPathFolderOuput() {
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(this.folderOutputFile == null ? new java.io.File(".") : folderOutputFile);
		chooser.setDialogTitle("Choose the directory to put images");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// disable the "All files" option.
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(fin) == JFileChooser.APPROVE_OPTION) {
			folderOutputFile = chooser.getSelectedFile();
			try {
				jtaFolderDest.setText(folderOutputFile.getCanonicalPath() + File.separatorChar);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void paintGrid(Graphics g) {
		int r, c, ww, hh, xStart, yStart, width, height, xOffset, yOffset;

		xStart = intValues[JTA_Meaning.xStart.index];
		yStart = intValues[JTA_Meaning.yStart.index];
		width = intValues[JTA_Meaning.width.index];
		height = intValues[JTA_Meaning.height.index];
		xOffset = intValues[JTA_Meaning.xOffset.index];
		yOffset = intValues[JTA_Meaning.yOffset.index];

		g.setColor(Color.blue);
		if (orig != null && width > 1 && height > 1) {
			ww = orig.getWidth();
			hh = orig.getHeight();

			r = yStart;
			while (r < hh) {
				c = xStart;
				while (c < ww) {
					g.drawRect(c, r, width, height);
					c += width + xOffset;
				}
				r += height + yOffset;
			}
		}
	}

	//

	// TODO METODI STATICI

	public static int indexFilenameExtension(String fn) {
		int i;
		if (fn == null || fn.trim().equals(""))
			return -1;
		i = fn.length();
		while (--i >= 0 && fn.charAt(i) != '.')
			;
		return i;
	}

	public static boolean hasExtension(String fn) {
		return indexFilenameExtension(fn) >= 0;
	}

	public static String removeExtension(String s) {
		int i;
		i = indexFilenameExtension(s);
		if (i < 0)
			return s;
		else if (i == 0)
			return "";
		return s.substring(0, i);
	}

	public static int convertStringToInteger(String s) {
		int r = 0, sign, l, p;
		char c;
		if (s != null) {
			l = s.length();
			if (l > 0) {
				p = sign = 1;
				// l--;
				while (--l >= 0) {
					c = s.charAt(l);
					if (c >= '0' && c <= '9') {
						r += (c - '0') * p;
						p *= 10;
						sign = 1;
					} else if (c == '-') {
						sign = -1;
					}
				}
				if (sign < 0) {
					r = -r;
				}
			}
		}
		// System.out.println("converted into " + r);
		return r;
	}

	public static final BufferedImage[] dissasembleImage(BufferedImage bi, int xstart, int ystart, int w, int h,
			int xjump, int yjump) {
		int wbi, hbi, c, r, i, t, ct, rt, cc, rr, s;
		BufferedImage temp;
		BufferedImage[] ret = null;
		MyLinkedList<BufferedImage> buff;
		MyLinkedList.NodeList<BufferedImage> iter;
		if (bi != null && w > 0 && h > 0 && xstart >= 0 && ystart >= 0 && xjump >= 0 && yjump >= 0) {
			wbi = bi.getWidth();
			hbi = bi.getHeight();
			if (wbi >= (xstart + w) && hbi >= (ystart + h)) {
				t = bi.getType();
				/* i due indici che scorrono "temp" */
				/*
				 * i punti di partenza da cui far ripartire gli scorrimenti di "bi" originale,
				 * nei due cicli più interni, per ottimizzare gli accessi a "bi"
				 */

				buff = new MyLinkedList<BufferedImage>();
				i = 0;
				r = ystart;
				while (r < hbi) {

					c = xstart;
					while (c < wbi) {

						temp = new BufferedImage(w, h, t);
						// ora ciclo per riempire temp:
						// preparazione variabili
						rt = r;
						// ciclo di settaggio di temp
						for (rr = 0; (rr < h && rt < hbi); rr++) {
							ct = c;
							for (cc = 0; (cc < w && ct < wbi); cc++) {
								temp.setRGB(cc, rr, bi.getRGB(ct++, rt));
							}
							rt++;
						}
						buff.add(temp);
						c += xjump + w;
					}
					r += yjump + h;
				}
				s = buff.size();
				ret = new BufferedImage[s];
				if (s > 0) {
					iter = buff.getHead();
					i = -1;
					do {
						ret[++i] = iter.getItem();
					} while ((iter = iter.getNext()) != null);
				}
			}
		}
		return ret;
	}

	//

	//

	// fra126-4.qwe58-7d
	public static double castStringDouble(String stringa_da_controllare) {
		double final_number = 0.0;
		boolean atLeastOneDigitFound = false;
		boolean prima_virgola_trovata = false;
		int lunghezza_stringa = stringa_da_controllare.length();
		int segno = 1;
		int x = 0;
		char numeriSimboli[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+', '.', ',' };
		int k = 0;
		String numeri_prima_del_punto = "";
		String numeri_dopo_il_punto = "";
		boolean somethingFound = false;
		while (k < lunghezza_stringa) {
			somethingFound = false;
			for (int g = 0; ((g < numeriSimboli.length) & (!somethingFound)); g++) {
				if (stringa_da_controllare.charAt(k) == numeriSimboli[g]) {
					if (g < 10) {// digits
						atLeastOneDigitFound = true;
						if (prima_virgola_trovata) {
							numeri_dopo_il_punto = stringa_da_controllare.charAt(k) + numeri_dopo_il_punto;
						}
						// if ( prima_virgola_trovata == false)
						else {
							numeri_prima_del_punto = numeri_prima_del_punto + stringa_da_controllare.charAt(k);
						}
						somethingFound = true;
					} else if ((g == 10) || (g == 11)) {// segno
						if (!atLeastOneDigitFound) {
							if (g == 10) {
								segno = -1;
							}
							somethingFound = true;
						} // se la prima cosa "utile" che trovo � un segno,
							// allora lo conto ..
							// se invece ho gi� trovato almeno una cifra o un
							// punto all'inizio (il quale far� poi presupporre
							// di anteporre uno 0), lo ignoro
					} else if ((g == 12) || (g == 13)) {
						prima_virgola_trovata = true;
						// somethingFound = true; // forse si potrebbe anche
						// omettere
					} // virgola
				}
			}
			k++;
		}
		while (x < numeri_prima_del_punto.length()) {
			// calcolo parte intera, prima della virgola
			somethingFound = false;
			for (int y = 0; ((!somethingFound) && (y < numeriSimboli.length)); y++) {
				if (numeri_prima_del_punto.charAt(x) == numeriSimboli[y]) {
					final_number += (y * Math.pow(10, (numeri_prima_del_punto.length() - (x + 1))));
					somethingFound = true;
				}
			}
			x++;
		}
		x = 0;
		if (prima_virgola_trovata) {
			while (x < numeri_dopo_il_punto.length()) {
				/*
				 * calcolo parte decimale, dopo della virgola
				 */
				somethingFound = false;
				for (int y = 0; ((!somethingFound) && (y < numeriSimboli.length)); y++) {
					if (numeri_dopo_il_punto.charAt(x) == numeriSimboli[y]) {
						final_number += (y * (1.0 / (Math.pow(10, (numeri_dopo_il_punto.length() - x)))));
						somethingFound = true;
					}
				}
				x++;
			}
		}
		return final_number * segno;
	}

	//

	// TODO CLASS

	class JLabelImageShow extends JLabel {
		private static final long serialVersionUID = 89595236023055L;

		JLabelImageShow() {
			super();
			addMouseMotionListener(new MouseMotionListener() {

				@Override
				public void mouseMoved(MouseEvent e) {
					repaint();
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					mouseMoved(e);
				}
			});
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(orig, 0, 0, null);
			paintGrid(g);
		}
	}

	static class JTA_KeyListener extends KeyAdapter {
		int index;
		int[] a;
		JTextArea jta;

		JTA_KeyListener(JTextArea jta, int index, int[] a) {
			this.jta = jta;
			this.index = index;
			this.a = a;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			a[index] = Math.max(0, convertStringToInteger(jta.getText()));
		}

	}
}