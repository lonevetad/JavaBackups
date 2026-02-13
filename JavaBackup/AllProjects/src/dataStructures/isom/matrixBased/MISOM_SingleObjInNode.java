package dataStructures.isom.matrixBased;

import dataStructures.isom.NodeIsom;
import tools.NumberManager;

public class MISOM_SingleObjInNode<Distance extends Number> extends MatrixInSpaceObjectsManager<Distance> {
	private static final long serialVersionUID = -7861257521420150L;

	public MISOM_SingleObjInNode(boolean isLazyNodeInstancing, int width, int height,
			NumberManager<Distance> weightManager) {
		super(isLazyNodeInstancing, width, height, weightManager);
	}

	public MISOM_SingleObjInNode(Long ID, boolean isLazyNodeInstancing, int width, int height,
			NumberManager<Distance> weightManager) {
		super(ID, isLazyNodeInstancing, width, height, weightManager);
	}

	@Override
	public NodeIsom<Distance> newNodeMatrix(int x, int y) { return new NodeIsomSingleObj<>(this, x, y); }
}