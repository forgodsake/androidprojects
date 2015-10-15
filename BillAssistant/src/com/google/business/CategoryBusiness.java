package com.google.business;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import com.google.R;
import com.google.business.base.BusinessBase;
import com.google.database.sqlitedal.SQLiteDALCategory;
import com.google.model.ModelCategory;
import com.google.model.ModelCategoryTotal;

public class CategoryBusiness extends BusinessBase {
	
	private SQLiteDALCategory mSqLiteDALCategory;
	private final String  TYPE_FLAG = " And TypeFlag= '" + getString(R.string.PayoutTypeFlag) + "'";
	
	public CategoryBusiness(Context pContext)
	{
		super(pContext);
		mSqLiteDALCategory = new SQLiteDALCategory(pContext);
	}
	
	public Boolean insertCategory(ModelCategory pInfo)
	{
		mSqLiteDALCategory.beginTransaction();
		try {
			Boolean result = mSqLiteDALCategory.insertCategory(pInfo);
			Boolean result2 = true;

			ModelCategory _ParentModelCategory = getModelCategoryByCategoryID(pInfo.getParentID());
			String path;
			if(_ParentModelCategory != null)
			{
				path = _ParentModelCategory.getPath() + pInfo.getCategoryID() + ".";
			}
			else {
				path = pInfo.getCategoryID() + ".";
			}
			
			pInfo.setPath(path);
			result2 = updateCategoryInsertTypeByCategoryID(pInfo);
			
			if(result && result2)
			{
				mSqLiteDALCategory.setTransactionSuccessful();
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSqLiteDALCategory.endTransaction();
		}
	}
	
	public Boolean deleteCategoryByCategoryID(int p_CategoryID)
	{
		String condition = " CategoryID = " + p_CategoryID;
		Boolean result = mSqLiteDALCategory.deleteCategory(condition);
		return result;
	}
	
	public Boolean deleteCategoryByPath(String pPath) throws Exception
	{
		int count = mSqLiteDALCategory.getCount("PayoutID", "v_Payout", " And Path Like '" + pPath + "%'");
		
		if(count != 0)
		{
			throw new Exception(getString(R.string.ErrorMessageExistPayout));
		}
		
		String condition = " And Path Like '" + pPath + "%'";
		Boolean result = mSqLiteDALCategory.deleteCategory(condition);
		return result;
		
	}
	
	public Boolean updateCategoryInsertTypeByCategoryID(ModelCategory p_Info)
	{
		String condition = " CategoryID = " + p_Info.getCategoryID();
		Boolean result = mSqLiteDALCategory.updateCategory(condition, p_Info);		
		
		if(result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public Boolean updateCategoryByCategoryID(ModelCategory pInfo)
	{
		mSqLiteDALCategory.beginTransaction();
		try {
			String condition = " CategoryID = " + pInfo.getCategoryID();
			Boolean result = mSqLiteDALCategory.updateCategory(condition, pInfo);
			Boolean result2 = true;
			

			ModelCategory parentModelCategory = getModelCategoryByCategoryID(pInfo.getParentID());
			String path;
			if(parentModelCategory != null)
			{
				path = parentModelCategory.getPath() + pInfo.getCategoryID() + ".";
			}
			else {
				path = pInfo.getCategoryID() + ".";
			}
			
			pInfo.setPath(path);
			result2 = updateCategoryInsertTypeByCategoryID(pInfo);
			
			if(result && result2)
			{
				mSqLiteDALCategory.setTransactionSuccessful();
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSqLiteDALCategory.endTransaction();
		}
	}
	
	public Boolean hideCategoryByByPath(String p_Path)
	{
		String condition = " Path Like '" + p_Path + "%'";
		ContentValues contentValues = new ContentValues();
		contentValues.put("State",0);
		Boolean result = mSqLiteDALCategory.updateCategory(condition, contentValues);

		if(result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public List<ModelCategory> getCategory(String pcondition)
	{
		return mSqLiteDALCategory.getCategory(pcondition);
	}
	
	public List<ModelCategory> getNotHideCategory()
	{
		return mSqLiteDALCategory.getCategory(TYPE_FLAG + " And State = 1");
	}
	
	public int getNotHideCount()
	{
		return mSqLiteDALCategory.getCount(TYPE_FLAG + " And State = 1");
	}
	
	public int getNotHideCountByParentID(int p_CategoryID)
	{
		return mSqLiteDALCategory.getCount(TYPE_FLAG + " And ParentID = " + p_CategoryID + " And State = 1");
	}
	
	public List<ModelCategory> getNotHideRootCategory()
	{
		return mSqLiteDALCategory.getCategory(TYPE_FLAG + " And ParentID = 0 And State = 1");
	}
	
	public List<ModelCategory> getNotHideCategoryListByParentID(int p_ParentID)
	{
		return mSqLiteDALCategory.getCategory(TYPE_FLAG + " And ParentID = " + p_ParentID + " And State = 1");
	}
	
	public ModelCategory getModelCategoryByParentID(int p_ParentID)
	{
		List list = mSqLiteDALCategory.getCategory(TYPE_FLAG + " And ParentID = " + p_ParentID);
		if(list.size() == 1)
		{
			return (ModelCategory)list.get(0);
		}
		else
		{
			return null;
		}
	}
	
	public ModelCategory getModelCategoryByCategoryID(int p_CategoryID)
	{
		List list = mSqLiteDALCategory.getCategory(TYPE_FLAG + " And CategoryID = " + p_CategoryID);
		if(list.size() == 1)
		{
			return (ModelCategory)list.get(0);
		}
		else
		{
			return null;
		}
	}
	
	public ArrayAdapter getRootCategoryArrayAdapter()
	{
		List list = getNotHideRootCategory();
		list.add(0,"--Please Choose--");
		ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, list);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return arrayAdapter;
	}
	
	public ArrayAdapter getAllCategoryArrayAdapter()
	{
		List list = getNotHideCategory();
		String _Name[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			_Name[i] = ((ModelCategory)list.get(i)).getCategoryName();
		}
		ArrayAdapter _ArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, list);
		return _ArrayAdapter;
		
	}
	
	public List<ModelCategoryTotal> categoryTotalByRootCategory()
	{
		String condition = TYPE_FLAG + " And ParentID = 0 And State = 1";
		List<ModelCategoryTotal> modelCategoryTotalList = categoryTotal(condition);
		
		return modelCategoryTotalList;
	}
	
	public List<ModelCategoryTotal> categoryTotalByParentID(int p_ParentID)
	{
		String condition = TYPE_FLAG + " And ParentID = " + p_ParentID;
		List<ModelCategoryTotal> modelCategoryTotalList = categoryTotal(condition);
		
		return modelCategoryTotalList;
	}
	
	public List<ModelCategoryTotal> categoryTotal(String pcondition)
	{
		String condition = pcondition;
		Cursor cursor = mSqLiteDALCategory.execSql("Select Count(PayoutID) As Count, Sum(Amount) As SumAmount, CategoryName From v_Payout Where 1=1 " + condition + " Group By CategoryName");
		List<ModelCategoryTotal> modelCategoryTotalList = new ArrayList<ModelCategoryTotal>();
		while (cursor.moveToNext()) {
			ModelCategoryTotal modelCategoryTotal = new ModelCategoryTotal();
			modelCategoryTotal.Count = cursor.getString(cursor.getColumnIndex("Count"));
			modelCategoryTotal.SumAmount = cursor.getString(cursor.getColumnIndex("SumAmount"));
			modelCategoryTotal.CategoryName = cursor.getString(cursor.getColumnIndex("CategoryName"));
			modelCategoryTotalList.add(modelCategoryTotal);
		}
		
		return modelCategoryTotalList;
	}
}
