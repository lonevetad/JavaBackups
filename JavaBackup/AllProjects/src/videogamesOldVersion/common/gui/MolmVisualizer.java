package common.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import common.PainterMolm;
import common.PainterMolmPaintingOncePerRun;
import common.abstractCommon.behaviouralObjectsAC.AbstractPainterSimple;
import common.mainTools.GraphicTools;
import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification.SS_Rectangular;

/**
 * Deprecated.<br>
 * Replaced by {@link MapGameView}.
 */
@Deprecated
public class MolmVisualizer extends JScrollPane {
	private static final long serialVersionUID = 96202807088022200L;

	public static final boolean PAINT_MOLM_ALL = false, PAINT_MOLM_VISIBLE_AREA_ONLY = true;

	/** Calls {@link #MolmVisualizer(boolean)} givin {@code true} by default. */
	public MolmVisualizer() {
		this(true);
	}

	/**
	 * If the {@link MatrixObjectLocationManager} (a.k.a. MOLM) will hold few huge objects heavy and
	 * slow to be painted, then pass {@code true} to increase the visualizer's performance.<br>
	 * If the MOLM will hold several different instances of {@link ObjectWithID}, expecially if they
	 * are little and light to be drawn, then {@code false} is recommended. <br>
	 * By default, it's {@code true}.
	 */
	public MolmVisualizer(boolean isNotRepaintingJetPaintedInstances) {
		this(isNotRepaintingJetPaintedInstances ? PainterMolmPaintingOncePerRun.newInstance()
				: PainterMolm.newInstance());
	}

	public MolmVisualizer(PainterMolm painter) {
		this(new JPanelMolmVis(), painter);
	}

