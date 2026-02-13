package tools.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import dataStructures.EditCosts;
import dataStructures.EditCosts.ActionCost;
import tools.EditDistance;
import tools.IterableSized;
import tools.IterableSized.CollectionDelegator;

public class EditDistanceLevenshtein implements EditDistance {

	/**
	 * Runs the Levenshtein distance.
	 *
	 * @param <T>
	 * @param firstSequence
	 * @param secondSequence
	 * @param differenceEvaluator function that evaluates the difference / cost of
	 *                            substitution between two values
	 * @return
	 */
	public static <T> long editDistanceLevenshtein(T[] firstSequence, T[] secondSequence, EditCosts<T> editCosts) {
		int shortestLength, longestLength;
		long diff, min;
		long[] row, prevRow, temp;
		T elemFromFirst, elemFromSecond;
		final ActionCost<T> ic, dc;

		shortestLength = firstSequence.length;
		longestLength = secondSequence.length;
		Objects.requireNonNull(editCosts);

		if (longestLength == 0)
			return Arrays.stream(firstSequence).map(editCosts::insertion).reduce(0L, (a, b) -> a + b);
		else if (shortestLength == 0)
			return Arrays.stream(secondSequence).map(editCosts::insertion).reduce(0L, (a, b) -> a + b);

		if (longestLength == 1) {
			final T e = secondSequence[0];
			return Arrays.stream(firstSequence).map(t -> {
				return Math.min(Math.min(editCosts.insertion(t), editCosts.deletion(t)), editCosts.substitution(t, e));
			}).reduce(0L, (a, b) -> a + b);
		} else if (shortestLength == 1) {
			final T e = firstSequence[0];
			return Arrays.stream(secondSequence).map(t -> {
				return Math.min(Math.min(editCosts.insertion(t), editCosts.deletion(t)), editCosts.substitution(t, e));
			}).reduce(0L, (a, b) -> a + b);
		}

		// let the second sequence be the longest to optimize the memory usage
		if (shortestLength > longestLength) { // swap
			int t;
			T[] tt;
			t = shortestLength;
			shortestLength = longestLength;
			longestLength = t;
			tt = firstSequence;
			firstSequence = secondSequence;
			secondSequence = tt;
			ic = editCosts::deletion;
			dc = editCosts::insertion;
		} else {
			ic = editCosts::insertion;
			dc = editCosts::deletion;
		}

		// NOTE: costs of insertion and deletion are fixed to 1

		row = new long[shortestLength + 1];
		prevRow = new long[shortestLength + 1];
		prevRow[0] = 0;
		for (int c = 0; c < shortestLength; c++) {
			prevRow[c + 1] = prevRow[c] + ic.getCost(firstSequence[c]);
		}

		for (int r = 0; r < longestLength; r++) {
			elemFromFirst = secondSequence[r];
//			row[0] = r;
			row[0] = prevRow[0] + 1; // insertion
			for (int c = 0; c < shortestLength; c++) {
				diff = editCosts.substitution(elemFromFirst, elemFromSecond = firstSequence[c]);
				if (diff == 0) {
					row[c + 1] = prevRow[c]; // copy diagonally
				} else {
					min = diff + prevRow[c]; // substitution
					// recycle the variable "diff" as temporary variable
					min = (min < (diff = row[c] + ic.getCost(elemFromSecond))) ? min : diff; // insert
					row[c + 1] = (min < (diff = prevRow[c + 1] + dc.getCost(elemFromFirst)) ? min : diff); // deletion
				}
			}
			// swap
			temp = prevRow;
			prevRow = row;
			row = temp;
		}
		return prevRow[shortestLength];
	}

