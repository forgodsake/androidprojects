package com.google.wash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class VerifyActivity extends Activity {
	
	private TimeCount count;
	@ViewInject(R.id.editText_phone)
	private EditText phoneNumber;
	@ViewInject(R.id.editText_code)
	private EditText phoneCode;
	@ViewInject(R.id.textView_countdown)
	private TextView countDown;
	@ViewInject(R.id.textView_state)
	private TextView textState;
	private  EventHandler eh;
	private  String number,code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.verify);
		ViewUtils.inject(this);
		
		count = new TimeCount(60000, 1000);//构造CountDownTimer对象
		Button goButton=(Button) findViewById(R.id.gobutton);
		ImageView back=(ImageView) findViewById(R.id.ivAppBack);
		
		textState.setTextSize(12);
		SpannableStringBuilder spannableStringBuilder=new SpannableStringBuilder("点击“手机绑定登陆”,即表示同意《超速洗用户协议》");
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#28ccfc"));  
		spannableStringBuilder.setSpan(new URLSpan("http://www.baidu.com"), 16, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableStringBuilder.setSpan(blueSpan, 16, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textState.setMovementMethod(LinkMovementMethod.getInstance());
		textState.setText(spannableStringBuilder);
		
		
	}
    
	@OnClick({R.id.gobutton,R.id.ivAppBack,R.id.textView_countdown})
	private void click(View v){
		number = phoneNumber.getText().toString();
		code = phoneCode.getText().toString();
		switch (v.getId()) {
		case R.id.gobutton:
			
			if (number.equals("")) {
				Toast.makeText(this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
			}else if (number.length()!=11) {
				Toast.makeText(this, "您输入的手机号码有误，请重新输入！", Toast.LENGTH_SHORT).show();
			}else{
				eh=new EventHandler(){
					 
		            @Override
		            public void afterEvent(final int event, final int result, final Object data) {
		            	runOnUiThread(new Runnable() {
							public void run() {
								
								if (result == SMSSDK.RESULT_COMPLETE) {
									if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					                	Toast.makeText(VerifyActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
					                	startActivity(new Intent(VerifyActivity.this,MainActivity.class));
					                	finish();
					                //提交验证码成功
					                }
								} else {
									Toast.makeText(VerifyActivity.this, "获取验证失败", Toast.LENGTH_SHORT).show();
								    //验证失败
									return;
								}
							}
						});
		            }
		        }; 
		       SMSSDK.registerEventHandler(eh); //注册短信回调
			   SMSSDK.submitVerificationCode("86",number,code);
			}
			break;
		case R.id.ivAppBack:
			finish();
			break;
		case R.id.textView_countdown:
			
			if (number.equals("")) {
				Toast.makeText(this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
			}else if (number.length()!=11) {
				Toast.makeText(this, "您输入的手机号码有误，请重新输入！", Toast.LENGTH_SHORT).show();
			}else {
				count.start();
				SMSSDK.getVerificationCode("86", number);
			}
			break;
		default:
			break;
		}
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
		    countDown.setText("重新获取验证码");
		    countDown.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			countDown.setClickable(false);
			countDown.setText(millisUntilFinished /1000+"秒后重新获取");
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (eh!=null) {
			eh.onUnregister();
		}
	}
}
