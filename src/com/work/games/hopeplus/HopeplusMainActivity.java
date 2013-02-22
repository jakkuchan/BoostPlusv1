package com.work.games.hopeplus;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.view.KeyEvent;

import com.work.games.common.CommonClass;
import com.work.games.common.EmoteButton;

@SuppressLint("DefaultLocale")
public class HopeplusMainActivity extends SimpleBaseGameActivity implements SensorEventListener {

	private Camera mCamera;
	private Scene mScene;
	private BitmapTextureAtlas mTexture;
	private ITextureRegion mCrystalBallRegion, mBackgroundRegion;
	private ButtonSprite mCrystalBall;
	private SpriteBackground mBackground;
	private Sprite mBackgroundSprite;
	private boolean mInitialized;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private float mLastX, mLastY, mLastZ;
	private HopeGenerator mGenerator;
	private BitmapTextureAtlas mButtonTexture;
	private Text mMessage, mAuthor, mLabel;
	private ITextureRegion mButtonLoveTexture, mButtonJoyTexture, mButtonPeaceTexture, mButtonTrustTexture,
			mButtonAdmirationTexture, mButtonSurpriseTexture;
	private Font mMessageFont, mAuthorFont;
	private TextOptions mTextOptions;
	private EmoteButton mButtonLove, mButtonJoy, mButtonPeace, mButtonTrust, mButtonAdmiration, mButtonSurprise;
	private LinkedList<EmoteButton> mEmoteList;
	private Emote mCurrentEmote;
	private SharedPreferences   mPref;
	private float				mRateVal, mPitchVal, mSensitivityVal;
	private Music bgMusic;
	private SettingsScene mSettingsScene;
	private ITextureRegion mGearTexture;
	private ITiledTextureRegion mMusicTexture;
	private ButtonSprite mGearBtn;
	private TiledSprite mMusicBtn;
	private int mCurrentScene;
	private final int MAIN_SCENE = 0, SETTINGS_SCENE = 1;
	private int mMusicToggle;
	private final int TOGGLE_ON = 0, TOGGLE_OFF = 1;

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_HEIGHT);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		//mSensorManager.registerListener(HopeplusMainActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		final EngineOptions mEngineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_HEIGHT), mCamera);
		mEngineOptions.getAudioOptions().setNeedsMusic(true);
		
		return mEngineOptions;
		
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 1024, TextureOptions.DEFAULT);
		mTexture.load();
		
		mButtonTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
		mButtonTexture.load();

		mBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTexture, this, "background.png", 0,0 );
		
		mCrystalBallRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mButtonTexture, this, "crystalball.png", 0,0);
		
		FontFactory.setAssetBasePath("font/");
		final ITexture requiemFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.NEAREST);
		final ITexture requiemFontTexture2 = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.NEAREST);
		mMessageFont = FontFactory.createFromAsset(this.getFontManager	(), requiemFontTexture, this.getAssets(), "Requiem.ttf", CommonClass.FONT_SIZE_M, true, android.graphics.Color.rgb(218, 150, 50));
		mMessageFont.load();
		
		mAuthorFont = FontFactory.createFromAsset(this.getFontManager(), requiemFontTexture2, this.getAssets(), "Requiem.ttf", CommonClass.FONT_SIZE_S, true, android.graphics.Color.WHITE);
		mAuthorFont.load();
		
		mButtonLoveTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "button_love.png", 336, 0);
		mButtonJoyTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "button_joy.png", 336, 250);
		mButtonPeaceTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "button_peace.png", 336, 500);
		mButtonTrustTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "button_trust.png", 336, 750);
		mButtonAdmirationTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "button_admiration.png", 0, 400);
		mButtonSurpriseTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "button_surprise.png", 0, 650);
		mGearTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "gear.png", 0, 900);
		mMusicTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mButtonTexture, this, "music.png", 0,950, 2, 1);
		mTextOptions = new TextOptions(AutoWrap.WORDS, 320, Text.LEADING_DEFAULT, HorizontalAlign.CENTER);
		mMessage = new Text(80, 230, mMessageFont, "", 512, mTextOptions, this.getVertexBufferObjectManager());
		mAuthor = new Text(80, 450, mAuthorFont, "", 256, mTextOptions, this.getVertexBufferObjectManager());
		mLabel = new Text(80, 600, mMessageFont, "", 256, mTextOptions, this.getVertexBufferObjectManager());
		
		// Load setting preferences
		loadSettingsValues();

		MusicFactory.setAssetBasePath("mfx/");
		try {
			bgMusic = MusicFactory.createMusicFromAsset(this.getMusicManager(), this, "DST-FogOfPeace.mp3");
			bgMusic.setLooping(true);
		} catch (final IllegalStateException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		mGenerator = new HopeGenerator(this, mMessage, mAuthor);
		mGenerator.setTalkerAttributes(mPitchVal, mRateVal);
		
		mEmoteList = new LinkedList<EmoteButton>();
		
		mMessage.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		//mMessageModifier= new AlphaModifier(5, 0, 1.0f);
		
	}

	@Override
	public Scene onCreateScene() {
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();

		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		mScene = new Scene();	
		mScene.setColor(Color.BLACK);
		mBackgroundSprite = new Sprite(0,0, mBackgroundRegion, vertexBufferObjectManager);
		mBackground = new SpriteBackground(mBackgroundSprite);
		mScene.setBackground(mBackground);
		
		mCrystalBall = new ButtonSprite(72, 145, mCrystalBallRegion, vertexBufferObjectManager);
		mCrystalBall.setOnClickListener( new OnClickListener()
		{
		    public void onClick( ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY )
		    {
		    	// Do not change display while the talker is speaking
		    	if(!mGenerator.isTalkerSpeaking()) {
			    	if(mCurrentEmote.getLabel().equalsIgnoreCase("love")) {
			    		mCurrentEmote = Emote.JOY;
			    	}
			    	else if(mCurrentEmote.getLabel().equalsIgnoreCase("joy")) {
			    		mCurrentEmote = Emote.PEACE;
			    	}
			    	else if(mCurrentEmote.getLabel().equalsIgnoreCase("peace")) {
			    		mCurrentEmote = Emote.TRUST;
			    	}
			    	else if(mCurrentEmote.getLabel().equalsIgnoreCase("trust")) {
			    		mCurrentEmote = Emote.ADMIRATION;
			    	}
			    	else if(mCurrentEmote.getLabel().equalsIgnoreCase("admiration")) {
			    		mCurrentEmote = Emote.SURPRISE;
			    	}
			    	else if(mCurrentEmote.getLabel().equalsIgnoreCase("surprise")) {
			    		mCurrentEmote = Emote.LOVE;
			    	}

			    	displaySpheres(true);
		    		mLabel.setText(mCurrentEmote.getLabel().toUpperCase(Locale.getDefault()));
			    	mMessage.setText("");
			    	mAuthor.setText("");
		    	}
		    }      
		});
		
		// Create Settings Screen Instance
		mSettingsScene = new SettingsScene(this.mEngine, this, this.getTextureManager(), this.getFontManager(), vertexBufferObjectManager);
		mSettingsScene.loadSettingValues();
		
		mScene.attachChild(mCrystalBall);
		mScene.registerTouchArea(mCrystalBall);
		
		textModifierReset();
		mScene.attachChild(mMessage);
		
		mScene.attachChild(mAuthor);
		mScene.attachChild(mLabel);
		
		initSpheres();
		mGearBtn = new ButtonSprite(410, 20, mGearTexture, vertexBufferObjectManager);
		mGearBtn.setOnClickListener( new OnClickListener()
		{
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(mCurrentScene != SETTINGS_SCENE) {
					mEngine.setScene(mSettingsScene);
					mCurrentScene = SETTINGS_SCENE;
				}
			}
		});
		mGearBtn.setVisible(true);
		mGearBtn.setScale(0.75f);
		mScene.attachChild(mGearBtn);
		mScene.registerTouchArea(mGearBtn);

		mMusicToggle = TOGGLE_ON;
		mMusicBtn = new TiledSprite(350, 20, mMusicTexture, vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_UP:
					if(mMusicToggle == TOGGLE_ON) {
						mMusicToggle = TOGGLE_OFF;
						bgMusic.pause();
						mMusicBtn.setCurrentTileIndex(TOGGLE_OFF);
					}
					else {
						mMusicToggle = TOGGLE_ON;
						bgMusic.resume();
						mMusicBtn.setCurrentTileIndex(TOGGLE_ON);
					}
					break;
				default:
					break;
				}
				
				return true;
			}
		};
		mMusicBtn.setScale(0.75f);
		mScene.attachChild(mMusicBtn);
		mScene.registerTouchArea(mMusicBtn);
		
		bgMusic.play();
		
		mInitialized = false;
		mCurrentScene = MAIN_SCENE;
						
		return mScene;
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
	    if(pKeyCode == KeyEvent.KEYCODE_BACK &&
	    		pEvent.getAction() == KeyEvent.ACTION_DOWN) {
	    	if(mCurrentScene == MAIN_SCENE) {
	    		bgMusic.pause();
	    		showExitDialog();
	    	}
	    	else {
    			mSettingsScene.saveSettingValues();
    			//mSettingsScene.unregisterTouches();
	    		mEngine.setScene(mScene);
	    		mCurrentScene = MAIN_SCENE;
	    		mGenerator.setTalkerAttributes(mSettingsScene.getPitch(), mSettingsScene.getRate());
	    		mGenerator.setTalker();
    			return true;
	    	}
	    }
	    return super.onKeyDown(pKeyCode, pEvent);
	}
	
	public void showExitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HopeplusMainActivity.this);
		builder.setMessage("Are you sure you want to Exit?").setCancelable(false).setTitle("Exit").setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						bgMusic.stop();
						mGenerator.close();
						mSensorManager.unregisterListener(HopeplusMainActivity.this);
						HopeplusMainActivity.this.finish();
					}
				}).setNegativeButton("No", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(mMusicToggle == TOGGLE_ON) {
							if(!bgMusic.isPlaying())
								bgMusic.resume();
							dialog.cancel();
						}
					}
				});
		
		AlertDialog mAlert = builder.create();
		mAlert.setIcon(R.drawable.icon);
		mAlert.show();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
	
		if(!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		}
		else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			
			if(deltaX < mSensitivityVal) deltaX = 0.0f;
			if(deltaY < mSensitivityVal) deltaY = 0.0f;
			if(deltaZ < mSensitivityVal) deltaZ = 0.0f;

			mLastX = x;
			mLastY = y;
			mLastZ = z;

			if(deltaX != deltaY) {
				displaySpheres(false);
				mGenerator.getHope(mCurrentEmote.getLabel());
				textModifierReset();
			}
		}
	}
	
	@Override
	public void onPause() {
		mSensorManager.unregisterListener(HopeplusMainActivity.this);
		mGenerator.close();
		if(bgMusic.isPlaying())
			bgMusic.pause();
		super.onPause();
	}
	
	@Override
	public void onResumeGame() {
		mSensorManager.registerListener(HopeplusMainActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mGenerator.resumeResources();
		if(!bgMusic.isPlaying())
			bgMusic.resume();
		super.onResumeGame();
	}
	
	
	/*
	 * @brief This method adds the runes to a LinkedList
	 * 			to help managing them.
	 */
	@SuppressLint("DefaultLocale")
	public void initSpheres() {

		// Create the EmoteButtons
		mButtonLove = new EmoteButton(120, 190, "love", mButtonLoveTexture, this.getVertexBufferObjectManager());
		mScene.attachChild(mButtonLove);
		mButtonLove.setVisible(false);
		mEmoteList.add(mButtonLove);

		mButtonJoy = new EmoteButton(120, 190, "joy", mButtonJoyTexture, this.getVertexBufferObjectManager());
		mScene.attachChild(mButtonJoy);
		mButtonJoy.setVisible(false);
		mEmoteList.add(mButtonJoy);

		mButtonPeace = new EmoteButton(120, 190, "peace", mButtonPeaceTexture, this.getVertexBufferObjectManager());
		mScene.attachChild(mButtonPeace);
		mButtonPeace.setVisible(false);
		mEmoteList.add(mButtonPeace);

		mButtonTrust = new EmoteButton(120, 190, "trust", mButtonTrustTexture, this.getVertexBufferObjectManager());
		mScene.attachChild(mButtonTrust);
		mButtonTrust.setVisible(false);
		mEmoteList.add(mButtonTrust);

		mButtonAdmiration = new EmoteButton(120, 190, "admiration", mButtonAdmirationTexture, this.getVertexBufferObjectManager());
		mScene.attachChild(mButtonAdmiration);
		mButtonAdmiration.setVisible(false);
		mEmoteList.add(mButtonAdmiration);

		mButtonSurprise = new EmoteButton(120, 190, "surprise", mButtonSurpriseTexture, this.getVertexBufferObjectManager());
		mScene.attachChild(mButtonSurprise);
		mButtonSurprise.setVisible(false);
		mEmoteList.add(mButtonSurprise);
		
		//Set default emote to Love
		mButtonLove.setVisible(true);
		mCurrentEmote = Emote.LOVE;
		mLabel.setText(mCurrentEmote.getLabel().toUpperCase(Locale.getDefault()));
		
	}
	
	public void displaySpheres(boolean visible) {
		Iterator<EmoteButton> runes = mEmoteList.iterator();
		EmoteButton _emote_b;
		
		while(runes.hasNext()) {
			_emote_b = runes.next();
			
			_emote_b.setVisible(false);
			if(visible) {
				// show current sphere
				if(_emote_b.getSentiment().equalsIgnoreCase(mCurrentEmote.getLabel()))
					_emote_b.setVisible(true);
			}
		}
	}

	protected void textModifierReset() {
		if(!mGenerator.isTalkerSpeaking()) {
			mMessage.clearEntityModifiers();
			mMessage.registerEntityModifier(new AlphaModifier(4,0,1.0f));
		}
	}
	
	private void loadSettingsValues() {
		mPref = getSharedPreferences(CommonClass.PREFERENCES_STRING, Context.MODE_PRIVATE);
		
		mRateVal = mPref.getFloat("Rate", CommonClass.SPEECH_RATE_L);
		mPitchVal = mPref.getFloat("Pitch", CommonClass.PITCH_LVL_M);
		mSensitivityVal = mPref.getFloat("Sensitivity", CommonClass.SENSITIVITY_MID);
	}

}
