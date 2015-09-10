package com.google.wash;

import java.util.ArrayList;
import java.util.List;

import com.google.wash.utils.Shared;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;


public class WelcomeActivity extends Activity {  
	  
    private View view1, view2, view3,view4;  
    private ViewPager viewPager;  //对应的viewPager  
    private List<View> viewList;//view数组  
     
    
    
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
       
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//        	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);  
       
        //判断当前是否首次打开软件
        if (!Shared.getWelcome(this)) {
			Intent intent = new Intent(WelcomeActivity.this,VerifyActivity.class);
			startActivity(intent);
			finish();
		}
            
        viewPager = (ViewPager) findViewById(R.id.viewpager);  
        LayoutInflater inflater=getLayoutInflater();  
        view1 = inflater.inflate(R.layout.introduce1, null);  
        view2 = inflater.inflate(R.layout.introduce2,null);  
        view3 = inflater.inflate(R.layout.introduce3, null);  
        view4 = inflater.inflate(R.layout.introduce4, null);  
          
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
        viewList.add(view1);  
        viewList.add(view2);  
        viewList.add(view3);  
        viewList.add(view4);  
          
          
        PagerAdapter pagerAdapter = new PagerAdapter() {  
              
            @Override  
            public boolean isViewFromObject(View arg0, Object arg1) {  
                return arg0 == arg1;  
            }  
              
            @Override  
            public int getCount() {  
                return viewList.size();  
            }  
              
            @Override  
            public void destroyItem(ViewGroup container, int position,  
                    Object object) {  
                container.removeView(viewList.get(position));  
            }  
              
            @Override  
            public Object instantiateItem(ViewGroup container, int position) {  
                container.addView(viewList.get(position));  
                Button button=(Button) (viewList.get(position)).findViewById(R.id.button1) ; 
                button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(WelcomeActivity.this,VerifyActivity.class);
						startActivity(intent);
	                    finish();
					}
				});  
                return viewList.get(position);  
            }
            
            
        };  
          
          
        viewPager.setAdapter(pagerAdapter);  
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if(arg0==viewList.size()-1){
					Intent intent=new Intent(WelcomeActivity.this,VerifyActivity.class);
					startActivity(intent);
                    finish();
                }
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
    } 
    
    @Override
    protected void onDestroy() {
    	Shared.putWelcome(this, false);
    	super.onDestroy();
    }
   
}  
