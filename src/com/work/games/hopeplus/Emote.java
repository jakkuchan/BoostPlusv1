package com.work.games.hopeplus;

public enum Emote {
	LOVE("love"),
	JOY("joy"),
	PEACE("peace"),
	TRUST("trust"),
	ADMIRATION("admiration"),
	SURPRISE("surprise");
	
	private String mLabel;
	
	private Emote(String label) {
		this.mLabel = label;
	}
	
	public String getLabel() {
		return mLabel;
	}
}
