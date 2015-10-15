package com.google.database.sqlitedal;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.R;
import com.google.database.base.SQLiteDALBase;
import com.google.model.ModelUser;
import com.google.utility.DateTools;

public class SQLiteDALUser extends SQLiteDALBase {

	public SQLiteDALUser(Context pContext) {
		super(pContext);
	}

	@Override
	protected String[] getTableNameAndPK() {
		
		return new String[]{"User","UserID"};
	}

	@Override
	protected Object findModel(Cursor pCursor) {
		ModelUser modelUser = new ModelUser();
		modelUser.setUserID(pCursor.getInt(pCursor.getColumnIndex("UserID")));
		modelUser.setUserName(pCursor.getString(pCursor.getColumnIndex("UserName")));
		Date createDate = DateTools.getDate(pCursor.getString(pCursor.getColumnIndex("CreateDate")), "yyyy-MM-dd HH:mm:ss");	
		modelUser.setCreateDate(createDate);
		modelUser.setState((pCursor.getInt(pCursor.getColumnIndex("State"))));
		
		return modelUser;
	}

	public boolean insertUser(ModelUser pModelUser) {
        ContentValues values=createParams(pModelUser);
        long newID=getDataBase().insert(getTableNameAndPK()[0], null, values);
        pModelUser.setUserID((int)newID);
	    return newID>0;
	}
	
	public boolean deleteUser(String pCondition){
		return delete(getTableNameAndPK()[0], pCondition);
	}
	
	public List<ModelUser> getUser(String pCondition){
		String sqlText  ="Select * From User Where 1=1 "+pCondition;
		return getList(sqlText);
	}
	
	public boolean updateUser(ModelUser pModelUser,String pCondition){
		ContentValues values=createParams(pModelUser);
		return getDataBase().update(getTableNameAndPK()[0], values, pCondition, null)>0;
	}
	
	public boolean updateUser(String pCondition,ContentValues pContentValues)
	{
		return getDataBase().update("User", pContentValues, pCondition, null) > 0;
	}

	@Override
	public void onCreate(SQLiteDatabase pSqLiteDatabase) {
		StringBuilder createTableScript = new StringBuilder();

		createTableScript.append("Create  TABLE User(");
		createTableScript
				.append("[UserID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		createTableScript.append(",[UserName] varchar(10) NOT NULL");
		createTableScript.append(",[CreateDate] datetime NOT NULL");
		createTableScript.append(",[State] integer NOT NULL");
		createTableScript.append(")");

		pSqLiteDatabase.execSQL(createTableScript.toString());

		initDefaultData(pSqLiteDatabase);

	}

	@Override
	public void onUpgrade(SQLiteDatabase pSqLiteDatabase) {

	}

	public ContentValues createParams(ModelUser pModelUser) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("UserName", pModelUser.getUserName());
		contentValues.put("CreateDate", DateTools.getFormatDateTime(
				pModelUser.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		contentValues.put("State", pModelUser.getState());
		return contentValues;
	}
    
	public void initDefaultData(SQLiteDatabase pDatabase){
		ModelUser modelUser= new ModelUser();
		String[] userName= getContext().getResources().getStringArray(R.array.InitDefaultUserName);
	    for (int i = 0; i < userName.length; i++) {
			modelUser.setUserName(userName[i]);
		    ContentValues values=createParams(modelUser);
	        pDatabase.insert(getTableNameAndPK()[0], null, values);
	    }
	}
}
