package com.google.database.base;

import java.util.ArrayList;

import com.google.R;

import android.content.Context;

public class SQLiteDateBaseConfig {
    
	private static final String DATABASE_NAME="BillDatabase";
	private static final int VERSION=1;
	private static SQLiteDateBaseConfig INSTANCE;
	private static Context mContext;
	
	public SQLiteDateBaseConfig (){
		
	}
	
	public static SQLiteDateBaseConfig getInstance(Context pContext){
		if(INSTANCE==null){
			INSTANCE=new SQLiteDateBaseConfig();
			mContext=pContext;
		}
		
		return INSTANCE;
	}
	
	public String getDataBaseName(){
		return DATABASE_NAME;
	}
	
	public int getVersion(){
		return VERSION;
	}
	
	public ArrayList<String> getTables(){
		ArrayList<String> list =new ArrayList<String>();
		String []sqliteDALClassName= mContext.getResources().getStringArray(R.array.SQLiteDALClassName);
		String packagePath= mContext.getPackageName()+".database.sqlitedal.";
		for (int i = 0; i < sqliteDALClassName.length; i++) {
			list.add(packagePath+sqliteDALClassName[i]);
		}
		return list;
	}
}
