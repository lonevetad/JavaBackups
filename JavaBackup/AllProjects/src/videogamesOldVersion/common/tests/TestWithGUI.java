package common.tests;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public abstract class TestWithGUI {

	public TestWithGUI() {
	}

	JFrame win;
	JPanel jp;
	GridBagLayout gbl;

	protected final void init() {
		win = new JFrame(this.getClass().getSimpleName());
		win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jp = new JPanel(gbl = new GridBagLayout()) {
			private static final long serialVersionUID = 9840651960L;

			@Override
			public Dimension getPreferredSize() {
				return win.getSize();
			}
		};
		win.add(jp);
		win.setSize(500, 300);
		continueInit();
		win.setVisible(true);
		win.setLocationRelativeTo(null);
	}

	protected abstract void continueInit();
}