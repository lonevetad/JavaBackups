package videogamesOldVersion.common.gui;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class JListHorizontalWrapping<E> extends JList<E> {
	private static final long serialVersionUID = 45615096598909L;

	public JListHorizontalWrapping() {
		super();
	}

	public JListHorizontalWrapping(E[] listData) {
		super(listData);
	}

	public JListHorizontalWrapping(ListModel<E> dataModel) {
		super(dataModel);
	}

	public JListHorizontalWrapping(Vector<? extends E> listData) {
		super(listData);
	}

	{
		setVisibleRowCount(-1);
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
	}

	public static void main(String[] args) {
		DefaultListModel<String> model = new DefaultListModel<>();
		JList<String> sList = new JListHorizontalWrapping<>(model);
		for (int i = 0; i < 100; i++) {
			model.addElement("String " + i);
		}

		sList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		// sList.setVisibleRowCount(-1);
		// sList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

		JFrame frame = new JFrame("Foo001");
		frame.getContentPane().add(new JScrollPane(sList));
		frame.getContentPane().setPreferredSize(new Dimension(400, 300));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}