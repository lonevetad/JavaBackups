package tools.remoteControl.msg;

import java.awt.image.BufferedImage;

import tools.remoteControl.controllerModel.AModelControllerRC;
import tools.remoteControl.controllerModel.MCMasterRC;

public class MsgScreencast extends AMessageCommand {
	private static final long serialVersionUID = -852310131L;
	protected int imgType, width, height;
	protected int[] rawPixels;
	protected transient BufferedImage reconstructed = null;

	public MsgScreencast(BufferedImage im) {
		super(MessageTypeRC.Image);
		this.rawPixels = null;
		this.height = this.width = this.imgType = 0;
		setUpImgageData(im);
	}

	protected void setUpImgageData(BufferedImage im) {
		int w, h, x, y, i;
		w = this.width = im.getWidth();
		h = this.height = im.getHeight();
		this.imgType = im.getType();
		rawPixels = new int[w * h];
		System.out.println("image with " + rawPixels.length + " pixels");
		i = 0;
		y = -1;
		while (++y < h) {
			x = -1;
			while (++x < w) {
				rawPixels[i++] = im.getRGB(x, y);
			}
		}
	}

	@Override
	public void execute(AModelControllerRC context) {
		if (context instanceof MCMasterRC) {
			if (reconstructed == null) {
				int w, h, x, y, i;
				BufferedImage bi;
				reconstructed = bi = new BufferedImage(width, height, imgType);
				i = 0;
				h = height;
				w = width;
				y = -1;
				while (++y < h) {
					x = -1;
					while (++x < w) {
						bi.setRGB(x, y, rawPixels[i++]);
					}
				}
			}
			((MCMasterRC) context).manageScreencast(reconstructed);
		}
	}
}