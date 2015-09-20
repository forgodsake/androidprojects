package com.google.wash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;

public class OpenActivity extends Activity{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.open);
        new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(OpenActivity.this,WelcomeActivity.class));
			    finish();
			}
		}, 2000);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
	
}
