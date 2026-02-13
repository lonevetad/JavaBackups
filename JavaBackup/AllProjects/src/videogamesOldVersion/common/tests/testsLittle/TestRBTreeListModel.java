package common.tests.testsLittle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import common.mainTools.Comparators;
import common.mainTools.JListModelTreeMap;
import tools.RedBlackTree;

public class TestRBTreeListModel {

	public TestRBTreeListModel() {
		model = new RedBlackTree<>(Comparators.STRING_COMPARATOR);
		listModel = JListModelTreeMap.newInstance(model);
	}

	RedBlackTree<String, String> model;
	JListModelTreeMap<String, String> listModel;

	// gui
	JFrame win;
	JPanel jp;
	JButton jbDeleteSelected, jbClear, jbFetchElementAt;
	JTextField jtf;
	JScrollPane jspJList, jspResult;
	JTextArea jta;
	JSpinner jspIndex;
	JList<String> jlist;
	GridBagLayout gbl;
	SpinnerNumberModel numberIndexSpinnerModel;

	//

	void init() {
		GridBagConstraints c;

		c = new GridBagConstraints();
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

		// set up gui

		jlist = new JList<>(this.listModel);
		jspJList = new JScrollPane(jlist);
		jspJList.setViewportView(jlist);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = c.gridwidth = 2;
		c.weighty = c.gridheight = 6;
		gbl.setConstraints(jspJList, c);
		jp.add(jspJList, c);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.setCellRenderer(new RBTCellRenderSimple());
		jlist.setModel(listModel);

		jbDeleteSelected = new JButton("Delete selected");
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = c.gridwidth = 1;
		c.weighty = c.gridheight = 1;
		gbl.setConstraints(jbDeleteSelected, c);
		jp.add(jbDeleteSelected, c);

		jbClear = new JButton("Clear");
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = c.gridwidth = 1;
		c.weighty = c.gridheight = 1;
		gbl.setConstraints(jbClear, c);
		jp.add(jbClear, c);

		jtf = new JTextField("Write something");
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = c.gridwidth = 2;
		c.weighty = c.gridheight = 1;
		jtf.setPreferredSize(new Dimension(100, 35));
		jtf.setMinimumSize(jtf.getPreferredSize());
		gbl.setConstraints(jtf, c);
		jp.add(jtf, c);

		jbFetchElementAt = new JButton("Fetch at");
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 2;
		c.weightx = c.gridwidth = 1;
		c.weighty = c.gridheight = 1;
		gbl.setConstraints(jbFetchElementAt, c);
		jp.add(jbFetchElementAt, c);

		numberIndexSpinnerModel = new SpinnerNumberModel(0, 0, 1, 1);
		jspIndex = new JSpinner(numberIndexSpinnerModel);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 3;
		c.gridy = 2;
		c.weightx = c.gridwidth = 1;
		c.weighty = c.gridheight = 1;
		gbl.setConstraints(jspIndex, c);
		jp.add(jspIndex, c);

		jta = new JTextArea();
		jspResult = new JScrollPane(jta);
		jspResult.setViewportView(jta);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 3;
		c.weightx = c.gridwidth = 2;
		c.weighty = c.gridheight = 3;
		gbl.setConstraints(jspResult, c);
		jp.add(jspResult, c);

		// set behaviours
		jbDeleteSelected.addActionListener(l -> {
			String md;
			md = jlist.getSelectedValue();
			if (md != null) {
				listModel.removeElement(md);
			}
		});
		jbClear.addActionListener(l -> listModel.clear());
		jtf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String s;
					s = jtf.getText();
					listModel.add(s);
					// model.add(s, s);
					numberIndexSpinnerModel.setMaximum(listModel.size() - 1);
					jtf.setText("");
					jlist.repaint();
				}
			}
		});
		jbFetchElementAt.addActionListener(l -> {
			jta.append("\n");
			jta.append(listModel.getElementAt(((Integer) jspIndex.getValue()).intValue()));
		});

		//
		win.setSize(500, 300);
		win.setVisible(true);
		win.setLocationRelativeTo(null);
	}

	// CLASSES

	static class RBTCellRenderSimple extends JLabel implements ListCellRenderer<String> {
		private static final long serialVersionUID = 84061845L;
		static final Border B_S = BorderFactory.createLineBorder(Color.BLUE, 3),
				B_US = BorderFactory.createLineBorder(Color.GRAY, 1);

		public RBTCellRenderSimple(/* Function<String, Integer> keyExtractor */) {
			super();
			// this.keyExtractor = keyExtractor;
		}

		// Function<String, Integer> keyExtractor;

		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {
			if (value != null) setText(value.toString());
			setBorder(isSelected ? B_S : B_US);
			return this;
		}

	}

	//

	public static void main(String[] args) {
		TestRBTreeListModel t;
		t = new TestRBTreeListModel();
		System.out.println("init start");
		t.init();
		System.out.println("init end");
	}

}