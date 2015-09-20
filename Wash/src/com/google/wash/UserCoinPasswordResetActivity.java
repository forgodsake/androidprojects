package com.google.wash;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UserCoinPasswordResetActivity extends Activity{
    
	@ViewInject(R.id.editText_phone)
	private EditText edit_phone;
	@ViewInject(R.id.editText_code)
	private EditText edit_code;
	@ViewInject(R.id.editText_new)
	private EditText edit_new;
	@ViewInject(R.id.editText_newagain)
	private EditText edit_newagain;
	@ViewInject(R.id.button_getcode)
	private Button bt_getcode;
	@ViewInject(R.id.button_save)
	private Button bt_save;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.password_reset);
	    ViewUtils.inject(this);
	}
	@OnClick({R.id.ivBack,R.id.button_getcode,R.id.button_save})
	private void click(View v){
		switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
		case R.id.button_getcode:
			String phonenum = edit_phone.getText().toString();
			if (phonenum.equals("")) {
				Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
			}else{
			edit_code.setText("48s2");
			}
			break;
		case R.id.button_save:
			String phone = edit_phone.getText().toString();
			String code = edit_code.getText().toString();
			String pass_new = edit_new.getText().toString();
			String pass_newagain = edit_newagain.getText().toString();
			if (phone.equals("")) {
				Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
			}else if (code.equals("")) {
				Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			}else if (pass_new.equals(pass_newagain)&&!pass_new.equals("")) {
				Toast.makeText(this, "重置成功", Toast.LENGTH_SHORT).show();
				finish();
			}else if(pass_new.equals("")||pass_newagain.equals("")){
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			}else if (!pass_new.equals(pass_newagain)) {
				Toast.makeText(this, "新密码两次输入不一致", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
} 
