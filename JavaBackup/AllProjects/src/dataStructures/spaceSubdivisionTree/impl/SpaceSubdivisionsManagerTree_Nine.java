package dataStructures.spaceSubdivisionTree.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dataStructures.MapTreeAVL;
import dataStructures.MapTreeAVL.Optimizations;
import dataStructures.spaceSubdivisionTree.SpaceSubdivisions.SingleSpaceSubdivision;
import dataStructures.spaceSubdivisionTree.SpaceSubdivisionsManagerTree;
import dataStructures.spaceSubdivisionTree.SpaceSubsectionNode;
import dataStructures.spaceSubdivisionTree.impl.SquareNineSpaceSubdivisions.NineRectangularSingleSpaceSubdivision;
import dataStructures.spaceSubdivisionTree.impl.utils.ObjectsSubdivision;
import dataStructures.spaceSubdivisionTree.impl.utils.SingleSpaceSectionsSubdivision;
import geometry.ObjectLocated;
import tools.Comparators;

/**
 * Version of a QuadTree where space is divided in nine squared pieces instead
 * of four, taking inspiration from cardinal points:
 * {@link SquareNineSpaceSubdivisions.NineRectangularSingleSpaceSubdivision }
 * <p>
 *
 * @deprecated because there is a list of features required but still not
 *             developed and some bugfixes:<br>
 *             <ul>
 *             <li>Test the add method</li>
 *             <li>fix the build method</li>
 *             <li>add the remove and fetch method (about removing, dealloc
 *             subdivisions if they are empty)</li>
 *             <li>perform a refactoring to handle shaped objects, not only
 *             point-like objects</li>
 *             <li>find a way to handle a object's moving event (i.e.: move an
 *             object from one subdivision to another)</li>
 *             </ul>
 */
// <li></li>
//TODO
@Deprecated
public class SpaceSubdivisionsManagerTree_Nine extends SpaceSubdivisionsManagerTree {

	//

	//

	//

	public SpaceSubdivisionsManagerTree_Nine(double minSquareSideLength, Rectangle baseShape) {
		super(minSquareSideLength, baseShape, SquareNineSpaceSubdivisions.getInstance(),
				new SubsectionDivisionRulerImpl());
	}

	//

	//

	public int getMaxDepth() {
		return (root == null) ? -1 : root.getMaxDepth();
	}

	@Override
	protected void build() {
		root = build(0, null, null, this.baseShape, 0.0, 0.0, this.baseShape.getWidth(), this.baseShape.getHeight());
	}

