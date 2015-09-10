package com.google.wash.entity;

import com.google.wash.R;

public class Clothes {
    
	 public int[] getImages() {
		return images;
	}
	public void setImages(int[] images) {
		this.images = images;
	}
	public String[] getNames() {
		return names;
	}
	public void setNames(String[] names) {
		this.names = names;
	}
	public String[] getPrices() {
		return prices;
	}
	public void setPrices(String[] prices) {
		this.prices = prices;
	}
	//初始化填充数据
    private int []images = new int []{
    		R.drawable.tshirt,R.drawable.shirt,R.drawable.windbreak,
    		R.drawable.suit,R.drawable.longwindbreak,R.drawable.sport,
    		R.drawable.pacakge
    	};
    private String [] names = new String[]{
    			"T恤","衬衫","短风衣","西装","长风衣","运动衣","袋洗"
    	};
    private String [] prices = new String[]{
    			"8","8","12","18","18","12","68"
    	};
}
