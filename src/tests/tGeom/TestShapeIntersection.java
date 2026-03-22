package tests.tGeom;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JComponent;

import geometry.AbstractShape2D;

public abstract class TestShapeIntersection extends TestGeneric {

	protected abstract class ShapeIntersectionModel extends ShapeModel {
		protected ShapeIntersectionModel(AbstractShape2D s1, AbstractShape2D s2) {
			super(s1);
			this.s2 = s2;
//			init();
		}

		AbstractShape2D s2;
		Polygon p2;
		List<Point2D> intersections;
		Point2D pointIntersection;
		MyObserver<Point2D> pointIntersectionObserver;
		MyObserver<List<Point2D>> listPointIntersectionObserver;

		public MyObserver<List<Point2D>> getListPointIntersectionObserver() {
			return listPointIntersectionObserver;
		}

		public Point2D getPointIntersection() {
			return pointIntersection;
		}

		public MyObserver<Point2D> getPointIntersectionObserver() {
			return pointIntersectionObserver;
		}

		public List<Point2D> getIntersections() {
			return intersections;
		}

		//

		public void setPointIntersectionObserver(MyObserver<Point2D> pointIntersectionObserver) {
			this.pointIntersectionObserver = pointIntersectionObserver;
		}

		public void setListPointIntersectionObserver(MyObserver<List<Point2D>> listPointIntersectionObserver) {
			this.listPointIntersectionObserver = listPointIntersectionObserver;
		}

		public void setIntersections(List<Point2D> intersections) {
			this.intersections = intersections;
			if (this.listPointIntersectionObserver != null)
				this.listPointIntersectionObserver.update(intersections);
		}

		public void setPointIntersection(Point2D pointIntersection) {
			this.pointIntersection = pointIntersection;
			if (this.pointIntersectionObserver != null)
				this.pointIntersectionObserver.update(pointIntersection);
		}

	}

	protected abstract class ShapeIntersectionView extends ShapeView {

		// nothing more right now
		JComponent cIntersectionShower;

		public JComponent getcIntersectionShower() {
			return cIntersectionShower;
		}

		public void setCIntersectionShower(JComponent c) {
			this.cIntersectionShower = c;
			if (c != null)
				((ShapeIntersectionModel) model).setListPointIntersectionObserver(l -> c.repaint());
		}
	}
}