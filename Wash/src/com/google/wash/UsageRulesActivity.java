package com.google.wash;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UsageRulesActivity extends Activity{
    
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.usage_rules);
	    ViewUtils.inject(this);
	    
	}
	
	@OnClick(R.id.ivBack)
	private void back(View v){
		finish();
	}
	
}
