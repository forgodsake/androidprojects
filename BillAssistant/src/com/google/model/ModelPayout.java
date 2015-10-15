package com.google.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ModelPayout implements Serializable {
	
	private int mPayoutID;
	
	private int mAccountBookID;
	
	private String mAccountBookName;
	
	private int mCategoryID;
	
	private String mCategoryName;
	
	private String mPath;
	
	private int mPayWayID;
	
	private int mPlaceID;
	
	private BigDecimal mAmount;
	
	private Date mPayoutDate;
	
	private String mPayoutType;
	
	private String mPayoutUserID;
	
	private String mComment;
	
	private Date mCreateDate = new Date();
	
	private int mState = 1;
	
	public int getPayoutID() {
		return mPayoutID;
	}
	
	public void setPayoutID(int pPayoutID) {
		this.mPayoutID = pPayoutID;
	}
	
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
	
	public int getCategoryID() {
		return mCategoryID;
	}
	
	public void setCategoryID(int pCategoryID) {
		this.mCategoryID = pCategoryID;
	}
	
	public String getPath() {
		return mPath;
	}
	
	public void setPath(String pPath) {
		this.mPath = pPath;
	}
	
	public String getCategoryName() {
		return mCategoryName;
	}
	
	public void setCategoryName(String pCategoryName) {
		this.mCategoryName = pCategoryName;
	}
	
	public int getPayWayID() {
		return mPayWayID;
	}
	
	public void setPayWayID(int pPayWayID) {
		this.mPayWayID = pPayWayID;
	}
	
	public int getPlaceID() {
		return mPlaceID;
	}
	
	public void setPlaceID(int pPlaceID) {
		this.mPlaceID = pPlaceID;
	}
	
	public BigDecimal getAmount() {
		return mAmount;
	}
	
	public void setAmount(BigDecimal pAmount) {
		this.mAmount = pAmount;
	}
	
	public Date getPayoutDate() {
		return mPayoutDate;
	}
	
	public void setPayoutDate(Date pPayoutDate) {
		this.mPayoutDate = pPayoutDate;
	}
	
	public String getPayoutType() {
		return mPayoutType;
	}
	
	public void setPayoutType(String pPayoutType) {
		this.mPayoutType = pPayoutType;
	}
	
	public String getPayoutUserID() {
		return mPayoutUserID;
	}
	
	public void setPayoutUserID(String pPayoutUserID) {
		this.mPayoutUserID = pPayoutUserID;
	}	
	
	public String getComment() {
		return mComment;
	}
	
	public void setComment(String pComment) {
		this.mComment = pComment;
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
}
