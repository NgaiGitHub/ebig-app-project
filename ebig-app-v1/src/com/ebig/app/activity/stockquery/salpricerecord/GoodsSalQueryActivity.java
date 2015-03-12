package com.ebig.app.activity.stockquery.salpricerecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmActivity;

/**
 * 商品流向查询主界面
 * @author Billow
 *
 */
public class GoodsSalQueryActivity extends BaseFirmActivity {

	private GoodsSalQueryFragment mFragment;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_fragment);
		FragmentManager fm = getSupportFragmentManager();
		mFragment = (GoodsSalQueryFragment) fm.findFragmentById(R.id.fragmentContainer);
		if (mFragment == null) {
			mFragment = GoodsSalQueryFragment.newIntent(getIntent().getExtras());
			fm.beginTransaction().add(R.id.fragmentContainer, mFragment).commit();
		}
		
	}
	
	public void onClick(View v){
		mFragment.onClick(v);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		setDefaultTitle("商品流向查询");
		//initCustomActionBar(R.layout.actionbar_goodssalquery);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_filter){
			//mFragment.onOptionsItemSelected(item);
			//return true;
			if(mFragment.mDateQueryPopWin ==null){
				mFragment.initDatePopWin();
			}
			mFragment.showDatePopWin();
		}
		return super.onOptionsItemSelected(item);
	}
}
