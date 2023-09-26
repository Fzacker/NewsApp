package com.java.fangzheng.ui;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.java.fangzheng.R;
import com.java.fangzheng.Bean.TabEntity;
import com.java.fangzheng.ui.Fragment.HomeFragment;
import com.java.fangzheng.ui.Fragment.ProfileFragment;
import com.java.fangzheng.ui.Fragment.SearchFragment;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {
    private Context mContext;
    private String[] mTitles = {"首页", "分类", "我的"};
    private int[] mIconUnselectIds = {R.mipmap.home_selected, R.mipmap.search_selected, R.mipmap.profile_selected};
    private int[] mIconSelectIds = {R.mipmap.home_unselected, R.mipmap.search_unselected, R.mipmap.profile_unselected};

    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private CommonTabLayout commonTabLayout;

    @Override
    public int initView() {
        return R.layout.activity_home;
    }

    @Override
    public void initData() {
        mViewPager = findViewById(R.id.viewPager);
        commonTabLayout = findViewById(R.id.commonTabLayout);
    }

    @Override
    public void initListener() {
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(SearchFragment.newInstance());
        mFragments.add(ProfileFragment.newInstance());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}