package tests.tDataStruct;

import java.awt.Point;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.BiFunction;

import dataStructures.graph.GraphSimple;
import dataStructures.graph.GraphSimpleAsynchronized;
import dataStructures.graph.GraphSimpleSynchronized;
import dataStructures.graph.PathFindStrategy;
import dataStructures.graph.PathGraph;
import dataStructures.graph.pathfind.PathFindAStar;
import dataStructures.graph.pathfind.PathFinderDijkstra;
import tools.Comparators;
import tools.LoggerMessages;

public class TestDijkstraPriorityQueue {
	protected static final int numberNodeGraphs = 14;

	public TestDijkstraPriorityQueue() {}

	//

	// TODO MAIN

	//

	public static void main(String[] args) {
		boolean isSynch, isDijkstra;
		int i, k;
		int[][] links;
		LoggerMessages log;
		GraphSimple<Integer, Integer> g;
		PathFindStrategy<Integer, Integer> pathFinder;
		PathGraph<Integer, Integer> p;
		Integer dest, distance;
		Scanner scan;
//		try {
//			log = new LoggerOnFile("output_TestDijkstraPQ.txt");
//		} catch (IOException e1) {
//			e1.printStackTrace();
		log = LoggerMessages.LOGGER_DEFAULT;
//		}

		log.log("Start test dikstra");

		isSynch = true;
		scan = new Scanner(System.in);
		System.out.println("Perform the synchronized version? type \"y\", otherwise type anything else");
		isSynch = "y".equals(scan.nextLine());
		System.out.println(
				"Perform the Dijkstra pathfind? Type \"y\", otherwise if you prefer AStar then type anything else");
		isDijkstra = "y".equals(scan.nextLine());

		scan.close();
		scan = null;

		pathFinder = isDijkstra ? new PathFinderDijkstra<>()
				: new PathFindAStar<Integer, Integer>(new AStarHeuristic());
		g = isSynch ? new GraphSimpleSynchronized<>(pathFinder, Comparators.INTEGER_COMPARATOR)
				: new GraphSimpleAsynchronized<>(pathFinder, Comparators.INTEGER_COMPARATOR);
		g.setLog(log);
		((PathFindAStar<Integer, Integer>) pathFinder).setGraph(g);
		i = -1;
		while (++i < numberNodeGraphs) {
			g.addNode(i);
		}

		log.log("graph with only nodes:");
		log.log(g.toString());
		log.log("\n\n");

		log.log("add links");
		links = new int[numberNodeGraphs][];
		// pairs of destination-distance
		i = 0;
		links[i++] = new int[] { //
				1, 4//
				, 2, 8//
				, 3, 10 };

		links[i++] = new int[] { 4, 3, 2, 2 };
		links[i++] = new int[] { 13, 4, 10, 7, 7, 4 };
		links[i++] = new int[] { 5, 2 };
		links[i++] = new int[] { 6, 2 };
		links[i++] = new int[] { 12, 8, 11, 1, 10, 1 };// 5
		links[i++] = new int[] { 7, 3 };
		links[i++] = new int[] { 8, 5 };
		links[i++] = new int[] { 10, 2, 11, 2, 9, 11 };
		links[i++] = null;// new int[] {};
		links[i++] = null; // new int[] { 5, 1, 8, 2 };// 10
		links[i++] = null; // new int[] { 8, 2 };
		links[i++] = null; // new int[] {};
		links[i++] = null; // new int[] {};

		log.log(Arrays.deepToString(links));

		log.log("now add them");

		i = 0;
		for (int[] link : links) {
			if (link != null) {
				k = 0;
				while (k < link.length) {
					// pairs of destination-distance
					dest = Integer.valueOf(link[k++]);
					distance = Integer.valueOf(link[k++]);
					log.log(i + "->" + dest + " dist: " + distance);
					g.addLink(i, dest, distance);
				}
			}
			i++;
		}

		log.log("graph with also links:");
		log.log(g.toString());
		log.log("\n\n now calculate path from 0 to 8:\n\n");

		p = g.getPath(0, 8);
		log.log("\n\n got path from 0 to 8:\n\n");
		log.log(String.valueOf(p));

		log.log("end test dikstra");
//		log.finalize();
	}

	protected static class AStarHeuristic implements BiFunction<Integer, Integer, Double> {

		static final Point[] heuristic;

		static {
			heuristic = new Point[] { //
					new Point(0, 4), // 0
					new Point(2, 2), // 1
					new Point(0, 4), // 2
					new Point(2, 8), // 3
					new Point(6, 0), // 4
					new Point(7, 8), // 5
					new Point(8, 1), // 6
					new Point(10, 2), // 7
					new Point(13, 3), // 8
					new Point(12, 6), // 9
					new Point(10, 4), // 10
					new Point(11, 9), // 11
					new Point(7, 10), // 12
					new Point(5, 5) // 13
			};
		}

		@Override
		public Double apply(Integer t, Integer u) {
			return heuristic[t].distance(heuristic[u]);
			// Point.distance(x1, y1, x2, y2);
		}

	}

}
