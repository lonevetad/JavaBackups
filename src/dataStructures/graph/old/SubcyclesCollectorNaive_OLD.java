package dataStructures.graph.old;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;

import dataStructures.MapTreeAVL;
import dataStructures.graph.GraphSimple;
import dataStructures.graph.GraphSimpleGenerator;
import dataStructures.graph.PathFindStrategy;
import dataStructures.graph.cycles.SubcyclesCollector;

public class SubcyclesCollectorNaive_OLD<E, Distance> implements SubcyclesCollector<E, Distance> {
	private static final long serialVersionUID = 541052402540L;

	@Override
	public List<GraphSimple<E, Distance>> collectCycles(boolean keepSource, GraphSimple<E, Distance> gsource,
			GraphSimpleGenerator<E, Distance> gsg) {
		boolean cycleFound, isYetVisited;
		int index;
//		Map<Integer,
		List<GraphSimple<E, Distance>> ret;
		GraphSimple<E, Distance> cycle, nodesLeft;
		Set<Entry<E, GraphSimple<E, Distance>.NodeGraph>> allElements;
		Function<Set<Entry<E, GraphSimple<E, Distance>.NodeGraph>>, Entry<E, GraphSimple<E, Distance>.NodeGraph>> elementsPeeker;
		MapTreeAVL<E, NodeGraphCycleColInfo<E, Distance>> nodeVisited;
		LinkedList<NodeGraphCycleColInfo<E, Distance>> nodeToVisit;
		GraphSimple<E, Distance>.NodeGraph adjNode;
		NodeGraphCycleColInfo<E, Distance> cni, start, adj;
		Iterator<GraphSimple<E, Distance>.NodeGraph> iter;
		GraphSimple<E, Distance>.NodeGraph nStart;

		if (gsource == null || gsg == null || gsource.isEmpty())
			return null;
		ret = // MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				// Comparators.INTEGER_COMPARATOR);
				new LinkedList<>();
		index = 0;
		// fill the node
		if (keepSource) {
			nodesLeft = gsource;
		} else {
			nodesLeft = gsource.deepCopy(gsg);
		}
		allElements = nodesLeft.getNodesSet();
//						gsource.getComparatorElements());
//find a way to peek elements
		if (allElements instanceof SortedSet<?>) {
			elementsPeeker = s -> ((SortedSet<Entry<E, GraphSimple<E, Distance>.NodeGraph>>) s).first();
		} else {
			elementsPeeker = new SetExtractor<>(gsource.getPathFinder());
		}
		nodeVisited = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, gsource.getComparatorElements());
		nodeToVisit = // MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
						// gsource.getComparatorElements());
				new LinkedList<>();
		while (!allElements.isEmpty()) {
			// the breadth-first is probably a flooding algorithm ...
			// get the first to start the breadth-first exploration with
			cycleFound = false;
			nStart = elementsPeeker.apply(allElements).getValue();

			System.out.println("starting with node: " + nStart);

			if (nodeVisited.containsKey(nStart.getElem())) {
				start = nodeVisited.get(nStart.getElem());
			} else
				start = new NodeGraphCycleColInfo<>(nStart);
//			nodesLeft.removeNode(start.node.getElem());
			if (start.cyclesBelongingLeft > 0) {

//			nodeToVisit.put(currentNode.getElem(), cni);
				start.father = start; // as assumption, because it's the starter
				nodeToVisit.clear();
//			nodeVisited.clear();
				nodeToVisit.add(start);
				nodeVisited.put(start.node.getElem(), start); // every time I add a node in nodeToVisit, I add there too
				/*
				 * NOTE: to avoid silly cycles due to undirected graphs, refuse to consider the
				 * start (i.e. the end of the cycle) as "found" if it's a direct adjacent of the
				 * current adjacent-node. Of course, if the graph is directed then the cycle
				 * will be closed. At the end of the cycle, this ambiguous situation will
				 * collapse: if exists a path longer than one simple adjacent then it's taken,
				 * otherwise the first adjacent is taken (the first will result a "random node",
				 * because it's just the first without any kind of predictable selection). THIS
				 * whole problem probably will never occur since this is a flooding algorithm
				 */
				while (!(cycleFound || nodeToVisit.isEmpty())) {
					int distAdj;
//				cni = nodeToVisit.removeMinimum().getValue(); // remove one random element, no matter who
					cni = nodeToVisit.removeFirst(); // remove one element
					distAdj = cni.distanceFromStart + 1;
					if (distAdj < 0)
						distAdj = Integer.MAX_VALUE; // handle overflow
					// start with a breadth-first
//				cni.node.forEachAdjacents((adj,dist)->{
//				} ); //no, eaarly stopping is required
					iter = cni.node.iteratorAdjacent();
					if (iter != null) {

						System.out.println("has cni.node [[" + cni.node + "]] has next? " + iter.hasNext());

						while ((!cycleFound) && iter.hasNext()) {
							adjNode = iter.next();
							// exists in cache?
							if (isYetVisited = nodeVisited.containsKey(adjNode.getElem())) {
								adj = nodeVisited.get(adjNode.getElem());
								if (index != adj.lastGraphBelonging) { // do you come from a previous scan?
									adj.lastGraphBelonging = index;
									adj.father = null;
									adj.distanceFromStart = 0;
								}
							} else {
								adj = new NodeGraphCycleColInfo<>(adjNode);
								adj.lastGraphBelonging = index;
							}
							// check adj father
							if (adj.father == null || adj.distanceFromStart > distAdj) {
								// update
								adj.father = cni;
								adj.distanceFromStart = distAdj;
								if (!isYetVisited) {
									nodeToVisit.addLast(adj); // expand the search
									nodeVisited.put(adjNode.getElem(), adj);
								}
							}
							// check the start: beware of directed graphs
							// cases:
							/*
							 * 1) isYetVisited is true: (adj==start) || (a new subcycle found) .. if second,
							 * then the next father-while's iteration will spot it, else .. good ... since
							 * this situation is surely happening in any case, because the start is present
							 * in "visited" since before the while, then just ignore it
							 */
							/*
							 * 2) simply adj==start: eureka, but... 2.1) graph is direct -> Eureka 2.2)
							 * graph undirect .. is adj a simple start's adjacent? if so ... not good
							 */
							/*
							 * BUT since the search is a flooding-like one, then the start will always be
							 * found by the shortest path: the cycle is composed by less than 3 nodes or
							 * isYetVisited will be true
							 */
							/*
							 * VERSION 3: no flooding, just breadth-first: if is Direct, then as soon as
							 * start is found the "ok" is signaled, otherwise
							 */
							/**
							 * VERSION 4: no matter if direct or no, if adj is not a start's adjacent, then
							 * a path is found. Otherwise, this 2-node-ring will be fond after every other
							 * more-than-2-rings
							 */
							if ((adj == start) && //
									(!start.node.hasAdjacent(cni.node))
							// (
							// (gsource.isDirected()) || //
//										 )//
							) {
								start.father = cni;
								cycleFound = true;

								System.out.println("CYCLE FOUND:");
								printCycle(start);
							}
						}
					}
				}
				nodeToVisit.clear();
				cycle = null;

				/*
				 * Note: each node N could belong to more than one cycle. This amount is stored
				 * in "cyclesBelongingLeft". This MUST be decreased at the end of each
				 * parent-while's cycle (by one because one of his adjacent node K has marked it
				 * as "father", a second one if that node N is adjacent to N). Then, check if
				 * it's zero or less, remove N and K to "nodesLeft".
				 */

				if (cycleFound) {
					/*
					 * build up the cyle and remove the nodes to allElements if, after removing the
					 * liks, no adjacents are left
					 */

					printCycle(start);

					cycle = gsg.newGraph(gsource.isDirected(), gsource.getPathFinder(),
							gsource.getComparatorElements());
					cni = start;
					do {
						cycle.addNode(cni.node.getElem());
						cycle.addLink(cni.node.getElem(), cni.father.node.getElem(),
								cni.father.node.getAdjacentDistance(cni.node));
						if (cni.node.hasAdjacent(cni.father.node))
							cni.cyclesBelongingLeft -= 2;
						else
							cni.cyclesBelongingLeft--;
						if (cni.cyclesBelongingLeft <= 0) // remove: no more cycle to belongs to
							nodesLeft.removeNode(cni.node.getElem());
						cni = cni.father;
					} while (cni != start);
				} else {
					// return null; // NO CYCLE EXISTS -> ignore this
					// search for 2-node-rings
					iter = start.node.iteratorAdjacent();
					if (iter != null) {
						boolean ring2NodeFound;
						ring2NodeFound = false;
						cni = null;

						System.out.println("no cycle found, how many adjacent has start?" + start.node.adjacentsSize());
						start.node.forEachAdjacents((nnnn, dddd) -> System.out.println(nnnn));
						System.out.println("after visiting:");
						System.out.println(nodeVisited);

						while (iter.hasNext()
								// search for the adjacent pointing to the start
								&& (!(ring2NodeFound = //
										(cni = nodeVisited.get(iter.next().getElem())).node//
//										(cni = iter.next())//
												.hasAdjacent(start.node))))
							;
						if (ring2NodeFound) {
//						nodesLeft.removeLink(start.node.getElem(), cni.node.getElem());
//						nodesLeft.removeLink(cni.node.getElem(), start.node.getElem());
							cycle = gsg.newGraph(gsource.isDirected(), gsource.getPathFinder(),
									gsource.getComparatorElements());
							cycle.addNode(start.node.getElem());
							cycle.addLink(start.node.getElem(), cni.node.getElem(),
									cni.node.getAdjacentDistance(start.node));

							start.cyclesBelongingLeft -= 2;
							if (start.cyclesBelongingLeft <= 0) // remove: no more cycle to belongs to
								nodesLeft.removeNode(start.node.getElem());
						}
					}
				}
				if (cycle != null) {
					ret.add(cycle);
					System.out.println("\n\n ADDED CYCLE:");
					System.out.println(cycle);
					System.out.println("+++++..........................................");
				} else
					nodesLeft.removeNode(start.node.getElem()); // it wasn't removed somewhere else, so remove it now
			}
			index++;
		}

