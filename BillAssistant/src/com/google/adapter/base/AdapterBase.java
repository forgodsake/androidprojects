package com.google.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class AdapterBase extends BaseAdapter {
    private List mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    
    public AdapterBase(Context pContext,List pList){
    	mList=pList;
    	mContext=pContext;
    	mLayoutInflater=LayoutInflater.from(mContext);
    }
    
    public void setList(List pList){
    	
    	mList=pList;
    }
    
    public List  getList(){
    	return mList;
    }

	public LayoutInflater getLayoutInflater(){
    	return mLayoutInflater;
    }
    
    public Context getContext(){
    	return mContext;
    }
    
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


}
