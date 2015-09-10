package com.google.wash;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.google.wash.fragment.SpringFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PieceActivity extends FragmentActivity {
	
    @ViewInject(R.id.viewPagerPiece)
	private ViewPager pager;
    @ViewInject(R.id.tabs)
    private PagerSlidingTabStrip tabStrip;
    private FragmentPagerAdapter adapter;
    private List<Fragment> fragments;
    private String [] titles= new String[]{
    	"春/秋装","夏装","冬装","皮衣"	
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.piece);
		ViewUtils.inject(this);
		fragments = new ArrayList<Fragment>();
        fragments.add(new SpringFragment());
        fragments.add(new SpringFragment());
        fragments.add(new SpringFragment());
        fragments.add(new SpringFragment());
		adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
			
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
		tabStrip.setViewPager(pager);
	}
}
