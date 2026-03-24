package games.generic.view.guiSwing;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

public class MouseClickListenerAdapter implements MouseInputListener {
	private static final long serialVersionUID = -700237989429624L;

	public static interface MouseEventPerformer {

		public void performMouseEvent(MouseEvent e);
	}

	public MouseClickListenerAdapter() {
		this(null);
	}

	public MouseClickListenerAdapter(MouseEventPerformer mep) {
		this.mouseEventPerformer = mep;
	}

	protected MouseEventPerformer mouseEventPerformer;

	//

	public MouseEventPerformer getMouseEventPerformer() {
		return mouseEventPerformer;
	}

	public void setMouseEventPerformer(MouseEventPerformer mouseEventPerformer) {
		this.mouseEventPerformer = mouseEventPerformer;
	}

	//

	public static MouseClickListenerAdapter newInstance(MouseEventPerformer mep) {
		return new MouseClickListenerAdapter(mep);
	}

	//

	@Override
	public void mouseClicked(MouseEvent e) {
		if (mouseEventPerformer != null) {
			mouseEventPerformer.performMouseEvent(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (mouseEventPerformer != null) {
			mouseEventPerformer.performMouseEvent(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (mouseEventPerformer != null) {
			mouseEventPerformer.performMouseEvent(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (mouseEventPerformer != null) {
			mouseEventPerformer.performMouseEvent(e);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (mouseEventPerformer != null) {
			mouseEventPerformer.performMouseEvent(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mouseEventPerformer != null) {
			mouseEventPerformer.performMouseEvent(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (mouseEventPerformer != null) {
			mouseEventPerformer.performMouseEvent(e);
		}
	}
}
