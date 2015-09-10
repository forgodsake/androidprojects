package com.google.wash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class VerifyActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.verify);
		Button goButton=(Button) findViewById(R.id.gobutton);
		goHome(goButton);
		ImageView back=(ImageView) findViewById(R.id.ivAppBack);
		goHome(back);
	}
	
	private void goHome(View v){
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(VerifyActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
