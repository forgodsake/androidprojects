package com.google.business;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.google.R;
import com.google.business.base.BusinessBase;
import com.google.database.sqlitedal.SQLiteDALPayout;
import com.google.model.ModelPayout;

public class PayoutBusiness extends BusinessBase {
	
	private SQLiteDALPayout m_SqLiteDALPayout;
	
	public PayoutBusiness(Context pContext)
	{
		super(pContext);
		m_SqLiteDALPayout = new SQLiteDALPayout(pContext);
	}
	
	public Boolean insertPayout(ModelPayout pInfo)
	{
		Boolean result = m_SqLiteDALPayout.insertPayout(pInfo);
		
		if(result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public Boolean deletePayoutByPayoutID(int pPayoutID)
	{
		String condition = " And PayoutID = " + pPayoutID;
		Boolean result = m_SqLiteDALPayout.deletePayout(condition);
		return result;
	}
	
	public Boolean deletePayoutByAccountBookID(int pAccountBookID)
	{
		String condition = " And AccountBookID = " + pAccountBookID;
		Boolean result = m_SqLiteDALPayout.deletePayout(condition);
		return result;
	}
	
	public Boolean updatePayoutByPayoutID(ModelPayout pInfo)
	{
		String condition = " PayoutID = " + pInfo.getPayoutID();
		Boolean result = m_SqLiteDALPayout.updatePayout(condition, pInfo);
		
		if(result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public List<ModelPayout> getPayout(String pcondition)
	{
		return m_SqLiteDALPayout.getPayout(pcondition);
	}
	
	public int getCount()
	{
		return m_SqLiteDALPayout.getCount("");
	}

	public List<ModelPayout> getPayoutByAccountBookID(int pAccountBookID)
	{
		String condition = " And AccountBookID = " + pAccountBookID + " Order By PayoutDate DESC,PayoutID DESC";
		return m_SqLiteDALPayout.getPayout(condition);
	}
	
	public String getPayoutTotalMessage(String pPayoutDate,int pAccountBookID)
	{
		String total[] = getPayoutTotalByPayoutDate(pPayoutDate,pAccountBookID);
		return getContext().getString(R.string.TextViewTextPayoutTotal, new Object[]{total[0],total[1]});
	}
	
	private String[] getPayoutTotalByPayoutDate(String pPayoutDate,int pAccountBookID)
	{
		String condition = " And PayoutDate = '" + pPayoutDate + "' And AccountBookID = " + pAccountBookID;
		return getPayoutTotal(condition);
	}
	
	public String[] getPayoutTotalByAccountBookID(int pAccountBookID)
	{
		String condition = " And AccountBookID = " + pAccountBookID;
		return getPayoutTotal(condition);
	}
	
	private String[] getPayoutTotal(String pCondition)
	{
		String sqlText = "Select ifnull(Sum(Amount),0) As SumAmount,Count(Amount) As Count From Payout Where 1=1 " + pCondition;
		String total[] = new String[2];
		Cursor cursor = m_SqLiteDALPayout.execSql(sqlText);
		if(cursor.getCount() == 1)
		{
			while(cursor.moveToNext())
			{
				total[0] = cursor.getString(cursor.getColumnIndex("Count"));
				total[1] = cursor.getString(cursor.getColumnIndex("SumAmount"));
			}
		}
		return total;
	}
	
	public List<ModelPayout> getPayoutOrderByPayoutUserID(String pCondition)
	{
		pCondition += " Order By PayoutUserID";
		List<ModelPayout> list = getPayout(pCondition);
		if(list.size() > 0)
		{
			return list;
		}
		
		return null;
	}
	
	public String[] getPayoutDateAndAmountTotal(String pCondition)
	{
		String sqlText = "Select Min(PayoutDate) As MinPayoutDate,Max(PayoutDate) As MaxPayoutDate,Sum(Amount) As Amount From Payout Where 1=1 " + pCondition;
		String ayoutTotal[] = new String[3];
		Cursor cursor = m_SqLiteDALPayout.execSql(sqlText);
		if(cursor.getCount() == 1)
		{
			while(cursor.moveToNext())
			{
				ayoutTotal[0] = cursor.getString(cursor.getColumnIndex("MinPayoutDate"));
				ayoutTotal[1] = cursor.getString(cursor.getColumnIndex("MaxPayoutDate"));
				ayoutTotal[2] = cursor.getString(cursor.getColumnIndex("Amount"));
			}
		}
		return ayoutTotal;
	}
}
