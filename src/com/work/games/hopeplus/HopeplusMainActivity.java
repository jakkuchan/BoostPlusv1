package com.work.games.hopeplus;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import com.work.games.common.CommonClass;
import com.work.games.common.EmoteButton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;



public class HopeplusMainActivity extends SimpleBaseGameActivity implements SensorEventListener {

	private Camera mCamera;
	private Scene mScene;
	private BitmapTextureAtlas mTexture;
	private ITextureRegion mLogoRegion;
	private Sprite mLogoSprite;
	private SpriteBackground mBackground;
	private boolean mInitialized;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private float mLastX, mLastY, mLastZ;
	private HopeGenerator mGenerator;
	private BitmapTextureAtlas mButtonTexture;
	private Text mMessage, mAuthor;
	private ITextureRegion mButtonLoveTexture, mButtonJoyTexture;
	private Font mSmallFont;
	private TextOptions mTextOptions;
	private EmoteButton mButtonLove, mButtonJoy;

			
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_HEIGHT);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(HopeplusMainActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		final EngineOptions mEngineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CommonClass.PORTRAIT_CAMERA_WIDTH, CommonClass.PORTRAIT_CAMERA_HEIGHT), mCamera);				
		return mEngineOptions;
		
	}

	
	@Override
	public void onCreateResources() {		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 1024, TextureOptions.DEFAULT);
		mTexture.load();
		
		mButtonTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.DEFAULT);
		mButtonTexture.load();

		mLogoRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mTexture, this, "board.png", 0,0);
		
		mSmallFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24);
		mSmallFont.load();

		mButtonLoveTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "button_love.png", 0, 0);
		mButtonJoyTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonTexture, this, "button_joy.png", 60, 0);
		mTextOptions = new TextOptions(AutoWrap.WORDS, 350, Text.LEADING_DEFAULT, HorizontalAlign.CENTER);
		mMessage = new Text(55, 120, mSmallFont, "", 512, mTextOptions, this.getVertexBufferObjectManager());
		mAuthor = new Text(55, 350, mSmallFont, "", 256, mTextOptions, this.getVertexBufferObjectManager());
		
		mGenerator = new HopeGenerator(this, mMessage, mAuthor);

		
		
	}

	@Override
	public Scene onCreateScene() {
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		mScene = new Scene();	
		mScene.setColor(Color.BLACK);
		
		final float centerX = 0;
		final float centerY = 0;
		
		mLogoSprite = new Sprite(centerX, centerY, mLogoRegion, vertexBufferObjectManager);
		mBackground = new SpriteBackground(mLogoSprite);
		
		mScene.setBackground(mBackground);
		
		mScene.attachChild(mMessage);
		mScene.attachChild(mAuthor);

		// Create the EmoteButtons
		mButtonLove = new EmoteButton(25, 450, "love", mButtonLoveTexture, this.getVertexBufferObjectManager());
		mScene.attachChild(mButtonLove);
		mScene.registerTouchArea(mButtonLove);

		mButtonJoy = new EmoteButton(95, 450, "joy", mButtonJoyTexture, this.getVertexBufferObjectManager());
		mScene.attachChild(mButtonJoy);
		mScene.registerTouchArea(mButtonJoy);

		mInitialized = false;
		return mScene;
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
	    if(pKeyCode == KeyEvent.KEYCODE_BACK &&
	    		pEvent.getAction() == KeyEvent.ACTION_DOWN) {
	    	showExitDialog();
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
						mSensorManager.unregisterListener(HopeplusMainActivity.this);
						HopeplusMainActivity.this.finish();
						mGenerator.close();
					}
				}).setNegativeButton("No", 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
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
			
			if(deltaX < CommonClass.SENSITIVITY_MID) deltaX = 0.0f;
			if(deltaY < CommonClass.SENSITIVITY_MID) deltaY = 0.0f;
			if(deltaZ < CommonClass.SENSITIVITY_MID) deltaZ = 0.0f;

			mLastX = x;
			mLastY = y;
			mLastZ = z;

			if(deltaX != deltaY) {
				mGenerator.getHope("love");
			}
		}
	}

}
