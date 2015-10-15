package com.google.wash.database;

import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.DropBoxManager;

public class DBStorage {
	
	private Context mcontext;
	private SQLiteDatabase mSqLiteDatabase;
	private final String DATABASE_NAME="newworld";
	private final String TABLE_NAME="clothes";
    public DBStorage(Context context){
    	mcontext=context;
    }
    
    public void openOrCreateDataBase(){
    		 try {
            	 MySql sql=new MySql(mcontext, DATABASE_NAME, null, 1);
            	 mSqLiteDatabase= sql.getWritableDatabase();
    		} catch (Exception e) {
    			
    		}
    	
    }
    
    public long insert(Map<String,String>  cloth){
    	if(mSqLiteDatabase!=null){
    	ContentValues values=new ContentValues();
    	values.put("image",cloth.get("image"));
    	values.put("num",cloth.get("num"));
    	values.put("name",cloth.get("name"));
    	values.put("price",cloth.get("price"));
    	return mSqLiteDatabase.insert(TABLE_NAME, null, values);
    	}
    	return -1;
    }
    
    public void dataBaseClose(){
    	mSqLiteDatabase.close();
    	mSqLiteDatabase=null;
    }
    
    public void delete(String name){
    	if(mSqLiteDatabase!=null){
        	mSqLiteDatabase.delete(TABLE_NAME,"name=?" , new String []{name});
        	}
    }
    
    public void deleteAll(){
    	if(mSqLiteDatabase!=null){
    		mSqLiteDatabase.delete(TABLE_NAME,"name like ?" , new String []{"%"});
    	}
    }
    
    public void update(Map<String,String>  cloth){
    	if(mSqLiteDatabase!=null){
    		ContentValues values=new ContentValues();
        	values.put("image",cloth.get("image"));
        	values.put("num",cloth.get("num"));
        	values.put("name",cloth.get("name"));
        	values.put("price",cloth.get("price"));
        	mSqLiteDatabase.update(TABLE_NAME, values, "name = ?", new String[]{cloth.get("name")});
        	}
    }
    public void update_num(int num,String name){
    	if(mSqLiteDatabase!=null){
    		ContentValues values=new ContentValues();
    		values.put("num",""+num);
    		mSqLiteDatabase.update(TABLE_NAME, values, "name = ?", new String[]{name});
    	}
    }
    
    public Cursor selectAll(){
    	if(mSqLiteDatabase!=null){
    		return mSqLiteDatabase.query(TABLE_NAME, new String[]{"_id","image","num","name","price"}, null, null, null, null, null);
    	}
    	return null;
    }
}
