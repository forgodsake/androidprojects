package com.google.wash.fragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.wash.ChooseActivity;
import com.google.wash.PieceActivity;
import com.google.wash.R;
import com.google.wash.ServiceScopeActivity;
import com.google.wash.utils.Shared;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class HomeFragment extends Fragment implements LocationListener{
    
	@ViewInject(R.id.tvCity)
	private TextView tvCity;
	@ViewInject(R.id.imageViewPacWash)
	private ImageView packageWash;
	@ViewInject(R.id.imageViewPiece)
	private ImageView pieceWash;
	@ViewInject(R.id.imageView_state)
	private ImageView image_state;
	@ViewInject(R.id.myShow)
	private ViewPager pager;
	@ViewInject(R.id.indicator)
	private View v;// 线性水平布局，负责动态调整导航图标
	private List<ImageView> views;
	private int []images=new int[]{
		R.drawable.a0,R.drawable.a1,R.drawable.a2,R.drawable.a3	
	};
	private String cityName;
	private LocationManager locManager;
	private ImageView[] indicator_imgs = new ImageView[4];//存放引导图片数组
	private ScheduledExecutorService scheduledExecutorService;
	private int currentItem = 0;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.home, container, false);
	    ViewUtils.inject(this, view);
	    tvCity.setText(Shared.getCityName(getActivity()));
	    initIndicator();
	    return view;
       }
	    /**
	     * 初始化引导图标
	     * 动态创建多个小圆点，然后组装到线性布局里
	     */
	    private void initIndicator(){
	      
	      ImageView imgView;
	     
	      
	      for (int i = 0; i < 4; i++) {
	        imgView = new ImageView(getActivity());
	        LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(20,20);
	        params_linear.setMargins(20, 20, 20, 20);
	        imgView.setLayoutParams(params_linear);
	        indicator_imgs[i] = imgView;
	        
	        if (i == 0) { // 初始化第一个为选中状态
	          
	          indicator_imgs[i].setBackgroundResource(R.drawable.dot_red);
	        } else {
	          indicator_imgs[i].setBackgroundResource(R.drawable.dot_blue);
	        }
	        ((ViewGroup)v).addView(indicator_imgs[i]);
	      }
       }
    @OnClick({R.id.imageViewPacWash,R.id.imageViewPiece,R.id.imageView_state})
    private void click(View view){
    	switch (view.getId()) {
		case R.id.imageViewPacWash:
			Intent intent1 = new Intent(getActivity(),ChooseActivity.class);
	    	startActivity(intent1);
			break;
		case R.id.imageViewPiece:
			Intent intent2 = new Intent(getActivity(),PieceActivity.class);
	    	startActivity(intent2);
	    	break;
		case R.id.imageView_state:
			Intent intent3 = new Intent(getActivity(),ServiceScopeActivity.class);
			startActivity(intent3);
			break;
		default:
			break;
		}
    	
    	
    	
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	views = new ArrayList<ImageView>();
    	for (int i = 0; i < images.length; i++) {
			ImageView imageView = new ImageView(getActivity()) ;
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(images[i]);
			views.add(imageView);
		}
    	 PagerAdapter adapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
			    return Integer.MAX_VALUE;
			}
			
			 @Override  
	            public void destroyItem(ViewGroup container, int position,  
	                    Object object) {  
	                container.removeView(views.get(position%views.size()));  
	            }  
	              
	            @Override  
	            public Object instantiateItem(ViewGroup container, int position) { 
	            	container.removeView(views.get(position%views.size())); 
	                container.addView(views.get(position%views.size()));    
	                return views.get(position%views.size());  
	            }
		};
		pager.setAdapter(adapter);
	    pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// 改变所有导航的背景图片为：未选中
			      for (int i = 0; i < indicator_imgs.length; i++) {
			        indicator_imgs[i].setBackgroundResource(R.drawable.dot_blue);
			      }
			      // 改变当前背景图片为：选中
			      indicator_imgs[arg0%4].setBackgroundResource(R.drawable.dot_red);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
    	
    	locManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    	boolean isOpen=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    	if (!isOpen) {
			Intent intent= new Intent(Settings.ACTION_LOCALE_SETTINGS);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(intent, 0);
		}
    	locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, this);
    }
    
    private Handler handler= new Handler(new Handler.Callback(){

		@Override
		public boolean handleMessage(Message msg) {
		    if (msg.what==1) {
				tvCity.setText(cityName);
			}else if (msg.what==2) {
			    currentItem = currentItem +1;
			    //设置当前页面
	            pager.setCurrentItem(currentItem);
			}
			return false;
		}
    	
    });
    
	@Override
	public void onLocationChanged(Location location) {
		double lat=0.0,lng=0.0;
		if (location!=null) {
			lat=location.getLatitude();
			lng=location.getLongitude();
		}else {
			cityName="未能获取城市信息";
		}
		List<Address> list=null;
		Geocoder coder= new Geocoder(getActivity());
		try {
			list=coder.getFromLocation(lat, lng, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				Address address=list.get(i);
				cityName=address.getLocality();
			}
		}
		handler.sendEmptyMessage(1);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		Shared.putCityName(getActivity(), cityName);
	    locManager.removeUpdates(this);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		 scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
         
         //每隔2秒钟切换一张图片
         scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(),10,10, TimeUnit.SECONDS);
    }
     
     //切换图片
     private class ViewPagerTask implements Runnable {
 
         @Override
        public void run() {
            
             //更新界面
             handler.sendEmptyMessage(2);
        }
        
     }
     
    	           
    
}
