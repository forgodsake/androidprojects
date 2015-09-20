package com.google.wash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.wash.entity.Password;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PayActivity extends Activity {
    
	@ViewInject(R.id.editTextPassword)
	private Password pass;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@ViewInject(R.id.button_confirm)
	private Button confirm;
	@ViewInject(R.id.button_reset)
	private Button button;
	@ViewInject(R.id.button_charge)
	private Button button_charge;
	@ViewInject(R.id.editTextPassword)
	private Password password;
	@ViewInject(R.id.ll_pay)
	private LinearLayout ll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		ViewUtils.inject(this);
		controlKeyboardLayout(ll,password);
	}
	
	private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                Log.i("KeitLog ","invisibleHeight:  "+rootInvisibleHeight);
                //若不可视区域高度大于100，则键盘显示


                //获取scrollToView在窗体的坐标
                Log.i("KeitLog ","rootInvisibleHeight > 100  "+String.valueOf(rootInvisibleHeight > 100));
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    Log.i("keitLog", " x=" + location[0] + "   y=" + location[1]);
                    Log.i("keitLog", " getHeight()=" + scrollToView.getHeight());
                    Log.i("keitLog"," rect.bottom="+rect.bottom);


                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }
	@OnClick({R.id.button_reset,R.id.ivBack,R.id.button_confirm,R.id.button_charge})
	private void click(View v){
		switch (v.getId()) {
		case R.id.button_reset:
			startActivity(new Intent(PayActivity.this,PasswordResetActivity.class));
			break;
		case R.id.ivBack:
            finish();
			break;
		case R.id.button_confirm:
			if (pass.getTextValue().length()==6) {
				Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
				setResult(2);
				finish();
			}else {
				Toast.makeText(this, "密码输入有误", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button_charge:
			startActivity(new Intent(PayActivity.this,RechargeActivity.class));
			break;
		default:
			break;
		}
	}
}
