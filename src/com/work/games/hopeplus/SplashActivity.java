package com.work.games.hopeplus;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.content.Intent;

import com.work.games.common.CommonClass;

public class SplashActivity extends SimpleBaseGameActivity {
		
	/*
	 * Fields
	 */
	private Camera				mCamera;
	private SplashScene			mSplashScene;
	private TitleScene			mTitleScene;
	
		
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_HEIGHT);
		
		final EngineOptions mEngineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_HEIGHT), mCamera);				
		return mEngineOptions;
	}

	@Override
	protected void onCreateResources() {
		
	}

	@Override
	protected Scene onCreateScene() {
		final VertexBufferObjectManager vbo = this.getVertexBufferObjectManager();
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		//Create Splash Screen Instance
		mSplashScene = new SplashScene(this, this.getTextureManager(), vbo);
		mSplashScene.registerUpdateHandler(new TimerHandler(CommonClass.SPLASH_TIME_2, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				mEngine.setScene(mTitleScene);
			}
	
		}));

		// Create Title Screen Instance
		mTitleScene = new TitleScene(this, this.getTextureManager(), vbo);
		mTitleScene.getStartButton().setOnClickListener( new OnClickListener()
		{
		    public void onClick( ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY )
		    {
				Intent intent = new Intent(SplashActivity.this, HopeplusMainActivity.class);
				finish();
				startActivity(intent);
		    }      
		});

		mTitleScene.getSettingsButton().setOnClickListener( new OnClickListener()
		{
		    public void onClick( ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY )
		    {
		    }      
		});

		// Return Splash screen first
		return mSplashScene;
	}

}
