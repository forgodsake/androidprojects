package com.google.wash;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ServiceScopeActivity extends Activity {
    
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_scope);
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.ivBack)
	private void click(View view){
		Intent intent = new Intent(ServiceScopeActivity.this,MainActivity.class);
		startActivity(intent);
		finish();
	}
	
}
