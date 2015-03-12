package com.ebig.app.activity.auditmgr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmActivity;
import com.ebig.app.dbctrl.AuditMgrDbCtrl;
import com.slidinglayer.SlidingLayer;

public class AuditActivity extends BaseFirmActivity {

	static protected SlidingLayer mSlidingLayer;
	private FragmentManager fm ;
	static protected FilterFragment mFilterFragment;
	
	//private RecordedFragment mRedFragment;// 历史记录
	static protected RecordFragment mRFragment;// 待审批记录
	protected AuditMgrDbCtrl mAuditDbCtrl ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_auditmgr);
		fm = getSupportFragmentManager();
		findFilterLayer();
		findViews();
		
		if(mRFragment == null)
			mRFragment = RecordFragment.newIntance(null);
		showFragment(mRFragment);
		mSlidingLayer.openLayer(true);
	}

	private void findFilterLayer() {
		mSlidingLayer = (SlidingLayer) findViewById(R.id.sliding_layer);
		if (mFilterFragment == null)
			mFilterFragment = FilterFragment.newIntance(null);
		fm.beginTransaction().add(mSlidingLayer.getId(),mFilterFragment).commit();
	}
	

	private void findViews() {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		setDefaultTitle("审批管理");
		//getMenuInflater().inflate(R.menu.audit_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(mSlidingLayer.isOpened()) return false;
		
		switch (item.getItemId()) {
		case R.id.auditmgr_finish:
//			if(mRedFragment == null)
//				mRedFragment = RecordedFragment.newIntance(null);
//			showFragment(mRedFragment);
//			mRedFragment.refashFragment();
//			if(mRFragment == null)
//				mRFragment = RecordFragment.newIntance(null);
			//showFragment(mRFragment);
			mRFragment.refashFragment(true);
			break;
		case R.id.auditmgr_wait:
//			if(mRFragment == null)
//				mRFragment = RecordFragment.newIntance(null);
			//showFragment(mRFragment);
			mRFragment.refashFragment(false);
			break;
		case R.id.auditmgr_group:
			mSlidingLayer.openLayer(true);
			break;

		default:
			break;
		}
		return true;
	}

	private void showFragment(Fragment fragment) {
		FragmentTransaction mTransaction = getSupportFragmentManager()
				.beginTransaction();
		mTransaction.setCustomAnimations(R.anim.flip_horizontal_in,R.anim.flip_horizontal_out);
		if(!fragment.isAdded()){
			mTransaction.add(R.id.fragmentContainer,fragment);			
		}else {
			//mTransaction.hide(mRedFragment);
			mTransaction.hide(mRFragment);
			
			mTransaction.show(fragment);
		}
		mTransaction.commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AuditMgrDbCtrl.clear();
	}
	
	public void actionReLoading(View v){
		// 没有数据UI
		View notDateLayer = findViewById(R.id.not_date_layer);
		notDateLayer.setVisibility(View.GONE);
		mRFragment.refashFragment(false);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mRFragment.refashFragment();
	}
}
