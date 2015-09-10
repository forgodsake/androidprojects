package com.google.wash.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.wash.R;
import com.google.wash.R.layout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class UserFragment extends Fragment{
    
	@ViewInject(R.id.gridViewMy)
	private GridView myGrid;
	
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
	    adapter = new SimpleAdapter(getActivity(), list, R.layout.grid_item_my, new String []{"image","des"}, new int[]{R.id.imageViewMy,R.id.textViewMy});
	    myGrid.setAdapter(adapter);
	}
}
