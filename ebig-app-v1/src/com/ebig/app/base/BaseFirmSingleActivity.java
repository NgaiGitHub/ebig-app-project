package com.ebig.app.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ebig.app.R;

/**
 * 快速组建单一  FragmentActivity
 * @author HungLau.Ngai
 *
 */
public abstract class BaseFirmSingleActivity extends BaseFirmActivity {

	abstract protected Fragment createFragment();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_fragment);
		FragmentManager fm = getSupportFragmentManager();
		Fragment mFragment = fm.findFragmentById(R.id.fragmentContainer);
		if (mFragment == null) {
			mFragment = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, mFragment).commit();
		}
	}

}
