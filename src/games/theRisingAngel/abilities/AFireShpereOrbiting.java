package games.theRisingAngel.abilities;

import java.awt.Point;

import games.generic.controlModel.GModality;
import geometry.AbstractShape2D;
import geometry.ObjectShaped;
import geometry.implementations.shapes.ShapeCircle;

// TODO to be completed
public class AFireShpereOrbiting extends AOrbitingDamagingObj {
	private static final long serialVersionUID = 1L;
	public static final int RARITY = 4, BLOB_RADIUS = 3, BLOB_RADIUS_FROM_CENTRE = 7;
	public static final String NAME = "Planetary Fire Spheres";

	public AFireShpereOrbiting(GModality gameModality) {
		super(gameModality, NAME);
		setRarityIndex(RARITY);
	}

	@Override
	public ObjectShaped newOrbitingObj() { return new FlamingOrb(); }

	@Override
	public int getOrbitingObjectRadius(int index, ObjectShaped os) { return orbitingObjectRadius; }

	@Override
	public int getLevel() { // TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLevel(int level) { // TODO Auto-generated method stub
	}

	//

	@Override
	public void move(GModality modality, int timeUnits) { // TODO Auto-generated method stub
	}

	@Override
	public void setShape(AbstractShape2D shape) { // TODO Auto-generated method stub
	}

	@Override
	protected void updateCenterPosition(GModality modality) { // TODO Auto-generated method stub
	}

	//

	//

	protected class FlamingOrb extends OrbDamaging {
		private static final long serialVersionUID = 1L;

		protected FlamingOrb() {
			super();
			Point p;
			p = getLocation();
			shape = new ShapeCircle(p.x, p.y, true, BLOB_RADIUS);
		}

		@Override
		public Long getID() { return ID; }

		@Override
		public AbstractShape2D getShape() { return shape; }

		@Override
		public void setShape(AbstractShape2D shape) { this.shape = shape; }
	}
}