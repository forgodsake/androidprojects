package com.google.wash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UserCoinPasswordActivity extends Activity{
    
	@ViewInject(R.id.button_quit)
	private Button quit;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@ViewInject(R.id.rl_password_change)
	private RelativeLayout rl_change;
	@ViewInject(R.id.rl_password_reset)
	private RelativeLayout rl_reset;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.password_manage);
	    ViewUtils.inject(this);
	}
	@OnClick({R.id.rl_password_change,R.id.rl_password_reset,R.id.ivBack,R.id.button_quit})
	private void click(View v){
		switch (v.getId()) {
		case R.id.rl_password_change:
			startActivity(new Intent(UserCoinPasswordActivity.this,UserCoinPasswordChangeActivity.class));
			finish();
			break;
		case R.id.rl_password_reset:
			startActivity(new Intent(UserCoinPasswordActivity.this,UserCoinPasswordResetActivity.class));
			finish();
			break;
		case R.id.ivBack:
			finish();
			break;
		case R.id.button_quit:
			startActivity(new Intent(UserCoinPasswordActivity.this,VerifyActivity.class));
			finish();
			break;
		default:
			break;
		}
	}
	
}
