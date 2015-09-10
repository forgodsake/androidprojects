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

import com.google.wash.ChooseActivity;
import com.google.wash.R;
import com.google.wash.entity.Clothes;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SpringFragment extends Fragment {
    
	@ViewInject(R.id.gridViewClothes)
	private GridView gridView;
	private SimpleAdapter adapter;
	private List<HashMap<String, Object>> list;
	private Clothes clothes=new Clothes();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.spring_wash, container,false);
		ViewUtils.inject(this, view);
		list = new ArrayList<HashMap<String,Object>>();
		for (int i = 0;i < clothes.getImages().length-1; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image",clothes.getImages()[i]);
			map.put("name", clothes.getNames()[i]);
			map.put("price","￥"+ clothes.getPrices()[i]);
			list.add(map);
		}
		adapter = new SimpleAdapter(getActivity(), list, R.layout.grid_item, 
				new String[]{"image","name","price"}, new int[]{R.id.imageViewGoods,R.id.textViewGoods,R.id.textViewPrice});
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),ChooseActivity.class);
				intent.putExtra("detail", position);
				startActivity(intent);
			}
		});
		return view;
	}
}
