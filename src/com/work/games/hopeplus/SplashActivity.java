package com.work.games.hopeplus;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import com.work.games.common.CommonClass;

import android.content.Intent;

public class SplashActivity extends SimpleBaseGameActivity {
		
	/*
	 * Fields
	 */
	private Camera				mCamera;
	private Scene				mScene;
	private BitmapTextureAtlas	mTexture;
	private ITextureRegion		mLogoRegion;
	private Sprite				mLogoSprite;
		
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_HEIGHT);
		
		final EngineOptions mEngineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_WIDTH), mCamera);				
		return mEngineOptions;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 362, TextureOptions.BILINEAR);
		mTexture.load();
		
		mLogoRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mTexture, this, "splash_logo.png", 0,0);
	}

	@Override
	protected Scene onCreateScene() {
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		mScene = new Scene();
		
		mScene.setColor(Color.BLACK);
		
		final float centerX = (CommonClass.PORTRAIT_CAMERA_WIDTH - mLogoRegion.getWidth()) / 2;
		final float centerY = (CommonClass.PORTRAIT_CAMERA_HEIGHT- mLogoRegion.getHeight()) /2;
		
		mLogoSprite = new Sprite(centerX, centerY, mLogoRegion, vertexBufferObjectManager);
		mLogoSprite.setScale(2);
		mScene.attachChild(mLogoSprite);
	
		mScene.registerUpdateHandler(new TimerHandler(CommonClass.SPLASH_TIME_2, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SplashActivity.this, HopeplusMainActivity.class);
				finish();
				startActivity(intent);
			}
	
		}));
		
		return mScene;
	}

}
