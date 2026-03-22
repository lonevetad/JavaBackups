package tools.gui.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

/**
 * Took from "culmat"'s reply on <a href=
 * "https://stackoverflow.com/questions/1738966/java-jtextfield-with-input-hint">StackOverflow</a>.
 */
public class HintTextFieldUI extends BasicTextFieldUI implements FocusListener {

	protected boolean hideOnFocus;
	protected int cacheComputedColorRGB = 0;
	protected String hint;
	protected Color color, cacheComputedColor;

	public Color getColor() { return color; }

	public void setColor(Color color) {
		this.color = color;
		repaint();
	}

	private void repaint() { if (getComponent() != null) { getComponent().repaint(); } }

	public boolean isHideOnFocus() { return hideOnFocus; }

	public void setHideOnFocus(boolean hideOnFocus) {
		this.hideOnFocus = hideOnFocus;
		repaint();
	}

	public String getHint() { return hint; }

	public void setHint(String hint) {
		this.hint = hint;
		repaint();
	}

	public HintTextFieldUI(String hint) { this(hint, false); }

	public HintTextFieldUI(String hint, boolean hideOnFocus) { this(hint, hideOnFocus, null); }

	public HintTextFieldUI(String hint, boolean hideOnFocus, Color color) {
		super();
		this.hint = hint;
		this.hideOnFocus = hideOnFocus;
		this.color = color;
	}

	@Override
	protected void paintSafely(Graphics g) {
		super.paintSafely(g);
		JTextComponent comp = getComponent();
		if (hint != null && hint.length() != 0 && comp.getText().length() == 0 && (!(hideOnFocus && comp.hasFocus()))) {
			Color oldColor = g.getColor();
			g.setColor((color != null) ? color : getDefaultHintColor());
			Insets ins = comp.getInsets();
			FontMetrics fm = g.getFontMetrics();
			if (g instanceof Graphics2D) {
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			}
			Font oldFont = g.getFont();
			g.setFont(oldFont.deriveFont(Font.ITALIC));
			// draw it :D
//			OLD
//			int padding = (comp.getHeight() - comp.getFont().getSize()) >> 1;
//			g.drawString(hint, 2, comp.getHeight() - padding - 1);
			g.drawString(hint, ins.left,
					Math.max(comp.getHeight(), comp.getPreferredSize().height) - fm.getDescent() - ins.bottom);
			g.setColor(oldColor);
			g.setFont(oldFont);
		}
	}

	protected Color getDefaultHintColor() {
		JTextComponent comp = getComponent();
		int c0 = comp.getBackground().getRGB();
		int c1 = comp.getForeground().getRGB();
		int m = 0xfefefefe;
		int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
		if (c2 != cacheComputedColorRGB || cacheComputedColor == null) {
			cacheComputedColor = new Color(cacheComputedColorRGB = c2, true);
		}
		return cacheComputedColor;
	}

	@Override
	public void focusGained(FocusEvent e) { if (hideOnFocus) { repaint(); } }

	@Override
	public void focusLost(FocusEvent e) { if (hideOnFocus) { repaint(); } }

	@Override
	protected void installListeners() {
		super.installListeners();
		getComponent().addFocusListener(this);
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		getComponent().removeFocusListener(this);
	}
}