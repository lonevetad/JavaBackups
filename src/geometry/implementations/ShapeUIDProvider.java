package geometry.implementations;

import tools.UniqueIDProvider.BaseUniqueIDProvider;

public class ShapeUIDProvider extends BaseUniqueIDProvider {

	public static final ShapeUIDProvider SHAPE_UNIQUE_ID_PROVIDER = newShapeIDProvider();

	public static ShapeUIDProvider newShapeIDProvider() { return new ShapeUIDProvider(); }

	public ShapeUIDProvider() {}
}