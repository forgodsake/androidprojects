package com.google.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.R;
import com.google.adapter.base.AdapterBase;
import com.google.business.AccountBookBusiness;
import com.google.business.PayoutBusiness;
import com.google.model.ModelAccountBook;

public class AccountBookAdapter extends AdapterBase {
     
	private Context mContext;
	   
	private class Holder
	{
		ImageView ivIcon;
		TextView tvName;
		TextView tvTotal;
		TextView tvMoney;
	}
	
	public AccountBookAdapter(Context pContext) {
		super(pContext, null);
		mContext=pContext;
		AccountBookBusiness AccountBookbusiness = new AccountBookBusiness(pContext);
		List list = AccountBookbusiness.getAccountBook("");
		setList(list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Holder holder;
		
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.account_book_list_item, null);
			holder = new Holder();
			holder.ivIcon = (ImageView)convertView.findViewById(R.id.ivAccountBookIcon);
			holder.tvName = (TextView)convertView.findViewById(R.id.tvAccountBookName);
			holder.tvTotal = (TextView)convertView.findViewById(R.id.tvTotal);
			holder.tvMoney = (TextView)convertView.findViewById(R.id.tvMoney);
			convertView.setTag(holder);
		}
		else {
			holder = (Holder) convertView.getTag();
		}
		
		ModelAccountBook modelAccountBook = (ModelAccountBook)getItem(position);
		if(modelAccountBook.getIsDefault() == 1)
		{
			holder.ivIcon.setImageResource(R.drawable.account_book_default);
		}
		else {
			holder.ivIcon.setImageResource(R.drawable.account_book_big_icon);
		}
		
		PayoutBusiness _BusinessPayout = new PayoutBusiness(getContext());
		String _Total[] = _BusinessPayout.getPayoutTotalByAccountBookID(modelAccountBook.getAccountBookID());		

		holder.tvTotal.setText(mContext.getString(R.string.TextViewTextAccountBookPayoutTotal, new Object[]{_Total[0]}));
		holder.tvName.setText(modelAccountBook.getAccountBookName());
		holder.tvMoney.setText(mContext.getString(R.string.TextViewTextAccountBookPayoutMoney, new Object[]{_Total[1]}));
		
		return convertView;
	}


}
