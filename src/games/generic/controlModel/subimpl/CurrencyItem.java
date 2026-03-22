package games.generic.controlModel.subimpl;

import java.awt.Point;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.currency.Currency;
import games.generic.controlModel.objects.InteractableObject;
import geometry.ObjectLocated;
import tools.ObjectNamedID;
import tools.impl.OWIDLongImpl;

/**
 * A currency that can be placed inside the map
 */
public abstract class CurrencyItem extends OWIDLongImpl implements InteractableObject, ObjectLocated, ObjectNamedID {
	private static final long serialVersionUID = 1L;

	public CurrencyItem() {
	}

	protected int amount;
	protected String name;
	protected Point location; // in "space" (GOM)
	protected Currency currency;

	//

	public int getAmount() {
		return amount;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Point getLocation() {
		return location;
	}

	public Currency getCurrency() {
		return currency;
	}

	//

	public void setAmount(int amount) {
		if (amount > 0) {
			this.amount = 0;
		} else {
			this.amount = amount;
		}
	}

	public ObjectNamed setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public void setLocation(Point location) {
		this.location = location;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void setLocation(int x, int y) {
		this.location.x = x;
		this.location.y = y;
	}

	//

}