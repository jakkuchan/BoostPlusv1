package com.work.games.hopeplus;

public enum Emote {
	LOVE("love"),
	JOY("joy");
	
	private String mLabel;
	
	private Emote(String label) {
		this.mLabel = label;
	}
	
	public String getLabel() {
		return mLabel;
	}
}
