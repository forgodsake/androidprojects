package com.google.activity;

import java.util.Date;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.google.R;
import com.google.activity.base.FrameActivity;
import com.google.adapter.GridAdapter;
import com.google.business.DataBackupBusiness;
import com.google.controls.SlideMenuItem;
import com.google.controls.SlideMenuView.OnSlideMenuListener;


public class MainActivity extends FrameActivity implements OnSlideMenuListener, OnClickListener{
    private GridView mGridview;
    private GridAdapter mGridAdapter;
    private AlertDialog mDatabaseBackupDialog;
    public DataBackupBusiness mDataBackupBusiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMainBody(R.layout.main_body);
        initVariable();
        initView();
        initListeners();
        bindData();
        hideTitleBackButton();
        createSlideMenu(R.array.SlideMenuActivityMain);
    }
    
    public void initVariable(){
    	mGridAdapter=new GridAdapter(this);
        mDataBackupBusiness=new DataBackupBusiness(this);
    }
    
    public void initView(){
    	mGridview=(GridView) findViewById(R.id.gvappgrid);
    }
    
    public void initListeners(){
    	mGridview.setOnItemClickListener(new OnAppGridItemClickListener());
    }
    
    private class OnAppGridItemClickListener implements OnItemClickListener
    {
    	
		@Override
		public void onItemClick(AdapterView adapterView, View view, int position,long arg3) {
			String menuName = (String)adapterView.getAdapter().getItem(position);
			if(menuName.equals("人员管理"))
			{ 
				openActivity(UserActivity.class);
				return;
			}
			if(menuName.equals("账本管理"))
			{
				openActivity(AccountBookActivity.class);
				return;
			}
			if(menuName.equals("类别管理"))
			{
				openActivity(CategoryActivity.class);
				return;
			}
			if(menuName.equals("记录消费"))
			{
				openActivity(PayoutAddOrEditActivity.class);
				return;
			}
			if(menuName.equals("查询消费"))
			{
				openActivity(PayoutActivity.class);
				return;
			}
			if(menuName.equals("统计管理"))
			{
				openActivity(StatisticsActivity.class);
				return;
			}
	   }
    }
    
    public void bindData(){
    	mGridview.setAdapter(mGridAdapter);
    }
  

	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		SlideMenuToggle();
		if (slideMenuItem.getItemID() == 0) {
			showDatabaseBackupDialog();
		}
	}
	
	private void showDatabaseBackupDialog()
	{
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		
		View view = layoutInflater.inflate(R.layout.database_backup, null);
		
		Button btnDatabaseBackup = (Button)view.findViewById(R.id.btnDatabaseBackup);
		Button btnDatabaseRestore = (Button)view.findViewById(R.id.btnDatabaseRestore);
		
		btnDatabaseBackup.setOnClickListener(this);
		btnDatabaseRestore.setOnClickListener(this);
		
		String title = getString(R.string.DialogTitleDatabaseBackup);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setView(view);
		builder.setIcon(R.drawable.database_backup);
		builder.setNegativeButton(getString(R.string.ButtonTextBack), null);
		mDatabaseBackupDialog = builder.show();
	}
	
	private void databaseBackup()
	{
		if(mDataBackupBusiness.DatabaseBackup(new Date()))
		{
			showMsg(R.string.DialogMessageDatabaseBackupSucceed);
		}
		else {
			showMsg(R.string.DialogMessageDatabaseBackupFail);
		}
		
		mDatabaseBackupDialog.dismiss();
	}
	
	private void databaseRestore()
	{
		if(mDataBackupBusiness.DatabaseRestore())
		{
			showMsg(R.string.DialogMessageDatabaseRestoreSucceed);
		}
		else {
			showMsg(R.string.DialogMessageDatabaseRestoreFail);
		}
		
		mDatabaseBackupDialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDatabaseBackup:
			databaseBackup();
			break;
		case R.id.btnDatabaseRestore:
			databaseRestore();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		 //进行横竖屏判断并修改布局
		 if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){ 
		    setContentView(R.layout.activity_main);  
		    addMainBody(R.layout.main_body_landscape);
		    initVariable();
	        initView();
	        initListeners();
	        bindData();
	        hideTitleBackButton();
	        removeBottomBox();
	     }else{  
	        setContentView(R.layout.activity_main);
		    addMainBody(R.layout.main_body);
		    initVariable();
	        initView();
	        initListeners();
	        bindData();
	        hideTitleBackButton();
	        createSlideMenu(R.array.SlideMenuActivityMain);
	     }
	}
}
