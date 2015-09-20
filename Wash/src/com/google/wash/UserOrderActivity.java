package com.google.wash;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.wash.fragment.FinishedFragment;
import com.google.wash.fragment.UnSendFragment;
import com.google.wash.fragment.UntakeFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UserOrderActivity extends FragmentActivity{
    
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@ViewInject(R.id.viewPagerOrder)
	private ViewPager pager;
	@ViewInject(R.id.tabs)
	private PagerSlidingTabStrip strip;
	private List<Fragment> fragments;
	private FragmentPagerAdapter adapter;
	private String[] titles = {
			"待取件","待送件","已完成"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.my_order);
	    ViewUtils.inject(this);
	    fragments = new ArrayList<Fragment>();
	    fragments.add(new UntakeFragment());
	    fragments.add(new UnSendFragment());
	    fragments.add(new FinishedFragment());
	    adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return fragments.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return titles[position];
			}
			
		};
		pager.setAdapter(adapter);
		strip.setViewPager(pager);
	}
	@OnClick(R.id.ivBack)
	private void click(View v){
		finish();
	}
}
