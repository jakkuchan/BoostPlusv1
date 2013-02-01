package com.work.games.common;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class EmoteButton extends ButtonSprite implements OnClickListener {

	private String mSentiment;
	private boolean isSelected;
	
	public EmoteButton(float pX, float pY, final String sentiment, ITextureRegion texture, 
			VertexBufferObjectManager vbo) {
		super(pX, pY, texture, vbo, null);
		
		this.mSentiment = sentiment;
		isSelected = false;
		setOnClickListener(this);
	}

	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		flipSelected();
		
	}
	
	public void flipSelected() {
		if(isSelected == true)
			isSelected = false;
		else
			isSelected = true;
	}	

	public String getSentiment() {
		return mSentiment;
	}
}
