package com.google.model;

import java.io.Serializable;
import java.util.Date;

public class ModelCategory implements Serializable {
	
	private int m_CategoryID;
	
	private String m_CategoryName;
	
	private String m_TypeFlag;
	
	private int m_ParentID = 0;
	
	private String m_Path;	
	
	private Date m_CreateDate = new Date();
	
	private int m_State = 1;
	
	public int getCategoryID() {
		return m_CategoryID;
	}
	
	public void setCategoryID(int p_CategoryID) {
		this.m_CategoryID = p_CategoryID;
	}
	
	public String getCategoryName() {
		return m_CategoryName;
	}
	
	public void setCategoryName(String p_CategoryName) {
		this.m_CategoryName = p_CategoryName;
	}
	
	public String getTypeFlag() {
		return m_TypeFlag;
	}
	
	public void setTypeFlag(String p_TypeFlag) {
		this.m_TypeFlag = p_TypeFlag;
	}
	
	public int getParentID() {
		return m_ParentID;
	}
	
	public void setParentID(int p_ParentID) {
		this.m_ParentID = p_ParentID;
	}
	
	public String getPath() {
		return m_Path;
	}
	
	public void setPath(String p_Path) {
		this.m_Path = p_Path;
	}
	
	public Date getCreateDate() {
		return m_CreateDate;
	}
	
	public void setCreateDate(Date p_CreateDate) {
		this.m_CreateDate = p_CreateDate;
	}
	
	public int getState() {
		return m_State;
	}
	
	public void setState(int p_State) {
		this.m_State = p_State;
	}
	
	@Override
	public String toString() {
		return m_CategoryName;
	}
}
