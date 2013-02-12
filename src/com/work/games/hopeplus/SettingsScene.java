package com.work.games.hopeplus;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;

import com.work.games.common.CommonClass;

public class SettingsScene extends Scene {
	
	private VertexBufferObjectManager mVbo;
	private TextureManager		mTextureManager;
	private FontManager			mFontManager;
	private Context				mContext;
	private Engine				mEngine;

	private ITextureRegion		mBackgroundRegion;
	private ITiledTextureRegion mTiledRegion;
	private BitmapTextureAtlas	mBackgroundTexture, mSettingsTexture;
	private Sprite				mBackgroundSprite;
	private SpriteBackground	mBackground;
	private TiledSprite			mRateBtn, mPitchBtn, mSensitivityBtn;
	private Font 				mTextFont;
	private TextOptions			mTextOptions;
	private Text				mRateLbl, mPitchLbl, mSensitivityLbl;
	private float				mRateVal, mPitchVal, mSensitivityVal;
		
	public SettingsScene(Engine pEngine, Context pContext, TextureManager pTextureManager, FontManager pFontMgr, VertexBufferObjectManager pVbo) {
		mEngine = pEngine;
		mTextureManager = pTextureManager;
		mFontManager = pFontMgr;
		mContext = pContext;
		mVbo = pVbo;
		
		initialize();
	}
	
	private void initialize() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("font/");

