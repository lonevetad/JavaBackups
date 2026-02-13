package videogamesOldVersion.common.mainTools.mOLM;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import tools.LoggerMessages;
import tools.MathUtilities;
import videogamesOldVersion.common.mainTools.mOLM.MatrixObjectLocationManager.COLORS_VISIT;
import videogamesOldVersion.common.mainTools.mOLM.MatrixObjectLocationManager.CollectorObstacles;
import videogamesOldVersion.common.mainTools.mOLM.MatrixObjectLocationManager.CoordinatesDeltaForAdjacentNodes;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractPathFinder;

/**
 * Implementation of {@link AbstractPathFinder}, using the Singleton pattern.
 */
public class PathFinderOLD implements AbstractPathFinder {
	private static final long serialVersionUID = -965107860L;
	private static PathFinderOLD instancePathFinder;

	private PathFinderOLD() {
	}

	public static PathFinderOLD getInstance() {
		if (instancePathFinder == null)
			instancePathFinder = new PathFinderOLD();
		return instancePathFinder;
	}

	//

	/**
	 * Using the simplified version of Dijkstra.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public List<Point> findPath(MatrixObjectLocationManager molm, NodeMatrix nodeStart, NodeMatrix nodeDest,
			ShapeSpecification ss, boolean strechingPath) {
		int i, xx, yy;
		long numberTimesPathFindRuns;
		double sum;
		LinkedList<Point> l;
		NodeMatrix u, v;
		CoordinatesDeltaForAdjacentNodes arc;
		LinkedList<NodeMatrix> pq;
		CollectorObstacles col;
		LoggerMessages log;

		log = (molm != null) ? LoggerMessages.loggerOrDefault(molm.getLog()) : LoggerMessages.LOGGER_DEFAULT;

		if (molm == null || nodeStart == null || nodeDest == null || ss == null) {
			log.log("ERROR: on PathFinder.findPath, some null argument(s):\n\tmolm null? " + (molm == null)
					+ ", ShapeSpecification? " + (ss == null) + ", Node(s) is/are null: start? " + (nodeStart == null)
					+ ", dest? " + (nodeDest == null));
			return null;
		}

		/* not modify original */
		ss = ss.clone();

		col = new CollectorObstacles(nodeStart.item, ItemCollector.COLLECT_FIRST_FOUND_TO_CHECK_EMPITY);
		col.setCollectIfBothNull(false).setMustCollectNulls(false);

		/*
		 * if there is someone in the area having center in the start point, before, and
		 * in the destination point, after, then no path exists.
		 */
		if (molm.isThereSomeoneOnShape(col, ss.setCenter(nodeDest.getX(), nodeDest.getY()))
				|| molm.isThereSomeoneOnShape(col, ss.setCenter(nodeStart.getX(), nodeStart.getY()))) {
			String error;
			error = "ERROR: on PathFinder.findPath, no path can exists form " + nodeDest.getSerialNodeID() + " to "
					+ nodeStart.getSerialNodeID();
			log.logAndPrint(error);
			return null;
		}

		NodeMatrix.reinitializeNode(nodeStart);
		NodeMatrix.reinitializeNode(nodeDest);
		nodeDest.lastPathFindSearc = nodeStart.lastPathFindSearc = numberTimesPathFindRuns = molm
				.getCounterRunPathFind_ForNewPathfindRun();

		nodeStart.fullPathLength = 0.0;
		nodeStart.color = COLORS_VISIT.GRAY;
		pq = new LinkedList<>();
		pq.addFirst(nodeStart);

		while(!pq.isEmpty()) {
			u = pq.removeFirst();
			u.color = COLORS_VISIT.BLACK;

			if (nodeDest.getPreviousOnPathfinding() == null || NodeMatrix.compNodeMatrix.compare(u, nodeDest) < 0) {
				i = -1;
				while(++i < MatrixObjectLocationManager.LENGTH_valuesCDFAN) {
					arc = MatrixObjectLocationManager.valuesCDFAN[i];
					if (arc != null) {

						xx = u.x + arc.dx;
						yy = u.y + arc.dy;
						// System.out.print("(" + xx + "," + yy + " : ");
						if (MathUtilities.isInside(xx, yy, molm)) {
							v = molm.getNodeMatrix(xx, yy);// m[yy][xx];
							// System.out.println(v.getSerialNodeID());

							if (v.lastPathFindSearc != numberTimesPathFindRuns) {
								NodeMatrix.reinitializeNode(v);
								// System.out.println("reinit " +
								// v.getSerialNodeID());
								v.lastPathFindSearc = numberTimesPathFindRuns;
							}

							// v.color = COLORS_VISIT.GRAY;
							sum = arc.weight + u.getFullPathLength();

							// reset and adjustthe collector
							ss.setXCenter(v.x);
							ss.setYCenter(v.y);
							// col.clear();

							// col.rbt.clear(); // delegato altrove
							if ((v.getPreviousOnPathfinding() == null || v.getFullPathLength() > sum)
									&& (!molm.isThereSomeoneOnShape(col, ss))
							// CHECK BUGGATO !!
							// && (v != s) */
							) {
								// v.fullPathLength = sum;
								v.addArcWeight(u, arc);
								v.setPreviousOnPathfinding(u);
								// pq.addLast(v);
								if (v.color != COLORS_VISIT.GRAY) {
									v.color = COLORS_VISIT.GRAY;
									pq.addLast(v);
									// System.out.println("adding " +
									// v.getSerialNodeID());
								}
							}
							/** } */
						}
						// else System.out.println();
					} else
						throw new RuntimeException("WTF arc null");
				}
			}
		} // fine while
		l = extractPath_PointList(nodeStart, nodeDest);
		return l;
	}

}