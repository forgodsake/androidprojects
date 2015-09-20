package com.google.wash;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@SuppressWarnings("deprecation")
public class Verify_net_Activity extends Activity{
    
	private TimeCount count;
	@ViewInject(R.id.editText_phone)
	private EditText phoneNumber;
	@ViewInject(R.id.editText_code)
	private EditText phoneCode;
	@ViewInject(R.id.textView_countdown)
	private TextView countDown;
	@ViewInject(R.id.textView_state)
	private TextView textState;
	private  String number,verifycode;
	NotificationManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.verify);
		ViewUtils.inject(this);
		
	    manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
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
		
		countDown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				number = phoneNumber.getText().toString();
				if (number.equals("")) {
					Toast.makeText(Verify_net_Activity.this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
				}else if (number.length()!=11) {
					Toast.makeText(Verify_net_Activity.this, "您输入的手机号码有误，请重新输入！", Toast.LENGTH_SHORT).show();
				}else {
					count.start();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							getverifycode();
							Message msg = new Message();
							msg.what = 1;
							handler.sendMessage(msg);
						}
					}).start();
				}
				
			}
		});
	}
    
	@OnClick({R.id.gobutton,R.id.ivAppBack})
	private void click(View v){
		number = phoneNumber.getText().toString();
		switch (v.getId()) {
		case R.id.gobutton:
			
			if (number.equals("")) {
				Toast.makeText(this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
			}else if (number.length()!=11) {
				Toast.makeText(this, "您输入的手机号码有误，请重新输入！", Toast.LENGTH_SHORT).show();
			}else{
				startActivity(new Intent(Verify_net_Activity.this,MainActivity.class));
			}
			break;
		case R.id.ivAppBack:
			finish();
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
	//从网络端获取验证码
	private void getverifycode(){
		HttpClient client = new DefaultHttpClient();
		String uri = "http://192.168.1.101:8080/demo/myClive?phone=13060372723";
		HttpGet get = new HttpGet(uri);
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				verifycode = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Notification notification = new Notification.Builder(Verify_net_Activity.this)
                .setTicker("您的验证码是。。。")
                .setContentTitle("验证码如下：")
                .setContentText(verifycode)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .build();
				manager.notify(1, notification);
				break;
			default:
				break;
		  }
		}	
	};
	
}
