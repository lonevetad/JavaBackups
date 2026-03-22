package dataStructures.graph;

import java.io.Serializable;
import java.util.function.BiFunction;

public interface DistanceCalculator<E, Distance> extends Serializable, BiFunction<E, E, Distance> {
}