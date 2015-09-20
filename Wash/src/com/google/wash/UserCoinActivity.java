package com.google.wash;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UserCoinActivity extends Activity{
    
	@ViewInject(R.id.edittext_charge)
	private EditText edit_charge;
	@ViewInject(R.id.button_charge)
	private Button button_charge;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.my_wash_coin);
	    ViewUtils.inject(this);
	}
	@OnClick({R.id.ivBack,R.id.button_charge})
	private void click(View v){
		switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
		case R.id.button_charge:
			if (edit_charge.getText().toString().length()>0) {
				Toast.makeText(this, "充值成功", Toast.LENGTH_SHORT).show();
				finish();
			}else {
				Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
