package com.google.database.sqlitedal;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.R;
import com.google.database.base.SQLiteDALBase;
import com.google.model.ModelCategory;
import com.google.utility.DateTools;

public class SQLiteDALCategory extends SQLiteDALBase {

	public SQLiteDALCategory(Context pContext) {
		super(pContext);
	}

	public Boolean insertCategory(ModelCategory pInfo) {
		ContentValues contentValues = creatParams(pInfo);
		Long pNewID = getDataBase().insert("Category", null, contentValues);
		pInfo.setCategoryID(pNewID.intValue());
		return pNewID > 0;
	}
	
	public Boolean deleteCategory(String pCondition)
	{
		return delete(getTableNameAndPK()[0], pCondition);
	}
	
	public Boolean updateCategory(String pCondition, ModelCategory pInfo)
	{
		ContentValues contentValues = creatParams(pInfo);
		return getDataBase().update("Category", contentValues, pCondition, null) > 0;
	}
	
	public Boolean updateCategory(String pCondition,ContentValues pContentValues)
	{
		return getDataBase().update("Category", pContentValues, pCondition, null) > 0;
	}
	
	public List<ModelCategory> getCategory(String pCondition)
	{
		String _SqlText = "Select * From Category Where  1=1 " + pCondition;
		return getList(_SqlText);
	}
	
	@Override
	protected ModelCategory findModel(Cursor pCursor)
	{
		ModelCategory modelCategory = new ModelCategory();
		modelCategory.setCategoryID(pCursor.getInt(pCursor.getColumnIndex("CategoryID")));
		modelCategory.setCategoryName(pCursor.getString(pCursor.getColumnIndex("CategoryName")));
		modelCategory.setTypeFlag(pCursor.getString(pCursor.getColumnIndex("TypeFlag")));
		modelCategory.setParentID(pCursor.getInt(pCursor.getColumnIndex("ParentID")));
		modelCategory.setPath(pCursor.getString(pCursor.getColumnIndex("Path")));
		Date createDate = DateTools.getDate(pCursor.getString(pCursor.getColumnIndex("CreateDate")), "yyyy-MM-dd HH:mm:ss");	
		modelCategory.setCreateDate(createDate);
		modelCategory.setState((pCursor.getInt(pCursor.getColumnIndex("State"))));
		
		return modelCategory;
	}
	
	private void initDefaultData(SQLiteDatabase pDataBase)
	{
		ModelCategory modelCategory = new ModelCategory();
		
		modelCategory.setTypeFlag(getContext().getString((R.string.PayoutTypeFlag)));
		modelCategory.setPath("");
		modelCategory.setParentID(0);
		String initDefaultCategoryNameArr[] = getContext().getResources().getStringArray(R.array.InitDefaultCategoryName);
		for(int i=0;i<initDefaultCategoryNameArr.length;i++)
		{
			modelCategory.setCategoryName(initDefaultCategoryNameArr[i]);
			
			ContentValues contentValues = creatParams(modelCategory);
			Long _NewID = pDataBase.insert("Category", null, contentValues);
			
			modelCategory.setPath(_NewID.intValue() + ".");
			contentValues = creatParams(modelCategory);
			pDataBase.update("Category", contentValues, " CategoryID = " + _NewID.intValue(), null);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase pDataBase) {
		StringBuilder createTableScript = new StringBuilder();
		createTableScript.append("Create  TABLE Category(");
		createTableScript.append("[CategoryID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		createTableScript.append(",[CategoryName] varchar(20) NOT NULL");
		createTableScript.append(",[TypeFlag] varchar(20) NOT NULL");
		createTableScript.append(",[ParentID] integer NOT NULL");
		createTableScript.append(",[Path] text NOT NULL");
		createTableScript.append(",[CreateDate] datetime NOT NULL");
		createTableScript.append(",[State] integer NOT NULL");
		createTableScript.append(")");
		
		pDataBase.execSQL(createTableScript.toString());
		initDefaultData(pDataBase);
	}

	@Override
	public void onUpgrade(SQLiteDatabase pDataBase) {
		
	}

	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{"Category","CategoryID"};
	}
	
	public ContentValues creatParams(ModelCategory pInfo) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("CategoryName", pInfo.getCategoryName());
		contentValues.put("TypeFlag", pInfo.getTypeFlag());
		contentValues.put("ParentID", pInfo.getParentID());
		contentValues.put("Path", pInfo.getPath());
		contentValues.put("CreateDate",DateTools.getFormatDateTime(pInfo.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		contentValues.put("State",pInfo.getState());
		return contentValues;
	}

}
