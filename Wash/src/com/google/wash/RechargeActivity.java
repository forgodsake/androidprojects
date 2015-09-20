package com.google.wash;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RechargeActivity extends Activity {
    
	@ViewInject(R.id.edittext_charge)
	private EditText edit_charge;
	@ViewInject(R.id.button_charge)
	private Button button_charge;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@ViewInject(R.id.tv_change_password)
	private TextView tvChange;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge);
		 ViewUtils.inject(this);
	}
	@OnClick({R.id.tv_change_password,R.id.ivBack,R.id.button_charge})
	private void click(View v){
		switch (v.getId()) {
		case R.id.tv_change_password:
			startActivity(new Intent(RechargeActivity.this,UserCoinPasswordActivity.class));
			break;
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
