package com.google.wash.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Shared {
	private static final String FILE_NAME="myShared";
	private static final String MODE_NAME="welcome";
	
	public static void putWelcome(Context context,boolean isFirst){
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
	    editor.putBoolean(MODE_NAME, isFirst);
	    editor.commit();
	}
	
	public static boolean getWelcome(Context context){
		boolean isFirst=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getBoolean(MODE_NAME, true);
		return isFirst;
	}
    
	public static void putCityName(Context context,String cityName) {
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
		editor.putString("cityname", cityName);
		editor.commit();
	}
	
	public static String getCityName(Context context){
		String cityname=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("cityname", "获取位置");
	    return cityname;
	}
}
