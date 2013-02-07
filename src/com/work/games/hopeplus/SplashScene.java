package com.work.games.hopeplus;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;

import com.work.games.common.CommonClass;

public class SplashScene extends Scene {
	
	private ITextureRegion		mSplashRegion;
	private BitmapTextureAtlas	mSplashTexture;
	private Sprite				mSplashSprite;
	private TextureManager		mTextureManager;
	private Context				mContext;
	private VertexBufferObjectManager mVbo;
	
	public SplashScene(Context pContext, TextureManager pTextureManager, VertexBufferObjectManager pVbo) {
		mTextureManager = pTextureManager;
		mContext = pContext;
		mVbo = pVbo;
		
		initialize();
	}
	
	private void initialize() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mSplashTexture = new BitmapTextureAtlas(mTextureManager, 256, 362, TextureOptions.BILINEAR);
		mSplashTexture.load();		
		mSplashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSplashTexture, mContext, "splash_logo.png", 0,0);
		
		// Set whole background to black
		this.setColor(Color.BLACK);
		
		final float centerX = (CommonClass.PORTRAIT_CAMERA_WIDTH - mSplashRegion.getWidth()) / 2;
		final float centerY = (CommonClass.PORTRAIT_CAMERA_HEIGHT- mSplashRegion.getHeight()) /2;
		
		mSplashSprite = new Sprite(centerX, centerY, mSplashRegion, mVbo);
		mSplashSprite.setScale(2);
		this.attachChild(mSplashSprite);

		
	}
}
