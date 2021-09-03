package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.haoyigou.hyg.R;
import java.util.List;


/**
 * Created by zhoutong on 2017/11/17
 */
public class NewViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] titles;
    private Context mcontext;

    public NewViewPagerAdapter(FragmentManager fm, String[] titles, List<Fragment> fragments, Context context) {
        super(fm,fragments);
        this.titles = titles;
        this.fragments = fragments;
        this.mcontext = context;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public View getTabView(int position) {
        View tabView = LayoutInflater.from(mcontext).inflate(R.layout.tablayout_item, null);
        TextView tabTitle = (TextView) tabView.findViewById(R.id.tv_tab_title);
        tabTitle.setText(titles[position]);
        return tabView;
    }
}