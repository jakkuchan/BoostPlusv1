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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;

import com.work.games.common.CommonClass;

public class SplashActivity extends SimpleBaseGameActivity {
		
	/*
	 * Fields
	 */
	private Camera				mCamera;
	private SplashScene			mSplashScene;
	private TitleScene			mTitleScene;
	private SettingsScene		mSettingsScene;
	private int					mCurrentScene;
	private final int			SPLASH_SCENE = 0, TITLE_SCENE = 1, SETTINGS_SCENE = 2; 
	private SharedPreferences   mPref;
	
		
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
				mCurrentScene = TITLE_SCENE;
				mEngine.setScene(mTitleScene);
			}
	
		}));

		// Create Title Screen Instance
		mTitleScene = new TitleScene(this.mEngine, this, this.getTextureManager(), vbo);
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
		    	mCurrentScene = SETTINGS_SCENE;
		    	mEngine.setScene(mSettingsScene);
		    }      
		});

		// Create Settings Screen Instance
		mSettingsScene = new SettingsScene(this.mEngine, this, this.getTextureManager(), this.getFontManager(), vbo);
		loadSettingValues();
		
		mCurrentScene = SPLASH_SCENE;
		
		// Return Splash screen first
		return mSplashScene;
	}
	
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
	    if(pKeyCode == KeyEvent.KEYCODE_BACK &&
	    		pEvent.getAction() == KeyEvent.ACTION_DOWN) {
	    		if(mCurrentScene == SETTINGS_SCENE) {
	    			saveSettingValues();
	    			mSettingsScene.unregisterTouches();
	    			mCurrentScene = TITLE_SCENE;
	    			mEngine.setScene(mTitleScene);
	    			return true;
	    		}
	    }
	    return super.onKeyDown(pKeyCode, pEvent);
	}

	private void saveSettingValues() {
		SharedPreferences.Editor editor = mPref.edit();
		
		editor.putFloat("Rate", mSettingsScene.getRate());
		editor.putFloat("Pitch", mSettingsScene.getPitch());
		editor.putFloat("Sensitivity", mSettingsScene.getSensitivity());
		editor.commit();	
	}
	
	private void loadSettingValues() {
		mPref = getSharedPreferences(CommonClass.PREFERENCES_STRING, Context.MODE_PRIVATE);
		
		float rate = mPref.getFloat("Rate", CommonClass.SPEECH_RATE_L);
		float pitch = mPref.getFloat("Pitch", CommonClass.PITCH_LVL_M);
		float sensitivity = mPref.getFloat("Sensitivity", CommonClass.SENSITIVITY_MID);
		mSettingsScene.setSettings(pitch, rate, sensitivity);
	}

}
