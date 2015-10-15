package com.google.database.base;

import java.util.ArrayList;

import com.google.utility.Reflection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static SQLiteDateBaseConfig mSqLiteDateBaseConfig;
	private static SQLiteHelper INSTANCE;
	private Context mContext;
	private Reflection mReflection;
	
	public interface SQLiteDataTable{
		public void onCreate(SQLiteDatabase pSqLiteDatabase);
		public void onUpgrade(SQLiteDatabase pSqLiteDatabase);
	}
	
	private SQLiteHelper(Context pContext){
		super(pContext, mSqLiteDateBaseConfig.getDataBaseName(), null, mSqLiteDateBaseConfig.getVersion());
	    mContext=pContext;
	}
    
	
	public static SQLiteHelper getInstance(Context pContext){
		
		if (INSTANCE == null) {
			mSqLiteDateBaseConfig=SQLiteDateBaseConfig.getInstance(pContext);
			INSTANCE = new SQLiteHelper(pContext);
			
		}
		
		return INSTANCE;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
       
		ArrayList<String> list=mSqLiteDateBaseConfig.getTables();
		mReflection=new Reflection();
		for (int i = 0; i < list.size(); i++) {
			try {
				SQLiteDataTable sqliteDataTable=(SQLiteDataTable) mReflection.newInstance(
						list.get(i),
						new Object[]{mContext}, 
						new Class[]{Context.class});
				sqliteDataTable.onCreate(db);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
