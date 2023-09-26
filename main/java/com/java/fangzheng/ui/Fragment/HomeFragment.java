package com.java.fangzheng.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.java.fangzheng.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements BottomCategoryFragment.DataChangeListener {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitlesOn =  new ArrayList<>();
    private List<String> mTitlesOff =  new ArrayList<>();
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private MyPagerAdapter myPagerAdapter;
    private ImageButton imageButton;

    @Override
    public void onDataChange(List<String> TitlesOn, List<String> TitlesOff) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        for (Fragment fragment : mFragments) {
            ft.remove(fragment);
        }
        ft.commitNow();
        mFragments.clear();
        for(String title : mTitlesOn){
            mFragments.add(NewsFragment.newInstance(title));
        }
        viewPager.setAdapter(myPagerAdapter);
        slidingTabLayout.notifyDataSetChanged();
        slidingTabLayout.onPageSelected(0);
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        String[] mTitle = {
                "推荐", "娱乐", "军事", "教育", "文化",
                "健康", "财经", "体育", "汽车", "科技", "社会"};
        Collections.addAll(fragment.mTitlesOn,mTitle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = v.findViewById(R.id.vp);
        slidingTabLayout = v.findViewById(R.id.SlidingTabLayout);
        imageButton = v.findViewById(R.id.imageButton);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for(String title : mTitlesOn){
            mFragments.add(NewsFragment.newInstance(title));
        }
        viewPager.setOffscreenPageLimit(mFragments.size());
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        slidingTabLayout.setViewPager(viewPager);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomCategoryFragment mBottomCategory = BottomCategoryFragment.newInstance(mTitlesOn,mTitlesOff);
                View view = getLayoutInflater().inflate(R.layout.fragment_bottom_category, null);
                mBottomCategory.setDataChangeListener(HomeFragment.this);
                mBottomCategory.show(getActivity().getSupportFragmentManager(), "bottom_category");
            }
        });
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
        public CharSequence getPageTitle(int position) {return mTitlesOn.get(position);}

        @Override
        public Fragment getItem(int position) {return mFragments.get(position);}
    }
}