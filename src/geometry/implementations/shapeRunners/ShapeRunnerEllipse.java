package geometry.implementations.shapeRunners;

public class ShapeRunnerEllipse {

	public ShapeRunnerEllipse() {
		// TODO Auto-generated constructor stub
	}

	/** (x,y) = center of ellipse */
//			protected static void fillEllipse_Unsafe(MatrixObjectLocationManager molm, int x, int y, int a, int b,
//					DoSomethingWithNode doswn) {
//				int yy, xx, tx, ty, len;
//				double bb;
//				yy = -1;
//				bb = b;
//				while (++yy <= b) {
//					xx = yy != 0 ? (int) Math.round(Math.cos(Math.asin((yy) / bb)) * a) : a;
//					if (xx > 0) {
//						runHorizontalSpan_Unsafe(tx = x - xx, y + yy, len = (xx << 1) + 1, molm, doswn);
//						if (yy != 0)
//							runHorizontalSpan_Unsafe(tx, y - yy, len, molm, doswn);
//					}
//				}

//	public boolean runOnEllipseNoRotation(MatrixObjectLocationManager molm, int x, int y, int width, int height,
//			DoSomethingWithNode dswi) {
//		int a, b;
//		if (dswi != null) {
//			a = width >> 1;
//			b = height >> 1;
//			if ((x - a) >= 0 && (x + (width - a)) < molm.getWidth() && (y - b) >= 0
//					&& (y + (height - b)) < molm.getHeight())
//				fillEllipse_Unsafe(molm, x, y, a, b, dswi);
//			else
//				fillEllipse_Safe(molm, x, y, a, b, dswi);
//			return true;
//		}
//		return false;
//	}
}
