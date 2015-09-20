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

public class UserCoinPasswordChangeActivity extends Activity{
    
	@ViewInject(R.id.editText_old)
	private EditText edit_old;
	@ViewInject(R.id.editText_new)
	private EditText edit_new;
	@ViewInject(R.id.editText_newagain)
	private EditText edit_newagain;
	@ViewInject(R.id.button_save)
	private Button bt_save;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.password_change);
	    ViewUtils.inject(this);
	}
	@OnClick({R.id.ivBack,R.id.button_save})
	private void click(View v){
		switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
		case R.id.button_save:
			String pass_old = edit_old.getText().toString();
			String pass_new = edit_new.getText().toString();
			String pass_newagain = edit_newagain.getText().toString();
			if (!pass_old.equals(pass_new)&&pass_new.equals(pass_newagain)&&!pass_old.equals("")&&!pass_new.equals("")) {
				Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
				finish();
			}else if(pass_new.equals("")||pass_newagain.equals("")||pass_old.equals("")){
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			}else if (!pass_new.equals(pass_newagain)) {
				Toast.makeText(this, "新密码两次输入不一致", Toast.LENGTH_SHORT).show();
			}else if(pass_old.equals(pass_new)||pass_old.equals(pass_newagain)&&!pass_old.equals("")){
				Toast.makeText(this, "新旧密码不能相同", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
} 
