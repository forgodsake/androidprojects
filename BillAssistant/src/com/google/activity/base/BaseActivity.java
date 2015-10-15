package com.google.activity.base;

import java.lang.reflect.Field;

import com.google.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class BaseActivity extends Activity {
    
	private ProgressDialog mProgressDialog;
	
	protected void openActivity(Class<?>  pClass){
    	Intent intent=new Intent();
    	intent.setClass(this, pClass);
    	startActivity(intent);
    }
    
    protected void showMsg(String pString){
    	Toast.makeText(this, pString, 1000).show();
    }
    
    protected void showMsg(int pResID) {
		Toast.makeText(this, pResID, 1000).show();
	}
    
    protected AlertDialog showAlertDialog(int pTitelResID,String pMessage,DialogInterface.OnClickListener pClickListener)
	{
		String title = getResources().getString(pTitelResID);
		return showAlertDialog(title, pMessage, pClickListener);
	}
    
    protected AlertDialog showAlertDialog(String pTitle,String pMessage,DialogInterface.OnClickListener pClickListener)
	{		
		return new AlertDialog.Builder(this)
		.setTitle(pTitle)
		.setMessage(pMessage)
		.setPositiveButton(R.string.ButtonTextYes, pClickListener)
		.setNegativeButton(R.string.ButtonTextNo, null)
		.show();
	}
    
    public void setAlertDialogIsClose(DialogInterface pDialog,Boolean pIsClose)
	{
		try {
			Field field = pDialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
		    field.set(pDialog, pIsClose);
		} catch (Exception e) {
		}
	}
    
	protected void ShowProgressDialog(int p_TitleResID,int p_MessageResID) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(getString(p_TitleResID));
		mProgressDialog.setMessage(getString(p_MessageResID));
		mProgressDialog.show();
	}

	protected void DismissProgressDialog() {
		if(mProgressDialog != null)
		{
			mProgressDialog.dismiss();
		}
	}
  
}
