package com.google.adapter;

import com.google.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    
    private class Holder{
    	ImageView ivIcon;
    	TextView tvName;
    }
    
    private int [] imageIds= new int[]{
    	R.drawable.grid_payout,R.drawable.grid_bill,R.drawable.grid_report,
    	R.drawable.grid_account_book,R.drawable.grid_category,R.drawable.grid_user
    };
    public GridAdapter(Context pContext){
    	mContext=pContext;
    }
    private String [] imageStrings=new String []{
       "记录消费","查询消费","统计管理","账本管理","类别管理","人员管理"
    };
	@Override
	public int getCount() {
		return imageStrings.length;
	}

	@Override
	public Object getItem(int arg0) {
		return imageStrings[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Holder holder;
		if(arg1==null){
			LayoutInflater _layoutInflater=LayoutInflater.from(mContext);
			arg1=_layoutInflater.inflate(R.layout.main_body_item, null);
			holder=new Holder();
			holder.ivIcon=(ImageView) arg1.findViewById(R.id.ivItemIcon);
			holder.tvName=(TextView) arg1.findViewById(R.id.tvName);
			arg1.setTag(holder);
		}else{
			holder=(Holder) arg1.getTag();
		}
		holder.ivIcon.setImageResource(imageIds[arg0]);
		LinearLayout.LayoutParams _layoutParams=new LinearLayout.LayoutParams(180,180);
		holder.ivIcon.setLayoutParams(_layoutParams);
		holder.ivIcon.setScaleType(ImageView.ScaleType.FIT_XY);
		holder.tvName.setText(imageStrings[arg0]);
		return arg1;
	}

}