	/**
	 * Runs the Levenshtein distance.
	 *
	 * @param <T>
	 * @param firstSequence
	 * @param secondSequence
	 * @param differenceEvaluator funciton that evaluates the difference / cost of
	 *                            substitution bewteen two values
	 * @return
	 */
	public static <T> long editDistanceLevenshtein(List<T> firstSequence, List<T> secondSequence,
			final EditCosts<T> editCosts) {
		int shortestLength, longestLength;
		long diff, min;
		long[] row, prevRow, temp;
		T elemFromFirst, elemFromSecond;
		final ActionCost<T> ic, dc;

		shortestLength = firstSequence.size();
		longestLength = secondSequence.size();
		Objects.requireNonNull(editCosts);

		if (longestLength == 0)
			return firstSequence.stream().map(editCosts::insertion).reduce(0L, (a, b) -> a + b);
		else if (shortestLength == 0)
			return secondSequence.stream().map(editCosts::insertion).reduce(0L, (a, b) -> a + b);

		if (longestLength == 1) {
			final T e = secondSequence.get(0);
			return firstSequence.stream().map(t -> {
				return Math.min(Math.min(editCosts.insertion(t), editCosts.deletion(t)), editCosts.substitution(t, e));
			}).reduce(0L, (a, b) -> a + b);
		} else if (shortestLength == 1) {
			final T e = firstSequence.get(0);
			return secondSequence.stream().map(t -> {
				return Math.min(Math.min(editCosts.insertion(t), editCosts.deletion(t)), editCosts.substitution(t, e));
			}).reduce(0L, (a, b) -> a + b);
		}

		// let the second sequence be the longest to optimize the memory usage
		if (shortestLength > longestLength) { // swap
			int t;
			List<T> tt;
			t = shortestLength;
			shortestLength = longestLength;
			longestLength = t;
			tt = firstSequence;
			firstSequence = secondSequence;
			secondSequence = tt;
			ic = editCosts::deletion;
			dc = editCosts::insertion;
		} else {
			ic = editCosts::insertion;
			dc = editCosts::deletion;
		}

		// NOTE: costs of insertion and deletion are fixed to 1

		row = new long[shortestLength + 1];
		prevRow = new long[shortestLength + 1];
		prevRow[0] = 0;
		for (int c = 0; c < shortestLength; c++) {
			prevRow[c + 1] = prevRow[c] + ic.getCost(firstSequence.get(c));
		}

		for (int r = 0; r < longestLength; r++) {
			elemFromFirst = secondSequence.get(r);
//			row[0] = r;
			row[0] = prevRow[0] + 1; // insertion
			for (int c = 0; c < shortestLength; c++) {
				diff = editCosts.substitution(elemFromFirst, elemFromSecond = firstSequence.get(c));
				if (diff == 0) {
					row[c + 1] = prevRow[c]; // copy diagonally
				} else {
					min = diff + prevRow[c]; // substitution
					// recycle the variable "diff" as temporary variable
					min = (min < (diff = row[c] + ic.getCost(elemFromSecond))) ? min : diff; // insert
					row[c + 1] = (min < (diff = prevRow[c + 1] + dc.getCost(elemFromFirst)) ? min : diff); // deletion
				}
			}
			// swap
			temp = prevRow;
			prevRow = row;
			row = temp;
		}
		return prevRow[shortestLength];
	}

	@Override
	public <T> long editDistance(T[] firstSequence, T[] secondSequence, EditCosts<T> cac) {
		return editDistanceLevenshtein(firstSequence, secondSequence, cac);
	}

	@Override
	public <T> long editDistance(List<T> firstSequence, List<T> secondSequence, EditCosts<T> cac) {
		return editDistanceLevenshtein(firstSequence, secondSequence, cac);
	}

	@Override
	public <T> long editDistance(IterableSized<T> firstSequence, IterableSized<T> secondSequence, EditCosts<T> cac) {
		List<T> a1, a2;
		Collection<T> cTemp;
		a1 = a2 = null;
		if (firstSequence instanceof CollectionDelegator<?>) {
			cTemp = ((CollectionDelegator<T>) firstSequence).getDelegator();
			if (cTemp instanceof List<?>) { a1 = (List<T>) cTemp; }
		}
		if (a1 == null) {
			a1 = new ArrayList<>(firstSequence.getSize());
			firstSequence.forEach(a1::add);
		}
		if (secondSequence instanceof CollectionDelegator<?>) {
			cTemp = ((CollectionDelegator<T>) secondSequence).getDelegator();
			if (cTemp instanceof List<?>) { a2 = (List<T>) cTemp; }
		}
		if (a2 == null) {
			a2 = new ArrayList<>(secondSequence.getSize());
			secondSequence.forEach(a2::add);
		}
		return editDistanceLevenshtein(a1, a2, cac);
	}

}