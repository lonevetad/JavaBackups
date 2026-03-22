package srl.parsers;

public class SRLToken {

	public SRLToken(String tokenContent, SRLTokenName tokenName) {
		super();
		this.tokenContent = tokenContent;
		this.tokenName = tokenName;
	}

	protected final String tokenContent;
	protected final SRLTokenName tokenName;

	public String getTokenContent() { return tokenContent; }

	public SRLTokenName getTokenName() { return tokenName; }

	@Override
	public String toString() { return "SRLToken <" + tokenName.name() + "; " + tokenContent + '>'; }

}