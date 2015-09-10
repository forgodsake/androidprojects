package com.google.wash;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.wash.fragment.BasketFragment;
import com.google.wash.fragment.HomeFragment;
import com.google.wash.fragment.MoreFragment;
import com.google.wash.fragment.UserFragment;

public class MainActivity extends FragmentActivity  {
	
    //通过代码调整radiobutton顶部图片大小

//		Drawable myImage0 = getResources().getDrawable(R.drawable.homebutton);
//		myImage0.setBounds(0, 0, 80, 80);
//		radioButton.setCompoundDrawables(null, myImage0, null, null);

    
	private List<Fragment> fragments;
	private FragmentPagerAdapter adapter;
	private ViewPager viewPager;
	private RadioGroup radioGruop;
	public int x;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		fragments=new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new BasketFragment());
		fragments.add(new UserFragment());
		fragments.add(new MoreFragment());
		
		adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
                
				return fragments.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				
				return fragments.get(arg0);
			}
		};
		
		viewPager=(ViewPager) findViewById(R.id.fragment);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
				RadioButton radioButton=(RadioButton) radioGruop.getChildAt(arg0);
				radioButton.setChecked(true);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			
				
			}
		});
		
		radioGruop=(RadioGroup)findViewById(R.id.radioGroup1);
		radioGruop.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio0:
					viewPager.setCurrentItem(0);
					break;
				case R.id.radio1:
					viewPager.setCurrentItem(1);
					break;
				case R.id.radio2:
					viewPager.setCurrentItem(2);
					break;
				case R.id.radio3:
					viewPager.setCurrentItem(3);
					break;
             	default:
					break;
				}
				
			}
		});
		
		if (getIntent().getIntExtra("num", 0)>0) {
		    viewPager.setCurrentItem(1);   
		}
	}
    
	
	
}
