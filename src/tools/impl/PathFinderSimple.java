package tools.impl;

import tools.PathFinder;

/** Simpler, more intuitive, interface for Path Finding. */
public interface PathFinderSimple<NodeType, Distance extends Number> extends PathFinder<NodeType, NodeType, Distance> {
}