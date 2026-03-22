package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.controlModel.GModality;
import games.generic.controlModel.subimpl.TimedObjectPeriodic;
import tools.UniqueIDProvider;

/*Simply prints some text*/
public class ObjPrinterTO implements TimedObjectPeriodic {
	private static final long serialVersionUID = 1L;
	long timeThreshold, accumulatedTimeElapsed;
	String text;
	final Long ID;
	final GModality gm;

	public ObjPrinterTO(GModality gm, long timeThreshold, String text) {
		super();
		this.gm = gm;
		this.ID = UniqueIDProvider.UDIP_GENERAL.getNewID();
		this.timeThreshold = timeThreshold;
		this.text = text;
		this.accumulatedTimeElapsed = 0;
	}

	@Override
	public Long getID() { return ID; }

	public String getText() { return text; }

	public void setText(String text) { this.text = text; }

	@Override
	public long getAccumulatedTimeElapsed() { return accumulatedTimeElapsed; }

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) { this.accumulatedTimeElapsed = newAccumulated; }

	@Override
	public long getTimeThreshold() { return timeThreshold; }

	@Override
	public void executeAction(GModality modality) { System.out.println(text); }

	@Override
	public void onAddedToGame(GModality gm) {}

	@Override
	public void onRemovedFromGame(GModality gm) {}

	@Override
	public String getName() { return "PRINTEEEEEEEEEEEEEEEEEEER"; }

	@Override
	public GModality getGameModality() { return gm; }

	@Override
	public void setGameModality(GModality gameModality) { // TODO Auto-generated method stub
	}
}