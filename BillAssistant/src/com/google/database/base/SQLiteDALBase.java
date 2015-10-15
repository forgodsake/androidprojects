package com.google.database.base;

import java.util.ArrayList;
import java.util.List;

import com.google.database.base.SQLiteHelper.SQLiteDataTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class SQLiteDALBase implements SQLiteDataTable{
	
	private Context mContext;
	
	private SQLiteDatabase mDataBase;
	
	public SQLiteDALBase(Context pContext)
	{
		mContext = pContext;
	}
	
	protected Context getContext()
	{
		return mContext;
	}
	
	public SQLiteDatabase getDataBase()
	{
		if(mDataBase == null)
		{
			mDataBase = SQLiteHelper.getInstance(mContext).getWritableDatabase();
		}
		
		return mDataBase;
	}
	
	public void beginTransaction()
	{
		mDataBase.beginTransaction();
	}
	
	public void setTransactionSuccessful()
	{
		mDataBase.setTransactionSuccessful();
	}
	
	public void endTransaction()
	{
		mDataBase.endTransaction();
	}
	
	public int getCount(String p_Condition)
	{
		String string[] = getTableNameAndPK();
		Cursor cursor = execSql("Select " + string[1] + " From " + string[0] + " Where 1=1 " + p_Condition);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
	
	public int getCount(String pPK,String pTableName, String pCondition)
	{
		Cursor cursor = execSql("Select " + pPK + " From " + pTableName + " Where 1=1 " + pCondition);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
	
	protected Boolean delete(String pTableName, String pCondition)
	{
		return getDataBase().delete(pTableName, " 1=1 " + pCondition, null) >= 0;
	}
	
	protected abstract String[] getTableNameAndPK();
	
	protected List getList(String pSqlText)
	{
		Cursor cursor = execSql(pSqlText);
		return cursorToList(cursor);
	}
	
	protected abstract Object findModel(Cursor pCursor);
	
	protected List cursorToList(Cursor pCursor)
	{
		List list = new ArrayList();
		while(pCursor.moveToNext())
		{
			Object object = findModel(pCursor);
			list.add(object);
		}
		pCursor.close();
		return list;
	}
	
	public Cursor execSql(String pSqlText)
	{
		return getDataBase().rawQuery(pSqlText, null);
	}
}
