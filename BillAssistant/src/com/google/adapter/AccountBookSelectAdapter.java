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
import com.google.model.ModelAccountBook;

public class AccountBookSelectAdapter extends AdapterBase {

	private class Holder
	{
		ImageView Icon;
		TextView Name;
	}
	
	public AccountBookSelectAdapter(Context p_Context)
	{
		this(p_Context,null);
		AccountBookBusiness _AccountBookBusiness = new AccountBookBusiness(p_Context);
		List _List = _AccountBookBusiness.getAccountBook("");
		setList(_List);
	}
	
	public AccountBookSelectAdapter(Context p_Context, List p_List) {
		super(p_Context, p_List);
	}

	@Override
	public View getView(int p_Position, View p_ConvertView, ViewGroup p_Parent) {
		Holder _Holder;
		
		if (p_ConvertView == null) {
			p_ConvertView = getLayoutInflater().inflate(R.layout.account_book_select_list_item, null);
			_Holder = new Holder();
			_Holder.Icon = (ImageView)p_ConvertView.findViewById(R.id.AccountBookIcon);
			_Holder.Name = (TextView)p_ConvertView.findViewById(R.id.AccountBookName);
			p_ConvertView.setTag(_Holder);
		}
		else {
			_Holder = (Holder) p_ConvertView.getTag();
		}
		
		ModelAccountBook _ModelAccountBook = (ModelAccountBook)getItem(p_Position);
		_Holder.Icon.setImageResource(R.drawable.account_book_small_icon);
		_Holder.Name.setText(_ModelAccountBook.getAccountBookName());
		
		return p_ConvertView;
	}
	
}
