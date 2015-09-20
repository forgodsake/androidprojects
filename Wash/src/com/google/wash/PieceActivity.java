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
import com.google.wash.fragment.SpringFragment;
import com.google.wash.fragment.Spring_net_Fragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PieceActivity extends FragmentActivity {
	
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
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
        fragments.add(new Spring_net_Fragment());
        fragments.add(new Spring_net_Fragment());
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
	@OnClick(R.id.ivBack)
	private void click(View v){
		finish();
	}
	
}
