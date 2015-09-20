package com.google.wash;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAddressActivity extends Activity {
    
	@ViewInject(R.id.textView_name)
    private TextView text_name;	
	@ViewInject(R.id.textView_phonenum)
	private TextView text_phone;
	@ViewInject(R.id.textView_address)
	private TextView text_address;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@ViewInject(R.id.tv_change_address)
	private TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.user_address);
	    ViewUtils.inject(this);
	    Intent  intent = getIntent();
	    String name = intent.getStringExtra("name");
	    String phonenum = intent.getStringExtra("phonenum");
	    String address = intent.getStringExtra("address");
	    if (name!=null) {
			text_name.setText(name);
			text_phone.setText(phonenum);
			text_address.setText(address);
		}
	    
	}
	@OnClick({R.id.tv_change_address,R.id.ivBack})
	private void click(View v){
		switch (v.getId()) {
		case R.id.tv_change_address:
			startActivity(new Intent(UserAddressActivity.this,UserAddressModifyActivity.class));
			finish();
			break;
		case R.id.ivBack:
            finish();
			break;
		default:
			break;
		}
	}
	
}
