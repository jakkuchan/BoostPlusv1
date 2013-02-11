package com.work.games.hopeplus;

import java.util.Locale;

import org.andengine.entity.text.Text;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import com.work.games.common.NetworkManager;
import com.work.games.webservice.WebServiceTask;

public class HopeGenerator implements OnInitListener {
	private Context mContext;
	private TextToSpeech mTalker = null;	
	private Text mMessageText;
	private Text mAuthorText;
	private String mMessageString;
	private String mAuthorString;
	private WebServiceTask mWst;
	private NetworkManager mNetMgr;
	private float mRate, mPitch;
	
	public HopeGenerator(Context context, Text message, Text author) {
		this.mContext = context;
		this.mMessageText = message;
		this.mAuthorText = author;
		this.mNetMgr = new NetworkManager(mContext);
	}
	
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			
            int result = mTalker.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
            		|| result == TextToSpeech.LANG_NOT_SUPPORTED) {                
            	Log.e("TTS", "This Language is not supported");
            } else {
            	Log.i("TTS", "TTS Engine has been initialized");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }		
	}
	
	public void close() {
		if(mTalker != null) {
			mTalker.stop();
			mTalker.shutdown();
		}
		
		if(mWst != null)
			mWst.cancel(true);
	}

	public Context getContext() {
		return this.mContext;
	}
	
	public void getHope(String sentiment) {
		this.mWst = new WebServiceTask(this, sentiment);

		if(mNetMgr.isNetworkAvailable()) {
			mWst.execute();
		}
		else {
			mMessageText.setText(mNetMgr.NETWORK_UNAVAILABLE);			
		}
	}
	
	public void outMessage() {
		if(!mTalker.isSpeaking()) {
			mTalker.speak(mMessageString, TextToSpeech.QUEUE_FLUSH, null);
			mMessageText.setText(mMessageString);
			mAuthorText.setText(mAuthorString);
		}
	}
	
	public void setMessageString(String message) {
		this.mMessageString = message;
	}
	
	public void setAuthorString(String author) {
		this.mAuthorString = author;
	}
	
	public boolean isTalkerSpeaking() {
		return mTalker.isSpeaking();
	}
	
	public TextToSpeech getTalker() {
		return mTalker;
	}

	public void resumeResources() {
		// close previous tts object
		if(mTalker != null)
			mTalker.shutdown();
		mTalker = new TextToSpeech(mContext, this);
		mTalker.setPitch(mPitch);
		mTalker.setSpeechRate(mRate);
	}
	
	public void setTalker(float pPitch, float pRate) {
		mPitch = pPitch;
		mRate = pRate;
	}
}
