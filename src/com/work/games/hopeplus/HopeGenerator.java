package com.work.games.hopeplus;

import org.andengine.entity.text.Text;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import com.work.games.common.NetworkManager;
import com.work.games.webservice.WebServiceTask;

public class HopeGenerator implements OnInitListener {
	private Context mContext;
	private TextToSpeech mTalker;	
	private Text mMessageText;
	private Text mAuthorText;
	private String mMessageString;
	private String mAuthorString;
	private WebServiceTask mWst;
	private NetworkManager mNetMgr;
	
	public HopeGenerator(Context context, Text message, Text author) {
		this.mContext = context;
		this.mMessageText = message;
		this.mAuthorText = author;
		this.mNetMgr = new NetworkManager(mContext);
		resumeResources();
	}
	
	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void close() {
		mTalker.shutdown();
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
		this.mTalker = new TextToSpeech(mContext, this);

	}
}
