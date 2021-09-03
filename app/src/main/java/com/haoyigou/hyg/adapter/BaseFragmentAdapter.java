package com.haoyigou.hyg.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.haoyigou.hyg.application.MApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @ explain:
 * @ author：witness on 2016/4/28 17:34
 */
public class BaseFragmentAdapter extends FragmentStatePagerAdapter {

    protected List<Fragment> mFragmentList;

    protected List<String> mTitles;


    private FragmentManager fm;

    public BaseFragmentAdapter(FragmentManager fm) {
        this(fm, null, null);
    }

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> mTitles) {
        super(fm);
        this.fm = fm;
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        this.mFragmentList = fragmentList;
        this.mTitles = mTitles;
    }

    public void add(Fragment fragment) {
        if (isEmpty()) {
            mFragmentList = new ArrayList<>();

        }
        mFragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        //        Logger.i("BaseFragmentAdapter position=" +position);
        return isEmpty() ? null : mFragmentList.get(position);
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        fm.beginTransaction().show(fragment).commit();
        MApplication.classifyParentLocation = "101";
        return fragment;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : mFragmentList.size();
    }

    public boolean isEmpty() {
        return mFragmentList == null;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container,position,object);
        fm.beginTransaction().hide(mFragmentList.get(position)).commit();
    }

    /*  @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }*/


}
