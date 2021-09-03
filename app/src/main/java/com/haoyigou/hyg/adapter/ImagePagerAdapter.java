package com.haoyigou.hyg.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


public class ImagePagerAdapter extends FragmentStatePagerAdapter {
	private List<String>list;
	 public ImagePagerAdapter(FragmentManager fm, List<String> list) {
		super(fm);
		this.list=list;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		//return new ImageDisplayFragment().create(list.get(position),position);
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return PagerAdapter.POSITION_NONE;
			}
	public void changeList(List<String>list){
		this.list=list;
	}
	
}
