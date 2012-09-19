package com.zhaoyunhe.triple;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ApplicationListener gameView=new Triple();
		AndroidApplicationConfiguration config=new AndroidApplicationConfiguration();
		config.useGL20=false;
		config.numSamples=2;
		initialize(gameView, config);
		
		DisplayMetrics mDisplayMetrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

		Log.d("d", mDisplayMetrics.widthPixels+":"+mDisplayMetrics.heightPixels);
	}

}
