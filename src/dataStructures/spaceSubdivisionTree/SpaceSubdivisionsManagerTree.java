package dataStructures.spaceSubdivisionTree;

import java.awt.Rectangle;
import java.io.Serializable;

import dataStructures.isom.MultiISOMRetangularCaching;
import dataStructures.spaceSubdivisionTree.impl.utils.SubsectionDivisionRuler;
import geometry.AbstractShape2D;

/**
 * TODO del 05-10-2019 <br>
 * Per implementare l'aggiunta di un oggetto con forma (shape), collezionare
 * tutti gli SpaceSubsectionNode "foglia" (senza children) che intersecano con
 * tale forma e in ciascuno fare l'aggiunta. Se la forma � un punto, usare il
 * modo già implementato per ottimizzare. Altrimenti, invocare un metodo
 * gatherAllNodesAbleToHold che restituisce una List<SpaceSubsectionNode>. Per
 * farlo, si prende ogni nodo ad incominciare dal root, e si vede se la forma
 * dell'oggetto dato "interseca" quella del nodo. A tal proposito, ogni
 * SpaceSubsectionNode ha una shape associata. Quindi si vede se tale shape
 * intersecano. si interseca con la forma dell'oggetto da inserire. Se si,
 * allora si vede se ha children. Se si, allora ricorsione su tutti i children
 * raggruppando i risultati in una unica LinkedList. Se no, allora tale nodo �
 * una foglia e si raccoglie tale nodo. <br>
 * Ottenuta tale lista, per ogni nodo si effettua un inserimento dell'oggetto
 * (avente una forma) da inserire.
 * <p>
 * Volendo, non � necessario fare la raccolta nella List e poi inserire in tutti
 * la forma, pu� bastare usare la ricorsione sul singolo nodo il quale farà
 * l'inserimento appena capisce che sia � una foglia sia c'� intersezione.
 */
// TODO
/**
 *
 * TODO NOTE of 13/06/2020 <br>
 *
 * 16/07/2020: SPIEGAZIONE: la pensai ai tempi di Isabella-Losanna,
 *
 * @deprecated Use {@link MultiISOMRetangularCaching} to build a
 *             {@link AbstractShape2D}-based implementation (and not just a
 *             {@link Rectangle}-based only).
 *
 *
 */
@Deprecated
public abstract class SpaceSubdivisionsManagerTree implements Serializable {
	private static final long serialVersionUID = -9982665230577L;

	public SpaceSubdivisionsManagerTree(double minSquareSideLength, Rectangle baseShape,
			SpaceSubdivisions spaceSubdivisions, SubsectionDivisionRuler divisionRuler) {
		super();
		if (minSquareSideLength <= 0.0)
			throw new IllegalArgumentException("Minimum square side's length cannot be zero or negative");
		if (baseShape == null || baseShape.getWidth() <= 0 || baseShape.getHeight() <= 0)
			throw new IllegalArgumentException("The base shape is null or empty:\n\t" + baseShape);
		if (spaceSubdivisions == null)
			throw new IllegalArgumentException("The SpaceSubdivisions cannot be null:\n\t" + divisionRuler);
		if (divisionRuler == null)
			throw new IllegalArgumentException("The division ruler cannot be null:\n\t" + divisionRuler);
//		if (spaceSubsectionNodeFactory == null)
//			throw new IllegalArgumentException(
//					"The space subsection node factory cannot be null:\n\t" + spaceSubsectionNodeFactory);

		this.minSquareSideLength = minSquareSideLength;
		this.baseShape = new Rectangle(baseShape);
		this.baseShape.setLocation(0, 0);
		this.spaceSubdivisions = spaceSubdivisions;
		this.divisionRuler = divisionRuler;
	}

	protected double minSquareSideLength;
	protected Rectangle baseShape;
	protected SpaceSubdivisions spaceSubdivisions;
	protected SubsectionDivisionRuler divisionRuler;
	protected SpaceSubsectionNode root;

	//

	public double getMinSquareSideLength() { return minSquareSideLength; }

	public Rectangle getBaseShape() { return baseShape; }

	public SubsectionDivisionRuler getDivisionManager() { return divisionRuler; }

	public SpaceSubdivisions getSpaceSubdivisions() { return spaceSubdivisions; }

	//

	// TODO ABSTRACT METHODS

	protected abstract void build();

	/*
	 * protected abstract SpaceSubsectionNode newSpaceSubsectionNode(int level,
	 * SpaceSubsectionNode father, SingleSpaceSubdivision spaceSectionFromFather,
	 * double x, double y);
	 */

	//

	// TODO CLASSES
}