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

public class CouponCodeVerifyActivity extends Activity{
    
	@ViewInject(R.id.editText_verify)
	private EditText edit_verify;
	@ViewInject(R.id.button_confirm)
	private Button button;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.coupon_code);
	    ViewUtils.inject(this);
	}
	@OnClick({R.id.ivBack,R.id.button_confirm})
	private void click(View v){
		switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
		case R.id.button_confirm:
			if (edit_verify.getText().toString().length()==6) {
				Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show();
				finish();
			}else {
				Toast.makeText(this, "请输入六位优惠码", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
