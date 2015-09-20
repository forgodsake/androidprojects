package com.google.wash.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.wash.CouponCodeShareActivity;
import com.google.wash.CouponCodeVerifyActivity;
import com.google.wash.CouponTicketActivity;
import com.google.wash.R;
import com.google.wash.UserAddressActivity;
import com.google.wash.UserCoinActivity;
import com.google.wash.UserOrderActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UserFragment extends Fragment{
    
	@ViewInject(R.id.gridViewMy)
	private GridView myGrid;
	@ViewInject(R.id.textView_ticket)
	private TextView textView;
	
	private List<HashMap<String, Object>> list;
	private SimpleAdapter adapter;
	
	private int [] images = new int [] {
		R.drawable.my1,	R.drawable.my2,	R.drawable.my3,	
		R.drawable.my4,	R.drawable.my5,	
	};
	
	private String [] des = new String []{
		"我的订单","洗衣币","我的地址",	
		"分享推荐码","验证推荐码"
	};
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.user, container, false);
	    ViewUtils.inject(this,view);
	    return view;
	}
	
	@OnClick(R.id.textView_ticket)
	private void click(View v){
		startActivity(new Intent(getActivity(),CouponTicketActivity.class));
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		list = new ArrayList<HashMap<String,Object>>();
	    for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> map =new HashMap<String, Object>();
			map.put("image", images[i]);
			map.put("des", des[i]);
			list.add(map);
		}
	    adapter = new SimpleAdapter(getActivity(), list, R.layout.grid_my_item, new String []{"image","des"}, new int[]{R.id.imageViewMy,R.id.textViewMy});
	    myGrid.setAdapter(adapter);
	    myGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Intent intent0 = new Intent(getActivity(),UserOrderActivity.class);
					startActivity(intent0);
					break;
				case 1:
					Intent intent1 = new Intent(getActivity(),UserCoinActivity.class);
					startActivity(intent1);
					break;
				case 2:
					Intent intent2 = new Intent(getActivity(),UserAddressActivity.class);
					startActivity(intent2);
					break;
				case 3:
					Intent intent3 = new Intent(getActivity(),CouponCodeShareActivity.class);
					startActivity(intent3);
					break;
				case 4:
					Intent intent4 = new Intent(getActivity(),CouponCodeVerifyActivity.class);
					startActivity(intent4);
					break;
				default:
					break;
				}
					
				
			}
		});
	}
}
