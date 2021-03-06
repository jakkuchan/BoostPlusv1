/*
 * Copyright Jakkugames 2013
 * http://jakkugames.host22.com
 *
 */

package com.work.games.hopeplus;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.content.Context;
import android.graphics.Typeface;

import com.work.games.common.CommonClass;

public class TitleScene extends Scene {
	
	private VertexBufferObjectManager mVbo;
	private TextureManager		mTextureManager;
	private Context				mContext;
	private BitmapTextureAtlas	mTitleTexture, mButtonTexture;
	private ITextureRegion		mStartRegion, mSettingsRegion;
	private ITiledTextureRegion mTitleRegion;
	private ButtonSprite		mButtonStart, mButtonSettings;
	private AnimatedSprite		mTitleSprite;
	private Engine				mEngine;
	private Text				mVersionText;
	private Font 				mVersionFont;
	
	public TitleScene(Engine pEngine, Context pContext, TextureManager pTextureManager, VertexBufferObjectManager pVbo) {
		mEngine = pEngine;
		mTextureManager = pTextureManager;
		mContext = pContext;
		mVbo = pVbo;
		
		initialize();
	}
	
	private void initialize() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("font/");

		mTitleTexture = new BitmapTextureAtlas(mTextureManager, 
				CommonClass.PORTRAIT_CAMERA_WIDTH*4, CommonClass.PORTRAIT_CAMERA_HEIGHT, TextureOptions.BILINEAR);
		mTitleTexture.load();
		mTitleRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mTitleTexture, mContext, "title_splash.png", 0,0,4,1);

		mButtonTexture = new BitmapTextureAtlas(mTextureManager, 256, 256, TextureOptions.BILINEAR);
		mButtonTexture.load();
		mStartRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mButtonTexture, mContext, "start_button.png", 0,0);
		mSettingsRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mButtonTexture, mContext, "settings_button.png", 0,45);
		
		mVersionFont = FontFactory.create(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256, 
				Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 20, android.graphics.Color.WHITE);
		mVersionFont.load();
		
		mVersionText = new Text(215, 350, mVersionFont, "v" + CommonClass.VERSION + CommonClass.BUILD, 
				new TextOptions(HorizontalAlign.CENTER), mVbo);
		
		// Set whole background to black
		this.setColor(Color.BLACK);
				
		mTitleSprite = new AnimatedSprite(0, 0, mTitleRegion, mVbo);
		mTitleSprite.animate(400, false);
		this.attachChild(mTitleSprite);
		
		final float centerX = (CommonClass.PORTRAIT_CAMERA_WIDTH - mStartRegion.getWidth()) / 2;
		final float centerY = (CommonClass.PORTRAIT_CAMERA_HEIGHT- mStartRegion.getHeight()) /2;

		mButtonStart = new ButtonSprite(centerX, centerY+200, mStartRegion, mVbo);
		mButtonStart.setVisible(true);
		mButtonStart.setScale(2);
		this.registerTouchArea(mButtonStart);		
		this.attachChild(mButtonStart);
		
		mButtonSettings = new ButtonSprite(centerX, centerY+300, mSettingsRegion, mVbo);
		mButtonSettings.setVisible(true);
		mButtonSettings.setScale(2);
		this.registerTouchArea(mButtonSettings);		
		this.attachChild(mButtonSettings);
		this.attachChild(mVersionText);

	}
	
	public ButtonSprite getStartButton() {
		return mButtonStart;
	}

	public ButtonSprite getSettingsButton() {
		return mButtonSettings;
	}
}
