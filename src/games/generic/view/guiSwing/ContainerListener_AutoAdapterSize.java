package games.generic.view.guiSwing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.Serializable;

public class ContainerListener_AutoAdapterSize extends ContainerAdapter implements Serializable {

	private static final long serialVersionUID = 84022534257031L;
	final Component componentToResize;

	private ContainerListener_AutoAdapterSize(Component componentToResize) {
		this.componentToResize = componentToResize;
	}

	@Override
	public void componentAdded(ContainerEvent e) {
		Dimension d;
		int neww, newh;
		Component c;

		try {
			c = e.getChild();
			if (c != null) {
				d = componentToResize.getPreferredSize();

				neww = Math.max(d.width, (c.getWidth() + c.getX()));
				newh = Math.max(d.height, (c.getHeight() + c.getY()));
				if (neww != d.width || newh != d.height) {
					d = null;
					c = null;
					componentToResize.setPreferredSize(new Dimension(neww, newh));
				}
				// System.out.println("\tneww = " + neww + " ___ newh = " + newh
				// + "____ xx = " + xx + "__ yy = " + yy);
			}
			/*
			 * Object o = e.getSource(); if (o != null && o instanceof Component) { c =
			 * (Component) o; d = comp.getPreferredSize(); neww = Math.max(d.width, xx =
			 * (c.getWidth() + c.getX())); newh = Math.max(d.height, yy = (c.getHeight() +
			 * c.getY())); if (neww != d.width || newh != d.height) { d = null; c = null;
			 * comp.setPreferredSize(new Dimension(neww, newh)); }
			 * System.out.println("\tneww = " + neww + " ___ newh = " + newh + "____ xx = "
			 * + xx + "__ yy = " + yy); }
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static ContainerListener_AutoAdapterSize newInstance(Component com) {
		if (com == null)
			throw new IllegalArgumentException("The given component is null.");
		return new ContainerListener_AutoAdapterSize(com);
	}
}