		// Set background
		mBackgroundTexture = new BitmapTextureAtlas(mTextureManager, 512, 1024, TextureOptions.DEFAULT);
		mBackgroundTexture.load();		
		mBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTexture, mContext, "settings_bg.png", 0,0 );
		mBackgroundSprite = new Sprite(0,0, mBackgroundRegion, mVbo);
		mBackground = new SpriteBackground(mBackgroundSprite);
		this.setBackground(mBackground);
		
		// Setup Fonts
		final ITexture requiemFontTexture = new BitmapTextureAtlas(mTextureManager, 256, 256, TextureOptions.NEAREST);
		mTextFont = FontFactory.createFromAsset(mFontManager, requiemFontTexture, mContext.getAssets(), "Requiem.ttf", CommonClass.FONT_SIZE_M, true, android.graphics.Color.rgb(220, 150, 50));
		mTextFont.load();
		mTextOptions = new TextOptions();
		mRateLbl = new Text(50, 210, mTextFont, "Speech Rate", 20, mTextOptions, mVbo);
		mPitchLbl = new Text(50, 310, mTextFont, "Pitch", 20, mTextOptions, mVbo);
		mSensitivityLbl = new Text(50, 410, mTextFont, "Sensitivity", 20, mTextOptions, mVbo);
				
		mSettingsTexture = new BitmapTextureAtlas(mTextureManager, 512, 512, TextureOptions.DEFAULT);
		mSettingsTexture.load();
		mTiledRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSettingsTexture, mContext, "lomihi.png", 0,0,3,1);
		
		mRateBtn = new TiledSprite(300, 180, mTiledRegion, mVbo) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_UP:
					if(mRateVal == CommonClass.SPEECH_RATE_L) {
						mRateVal = CommonClass.SPEECH_RATE_M;
						mRateBtn.setCurrentTileIndex(CommonClass.SETTING_MID);
					}
					else if(mRateVal == CommonClass.SPEECH_RATE_M) {
						mRateVal = CommonClass.SPEECH_RATE_H;
						mRateBtn.setCurrentTileIndex(CommonClass.SETTING_HI);
					}
					else if(mRateVal == CommonClass.SPEECH_RATE_H) {
						mRateVal = CommonClass.SPEECH_RATE_L;
						mRateBtn.setCurrentTileIndex(CommonClass.SETTING_LOW);
					}
					break;
				default:
					break;
				}
				
				return true;
			}
		};
		this.registerTouchArea(mRateBtn);
		
		mPitchBtn = new TiledSprite(300, 280, mTiledRegion.deepCopy(), mVbo) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_UP:
					if(mPitchVal == CommonClass.PITCH_LVL_S) {
						mPitchVal = CommonClass.PITCH_LVL_M;
						mPitchBtn.setCurrentTileIndex(CommonClass.SETTING_MID);
					}
					else if(mPitchVal == CommonClass.PITCH_LVL_M) {
						mPitchVal = CommonClass.PITCH_LVL_H;
						mPitchBtn.setCurrentTileIndex(CommonClass.SETTING_HI);
					}
					else if(mPitchVal == CommonClass.PITCH_LVL_H) {
						mPitchVal = CommonClass.PITCH_LVL_S;
						mPitchBtn.setCurrentTileIndex(CommonClass.SETTING_LOW);
					}
					break;
				default:
					break;
				}
				
				return true;
			}
		};
		this.registerTouchArea(mPitchBtn);

		mSensitivityBtn = new TiledSprite(300, 380, mTiledRegion.deepCopy(), mVbo) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_UP:
					if(mSensitivityVal == CommonClass.SENSITIVITY_LOW) {
						mSensitivityVal = CommonClass.SENSITIVITY_MID;
						mSensitivityBtn.setCurrentTileIndex(CommonClass.SETTING_MID);
					}
					else if(mSensitivityVal == CommonClass.SENSITIVITY_MID) {
						mSensitivityVal = CommonClass.SENSITIVITY_HIGH;
						mSensitivityBtn.setCurrentTileIndex(CommonClass.SETTING_HI);
					}
					else if(mSensitivityVal == CommonClass.SENSITIVITY_HIGH) {
						mSensitivityVal = CommonClass.SENSITIVITY_LOW;
						mSensitivityBtn.setCurrentTileIndex(CommonClass.SETTING_LOW);
					}
					break;
				default:
					break;
				}
				
				return true;
			}
		};
		this.registerTouchArea(mSensitivityBtn);
				
		this.attachChild(mRateLbl);
		this.attachChild(mRateBtn);
		this.attachChild(mPitchLbl);
		this.attachChild(mPitchBtn);
		this.attachChild(mSensitivityLbl);
		this.attachChild(mSensitivityBtn);
	}

	public void setSettings(float pPitch, float pRate, float pSensitivity) {
		mPitchVal = pPitch;
		mRateVal = pRate;
		mSensitivityVal = pSensitivity;
		updateValues();
	}
	
	public float getPitch() {
		return mPitchVal;
	}
	
	public float getRate() {
		return mRateVal;
	}

	public float getSensitivity() {
		return mSensitivityVal;
	}
	
	private void updateValues() {
		// Pitch
		if(mPitchVal == CommonClass.PITCH_LVL_S) {
			mPitchBtn.setCurrentTileIndex(CommonClass.SETTING_LOW);
		}
		else if(mPitchVal == CommonClass.PITCH_LVL_M) {
			mPitchBtn.setCurrentTileIndex(CommonClass.SETTING_MID);
		}
		else if(mPitchVal == CommonClass.PITCH_LVL_H) {
			mPitchBtn.setCurrentTileIndex(CommonClass.SETTING_HI);
		}
		
		// Rate
		if(mRateVal == CommonClass.SPEECH_RATE_L) {
			mRateBtn.setCurrentTileIndex(CommonClass.SETTING_LOW);
		}
		else if(mRateVal == CommonClass.SPEECH_RATE_M) {
			mRateBtn.setCurrentTileIndex(CommonClass.SETTING_MID);
		}
		else if(mRateVal == CommonClass.SPEECH_RATE_H) {
			mRateBtn.setCurrentTileIndex(CommonClass.SETTING_HI);
		}
		
		// Sensitivity
		if(mSensitivityVal == CommonClass.SENSITIVITY_LOW) {
			mSensitivityBtn.setCurrentTileIndex(CommonClass.SETTING_LOW);
		}
		else if(mSensitivityVal == CommonClass.SENSITIVITY_MID) {
			mSensitivityBtn.setCurrentTileIndex(CommonClass.SETTING_MID);
		}
		else if(mSensitivityVal == CommonClass.SENSITIVITY_HIGH) {
			mSensitivityBtn.setCurrentTileIndex(CommonClass.SETTING_HI);
		}
	}
	
	public void unregisterTouches() {
		this.unregisterTouchArea(mSensitivityBtn);
		this.unregisterTouchArea(mPitchBtn);
		this.unregisterTouchArea(mRateBtn);
	}
}
