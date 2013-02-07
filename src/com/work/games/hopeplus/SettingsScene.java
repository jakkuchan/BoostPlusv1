package com.work.games.hopeplus;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;

public class SettingsScene extends Scene {
	
	private VertexBufferObjectManager mVbo;
	private ITextureRegion		mSplashRegion;
	private BitmapTextureAtlas	mSplashTexture;
	private Sprite				mSplashSprite;
	private TextureManager		mTextureManager;
	private Context				mContext;
	private Engine				mEngine;
	
	public SettingsScene(Engine pEngine, Context pContext, TextureManager pTextureManager, VertexBufferObjectManager pVbo) {
		mEngine = pEngine;
		mTextureManager = pTextureManager;
		mContext = pContext;
		mVbo = pVbo;
		
		initialize();
	}
	
	private void initialize() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		// Set whole background to black
		this.setBackground(new Background(Color.YELLOW));
	}
}
