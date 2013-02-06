package com.work.games.hopeplus;

import java.util.Locale;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
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
	private Scene				mSplashScene, mTitleScene;
	private BitmapTextureAtlas	mSplashTexture, mTitleTexture, mButtonTexture;
	private ITextureRegion		mSplashRegion, mStartRegion, mSettingsRegion;
	private ITiledTextureRegion mTitleRegion;
	private Sprite				mSplashSprite;
	private ButtonSprite		mButtonStart, mButtonSettings;
	private AnimatedSprite		mTitleSprite;
	
		
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_HEIGHT);
		
		final EngineOptions mEngineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_HEIGHT), mCamera);				
		return mEngineOptions;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mSplashTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 362, TextureOptions.BILINEAR);
		mSplashTexture.load();		
		mSplashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSplashTexture, this, "splash_logo.png", 0,0);

		mTitleTexture = new BitmapTextureAtlas(this.getTextureManager(), 
				CommonClass.PORTRAIT_CAMERA_WIDTH*4, CommonClass.PORTRAIT_CAMERA_HEIGHT, TextureOptions.BILINEAR);
		mTitleTexture.load();
		mTitleRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mTitleTexture, this, "title_splash.png", 0,0,4,1);

		mButtonTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mButtonTexture.load();
		mStartRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "start_button.png", 0,0);
		mSettingsRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "settings_button.png", 0,45);
		
	}

	@Override
	protected Scene onCreateScene() {
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		// Create Splash Scene
		mSplashScene = new Scene();
		mSplashScene.setColor(Color.BLACK);
		
		float centerX = (CommonClass.PORTRAIT_CAMERA_WIDTH - mSplashRegion.getWidth()) / 2;
		float centerY = (CommonClass.PORTRAIT_CAMERA_HEIGHT- mSplashRegion.getHeight()) /2;
		
		mSplashSprite = new Sprite(centerX, centerY, mSplashRegion, vertexBufferObjectManager);
		mSplashSprite.setScale(2);
		mSplashScene.attachChild(mSplashSprite);
	
		mSplashScene.registerUpdateHandler(new TimerHandler(CommonClass.SPLASH_TIME_2, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				mEngine.setScene(mTitleScene);
			}
	
		}));
		
		// Create Title Scene
		mTitleScene = new Scene();
				
		mTitleSprite = new AnimatedSprite(0, 0, mTitleRegion, vertexBufferObjectManager);
		mTitleSprite.animate(400, false);
		mTitleScene.attachChild(mTitleSprite);
		
		centerX = (CommonClass.PORTRAIT_CAMERA_WIDTH - mStartRegion.getWidth()) / 2;
		centerY = (CommonClass.PORTRAIT_CAMERA_HEIGHT- mStartRegion.getHeight()) /2;

		mButtonStart = new ButtonSprite(centerX, centerY+200, mStartRegion, vertexBufferObjectManager);
		mButtonStart.setVisible(true);
		mButtonStart.setScale(2);
		mButtonStart.setOnClickListener( new OnClickListener()
		{
		    public void onClick( ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY )
		    {
				Intent intent = new Intent(SplashActivity.this, HopeplusMainActivity.class);
				finish();
				startActivity(intent);
		    }      
		});
		mTitleScene.registerTouchArea(mButtonStart);		
		mTitleScene.attachChild(mButtonStart);
		
		mButtonSettings = new ButtonSprite(centerX, centerY+300, mSettingsRegion, vertexBufferObjectManager);
		mButtonSettings.setVisible(true);
		mButtonSettings.setScale(2);
		mButtonSettings.setOnClickListener( new OnClickListener()
		{
		    public void onClick( ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY )
		    {
		    }      
		});
		mTitleScene.registerTouchArea(mButtonSettings);		
		mTitleScene.attachChild(mButtonSettings);
		
		// Return Splash screen first
		return mSplashScene;
	}

}
