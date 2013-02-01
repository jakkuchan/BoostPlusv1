package com.work.games.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {

	private Context mContext;
	public final String NETWORK_UNAVAILABLE = "Internet Connection is required.";
	
	public NetworkManager(Context context) {
		this.mContext = context;
	}
	
	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		return activeNetwork != null;
	}
}
