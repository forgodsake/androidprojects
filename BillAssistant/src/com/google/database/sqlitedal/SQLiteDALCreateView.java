package com.google.database.sqlitedal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.database.base.SQLiteHelper.SQLiteDataTable;

public class SQLiteDALCreateView implements SQLiteDataTable {

	private Context mContext;
	
	public SQLiteDALCreateView(Context pContext)
	{
		mContext = pContext;
	}
	
	@Override
	public void onCreate(SQLiteDatabase pDataBase) {
		StringBuilder createTableScript = new StringBuilder();
		
		createTableScript.append("Create View v_Payout As ");
		createTableScript.append("select a.*,b.ParentID,b.categoryname,b.Path,b.TypeFlag,c.AccountBookName from payout a LEFT JOIN category b ON a.categoryID = b.categoryID  LEFT JOIN accountbook c ON a.AccountBookID = c.AccountBookID");
		
		pDataBase.execSQL(createTableScript.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase pDataBase) {
		
	}

}
