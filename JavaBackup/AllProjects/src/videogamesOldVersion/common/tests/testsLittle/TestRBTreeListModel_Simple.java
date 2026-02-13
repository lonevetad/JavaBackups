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
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import common.abstractCommon.behaviouralObjectsAC.MyComparator;
import common.mainTools.JListModelTreeMap;
import tools.RedBlackTree;

public class TestRBTreeListModel_Simple {

	public TestRBTreeListModel_Simple() {
		model = new RedBlackTree<>(Integer::compare);
		listModel = JListModelTreeMap.newInstance(model, m -> m.i);
	}

	RedBlackTree<Integer, ModelData> model;
	JListModelTreeMap<Integer, ModelData> listModel;

	// gui
	JFrame fin;
	JPanel jp;
	JButton jbDeleteSelected, jbClear;
	JTextField jtf;
	JScrollPane jspJList;
	JList<ModelData> jlist;
	GridBagLayout gbl;

	//

	void init() {
		GridBagConstraints c;
		fin = new JFrame(this.getClass().getSimpleName());
		fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jp = new JPanel(gbl = new GridBagLayout()) {
			private static final long serialVersionUID = 9840651960L;

			@Override
			public Dimension getPreferredSize() {
				return fin.getSize();
			}
		};
		fin.add(jp);
		// set up gui
		jbDeleteSelected = new JButton("Delete selected");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = c.gridwidth = 1;
		c.weighty = c.gridheight = 1;
		gbl.setConstraints(jbDeleteSelected, c);
		jp.add(jbDeleteSelected, c);

		jtf = new JTextField("Write something");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = c.gridwidth = 2;
		c.weighty = c.gridheight = 1;
		jtf.setPreferredSize(new Dimension(100, 35));
		jtf.setMinimumSize(jtf.getPreferredSize());
		gbl.setConstraints(jtf, c);
		jp.add(jtf, c);

		jbClear = new JButton("Clear");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = c.gridwidth = 1;
		c.weighty = c.gridheight = 1;
		gbl.setConstraints(jbClear, c);
		jp.add(jbClear, c);

		jlist = new JList<>(this.listModel);
		jspJList = new JScrollPane(jlist);
		jspJList.setViewportView(jlist);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = c.gridwidth = 4;
		c.weighty = c.gridheight = 7;
		gbl.setConstraints(jspJList, c);
		jp.add(jspJList, c);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.setCellRenderer(new RBTCellRenderSimple());
		jlist.setModel(listModel);

		// set behaviours
		jbDeleteSelected.addActionListener(l -> {
			ModelData md;
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
					listModel.add(new ModelData(jtf.getText()));
					jtf.setText("");
					jlist.repaint();
				}
			}
		});

		//
		fin.setSize(300, 300);
		fin.setVisible(true);
	}

	// CLASSES

	static class ModelData {
		public static final MyComparator<ModelData> COMPARATOR_ModelData = (md1, md2) -> {
			if (md1 == md2) return 0;
			if (md1 == null) return -1;
			if (md2 == null) return 1;
			return Integer.compare(md1.i.intValue(), md2.i.intValue());
		};
		static int counter = 0;
		final Integer i;
		String s;

		public ModelData(String s) {
			super();
			this.i = counter++;
			this.s = s;
		}

		@Override
		public String toString() {
			return s.toString();
		}
	}

	static class RBTCellRenderSimple extends JLabel implements ListCellRenderer<ModelData> {
		private static final long serialVersionUID = 84061845L;
		static final Border B_S = BorderFactory.createLineBorder(Color.BLUE, 3),
				B_US = BorderFactory.createLineBorder(Color.GRAY, 1);

		public RBTCellRenderSimple(/* Function<String, Integer> keyExtractor */) {
			super();
			// this.keyExtractor = keyExtractor;
		}

		// Function<String, Integer> keyExtractor;

		@Override
		public Component getListCellRendererComponent(JList<? extends ModelData> list, ModelData value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setText(value.toString());
			setBorder(isSelected ? B_S : B_US);
			return this;
		}

	}

	//

	public static void main(String[] args) {
		TestRBTreeListModel_Simple t;
		t = new TestRBTreeListModel_Simple();
		System.out.println("init start");
		t.init();
		System.out.println("init end");
	}

}