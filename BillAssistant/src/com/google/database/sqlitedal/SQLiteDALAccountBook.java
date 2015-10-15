package com.google.database.sqlitedal;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.R;
import com.google.database.base.SQLiteDALBase;
import com.google.model.ModelAccountBook;
import com.google.utility.DateTools;

public class SQLiteDALAccountBook extends SQLiteDALBase {

	public SQLiteDALAccountBook(Context pContext) {
		super(pContext);
	}

	public Boolean insertAccountBook(ModelAccountBook pInfo) {
		ContentValues contentValues = creatParams(pInfo);
		Long pNewID = getDataBase().insert("AccountBook", null, contentValues);
		pInfo.setAccountBookID(pNewID.intValue());
		return pNewID > 0;
	}
	
	public Boolean deleteAccountBook(String pCondition)
	{
		return delete(getTableNameAndPK()[0], pCondition);
	}
	
	public Boolean updateAccountBook(String pCondition, ModelAccountBook pInfo)
	{
		ContentValues contentValues = creatParams(pInfo);
		return getDataBase().update("AccountBook", contentValues, pCondition, null) > 0;
	}
	
	public Boolean updateAccountBook(String pCondition,ContentValues pContentValues)
	{
		return getDataBase().update("AccountBook", pContentValues, pCondition, null) > 0;
	}
	
	public List<ModelAccountBook> getAccountBook(String pCondition)
	{
		String sqlText = "Select * From AccountBook Where  1=1 " + pCondition;
		return getList(sqlText);
	}
	
	@Override
	protected ModelAccountBook findModel(Cursor pCursor)
	{
		ModelAccountBook modelAccountBook = new ModelAccountBook();
		modelAccountBook.setAccountBookID(pCursor.getInt(pCursor.getColumnIndex("AccountBookID")));
		modelAccountBook.setAccountBookName(pCursor.getString(pCursor.getColumnIndex("AccountBookName")));
		Date createDate = DateTools.getDate(pCursor.getString(pCursor.getColumnIndex("CreateDate")), "yyyy-MM-dd HH:mm:ss");	
		modelAccountBook.setCreateDate(createDate);
		modelAccountBook.setIsDefault(pCursor.getInt(pCursor.getColumnIndex("IsDefault")));
		modelAccountBook.setState((pCursor.getInt(pCursor.getColumnIndex("State"))));
		
		return modelAccountBook;
	}

	private void initDefaultData(SQLiteDatabase pDataBase)
	{
		String accountBook[] = getContext().getResources().getStringArray(R.array.InitDefaultDataAccountBookName);
		ModelAccountBook modelAccountBook = new ModelAccountBook();
		modelAccountBook.setAccountBookName(accountBook[0]);
		modelAccountBook.setIsDefault(1);
	
		ContentValues contentValues = creatParams(modelAccountBook);
		pDataBase.insert("AccountBook", null, contentValues);
	}
	
	@Override
	public void onCreate(SQLiteDatabase pDataBase) {
		StringBuilder createTableScript = new StringBuilder();
		
		createTableScript.append("Create  TABLE AccountBook(");
		createTableScript.append("[AccountBookID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		createTableScript.append(",[AccountBookName] varchar(20) NOT NULL");
		createTableScript.append(",[CreateDate] datetime NOT NULL");
		createTableScript.append(",[State] integer NOT NULL");
		createTableScript.append(",[IsDefault] integer NOT NULL");
		createTableScript.append(")");
		
		pDataBase.execSQL(createTableScript.toString());
		
		initDefaultData(pDataBase);
	}

	@Override
	public void onUpgrade(SQLiteDatabase pDataBase) {
		
	}

	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{"AccountBook","AccountBookID"};
	}

	public ContentValues creatParams(ModelAccountBook pInfo) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("AccountBookName", pInfo.getAccountBookName());
		contentValues.put("CreateDate",DateTools.getFormatDateTime(pInfo.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
		contentValues.put("State",pInfo.getState());
		contentValues.put("IsDefault",pInfo.getIsDefault());
		return contentValues;
	}

}