		/*
		 * l'idea e' quella di iterare per tutti i nodi fino a svuotare allElements (e
		 * quindi nodesLeft), aggiungendo i nodi trovati a nodeVisited (che potrebbe
		 * essere una mappa<E,NodeGraph> direttamente) simulando un "messaggio" che
		 * viene passato attraverso la rete, da vicino a vicino, a partire dalla
		 * sorgente. Per ogni nodo in nodeToVisit, a cominciare dal primo (gia aggiunto
		 * a nodeVisited e nodeToVisit), si aggiungono tutti gli adiacenti a nodeToVisit
		 * non presenti in nodeVisited (ignorando quindi quelli gia presenti. SE si
		 * incontra, tra gli adiacienti, il nodo di partenza, ALLORA si ha concluso un
		 * ciclo.
		 */
		/**
		 * NOTA: bisognerebbe, come in dijkstra, AMPLIARE i nodi (anziche' semplicemente
		 * riciclare NodeGraph) con una struttura che tiene traccia del "padre" che ha
		 * raggiunto un vicino, cosi da poi fare baacktrace per ottenere tutti i nodi
		 * dell'anello.<bp>INOLTRE, evitare i cicli ovvi nel caso di grafi indiretti
		 * (ogni nodo crea un anello con un suo adiacente, proprio perch'e i link sono
		 * doppi)
		 */

