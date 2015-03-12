package com.ebig.app.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ebig.app.R;
import com.tpos.widget.pagertab.PagerSlidingTabStrip.IconTextTabProvider;

/**
 * FragmentStatePagerAdapter �� FragmentPagerAdapter
 * �����������ǰ����fragment���׸ɵ���ÿһ�εķ�ҳ����getItem(),�����µ�Fragment;
 * ���߾ͱ������ڴ��У��ȴ��´ε���.
 * @author HungLau.Ngai
 * 
 */
public class AppPageAdapter extends FragmentPagerAdapter implements IconTextTabProvider/*FragmentStatePagerAdapter*/ {

//	private FragmentManager mFragmentManager;
//	private Fragment mFragment;
	private String[] mFragmentCls;
//	private LinkedList<Fragment> mFragments;
	private String[] mTitles;
	private Context mContext;
	
	// δѡ��ͼ��
	private Integer[] mIcons = new Integer[] { R.drawable.funs,
			R.drawable.contacts, R.drawable.ebig,
			R.drawable.isacc };
	// ��ѡ��ͼ��
	private Integer[] mIconsSel = new Integer[] { R.drawable.funs_sel,
			R.drawable.contacts_sel, R.drawable.ebig_sel,
			R.drawable.isacc_sel };

	public AppPageAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	    mTitles = mContext.getResources().getStringArray(R.array.pageTabTitle);
	    mFragmentCls = mContext.getResources().getStringArray(R.array.pageTabFragmentCls);
//	    mFragmentManager = fm;
//	    mFragments  = new LinkedList<Fragment>();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}

	@Override
	public int getPageIconResId(int position) {
		return mIcons[position];
	}
	
	@Override
	public int getPageIconSelResId(int position) {
		return mIconsSel[position];
	}

	
	
	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public Fragment getItem(int position) { 
		return Fragment.instantiate(mContext, mFragmentCls[position], new Bundle(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment mFragment = (Fragment) super.instantiateItem(container, position);
//		Bundle mBundle = new Bundle();
//		mBundle.putInt("position", position);
//		mFragment.setArguments(mBundle);
		return mFragment;
	}
 
}
