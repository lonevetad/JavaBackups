package tests.tGeom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.TextField;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import geometry.implementations.shapeRunners.ShapeRunnerCircleBorder;
import geometry.implementations.shapeRunners.ShapeRunnerCircleFilled;
import geometry.implementations.shapes.ShapeCircle;
import geometry.pointTools.PointConsumer;
import tools.GraphicTools;

@Deprecated
public class TestRunnerCircle extends TestGeneric {
	static final int PIXEL_SQUARE_POINT = 5, MAX_SQUARE_PIXEL = 700; //

	public TestRunnerCircle() {
	}

	protected class CircleRunnerModel extends ShapeModel {
		protected CircleRunnerModel() {
			super(new ShapeCircle(false));
			ctpoi = new ColorToPaintOnImage();
		}

		BufferedImage bi;
		ShapeCircle c;
		MyObserver<ShapeCircle> cicleObserver;
		MyObserver<ColorToPaintOnImage> imageObserver;
		final ColorToPaintOnImage ctpoi;

		@Override
		void init() {
			c = (ShapeCircle) super.s1;
			ctpoi.bi = bi = new BufferedImage(MAX_SQUARE_PIXEL, MAX_SQUARE_PIXEL, BufferedImage.TYPE_INT_ARGB);
			setDiameter(1);
		}

		public void setDiameter(int diameter) {
			int rr;
			if (diameter < 1)
				return;
			rr = 1 + (diameter >> 1);
			ctpoi.c = Color.LIGHT_GRAY;
			if (imageObserver != null)
				imageObserver.update(ctpoi);
			c.setDiameter(diameter);
			c.setCenter(rr, rr);
			ctpoi.c = Color.BLUE;
			if (imageObserver != null)
				imageObserver.update(ctpoi);
			if (cicleObserver != null)
				cicleObserver.update(c);
		}

		void setCicleObserver(MyObserver<ShapeCircle> cicleObserver) {
			this.cicleObserver = cicleObserver;
		}

		void setImageObserver(MyObserver<ColorToPaintOnImage> imageObserver) {
			this.imageObserver = imageObserver;
		}
	}

	protected class ColorToPaintOnImage {
		Color c;
		BufferedImage bi;
	}

	protected class SquarePainter implements PointConsumer, MyObserver<ColorToPaintOnImage> {
		private static final long serialVersionUID = 1L;
		CircleRunnerModel m;
		Graphics g;
		ColorToPaintOnImage ctpoi;

		@Override
		public void accept(Point p) {
			BufferedImage bi;
			int x, col, px, py;
			bi = this.ctpoi.bi;
			if (g == null || bi == null)
				return;
			col = ctpoi.c.getRGB();
			py = (int) p.getY() * PIXEL_SQUARE_POINT;
			x = (int) p.getX() * PIXEL_SQUARE_POINT;
			for (int r = 0; r < PIXEL_SQUARE_POINT; r++) {
				px = x;
				for (int c = 0; c < PIXEL_SQUARE_POINT; c++)
					bi.setRGB(px++, py, col);
				py++;
			}
		}

		@Override
		public void update(ColorToPaintOnImage ctpoi) {
			BufferedImage bi;
			if (ctpoi == null)
				return;
			this.ctpoi = ctpoi;
			bi = this.ctpoi.bi;
			if (bi == null)
				return;
			this.g = bi.getGraphics();
			(m.c.isFilled() ? ShapeRunnerCircleFilled.getInstance() : ShapeRunnerCircleBorder.getInstance())
					.runShape(m.c, this);
		}
	}

	protected class CircleRunnerView extends ShapeView {
		SquarePainter squarePainter;

		@Override
		void init() {
			GridBagConstraints c;
			CircleRunnerModel m;
			JPanel jp;
			TextField tf;
			JButton jb;
			JSpinner js;
			JCheckBox jcb;
			m = (CircleRunnerModel) model;
			squarePainter = new SquarePainter();
			squarePainter.m = m;
			m.setImageObserver(squarePainter);
			c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.weightx = 0;
			c.weighty = 2;
			c.gridwidth = 2;
			c.gridheight = 1;
			tf = new TextField("16");
			pContainer.add(tf, c);
			jcb = new JCheckBox("V = is filled");
			c.gridx = 2;
			pContainer.add(jcb, c);
			c.gridx = 4;
			jb = new JButton("Set the radius");
			pContainer.add(jb, c);
			c.gridx = 6;
			js = new JSpinner(new SpinnerNumberModel(16, 1, 1000, 1));
//			jbDiameter = new JButton("Set the radius");
			pContainer.add(js, c);

			jp = new JPanel() {
				private static final long serialVersionUID = -7775420L;

				@Override
				protected void paintComponent(Graphics g) {
					if (squarePainter.ctpoi != null)
						g.drawImage(squarePainter.ctpoi.bi, 0, 0, null);
					g.setColor(Color.GREEN);
					GraphicTools.paintGrid(g, JPANEL_DIMENSION, JPANEL_DIMENSION, PIXEL_SQUARE_POINT);
//					squarePainter.g = g;
				}

				@Override
				public Dimension getSize() {
					return new Dimension(getWidth(), getHeight());
				}

				@Override
				public int getWidth() {
					return JPANEL_DIMENSION;
				}

				@Override
				public int getHeight() {
					return JPANEL_DIMENSION;
				}
				/*
				 * @Override public int getX() { return 0; }
				 *
				 * @Override public int getY() { return 0; }
				 */
			};
			jp.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = c.weighty = c.gridwidth = c.gridheight = 8;
			pContainer.add(jp, c);
			m.setCicleObserver(circ -> {
				jp.repaint();
				fin.repaint();
			});
			jb.addActionListener(l -> {
				m.setDiameter(Integer.parseInt(tf.getText()));
			});
			js.addChangeListener(e -> {
				m.setDiameter((Integer) js.getValue());
			});
			jcb.addChangeListener(l -> {
				m.c.setFilled(jcb.isSelected());
			});
			jp.setLocation(0, 0);
			jp.setSize(JPANEL_DIMENSION, JPANEL_DIMENSION);
			jp.setPreferredSize(jp.getSize());
			jp.requestFocus();
			fin.setSize(jp.getSize());
			fin.setVisible(true);
		}

	}

	@Override
	ShapeModel newModel() {
		return new CircleRunnerModel();
	}

	@Override
	ShapeView newView() {
		return new CircleRunnerView();
	}

	@Override
	void init() {
	}

	public static void main(String[] args) {
		TestRunnerCircle t;
		t = new TestRunnerCircle();
		t.startTest();
	}
}