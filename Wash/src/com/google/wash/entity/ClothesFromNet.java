package com.google.wash.entity;

import java.util.List;



public class ClothesFromNet {
	
    public List<WashInfo> washInfo;
    
    public static class WashInfo{
    	
    	private String WashHead;
    	private String WashName;
    	private String Amount;
    	
    	public String getWashHead() {
			return WashHead;
		}
		public void setWashHead(String washHead) {
			WashHead = washHead;
		}
		public String getWashName() {
			return WashName;
		}
		public void setWashName(String washName) {
			WashName = washName;
		}
		public String getAmount() {
			return Amount;
		}
		public void setAmount(String amount) {
			Amount = amount;
		}
    	
    }
}
