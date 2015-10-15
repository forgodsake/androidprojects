package com.google.activity.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.R;
import com.google.controls.SlideMenuItem;
import com.google.controls.SlideMenuView;

public class FrameActivity extends BaseActivity {

	private SlideMenuView mSlideMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		View view = findViewById(R.id.ivAppBack);
		OnBackListener onBackListener = new OnBackListener();
		view.setOnClickListener(onBackListener);
	}
	
	private class OnBackListener implements android.view.View.OnClickListener
	{
		public void onClick(View view)
		{
			finish();
		}
	}
	
	protected void hideTitleBackButton()
	{
		findViewById(R.id.ivAppBack).setVisibility(View.GONE);
	}

	protected void addMainBody(int pResID) {
		LinearLayout mainBody = (LinearLayout) findViewById(R.id.mainbody);
		View view = LayoutInflater.from(this).inflate(pResID, null);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mainBody.addView(view, lp);
	}
	
	protected void addMainBody(View pView)
	{
		LinearLayout mainBody = (LinearLayout)findViewById(R.id.mainbody);		
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		mainBody.addView(pView,_LayoutParams);
	}
	

	protected void createSlideMenu(int pResID) {

		mSlideMenu = new SlideMenuView(this);
		String[] menuItem = getResources().getStringArray(pResID);
		for (int i = 0; i < menuItem.length; i++) {
			SlideMenuItem item = new SlideMenuItem(i, menuItem[i]);
			mSlideMenu.add(item);
		}
		mSlideMenu.bindList();
	}

	protected void SlideMenuToggle() {
		mSlideMenu.toggle();
	}
	
	protected void removeBottomBox()
	{
		mSlideMenu = new SlideMenuView(this);
		mSlideMenu.removeBottomBox();
	}
    
	protected void setTopBarTitle(String pText) {
		TextView tvTitle = (TextView) findViewById(R.id.tvTopTitle);
		tvTitle.setText(pText);
	}
	
	protected void CreateMenu(Menu pMenu) {
		int groupID = 0;
		int order = 0;
		int[] pItemID = { 1, 2 };

		for (int i = 0; i < pItemID.length; i++) {
			switch (pItemID[i]) {
			case 1:
				pMenu.add(groupID, pItemID[i], order, R.string.MenuTextEdit);
				break;
			case 2:
				pMenu.add(groupID, pItemID[i], order,
						R.string.MenuTextDelete);
				break;
			default:
				break;
			}
		}
	}
}
