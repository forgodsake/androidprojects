package com.google.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.R;
import com.google.adapter.base.AdapterBase;
import com.google.business.UserBusiness;
import com.google.model.ModelUser;

public class UserAdapter extends AdapterBase {
       
	   
	   private class Holder{
		    ImageView ivUserIcon;
	    	TextView tvUserName;
	    }
	    
	public UserAdapter(Context pContext) {
		super(pContext,null );
		UserBusiness user=new UserBusiness(pContext);
		List list = user.getNotHideUser();
		setList(list);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView==null){
			convertView=getLayoutInflater().inflate(R.layout.user_list_item, null);
			holder=new Holder();
			holder.ivUserIcon=(ImageView) convertView.findViewById(R.id.ivUserIcon);
			holder.tvUserName=(TextView) convertView.findViewById(R.id.tvUserName);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		ModelUser modelUser=(ModelUser)getList().get(position);
		LinearLayout.LayoutParams _layoutParams=new LinearLayout.LayoutParams(180,180);
		holder.tvUserName.setText(modelUser.getUserName());
		
		return convertView;
	}

		

}
