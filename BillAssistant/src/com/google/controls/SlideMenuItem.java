package com.google.controls;


public class SlideMenuItem {
	private int mItemID;
	private String mTitle;
    
    public SlideMenuItem(int pItemID,String pTitle){
    	mItemID=pItemID;
    	mTitle=pTitle;
    }
   public int getItemID() {
		return mItemID;
	}
	public void setItemID(int pItemID) {
		this.mItemID = pItemID;
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String pTitle) {
		this.mTitle = pTitle;
	}

}
