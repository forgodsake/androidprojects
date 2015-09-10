package com.google.wash.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.EventLogTags.Description;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.wash.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MoreFragment extends Fragment {
	
	@ViewInject(R.id.listViewMore)
	private ListView moreList;
	private SimpleAdapter adapter;
	private List<HashMap<String, Object>> list;
	
	private int [] images = new int [] {
			R.drawable.more1,R.drawable.more2,R.drawable.more3,
			R.drawable.more4,R.drawable.more5,R.drawable.more6
	};
	private String [] des = new String [] {
			"联系客服","常见问题","服务范围","关于我们","用户协议","意见反馈"
	};
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.more, container, false);
	    ViewUtils.inject(this, view);
	    return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		list = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", images[i]);
			map.put("des", des[i]);
			list.add(map);
		}
		adapter = new SimpleAdapter(getActivity(), list, R.layout.more_item, new String[]{"image","des"}, new int []{R.id.imageViewIcon,R.id.textViewDescri});
		moreList.setAdapter(adapter);
	}
}
