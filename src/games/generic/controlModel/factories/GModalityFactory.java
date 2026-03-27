package games.generic.controlModel.factories;

import java.util.Objects;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;

public abstract class GModalityFactory {

	public GModalityFactory(String nameGModality, GModalityFactoryContext gModalityFactoryContext) {
		super();
		Objects.requireNonNull(nameGModality);
		Objects.requireNonNull(gModalityFactoryContext);
		this.nameGModality = nameGModality;
		this.gModalityFactoryContext = gModalityFactoryContext;
	}

	public GModalityFactory(String nameGModality) {
		this(nameGModality, GModalityFactoryContext.getDefaultContext());
	}

	protected final String nameGModality;
	protected GModalityFactoryContext gModalityFactoryContext;

	//

	public String getNameGModality() {
		return this.nameGModality;
	}

	public GModalityFactoryContext getGModalityFactoryContext() {
		return gModalityFactoryContext;
	}

	//

	public void setgModalityFactoryContext(GModalityFactoryContext gModalityFactoryContext) {
		this.gModalityFactoryContext = gModalityFactoryContext;
	}

	//

	/**
	 * Build a new {@link GModality}. To add other parameters on that game modality,
	 * just design this interface's implementation.
	 */
	public abstract GModality newGameModality(GController gc, String gameModalityName);

	public final GModality newAndSetupGameModality(GController gc, String gameModalityName) {
		GModality gm = this.newGameModality(gc, gameModalityName);
		gm.onCreate(this.getGModalityFactoryContext());
		return gm;
	}
}