	/**
	 * @param baseShape the shape of the whole map
	 * @param xTopLeft  the x component of the top-left corner of the section
	 *                  processed in the current call.
	 * @param yTopLeft  the y component of the top-left corner of the section
	 *                  processed in the current call.
	 * @param width     width of the section that is currently processing
	 * @param height    height of the section that is currently processing
	 */
	protected SpaceSubsectionNode_Nine build(int level, SpaceSubsectionNode_Nine father,
			SquareNineSpaceSubdivisions.NineRectangularSingleSpaceSubdivision sectionFromFather, Rectangle baseShape,
			double xTopLeft, double yTopLeft, double width, double height) {
		int newLevel;
		double maxSize, halfSquareMap;
		final SpaceSubsectionNode_Nine res;
		maxSize = Math.max(width, height);
		halfSquareMap = maxSize / 2.0;
		res = new SpaceSubsectionNode_Nine(level, father, sectionFromFather, xTopLeft + halfSquareMap,
				yTopLeft + halfSquareMap, maxSize);
		if (maxSize < minSquareSideLength)
			return res;

		// if it's completly outside, then return null
		if (((xTopLeft + maxSize) < baseShape.getX())//
				&& ((yTopLeft + maxSize) < baseShape.getY())//
				&& (xTopLeft >= baseShape.getWidth())//
				&& (yTopLeft >= baseShape.getHeight())//
		)
			return null;

		if (width != height || //
		// if its contained, then no more suddivision is needed
				(!baseShape.contains(xTopLeft, yTopLeft, maxSize, maxSize))) {
			// it's a rectangle, not a square: decompose
			int i, len;
			double thirdSizeSection;
			Point2D.Double topLeftCorner;
//			SquareNineSpaceSubdivisions.NineRectangularSingleSpaceSubdivision ss;

			topLeftCorner = new Point2D.Double(xTopLeft, yTopLeft);
			len = this.spaceSubdivisions.countSubdivisions();
			thirdSizeSection = maxSize / 3.0;
			i = -1;
			newLevel = level + 1;
			while (++i < len) {
				spaceSubdivisions.getSubdivisions().forEach(ss -> {
					Point2D pointCenterNewSubsection;
					SpaceSubsectionNode_Nine child;
					topLeftCorner.setLocation(xTopLeft, yTopLeft);
					pointCenterNewSubsection = ss.centerOfDeeperSubsection(res);
					child = build(newLevel, res, (NineRectangularSingleSpaceSubdivision) ss, baseShape,
							pointCenterNewSubsection.getX(), pointCenterNewSubsection.getY(), thirdSizeSection,
							thirdSizeSection);
					if (child != null)
						res.setChild(ss, child);
				});

			}
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb;
		List<String> ret;

		sb = new StringBuilder();
		sb.append("SpaceSubdivisionsManagerTree_Nine bounded to minimum square side length: ");
		sb.append(minSquareSideLength);
		sb.append("\nwith base shape: ");

		sb.append(baseShape);
		if (root == null)
			sb.append("null");
		else {
			int maxDepth;
			maxDepth = this.getMaxDepth();
			ret = ((SpaceSubsectionNode_Nine) root).toString(maxDepth);
			if (ret != null)
				for (String s : ret)
					sb.append(s);
		}
		return sb.toString();
	}

	public BufferedImage countInImage() {
		BufferedImage bi;
		if (root == null)
			return null;
		bi = new BufferedImage((int) ((SpaceSubsectionNode_Nine) root).getWidth(),
				(int) ((SpaceSubsectionNode_Nine) root).getHeight(), BufferedImage.TYPE_INT_ARGB);
		((SpaceSubsectionNode_Nine) root).countInImage(bi);
		return bi;
	}

	//

	//

	// TODO CLASS

	public class SpaceSubsectionNode_Nine extends RectangularSpaceSubsectionNode {
//		final double centralSquareRadius;
		SpaceSubsectionNode_Nine[] children; // if any, move all of objects in this portion to each fields
		Map<Point2D, ObjectLocated> objectsStored;

		/**
		 * @param level the depth of this section from the root ones (having 0 as level)
		 * @father the subsection which this instance belongs to
		 * @param sectionFromFather the section which this instance was originated from
		 * @param x                 the x-component of this section's location
		 * @param y                 the y-component of this section's location
		 * @param lengthSide        the length of the square this subsection is
		 *                          representing
		 */
		protected SpaceSubsectionNode_Nine(int level, SpaceSubsectionNode_Nine father,
				SingleSpaceSubdivision sectionFromFather, double x, double y, double lengthSide) {
			super(level, father, sectionFromFather, x, y, lengthSide, lengthSide);
			/*
			 * Sections are 9, i.e. 3x3, so the radius of each inner subsections is 1/(3*2)
			 * = 1/6 of this original dimension (the "lengthSide" parameter)
			 */
//			this.centralSquareRadius = lengthSide / 6.0;
		}

		protected Map<Point2D, ObjectLocated> newMap() {
			return MapTreeAVL.newMap(Optimizations.MinMaxIndexIteration,
					Comparators.POINT_2D_COMPARATOR_HIGHEST_LEFTMOST_FIRST);
		}

		//

		//
		@Override
		public int getMaxDepth() {
			int i, len, levelComputed, childLevel;
			SpaceSubsectionNode_Nine child;
			levelComputed = level;
			if (children != null) {
				len = children.length;
				i = -1;
				while (++i < len) {
					child = children[i];
					if (child != null && (childLevel = child.getMaxDepth()) > levelComputed)
						levelComputed = childLevel;
				}
			}
			return levelComputed;
		}

		@Override
		public void setChild(SingleSpaceSubdivision where, SpaceSubsectionNode child) {
			if (this.children == null)
				this.children = new SpaceSubsectionNode_Nine[spaceSubdivisions.countSubdivisions()];
			this.children[where.getOrdinal()] = (SpaceSubsectionNode_Nine) child;
		}

		/** This method should be protected, not public */
		public Map<Point2D, ObjectLocated> getObjectsStored() {
			return objectsStored;
		}

		//

		@Override
		public SingleSpaceSubdivision getSpaceSectionFromFather() {
			return spaceSectionFromFather;
		}

		public SpaceSubsectionNode_Nine[] getChildren() {
			return children;
		}

		/** Very heavy operation */
		@Override
		public Map<Point2D, ObjectLocated> getObjectsInSection() {
			Map<Point2D, ObjectLocated> m;
			m = newMap();
			getObjectsInSection(m);
			return m.isEmpty() ? null : m;
		}

		protected void getObjectsInSection(final Map<Point2D, ObjectLocated> m) {
			if (objectsStored != null) {
				objectsStored.forEach((p, o) -> m.put(p, o));
				return;
			}
			if (children == null) // so, no one is there
				return;
			for (SpaceSubsectionNode_Nine c : children)
				if (c != null)
					c.getObjectsInSection(m);
		}

		@Override
		public int getObjectsCount() {
			int partialSum;
			if (this.objectsStored != null)
				return this.objectsStored.size();
			if (children == null)
				return 0;
			partialSum = 0;
			for (SpaceSubsectionNode_Nine c : children)
				if (c != null)
					partialSum += c.getObjectsCount();
			return partialSum;
		}

		public void forEach(BiConsumer<Point2D, ObjectLocated> action) {
			if (this.objectsStored != null)
				this.objectsStored.forEach(action);
			if (children == null)
				return;
			for (SpaceSubsectionNode_Nine c : children)
				if (c != null)
					c.forEach(action);
		}

		public void add(ObjectLocated o) {
			Point2D position;
			ObjectsSubdivision subdivision;
			if (o == null)
				return;
			// e qui viene il divertimento
			if (children == null && objectsStored == null) {
				objectsStored = newMap();
			}
			// TODO test it
			position = o.getLocation();
			if (position == null)
				return;
			if (objectsStored != null) {
				objectsStored.put(position, o);
				subdivision = divisionRuler.isSubdivisionNeeded(this, SpaceSubdivisionsManagerTree_Nine.this);
				// need subdivision?
				if (subdivision != null && subdivision.isEnoughSpreaded()) {
					int nextLevel;
					double thirdWidth, thirdHeight;
					SingleSpaceSectionsSubdivision[] suddivision;
					SpaceSubsectionNode_Nine newSubsection;

					SquareNineSpaceSubdivisions sSubds_Nine;
					sSubds_Nine = (SquareNineSpaceSubdivisions) spaceSubdivisions;

					SingleSpaceSubdivision sectionOfSubsection;
					objectsStored = null;
					suddivision = subdivision.getObsjSpreaded();
					children = new SpaceSubsectionNode_Nine[suddivision.length];
					nextLevel = level + 1;
					thirdWidth = this.getWidth() / sSubds_Nine.getColumnsSubdivisionsAmount();
					thirdHeight = this.getHeight() / sSubds_Nine.getRowsSubdivisionsAmount();
					assert thirdWidth == thirdHeight : "Not a square? thirdWidth: " + thirdWidth + ", thirdHeight: "
							+ thirdHeight
					// create nodes
					for (SingleSpaceSectionsSubdivision singleSquare : suddivision) {
						sectionOfSubsection = singleSquare.getSpaceSubdivision();
						newSubsection = new SpaceSubsectionNode_Nine(nextLevel, this, sectionOfSubsection, //
								(getXCenter() + sectionOfSubsection.getXDeltaFromCenter() * thirdWidth), //
								(getYCenter() + sectionOfSubsection.getYDeltaFromCenter() * thirdHeight), //
								thirdWidth); //

						children[sectionOfSubsection.getOrdinal()] = newSubsection;
					}
				}
			} else {
				// add to subsection
				SingleSpaceSubdivision dir;
//				dir = sectionCointaining_Version2(position, this);
				dir = spaceSubdivisions.sectionCointaining(position, this);
				// recursion
				children[dir.getOrdinal()].add(o);
			}
		}

		public ObjectLocated fetch(Point2D.Double position) {
			if (position == null)
				return null;
			if ((getXCenter() + getWidth() < position.getX()) || //
					(getYCenter() + getHeight() < position.getY()) || //
					(getXCenter() - getWidth() > position.getX()) || //
					(getYCenter() - getHeight() > position.getY()))
				return null;

			if (this.children == null)
				return this.objectsStored == null ? null : this.objectsStored.get(position);
			else {
//				SquareNineSpaceSubdivisions.NineRectangularSingleSpaceSubdivision
				SingleSpaceSubdivision ss;
				SpaceSubsectionNode_Nine child;
				ss = spaceSubdivisions.sectionCointaining(position, this);
				child = this.children[ss.getOrdinal()];
				if (child == null)
					return null;
				return child.fetch(position);
			}
		}

		@SuppressWarnings("unchecked")
		public List<String> toString(int maxDepthFromAbove) {
			int maxCharsCountEachLine = 6, numEmptyChars;
			String countObjects, emptyLine, lastLine;
			StringBuilder sb;
			List<String> ret;
			ret = new LinkedList<>();
			numEmptyChars = maxCharsCountEachLine * (maxDepthFromAbove - 1);

			sb = new StringBuilder(1024);
			for (int x = maxCharsCountEachLine - 1; x > 0; x++)
				sb.append('_');
			lastLine = sb.append('|').toString();
			sb.setLength(0);

			if (children == null) {
				// i'm the final node
				int t, linesAboveAndUp;

				countObjects = Integer.toString(objectsStored == null ? 0 : objectsStored.size());
				for (int x = maxCharsCountEachLine - 1; x > 0; x++)
					sb.append(' ');
				emptyLine = sb.append('|').toString();
				sb.setLength(0);

				linesAboveAndUp = (numEmptyChars >> 1) - 1;
				// print objects count
				for (int x = linesAboveAndUp; x > 0; x++)
					ret.add(emptyLine);
				if ((countObjects.length() + 1) > maxCharsCountEachLine) {
					for (int x = maxCharsCountEachLine - 2; x > 0; x++)
						sb.append('9');
					ret.add(sb.append("+|").toString());
					sb.setLength(0);
				} else {
					t = numEmptyChars - countObjects.length();
					for (int i = (t >> 1); i > 0; i--)
						sb.append(' ');
					sb.append(countObjects);
					for (int i = ((t >> 1) + (t & 0x1)); i > 0; i--)
						sb.append(' ');
					sb.append('|');
					ret.add(sb.toString());
					sb.setLength(0);
				}
				for (int x = linesAboveAndUp; x > 0; x++)
					ret.add(emptyLine);

				ret.add(lastLine);
				sb = null;// .setLength(0);

			} else {
				// TODO
				int i, len;
				Iterator<String>[] retChildsInLines;
				SpaceSubsectionNode_Nine child;
				SquareNineSpaceSubdivisions.NineRectangularSingleSpaceSubdivision ss;
				Iterator<String> iterReference;

				retChildsInLines = new Iterator[3];
				maxDepthFromAbove--;
				sb.setLength(0);
				for (int x = maxCharsCountEachLine - 1; x > 0; x++)
					sb.append(' ');
				emptyLine = sb.append('|').toString();
				sb.setLength(0);

				for (SquareNineSpaceSubdivisions.NineRectangularSingleSpaceSubdivision[] lineSS : SquareNineSpaceSubdivisions.rowsInLIne) {
					len = lineSS.length;
					i = -1;
					while (++i < len) {
						ss = lineSS[i];
						child = this.children[ss.getOrdinal()];
						retChildsInLines[i] = child == null ? null : child.toString(maxDepthFromAbove).iterator();
					}
					sb.setLength(0);
					len = 0;
					//
					iterReference = null;

					len = retChildsInLines.length;
					i = -1;
					while (iterReference == null && ++i < len)
						if (retChildsInLines[i] != null)
							iterReference = retChildsInLines[i];

					if (iterReference == null) {
						for (int x = numEmptyChars - 1; x > 0; x--) {
							i = -1;
							while (++i < len)
								sb.append(emptyLine);
							ret.add(sb.toString());
							sb.setLength(0);
						}
						i = -1;
						while (++i < len)
							ret.add(lastLine);
					} else {
						for (int x = numEmptyChars - 1; x > 0; x--) {
							for (Iterator<String> iterChild : retChildsInLines)
								sb.append(iterChild == null ? emptyLine : iterChild.next());
							ret.add(sb.toString());
							sb.setLength(0);
						}
						i = -1;
						while (++i < len)
							ret.add(lastLine);
					}
				}
			}
			return ret;
		}

		public void countInImage(BufferedImage bi) {
			double hSSL;
			double x, y;
			Graphics g;
			g = bi.getGraphics();
			g.setColor(Color.BLUE);
			x = getXCenter();
			y = getYCenter();
			hSSL = getWidth() / 2.0;// width == height
			// vertical
			g.drawLine((int) (x + hSSL), (int) (y - hSSL), (int) (x + hSSL), (int) (y + hSSL));
			g.drawLine((int) (x - hSSL), (int) (y - hSSL), (int) (x - hSSL), (int) (y + hSSL));
			// horizontal
			g.drawLine((int) (x - hSSL), (int) (y - hSSL), (int) (x + hSSL), (int) (y - hSSL));
			g.drawLine((int) (x - hSSL), (int) (y + hSSL), (int) (x + hSSL), (int) (y + hSSL));
			if (children == null) {
				String s;
				s = Integer.toString(objectsStored == null ? 0 : objectsStored.size());
				g.drawString(s, (int) (x - (5 * (s.length() >> 1))), (int) y);
			} else
				for (SpaceSubsectionNode_Nine child : children)
					if (child != null)
						child.countInImage(bi);
		}
	}// end class

	public static void main(String[] args) {
		SpaceSubdivisionsManagerTree_Nine t;
		BufferedImage bi;
		Icon ic;
		JFrame fin;
		JPanel jp;
		JLabel jl;

		System.out.println("Start");
		t = new SpaceSubdivisionsManagerTree_Nine(100, new Rectangle(1200, 900));
		System.out.println("built");
		System.out.println(t.getMaxDepth());
//		System.out.println("toString");
//		System.out.println(t.toString());

		bi = t.countInImage();
		if (bi != null) {
			ic = new ImageIcon(bi);
			jl = new JLabel(ic);
			jl.setLocation(0, 0);
			jp = new JPanel();
			jp.add(jl);
			jp.setLocation(0, 0);
			fin = new JFrame("LEL");
			fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			fin.add(jp);
			fin.pack();
			fin.setVisible(true);
		}

		System.out.println("end");
	}
}