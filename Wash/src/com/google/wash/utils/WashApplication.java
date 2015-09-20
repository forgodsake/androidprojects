package com.google.wash.utils;


import android.app.Application;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

public class WashApplication extends Application{
    
	  @Override
	     public void onCreate() {
	         super.onCreate();
	         JPushInterface.setDebugMode(true);
	         JPushInterface.init(this);
	         SMSSDK.initSDK(this, "a65806b47e40", "02b6a2279b20f530cd303c7b96d3e9ea ");
	     }
}
