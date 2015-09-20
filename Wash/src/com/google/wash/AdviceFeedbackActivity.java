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

public class AdviceFeedbackActivity extends Activity{
    
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@ViewInject(R.id.editText_advice)
	private EditText advice;
	@ViewInject(R.id.button_feedback)
	private Button feedback;
	private String content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.advice_feedback);
	    ViewUtils.inject(this);
	}
	@OnClick({R.id.ivBack,R.id.button_feedback})
	private void click(View v){
		switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
		case R.id.button_feedback:
			content = advice.getText().toString();
			if (!content.equals("")) {
				Toast.makeText(this, "感谢你的反馈，我们会更加努力，为您提供优质服务！", Toast.LENGTH_SHORT).show();
			    finish();
			}else {
				Toast.makeText(this, "请输入反馈信息！", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
