package com.google.business;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.google.business.base.BusinessBase;
import com.google.database.sqlitedal.SQLiteDALUser;
import com.google.model.ModelUser;

public class UserBusiness extends BusinessBase{
    
	private SQLiteDALUser mDalUser;
	
	public UserBusiness(Context pContext) {
		super(pContext);
		mDalUser=new SQLiteDALUser(pContext);
	}
    
	public boolean insertUser(ModelUser pModelUser){
		return mDalUser.insertUser(pModelUser);
	}
	
	public boolean deleteUserByUserID(int pUserID){
		String condition="AND UserID="+pUserID;
		return mDalUser.deleteUser(condition);
	}
	
	public boolean updateUserByUserID(ModelUser pModelUser){
		String condition= " UserID="+pModelUser.getUserID();
		return mDalUser.updateUser(pModelUser, condition);
	}
	 
	public List<ModelUser> getNotHideUser()
	{
		return  mDalUser.getUser(" And State = 1");
	}
	
	private List<ModelUser> getUser(String pCondition){
		return mDalUser.getUser(pCondition);
	}
	
	public ModelUser getModelUserByUserID(int pUserID){
		List<ModelUser> list= mDalUser.getUser("AND UserID= "+pUserID);
	    if (list.size()==1) {
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public List<ModelUser> getUserListByUserID(String [] pUserID){
		List<ModelUser> list = new ArrayList<ModelUser>();
		for (int i = 0; i < pUserID.length; i++) {
			list.add(getModelUserByUserID(Integer.valueOf(pUserID[i])));
		}
		return list;
	}
	
	public Boolean HideUserByUserID(int pUserID)
	{
		String condition = " UserID = " + pUserID;
		ContentValues contentValues = new ContentValues();
		contentValues.put("State",0);
		Boolean result = mDalUser.updateUser(condition, contentValues);

		if(result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isExistUserByUserName(String pUserName,Integer pUserID)
	{
		String condition = " And UserName = '" + pUserName + "'";
		if(pUserID != null)
		{
			condition += " And UserID <> " + pUserID;
		}
		List _List = mDalUser.getUser(condition);
		if (_List.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getUserNameByUserID(String p_UserID)
	{
		List<ModelUser> _List = getUserListByUserID(p_UserID.split(","));
		String _Name = "";
		
		for(int i=0;i<_List.size();i++)
		{
			_Name += _List.get(i).getUserName() + ",";
		}
		return _Name;
	}
}
