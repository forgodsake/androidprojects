package com.google.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.R;
import com.google.adapter.base.AdapterBase;
import com.google.controls.SlideMenuItem;

public class SlideMenuAdapter extends AdapterBase {
       
	   private Context mContext;
	   
	   private class Holder{
	    	TextView tvMenuName;
	    }
	    
	public SlideMenuAdapter(Context pContext,List pList) {
		super(pContext,pList );
		mContext=pContext;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView==null){
			convertView=getLayoutInflater().inflate(R.layout.slide_menu_item, null);
			holder=new Holder();
			holder.tvMenuName=(TextView) convertView.findViewById(R.id.tvMenuName);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		SlideMenuItem item=(SlideMenuItem)getList().get(position);
		LinearLayout.LayoutParams _layoutParams=new LinearLayout.LayoutParams(180,180);
		holder.tvMenuName.setText(item.getTitle());
		
		return convertView;
	}

		

}
