package tools.gui.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * Took from "Adam Gawne-Cain"'s reply on <a href=
 * "https://stackoverflow.com/questions/1738966/java-jtextfield-with-input-hint">StackOverflow</a>.
 *
 * @deprecated use {@link HintTextFieldUI} instead invoking
 *             <code>justATextField.setUI(new HintTextFieldUI("blabla"))</code>
 */
@Deprecated
public class JTextFieldHint extends JTextField {
	private static final long serialVersionUID = 321055102541521L;
	public static final String EMPTY_STRING = "";

	public JTextFieldHint() { super(); }

	public JTextFieldHint(Document doc, String text, int columns) { super(doc, text, columns); }

	public JTextFieldHint(int columns) { super(columns); }

	public JTextFieldHint(String text, int columns) { super(text, columns); }

	public JTextFieldHint(String text) { super(text); }

	protected String hint = EMPTY_STRING;
	protected int cachedHintColorRBG = 0;
	protected Color cachedHintColor = null;

	public String getHint() { return hint; }

	public void setHint(String hint) { this.hint = hint; }

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (getText().length() == 0 && hint != null && hint != EMPTY_STRING) {
			int h = getHeight();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			Insets ins = getInsets();
			FontMetrics fm = g.getFontMetrics();
			int c0 = getBackground().getRGB();
			int c1 = getForeground().getRGB();
			int m = 0xfefefefe;
			int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
			Color oldColor = g.getColor();
			if (cachedHintColor == null || c2 != cachedHintColorRBG) {
				g.setColor(cachedHintColor = new Color(cachedHintColorRBG = c2, true));
			}
			Font oldFont = g.getFont();
			g.setFont(oldFont.deriveFont(Font.ITALIC));
			// draw it :D
			g.drawString(hint, ins.left, // old version: h / 2 + fm.getAscent() / 2 - 2
					// ((h + fm.getDescent())>>1) - 2
					h - fm.getDescent() - ins.bottom);
			g.setColor(oldColor);
			g.setFont(oldFont);
		}
	}
}