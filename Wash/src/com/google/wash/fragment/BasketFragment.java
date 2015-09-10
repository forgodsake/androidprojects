package com.google.wash.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.wash.R;
import com.google.wash.entity.Clothes;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class BasketFragment extends Fragment {
    
	@ViewInject(R.id.imageViewBlank)
	private ImageView blank;
	@ViewInject(R.id.listViewAdd)
	private ListView washList;
	private Set<String> set ;
	private SimpleAdapter adapter;
	private List<HashMap<String, Object>> list;
	private Clothes clothes=new Clothes();
    private Integer x;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.basket, container, false);
	    ViewUtils.inject(this, view);
	    return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		list = new ArrayList<HashMap<String,Object>>();
		int num = getActivity().getSharedPreferences("myShared", Context.MODE_PRIVATE).getInt("num", 0);
		adapter = new SimpleAdapter(getActivity(), list, R.layout.basket_list_item, new String[]{"image","name","price","num"}, new int []{R.id.imageViewGoods,R.id.textViewName,R.id.textViewPrice,R.id.textViewNum});
		washList.setAdapter(adapter);
		x =Integer.valueOf(getActivity().getSharedPreferences("myShared", Context.MODE_PRIVATE).getInt("position", 6));
		set=(Set<String>)getActivity().getSharedPreferences("myShared",Context.MODE_PRIVATE ).getStringSet("saveData", new HashSet<String>()) ;
		Iterator<String> itr=set.iterator();
	    while(itr.hasNext()){
	       String d=(String)itr.next();
		   Integer x1 = Integer.valueOf(d);
		   HashMap<String, Object>   map =  new HashMap<String, Object>();
		   map.put("image",clothes.getImages()[x1]);
		   map.put("name", clothes.getNames()[x1]);
		   map.put("price","￥"+ Integer.parseInt(clothes.getPrices()[x1])*1);
		   map.put("num",1 );
		   list.add(map);
		   adapter.notifyDataSetChanged();
		  }
		
		
		if (num>0) {
			blank.setVisibility(View.GONE);
		    if(!set.contains(String.valueOf(x))){
				set.add(String.valueOf(x));
				HashMap<String, Object>   map =  new HashMap<String, Object>();
				map.put("image",clothes.getImages()[x]);
				map.put("name", clothes.getNames()[x]);
				map.put("price","￥"+ Integer.parseInt(clothes.getPrices()[x])*num);
				map.put("num",num );
				list.add(map);
				adapter.notifyDataSetChanged();
				  //手动设置listview长度
			    int totalHeight = 0;
			    for (int i = 0; i < adapter.getCount(); i++) {
			    	View listItem = adapter.getView(i, null, washList);
			    	listItem.measure(0, 0);
			    	totalHeight += listItem.getMeasuredHeight();
			    }
			    ViewGroup.LayoutParams params = washList.getLayoutParams();
			    params.height = totalHeight + (washList.getDividerHeight() * (adapter.getCount() - 1));
			    washList.setLayoutParams(params);
			}else {
				HashMap<String, Object>  map = list.get(position());
				map.put("num",num );
				map.put("price","￥"+ Integer.parseInt(clothes.getPrices()[position()])*num);
				adapter.notifyDataSetChanged();
				  //手动设置listview长度
			    int totalHeight = 0;
			    for (int i = 0; i < adapter.getCount(); i++) {
			    	View listItem = adapter.getView(i, null, washList);
			    	listItem.measure(0, 0);
			    	totalHeight += listItem.getMeasuredHeight();
			    }
			    ViewGroup.LayoutParams params = washList.getLayoutParams();
			    params.height = totalHeight + (washList.getDividerHeight() * (adapter.getCount() - 1));
			    washList.setLayoutParams(params);
			};
			getActivity().getSharedPreferences("myShared", Context.MODE_APPEND).edit().putStringSet("saveData", set).commit();
		}
	}
	
	public Integer position(){
		Iterator<String> itr=set.iterator();
		int i = -1;
		  while(itr.hasNext()){
			  i++;
		   String d=(String)itr.next();
		   if (Integer.parseInt(d)==x) {
			return i;
		   }
		  }
		return 0;
	}

}
