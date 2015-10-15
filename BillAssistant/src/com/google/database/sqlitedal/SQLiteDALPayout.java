package com.google.database.sqlitedal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.database.base.SQLiteDALBase;
import com.google.model.ModelPayout;
import com.google.utility.DateTools;

public class SQLiteDALPayout extends SQLiteDALBase {

	public SQLiteDALPayout(Context pContext) {
		super(pContext);
	}

	public Boolean insertPayout(ModelPayout pInfo) {
		ContentValues contentValues = CreatParms(pInfo);
		Long pNewID = getDataBase().insert("Payout", null, contentValues);
		pInfo.setPayoutID(pNewID.intValue());
		return pNewID > 0;
	}
	
	public Boolean deletePayout(String pCondition)
	{
		return delete(getTableNameAndPK()[0], pCondition);
	}
	
	public Boolean updatePayout(String pCondition, ModelPayout pInfo)
	{
		ContentValues contentValues = CreatParms(pInfo);
		return getDataBase().update("Payout", contentValues, pCondition, null) > 0;
	}
	
	public Boolean UpdatePayout(String pCondition,ContentValues pContentValues)
	{
		return getDataBase().update("Payout", pContentValues, pCondition, null) > 0;
	}
	
	public List<ModelPayout> getPayout(String pCondition)
	{
		String sqlText = "Select * From v_Payout Where  1=1 " + pCondition;
		return getList(sqlText);
	}
	
	@Override
	protected ModelPayout findModel(Cursor pcursor)
	{
		ModelPayout modelPayout = new ModelPayout();
		modelPayout.setPayoutID(pcursor.getInt(pcursor.getColumnIndex("PayoutID")));
		modelPayout.setAccountBookID(pcursor.getInt((pcursor.getColumnIndex("AccountBookID"))));
		modelPayout.setAccountBookName((pcursor.getString(pcursor.getColumnIndex("AccountBookName"))));
		modelPayout.setCategoryID(pcursor.getInt((pcursor.getColumnIndex("CategoryID"))));
		modelPayout.setCategoryName((pcursor.getString(pcursor.getColumnIndex("CategoryName"))));
		modelPayout.setPath((pcursor.getString(pcursor.getColumnIndex("Path"))));
		modelPayout.setPayWayID(pcursor.getInt((pcursor.getColumnIndex("PayWayID"))));
		modelPayout.setPlaceID(pcursor.getInt((pcursor.getColumnIndex("PlaceID"))));
		modelPayout.setAmount(new BigDecimal(pcursor.getString(((pcursor.getColumnIndex("Amount"))))));
		Date _PayoutDate = DateTools.getDate(pcursor.getString(pcursor.getColumnIndex("PayoutDate")), "yyyy-MM-dd");	
		modelPayout.setPayoutDate(_PayoutDate);
		modelPayout.setPayoutType((pcursor.getString(pcursor.getColumnIndex("PayoutType"))));
		modelPayout.setPayoutUserID((pcursor.getString(pcursor.getColumnIndex("PayoutUserID"))));
		modelPayout.setComment((pcursor.getString(pcursor.getColumnIndex("Comment"))));
		Date _CreateDate = DateTools.getDate(pcursor.getString(pcursor.getColumnIndex("CreateDate")), "yyyy-MM-dd HH:mm:ss");	
		modelPayout.setCreateDate(_CreateDate);
		modelPayout.setState((pcursor.getInt(pcursor.getColumnIndex("State"))));
		
		return modelPayout;
	}

	@Override
	public void onCreate(SQLiteDatabase pDataBase) {
		StringBuilder createTableScript = new StringBuilder();
		
		createTableScript.append("Create  TABLE Payout(");
		createTableScript.append("[PayoutID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		createTableScript.append(",[AccountBookID] integer NOT NULL");
		createTableScript.append(",[CategoryID] integer NOT NULL");
		createTableScript.append(",[PayWayID] integer");
		createTableScript.append(",[PlaceID] integer");
		createTableScript.append(",[Amount] decimal NOT NULL");
		createTableScript.append(",[PayoutDate] datetime NOT NULL");
		createTableScript.append(",[PayoutType] varchar(20) NOT NULL");
		createTableScript.append(",[PayoutUserID] text NOT NULL");
		createTableScript.append(",[Comment] text");
		createTableScript.append(",[CreateDate] datetime NOT NULL");
		createTableScript.append(",[State] integer NOT NULL");
		createTableScript.append(")");
		
		pDataBase.execSQL(createTableScript.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase pDataBase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{"Payout","PayoutID"};
	}

	public ContentValues CreatParms(ModelPayout pInfo) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("AccountBookID",pInfo.getAccountBookID());
		contentValues.put("CategoryID",pInfo.getCategoryID());
		contentValues.put("PayWayID",pInfo.getPayWayID());
		contentValues.put("PlaceID",pInfo.getPlaceID());
		contentValues.put("Amount",pInfo.getAmount().toString());
		contentValues.put("PayoutDate",DateTools.getFormatDateTime(pInfo.getPayoutDate(),"yyyy-MM-dd"));
		contentValues.put("PayoutType",pInfo.getPayoutType());
		contentValues.put("PayoutUserID",pInfo.getPayoutUserID());
		contentValues.put("Comment",pInfo.getComment());
		contentValues.put("CreateDate",DateTools.getFormatDateTime(pInfo.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
		contentValues.put("State",pInfo.getState());
		
		return contentValues;
	}

}
