package stuffs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

public class PrologMazeGenerator {
	public static final Color COL = Color.BLUE;
	public static final int MAZE_SQUARE_SIZE = 15;

	static interface MazeConsumer {
		void doOnPixel(boolean[][] maze, int x, int y);
	}

	public PrologMazeGenerator() {
	}

	// view
	JFrame jf;
	JLabel jlMousePosition;
	TextArea taProlog;
	JTextField jtfNumCol, jtfNumRow;
	JButton jbSetSize, jbBuild, jbClear;
//	BufferedImage biMaze;
	JPanel jpMaze, jpFin, jpNorth;
	JScrollPane jspMaze;
	// model
	boolean[][] maze;

	void buildGUI() {
		jf = new JFrame("Prolog Maze Generator (row by row)");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jpFin = new JPanel();
		/*
		 * {
		 *
		 * @Override public Dimension getSize() { return jf.getSize(); }
		 *
		 * @Override public Dimension getPreferredSize() { return jf.getPreferredSize();
		 * }
		 *
		 * @Override public int getWidth() { return jf.getWidth(); }
		 *
		 * @Override public int getHeight() { return jf.getHeight(); } }
		 */
		jpFin.setLayout(new BorderLayout());
		jpFin.setBackground(COL);
		jf.add(jpFin);

		jpNorth = new JPanel();
		jpFin.add(jpNorth, BorderLayout.NORTH);
		jpNorth.add(new JLabel("N� Rows:"));
		jtfNumRow = new JTextField("10");
		jtfNumRow.setSize(50, 25);
		jpNorth.add(jtfNumRow);
		jpNorth.add(new JLabel("N� Columns:"));
		jtfNumCol = new JTextField("10");
		jtfNumCol.setSize(jtfNumRow.getSize());
		jpNorth.add(jtfNumCol);
		jbSetSize = new JButton("Set size");
		jbSetSize.addActionListener(l -> resizeMatrix());
		jpNorth.add(jbSetSize);

		jbClear = new JButton("Clear");
		jbClear.addActionListener(l -> clearMatrix());
		jpNorth.add(jbClear);
		jbBuild = new JButton("Build");
		jbBuild.addActionListener(l -> printMatrixToProlog());
		jpNorth.add(jbBuild);

		//

		jpMaze = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				paintMaze(g);
			}
		};
		jspMaze = new JScrollPane(jpMaze);
		jspMaze.setViewportView(jpMaze);
		jpFin.add(jspMaze, BorderLayout.CENTER);
		jlMousePosition = new JLabel("Mouse position: ");
		jspMaze.setColumnHeaderView(jlMousePosition);
		jspMaze.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jspMaze.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jspMaze.setSize(500, 500);
		jspMaze.setPreferredSize(jspMaze.getSize());
		MouseAdapter ma;
		ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int y, x;
					if (maze == null)
						return;
					y = e.getY() / MAZE_SQUARE_SIZE;
					x = e.getX() / MAZE_SQUARE_SIZE;
					if (y >= maze.length || x >= maze[0].length)
						return;
					showMousePosition(x, y);
					setAt(x, y, e.getButton() == MouseEvent.BUTTON1);
					jpMaze.repaint();
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				int y, x;
				if (maze == null)
					return;
				y = e.getY() / MAZE_SQUARE_SIZE;
				x = e.getX() / MAZE_SQUARE_SIZE;
				if (y >= maze.length || x >= maze[0].length)
					return;
				showMousePosition(x, y);
				setAt(x, y, !(e.isShiftDown() || e.isControlDown() || e.isAltDown()));
				jpMaze.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				int y, x;
				if (maze == null)
					return;
				y = e.getY() / MAZE_SQUARE_SIZE;
				x = e.getX() / MAZE_SQUARE_SIZE;
				if (y >= maze.length || x >= maze[0].length)
					return;
				showMousePosition(x, y);
			}
		};
		jpMaze.addMouseListener(ma);
		jpMaze.addMouseMotionListener(ma);

		//

		taProlog = new TextArea();
		jpFin.add(taProlog, BorderLayout.EAST);
		taProlog.setSize(150, 450);
		taProlog.setPreferredSize(taProlog.getSize());

		//

		jf.setSize(500, 500);
		jpFin.setSize(jf.getSize());
		jpFin.setPreferredSize(jf.getPreferredSize());
		jf.setVisible(true);
		System.out.println(jf.getSize());
	}

	//

	void showMousePosition(int x, int y) {
		jlMousePosition.setText("Mouse position: (x" + x + ", y:" + y + ") -> ROW:" + y + " / COL:" + x);
	}

	void setAt(int x, int y, boolean value) {
		maze[y][x] = value;
	}

	void paintMaze(Graphics g) {
		int w, h;
		if (maze == null)
			return;
		forEachMazeNode((m, x, y) -> {
			g.setColor(m[y][x] ? COL : Color.LIGHT_GRAY);
			g.fillRect(x * MAZE_SQUARE_SIZE, y * MAZE_SQUARE_SIZE, MAZE_SQUARE_SIZE, MAZE_SQUARE_SIZE);
		});
		h = maze.length;
		w = maze[0].length;
		g.setColor(Color.BLACK);
		paintGrid(g, 0, 0, w * MAZE_SQUARE_SIZE, h * MAZE_SQUARE_SIZE, MAZE_SQUARE_SIZE, false);
	}

	void forEachMazeNode(MazeConsumer mc) {
		boolean[] r;
		for (int y = 0; y < maze.length; y++) {
			r = maze[y];
			for (int x = 0; x < r.length; x++)
				mc.doOnPixel(maze, x, y);
		}
	}

	void resizeMatrix() {
		int h, w;
		h = Integer.parseInt(this.jtfNumRow.getText());
		w = Integer.parseInt(this.jtfNumCol.getText());
		this.maze = new boolean[h][w];
		this.jpMaze.setSize(w * MAZE_SQUARE_SIZE, h * MAZE_SQUARE_SIZE);
		this.jpMaze.setPreferredSize(this.jpMaze.getSize());
		jpFin.repaint();
	}

	void clearMatrix() {
		forEachMazeNode((m, x, y) -> m[y][x] = false);
		taProlog.setText("");
		jpFin.repaint();
	}

	void printMatrixToProlog() {
		final StringBuilder sb;
		sb = new StringBuilder(maze.length << 3);
		sb.append("num_row(").append(maze.length).append(").\n");
		sb.append("num_col(").append(maze[0].length).append(").\n\n");
		sb.append("startNode(pos( ROW_START, COL_START )).\n");
		sb.append("endNode(pos( ROW_END, COL_END )).\n\n");
		forEachMazeNode((m, x, y) -> {
			if (m[y][x])
				sb.append("occupied(pos(").append(y).append(',').append(x).append(")).\n");
		});
		this.taProlog.setText(sb.toString());
	}

	public static void paintGrid(Graphics g, int x, int y, int w, int h, int sizeSquare, boolean performChecks) {
		int r, c, w_1, h_1;
		if (performChecks) {
			if (g == null)
				return;
			if (w < 1 || h < 1 || sizeSquare < 0)
				throw new IllegalArgumentException("ERROR: on paintGrid, Invalid parameters:\n\twidth: " + w
						+ ", height: " + h + ", sizeSquare: " + sizeSquare);
			if (x < 0) {
				w += x;
				x = 0;
			}
			if (y < 0) {
				h += y;
				y = 0;
			}
		}
		h_1 = y + (h - 1);
		// c = (x != 0) ? (x - (x % sizeSquare)) : 0;
		c = 0;
		while((c += sizeSquare) < w) {
			w_1 = x + c;// recycle
			g.drawLine(w_1, y, w_1, h_1);
		}

		w_1 = x + (w - 1);
		// r = (y != 0) ? (y - (y % sizeSquare)) : 0;
		r = 0;
		while((r += sizeSquare) < h) {
			h_1 = y + r;// recycle
			g.drawLine(x, h_1, w_1, h_1);
		}
	}

	//

	//

	public static void main(String[] args) {
		PrologMazeGenerator pmg;
		pmg = new PrologMazeGenerator();
		pmg.buildGUI();
	}
}