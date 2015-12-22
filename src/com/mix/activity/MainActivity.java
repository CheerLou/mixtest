package com.mix.activity;

import java.io.FileNotFoundException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import com.example.mixtest.R;
import com.mix.fragment.FirstFragment;
import com.mix.fragment.FourthFragment;
import com.mix.fragment.SecondFragment;
import com.mix.fragment.ThirdFragment;
import com.mix.utils.MyApplication;

import android.R.integer;
import android.app.Activity;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ActionProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Build;
import android.preference.PreferenceManager;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private ViewPager mViewPager;
	private FragmentPagerAdapter mFragmentPagerAdapter;
	private List<Fragment> mFragments;
	
	private LinearLayout mTabFirstLayout;
	private LinearLayout mTabSecondLayout;
	private LinearLayout mTabThirdLayout;
	private LinearLayout mTabFourthLayout;
	
	private ImageButton mImgFirst;
	private ImageButton mImgSecond;
	private ImageButton mImgThird;
	private ImageButton mImgFourth;
	
	private ImageButton mImgSettings;
	
	private long exitTime;
	
	protected SharedPreferences pref;
	protected SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mImgSettings = (ImageButton) findViewById(R.id.settings);
        mImgSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MyApplication.getContext(), "成功点击设置!此时强制下线！", Toast.LENGTH_SHORT).show();
				editor = pref.edit();
				editor.putBoolean("auto", false);
				editor.commit();
				Intent intent = new Intent(MainActivity.this,SignInActivity.class);
				startActivity(intent);
				finish();
			}
		});
    }
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
    		ExitApp();
    	}
    	return false;
    }
    private void ExitApp() {
		if((System.currentTimeMillis()-exitTime) > 2000){
			Toast.makeText(MyApplication.getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}

	//退出程序销毁，不销毁。后退一次提示toast,后退两次退出程序
   
   
    private void initView(){
    	mTabFirstLayout = (LinearLayout) findViewById(R.id.id_tab_first);
    	mTabSecondLayout = (LinearLayout) findViewById(R.id.id_tab_second);
    	mTabThirdLayout = (LinearLayout) findViewById(R.id.id_tab_third);
    	mTabFourthLayout = (LinearLayout) findViewById(R.id.id_tab_fourth);
    	
    	mImgFirst = (ImageButton) findViewById(R.id.id_tab_button_first);
    	mImgSecond = (ImageButton) findViewById(R.id.id_tab_button_second);
    	mImgThird = (ImageButton) findViewById(R.id.id_tab_button_third);
    	mImgFourth = (ImageButton) findViewById(R.id.id_tab_button_fourth);
    	
    	mViewPager = (ViewPager) findViewById(R.id.viewpager);
    	
    	mFragments = new ArrayList<Fragment>();
    	Fragment mTab01 = new FirstFragment();
    	Fragment mTab02 = new SecondFragment();
    	Fragment mTab03 = new ThirdFragment();
    	Fragment mTab04 = new FourthFragment();
    	mFragments.add(mTab01);
    	mFragments.add(mTab02);
    	mFragments.add(mTab03);
    	mFragments.add(mTab04);
    	
    	mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mFragments.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};
    	
		mViewPager.setAdapter(mFragmentPagerAdapter);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				int currentItem = mViewPager.getCurrentItem();
				setTab(currentItem);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});;
    	
    }
    
    private void initEvent(){
    	mTabFirstLayout.setOnClickListener(this);
    	mTabSecondLayout.setOnClickListener(this);
    	mTabThirdLayout.setOnClickListener(this);
    	mTabFourthLayout.setOnClickListener(this);
    }
    
    private void setTab(int i){
    	setImg();
    	switch (i) {
		case 0:
			mImgFirst.setImageResource(R.drawable.ic_launcher_red);
			break;
		case 1:
			mImgSecond.setImageResource(R.drawable.ic_launcher_red);
			break;
		case 2:
			mImgThird.setImageResource(R.drawable.ic_launcher_red);
			break;
		case 3:
			mImgFourth.setImageResource(R.drawable.ic_launcher_red);
			break;
		default:
			break;
		}
    }
    
    private void setImg(){
    	mImgFirst.setImageResource(R.drawable.ic_launcher);
    	mImgSecond.setImageResource(R.drawable.ic_launcher);
    	mImgThird.setImageResource(R.drawable.ic_launcher);
    	mImgFourth.setImageResource(R.drawable.ic_launcher);
    	
    }
    
    private void setSelect(int i){
    	setTab(i);
    	mViewPager.setCurrentItem(i);
    }
    
    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
		case R.id.id_tab_first:
			setSelect(0);
			break;
		case R.id.id_tab_second:
			setSelect(1);
			break;
		case R.id.id_tab_third:
			setSelect(2);
			break;
		case R.id.id_tab_fourth:
			setSelect(3);
			break;
		default:
			break;
		}
    	
    }
    
    
}
