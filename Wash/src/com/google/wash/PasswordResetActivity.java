package com.google.wash;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.wash.entity.Password;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PasswordResetActivity extends Activity {
    
	@ViewInject(R.id.edit_password)
	private Password edit;
	@ViewInject(R.id.confirm_password)
	private Password confirm;
	@ViewInject(R.id.button_confirm)
	private Button confirm_bt;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.password_pay_reset);
	    ViewUtils.inject(this);
	}
	@OnClick({R.id.ivBack,R.id.button_confirm})
	private void click(View v){
		switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
		case R.id.button_confirm:
			
			String pass1 = edit.getTextValue();
			String pass2 = confirm.getTextValue();
			if (pass1.equals(pass2)&&!pass1.equals("")) {
				Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
				finish();
			}else {
				Toast.makeText(this, "密码修改失败", Toast.LENGTH_SHORT).show();
			}
		default:
			break;
		}
		
	}
	
	
}
