package com.google.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.R;
import com.google.adapter.base.AdapterBase;
import com.google.business.PayoutBusiness;
import com.google.business.UserBusiness;
import com.google.model.ModelPayout;
import com.google.utility.DateTools;

public class PayoutAdapter extends AdapterBase {

	private class Holder
	{
		ImageView Icon;
		TextView Name;
		TextView Total;
		TextView PayoutUserAndPayoutType;
		View RelativeLayoutDate;
	}
	
	private PayoutBusiness m_PayoutBusiness;
	private int mAccountBookID;
	
	public PayoutAdapter(Context p_Context,int p_AccountBookID)
	{
		this(p_Context,null);
		m_PayoutBusiness = new PayoutBusiness(p_Context);
		mAccountBookID = p_AccountBookID;
		List _List = m_PayoutBusiness.getPayoutByAccountBookID(p_AccountBookID);
		setList(_List);
	}
	
	public PayoutAdapter(Context p_Context, List p_List) {
		super(p_Context, p_List);
	}

	@Override
	public View getView(int p_Position, View p_ConvertView, ViewGroup p_Parent) {
		Holder _Holder;
		
		if (p_ConvertView == null) {
			p_ConvertView = getLayoutInflater().inflate(R.layout.payout_list_item, null);
			_Holder = new Holder();
			_Holder.Icon = (ImageView)p_ConvertView.findViewById(R.id.PayoutIcon);
			_Holder.Name = (TextView)p_ConvertView.findViewById(R.id.PayoutName);
			_Holder.Total = (TextView)p_ConvertView.findViewById(R.id.Total);
			_Holder.PayoutUserAndPayoutType = (TextView)p_ConvertView.findViewById(R.id.PayoutUserAndPayoutType);
			_Holder.RelativeLayoutDate = p_ConvertView.findViewById(R.id.RelativeLayoutDate);
			p_ConvertView.setTag(_Holder);
		}
		else {
			_Holder = (Holder) p_ConvertView.getTag();
		}

		_Holder.RelativeLayoutDate.setVisibility(View.GONE);
		ModelPayout _ModelPayout = (ModelPayout)getItem(p_Position);
		String _PayoutDate = DateTools.getFormatShortTime(_ModelPayout.getPayoutDate());
		Boolean _IsShow = false;
		if(p_Position > 0)
		{
			ModelPayout _ModelPayoutLast = (ModelPayout)getItem(p_Position - 1);
			String _PayoutDateLast = DateTools.getFormatShortTime(_ModelPayoutLast.getPayoutDate());
			_IsShow = !_PayoutDate.equals(_PayoutDateLast);
		}
		if(_IsShow || p_Position == 0)
		{
			_Holder.RelativeLayoutDate.setVisibility(View.VISIBLE);
			String _Message = m_PayoutBusiness.getPayoutTotalMessage(_PayoutDate,mAccountBookID); 
			((TextView)_Holder.RelativeLayoutDate.findViewById(R.id.tvPayoutDate)).setText(_PayoutDate);
			((TextView)_Holder.RelativeLayoutDate.findViewById(R.id.tvTotal)).setText(_Message);
		}
		
		_Holder.Icon.setImageResource(R.drawable.payout_small_icon);
		_Holder.Total.setText(_ModelPayout.getAmount().toString());
		_Holder.Name.setText(_ModelPayout.getCategoryName());
		
		UserBusiness _UserBusiness = new UserBusiness(getContext());
		String _UserNameString = _UserBusiness.getUserNameByUserID(_ModelPayout.getPayoutUserID());
		_Holder.PayoutUserAndPayoutType.setText(_UserNameString + " " + _ModelPayout.getPayoutType());
		
		return p_ConvertView;
	}
	
}
