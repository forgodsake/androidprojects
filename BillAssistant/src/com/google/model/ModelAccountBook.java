package com.google.model;

import java.util.Date;

public class ModelAccountBook {
	//账本表主键ID
	private int mAccountBookID;
	//账本名称
	private String mAccountBookName;
	//创建日期
	private Date mCreateDate = new Date();
	//账本状态
	private int mState = 1;
	//是否为默认账本 0否1是
	private int mIsDefault;
	
	public int getAccountBookID() {
		return mAccountBookID;
	}
	
	public void setAccountBookID(int pAccountBookID) {
		this.mAccountBookID = pAccountBookID;
	}
	
	public String getAccountBookName() {
		return mAccountBookName;
	}
	
	public void setAccountBookName(String pAccountBookName) {
		this.mAccountBookName = pAccountBookName;
	}
	
	public Date getCreateDate() {
		return mCreateDate;
	}
	
	public void setCreateDate(Date pCreateDate) {
		this.mCreateDate = pCreateDate;
	}
	
	public int getState() {
		return mState;
	}
	
	public void setState(int pState) {
		this.mState = pState;
	}
	
	public int getIsDefault() {
		return mIsDefault;
	}
	
	public void setIsDefault(int pIsDefault) {
		this.mIsDefault = pIsDefault;
	}
}
