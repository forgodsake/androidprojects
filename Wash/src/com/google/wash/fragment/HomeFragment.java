package com.google.wash.fragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.google.wash.ChooseActivity;
import com.google.wash.PieceActivity;
import com.google.wash.R;
import com.google.wash.VerifyActivity;
import com.google.wash.WelcomeActivity;
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
	@ViewInject(R.id.myShow)
	private ViewPager pager;
	private List<ImageView> views;
	private int []images=new int[]{
		R.drawable.a0,R.drawable.a1,R.drawable.a2,R.drawable.a3	
	};
	private String cityName;
	private LocationManager locManager;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.home, container, false);
	    ViewUtils.inject(this, view);
	    tvCity.setText(Shared.getCityName(getActivity()));
	    return view;
       }
    @OnClick({R.id.imageViewPacWash,R.id.imageViewPiece})
    private void click(View view){
    	switch (view.getId()) {
		case R.id.imageViewPacWash:
			Intent intent1 = new Intent(getActivity(),ChooseActivity.class);
	    	startActivity(intent1);
			break;
		case R.id.imageViewPiece:
			Intent intent2 = new Intent(getActivity(),PieceActivity.class);
	    	startActivity(intent2);
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
			    return views.size();
			}
			
			 @Override  
	            public void destroyItem(ViewGroup container, int position,  
	                    Object object) {  
	                container.removeView(views.get(position));  
	            }  
	              
	            @Override  
	            public Object instantiateItem(ViewGroup container, int position) {  
	                container.addView(views.get(position));    
	                return views.get(position);  
	            }
		};
		pager.setAdapter(adapter);
    	
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
}
