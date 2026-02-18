package tests.tDataStruct;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import dataStructures.HeapFibonacci;
import tools.gui.swing.HeapFibonacciVisualizerHelper;
import tools.gui.swing.HeapFibonacciVisualizerHelper.LocationData;

public class Test_HeapFibonacci_01_Simple {
	static final int CELL_SIZE = 40;
	static JFrame fin;
	static JScrollPane jsp;
	static JPanel jp;

	protected static <T> void setupGUI(HeapFibonacci<T> h) {
		fin = new JFrame("test heap fibonacci 01");
		fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fin.setLayout(new BorderLayout());

		JButton jbExtractMin;
		jbExtractMin = new JButton("extract min");
		jbExtractMin.addActionListener(l -> {
			T min;

			System.out.println("old size: " + h.size());
			min = h.extractMin();
			System.out.println("new size: " + h.size());
			System.out.println("extracted:");
			System.out.println(min);
			jp.removeAll();
			jp.repaint();

			HeapFibonacciVisualizerHelper.displayData(h, Test_HeapFibonacci_01_Simple::showData);
			jp.repaint();
		});
		jbExtractMin.setSize(100, 40);
		fin.add(jbExtractMin, BorderLayout.NORTH);

		jp = new JPanel();
		jp.setLayout( //
				// new GridLayout(5, CELL_SIZE >> 2) //
				null //
		);
		jp.addContainerListener(new ContainerAdapter() {

			@Override
			public void componentAdded(ContainerEvent e) {
				Component c;
//				c = e.getComponent();
				c = e.getChild();
				jp.setSize(
						//
						Math.max(jp.getWidth(), c.getX() + c.getWidth()) //
				, //
						Math.max(jp.getHeight(), c.getY() + c.getHeight())//
				);
				jp.setPreferredSize(jp.getSize());
				super.componentAdded(e);
			}

		});
		jsp = new JScrollPane(jp);
		jsp.setViewportView(jp);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		fin.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
//				System.out.println(fin.getPreferredSize());
				jsp.setSize(fin.getWidth() - 25, fin.getHeight() - 100);
				jsp.setPreferredSize(new Dimension((int) fin.getPreferredSize().getWidth() - 25,
						(int) (fin.getPreferredSize().getHeight() - 25)));
			}
		});
		jsp.setLocation(0, 45);
		fin.add(jsp, BorderLayout.CENTER);

		fin.setSize(200, 200);
		fin.setVisible(true);
//		fin.show();
	}

	public static void main(String[] args) {
		final int MAX = 16;
		HeapFibonacci<Integer> h;

		System.out.println("START");
		h = new HeapFibonacci<Integer>(Integer::compareTo, true);
		System.out.println("heap allocato");

// TODO fill
		System.out.println("insert inizial");
		h.insert(0);
		for (int i = 0; i < MAX; i++) {
			h.insert(i);
		}
//		h.extractMin();

		h.cleanUp();

		System.out.println("finito :D\n\n");

		//

		// gui & family
		setupGUI(h);

		System.out.println("display data");
		HeapFibonacciVisualizerHelper.displayData(h, Test_HeapFibonacci_01_Simple::showData);

		System.out.println("fine");
	}

	protected static <T> void showData(Map<Integer, LocationData<T>> nodesLocation, T data, LocationData<T> loc) {
		// TODO : visualizza su schermo
// remember to use "fin"
		JLabel jl;
		LocationData<T> parent;

		if (loc.fatherLocation != null) {
			parent = nodesLocation.get(loc.fatherLocation.ID);
		} else {
			parent = null;
		}

		jl = new JLabel(
				String.valueOf(data) + " -> p: " + String.valueOf(parent == null ? "null" : parent.node.getData()));
		jl.setLocation(loc.x * ((CELL_SIZE * 3) + 5), loc.y * (CELL_SIZE + 5));
		jl.setSize(CELL_SIZE * 3, CELL_SIZE);
		jl.setBorder(BorderFactory.createLineBorder(Color.red, 2));
		jp.add(jl);

		System.out.println("showing \"" + data + "\" at (" + loc.x + ", " + loc.y + ")");
		fin.setSize(Math.max(fin.getWidth(), CELL_SIZE * loc.x), Math.max(fin.getHeight(), CELL_SIZE * loc.y));
	}
}