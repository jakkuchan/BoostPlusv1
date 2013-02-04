package com.work.games.common;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class EmoteButton extends Sprite {

	private String mSentiment;
	private boolean isSelected;
	
	public EmoteButton(float pX, float pY, final String sentiment, ITextureRegion texture, 
			VertexBufferObjectManager vbo) {
		super(pX, pY, texture, vbo);
		
		this.mSentiment = sentiment;
		isSelected = false;
	}
	
	public String getSentiment() {
		return mSentiment;
	}
	
	public boolean getSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean value) {
		isSelected = value;
	}
	
}