		return ret;
	}

	static <E, Distance> void printCycle(NodeGraphCycleColInfo<E, Distance> start) {
		NodeGraphCycleColInfo<E, Distance> cycleIter;
		cycleIter = start;
		System.out.println("printing cycle");
		while (cycleIter.father != start) {
			System.out.println("cycleIter.node.e: " + cycleIter.node.getElem());
			cycleIter = cycleIter.father;
		}
	}

	/**
	 * Check if exists a lowest common subsumer that is not the starting point (the
	 * tree's root) , that is recognised thanks to its father: it's just itself.
	 */
	public static <E, Distance> boolean isFatherButNotStart(// Comparator<E> ecomp, //
			NodeGraphCycleColInfo<E, Distance> thisNode, NodeGraphCycleColInfo<E, Distance> maybeFamiliar) {
		boolean cancontinueThis, cancontinueFamiliar;
//		Set<E> ancestorsThis, ancestorsFamiliar;
		NodeGraphCycleColInfo<E, Distance> tf, ff;
		cancontinueThis = cancontinueFamiliar = true;
		if (thisNode == maybeFamiliar)
			return true;
		/*
		 * since the search algorithm is the flooding one, then the distance from each
		 * of the two points and the "lowest common subsumer" differs fro at maximum one
		 */
//		ancestorsThis = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, ecomp).keySet();
//		ancestorsFamiliar = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, ecomp).keySet();
//		ancestorsThis.add(thisNode.node.getElem());
//		ancestorsFamiliar.add(maybeFamiliar.node.getElem());
		while (cancontinueThis || cancontinueFamiliar) {
			tf = thisNode.father;
			ff = maybeFamiliar.father;
			if (thisNode == maybeFamiliar || tf == maybeFamiliar || thisNode == ff || tf == ff)
				return true;
//			if (
			cancontinueThis = (tf != thisNode); // )
//				ancestorsThis.add(tf.node.getElem());
//			if(
			cancontinueFamiliar = (ff != maybeFamiliar); // )
//				ancestorsFamiliar.add(ff.node.getElem());
			/*
			 * if( ((ancestorsThis.contains(maybeFamiliar) || ancestorsThis.contains(ff)) &&
			 * (ff!=ff.father)) || ((ancestorsFamiliar.contains(thisNode) ||
			 * ancestorsFamiliar.contains(tf)) && (tf!=tf.father)) ) return true
			 */
			thisNode = tf;
			maybeFamiliar = ff;
		}
		return false;
	}

	//

	static class NodeGraphCycleColInfo<E, Distance> {
		int distanceFromStart, cyclesBelongingLeft; // the one from father is unary
		int lastGraphBelonging;
		GraphSimple<E, Distance>.NodeGraph node;
		NodeGraphCycleColInfo<E, Distance> father;

		public NodeGraphCycleColInfo(GraphSimple<E, Distance>.NodeGraph node) {
			super();
			this.node = node;
			this.father = null;
			this.distanceFromStart = this.lastGraphBelonging = 0;
			this.cyclesBelongingLeft = node.adjacentsSize();
		}
	}

	static class SetExtractor<E, Distance> implements //
			Function<Set<Entry<E, GraphSimple<E, Distance>.NodeGraph>>, Entry<E, GraphSimple<E, Distance>.NodeGraph>> {
		Iterator<Entry<E, GraphSimple<E, Distance>.NodeGraph>> iter;
		Set<Entry<E, GraphSimple<E, Distance>.NodeGraph>> set;

		/** Parameter needed to parametrize the class in the right way. */
		SetExtractor(PathFindStrategy<E, Distance> pf) {
			set = null;
		}

		@Override
		public Entry<E, GraphSimple<E, Distance>.NodeGraph> apply(Set<Entry<E, GraphSimple<E, Distance>.NodeGraph>> s) {
			if (this.set == null) {
				this.set = s;
				this.iter = s.iterator();
			}
			return iter.next();
		}
	}
}