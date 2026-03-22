package games.generic.view.guiSwing;

import java.awt.event.MouseEvent;

import videogamesOldVersion.common.abstractCommon.MouseClickListener;

public class MouseClickListenerAdapter implements MouseClickListener {
	private static final long serialVersionUID = -700237989429624L;

	public static interface MouseEventPerformer {

		public void performMouseEvent(MouseEvent e);
	}

	public MouseClickListenerAdapter() { this(null); }

	public MouseClickListenerAdapter(MouseEventPerformer mep) { this.mouseEventPerformer = mep; }

	protected MouseEventPerformer mouseEventPerformer;

	//

	public MouseEventPerformer getMouseEventPerformer() { return mouseEventPerformer; }

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
		if (mouseEventPerformer != null)
			mouseEventPerformer.performMouseEvent(e);
	}

}