	protected MolmVisualizer(JPanelMolmVis view, PainterMolm painter) {
		super(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		setPainter(painter);
		(jpMolmVisualizer = view).mv = this;
		setViewportView(view);

		areaMolmToBeDrawn = ShapeSpecification.newRectangle(true, 0, 0, 1, 1, 0);
		setSomethingToDoAfterPaint(null);
		// avoid infinite-recursion
		canPaintMolms = true;
		paintingVisibleAreaOnly = PAINT_MOLM_VISIBLE_AREA_ONLY;

		repaintsOnScroll = false;
		addRepainterOnScroll();
		setMolmRepainterUncommonFunction();
	}

	// FIELDS
	// avoid infinite-recursion
	protected boolean canPaintMolms, repaintsOnScroll, paintingVisibleAreaOnly;
	protected JPanelMolmVis jpMolmVisualizer;
	protected SS_Rectangular areaMolmToBeDrawn;
	protected AbstractMatrixObjectLocationManager[] allMolms;
	protected PainterMolm painter;
	protected AbstractPainterSimple somethingToDoAfterPaint, paintAllMolm, paintMolmVisibleAreaOnly;

	//

	//

	public int getSizeSquare() {
		return painter == null ? Integer.MIN_VALUE : painter.getWidth();
	}

	public boolean isNotPainting() {
		return canPaintMolms();
	}

	public boolean canPaintMolms() {
		return canPaintMolms;
	}

	public JPanelMolmVis getJpMolmVisualizer() {
		return jpMolmVisualizer;
	}

	public AbstractMatrixObjectLocationManager[] getAllMolms() {
		return allMolms;
	}

	public PainterMolm getPainter() {
		return painter;
	}

	public AbstractPainterSimple getSomethingToDoAfterPaint() {
		return somethingToDoAfterPaint;
	}

	public boolean isRepaintsOnScroll() {
		return repaintsOnScroll;
	}

	public boolean isPaintingVisibleAreaOnly() {
		return paintingVisibleAreaOnly;
	}

	//

	public MolmVisualizer setRepaintsOnScroll(boolean repaintsOnScroll) {
		this.repaintsOnScroll = repaintsOnScroll;
		return this;
	}

	public MolmVisualizer setPaintingVisibleAreaOnly(boolean paintingVisibleAreaOnly) {
		this.paintingVisibleAreaOnly = paintingVisibleAreaOnly;
		return this;
	}

	public MolmVisualizer setSizeSquare(int sizeSquare) {
		int ss;
		// this.sizeSquare ;
		if (painter != null) painter.setWidth(ss = Math.max(1, sizeSquare)).setHeight(ss);
		return this;
	}

	public MolmVisualizer setAllMolms(AbstractMatrixObjectLocationManager[] allMolms) {
		int maxw, maxh, w, h;
		if (allMolms != null && allMolms.length > 0) {
			this.allMolms = allMolms;
			maxw = maxh = 4;
			for (AbstractMatrixObjectLocationManager m : allMolms) {
				w = m.getWidthMicropixel();
				h = m.getHeightMicropixel();
				if (maxw < w) maxw = w;
				if (maxh < h) maxh = h;
			}
			jpMolmVisualizer.setSize(maxw * getSizeSquare(), maxh * getSizeSquare());
			jpMolmVisualizer.setPreferredSize(jpMolmVisualizer.getSize());
			// System.out.println("MOLM visualizer: setAllMolms .. jpGridViewer size: " +
			// jpMolmVisualizer.getSize());
		}
		return this;
	}

	public MolmVisualizer setPainter(PainterMolm painter) {
		if (painter == null) throw new IllegalArgumentException("PainterMolm cannot be null !");
		this.painter = painter;
		return this;
	}

	public MolmVisualizer setSomethingToDoAfterPaint(AbstractPainterSimple somethingToDoAfterPaint) {
		this.somethingToDoAfterPaint = AbstractPainterSimple.getOrDefault(somethingToDoAfterPaint);
		return this;
	}

	//

	protected void paintMolms_CommonCode(Graphics g, AbstractPainterSimple differentCode) {
		int xx, yy, ss, ww, hh, xStartGrid, yStartGrid, ssHuge;
		AbstractMatrixObjectLocationManager[] am;
		Color c;

		if (
		/**
		 * avoid infinite-recursion and multiple threads painting molms asynchronously and in
		 * concurrence
		 */
		canPaintMolms /**/
				&& g != null && painter != null && ((am = allMolms) != null) && am.length > 0) {
			canPaintMolms = false;
			xx = Math.abs(jpMolmVisualizer.getX());
			yy = Math.abs(jpMolmVisualizer.getY());
			ss = getSizeSquare();
			ww = jpMolmVisualizer.getWidth();
			hh = jpMolmVisualizer.getHeight();

			c = g.getColor();
			g.setColor(Color.LIGHT_GRAY);
			painter.setGraphics(g);
			painter.prepareForPaintingRun();

			differentCode.paintOn(g);

			g.setColor(Color.GRAY);
			GraphicTools.paintGrid(g, xx - (xx % ss), yy - (yy % ss), ww, hh, ss);
			// every each 4 line, make a wider one
			ssHuge = ss << 2;
			xStartGrid = xx - (xx % ssHuge);
			yStartGrid = yy - (yy % ssHuge);
			GraphicTools.paintGrid(g, xStartGrid - 1, yStartGrid - 1, ww, hh, ssHuge);
			GraphicTools.paintGrid(g, xStartGrid + 1, yStartGrid + 1, ww, hh, ssHuge);

			g.setColor(c);

			somethingToDoAfterPaint.paintOn(g);
			// re-enablePainting
			canPaintMolms = true;
		}
	}

	public void paintMolms(Graphics g) {
		if (paintingVisibleAreaOnly)
			paintMolms_OnlyVisibleArea(g);
		else
			paintMolms_AllMolm(g);
	}

	public void paintMolms_AllMolm(Graphics g) {
		paintMolms_CommonCode(g, paintAllMolm);
	}

	public void paintMolms_OnlyVisibleArea(Graphics g) {
		paintMolms_CommonCode(g, paintMolmVisibleAreaOnly);
	}

	void addRepainterOnScroll() {
		AdjustmentListener al;
		al = e -> {
			if (repaintsOnScroll) jpMolmVisualizer.repaint();
		};
		this.getHorizontalScrollBar().addAdjustmentListener(al);
		this.getVerticalScrollBar().addAdjustmentListener(al);
	}

	protected void setMolmRepainterUncommonFunction() {
		paintAllMolm = (new PaintAllMolms(this));
		paintMolmVisibleAreaOnly = (new PaintVisibleAreaOnly(this));
	}

	protected void paintMolmAll(Graphics g) {
		for (AbstractMatrixObjectLocationManager m : allMolms)
			m.forEach(painter);
	}

	protected void paintMolmVisibleAreaOnly(Graphics g) {
		int xx, yy, ss, ww, hh;
		xx = Math.abs(jpMolmVisualizer.getX());
		yy = Math.abs(jpMolmVisualizer.getY());
		ss = getSizeSquare();
		ww = jpMolmVisualizer.getWidth();
		hh = jpMolmVisualizer.getHeight();
		this.areaMolmToBeDrawn.setLeftBottomCorner(xx / ss, yy / ss);
		// integer division
		// this.rectangleToBeDrawn.setWidth((ww + (ss - 1)) / ss);
		// this.rectangleToBeDrawn.setHeight((hh + (ss - 1)) / ss);
		this.areaMolmToBeDrawn.setWidth(ww / ss);
		this.areaMolmToBeDrawn.setHeight(hh / ss);
		for (AbstractMatrixObjectLocationManager m : allMolms)
			m.runOnShape(areaMolmToBeDrawn, painter);
	}

	@Override
	public void addMouseListener(MouseListener l) {
		jpMolmVisualizer.addMouseListener(l);
	}

	@Override
	public void addMouseMotionListener(MouseMotionListener l) {
		jpMolmVisualizer.addMouseMotionListener(l);
	}

	/*
	 * public void addMouseWheelListener(MouseWheelListener l) {
	 * jpMolmVisualizer.addMouseWheelListener(l); }
	 */

	//

	// TODO CLASSES

	protected static class JPanelMolmVis extends JPanel {
		private static final long serialVersionUID = 160970963343376L;

		public JPanelMolmVis() {
			this(null);
		}

		public JPanelMolmVis(MolmVisualizer mv) {
			this(mv, null);
		}

		public JPanelMolmVis(MolmVisualizer mv, LayoutManager layout) {
			super(layout);
			this.mv = mv;
		}

		MolmVisualizer mv;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			paintMolms(g);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			// super.paintComponents(g);
			paintMolms(g);
		}

		public void paintMolms(Graphics g) {
			mv.paintMolms(g);
		}

	}

	public static class PaintAllMolms implements AbstractPainterSimple {
		private static final long serialVersionUID = 1121651650L;
		MolmVisualizer mv;

		PaintAllMolms(MolmVisualizer mv) {
			this.mv = mv;
		}

		@Override
		public void paintOn(Graphics g) {
			mv.paintMolmAll(g);
		}
	}

	public static class PaintVisibleAreaOnly implements AbstractPainterSimple {
		private static final long serialVersionUID = 1121651650L;
		MolmVisualizer mv;

		PaintVisibleAreaOnly(MolmVisualizer mv) {
			this.mv = mv;
		}

		@Override
		public void paintOn(Graphics g) {
			mv.paintMolmVisibleAreaOnly(g);
		}
	}
}