package com.google.controls;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.R;
import com.google.adapter.SlideMenuAdapter;

public class SlideMenuView {
    private Activity mActivity;
    private List mSlideList;
    private RelativeLayout mBottomBox;
    private boolean mIsClosed;
    private OnSlideMenuListener mOnSlideMenuListener;
    
    public interface OnSlideMenuListener{
    	public abstract void onSlideMenuItemClick(View view,SlideMenuItem slideMenuItem);
    }
    
    public SlideMenuView (Activity pActivity){
    	mActivity=pActivity;
    	initView();
    	if (pActivity instanceof OnSlideMenuListener) {
			mOnSlideMenuListener = (OnSlideMenuListener) pActivity;
			initVariable();
			initListeners();
		}
    }
    
	public void initVariable() {
		mSlideList=new ArrayList();
		mIsClosed=true;
	}

	public void initView() {
		mBottomBox=(RelativeLayout) mActivity.findViewById(R.id.bottom_box);
	}

    public void initListeners(){
    	mBottomBox.setOnClickListener(new OnSlideMenuClick());
    	mBottomBox.setFocusableInTouchMode(true);
    	mBottomBox.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_MENU&&event.getAction()==KeyEvent.ACTION_UP){
					toggle();
				}
				return false;
			}
		});
    }
    
	private void open(){
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.BELOW,R.id.top_title);
		mBottomBox.setLayoutParams(lp);
		mIsClosed=false;
	}
	
	private void close(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		int height=(int) (68*displayMetrics.density)	;
		
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,height);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mBottomBox.setLayoutParams(lp);
		mIsClosed=true;
	}
	
	public void toggle(){
		if(mIsClosed){
			open();
		}else{
			close();
		}
	}
	
	public void removeBottomBox()
	{
		RelativeLayout mainLayout = (RelativeLayout)mActivity.findViewById(R.id.layMainLayout);
		mainLayout.removeView(mBottomBox);
		mBottomBox = null;
	}
	
	public void add(SlideMenuItem pSlideMenuItem){
		mSlideList.add(pSlideMenuItem);
	}
	
	public void bindList(){
		SlideMenuAdapter slideMenuAdapter=new SlideMenuAdapter(mActivity, mSlideList);
		ListView listView=(ListView) mActivity.findViewById(R.id.lv_slideMenu);
		listView.setAdapter(slideMenuAdapter);
		listView.setOnItemClickListener(new OnSlideMenuItemClick());
	}
	
	private class OnSlideMenuItemClick implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SlideMenuItem slideMenuItem=(SlideMenuItem) parent.getItemAtPosition(position);
			mOnSlideMenuListener.onSlideMenuItemClick(mBottomBox, slideMenuItem);
			
		}
		
	}
	
	private class OnSlideMenuClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			toggle();
		}
		
	}

}
