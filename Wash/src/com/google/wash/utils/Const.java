package com.google.wash.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class Const {
	//建立一个public static的list用来放activity 
    public static List activityList = new ArrayList(); 
    
    
      //finish所有list中的activity 
    public static void exit(){    
        int size=activityList.size();     
        for(int i=0;i<size;i++){        
            if(activityList.get(i)!=null){            
                ((Activity) activityList.get(i)).finish();        
                }     
            } 
    }
}
