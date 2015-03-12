package com.ebig.app.base;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.base.intf.BaseToastInterface;
import com.ebig.app.custom.ui.DefaultActionBar;
import com.ebig.zxing.CaptureActivity;

/**
 * 基类,建议最低使用为4.0<br />
 * 1、有固定的ActionBar,为统一风格。<br />
 * 2、
 * 
 * @author HungLau.Ngai
 * 
 */
public abstract class BaseFirmActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// 作为3.0以后的版本,在网络访问需要设置以下信息。
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		// 解决没有ActionBar的问题
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
	}
	
	/**
	 * 启动扫描,结果返回调用onScanCodeResult(String code)
	 */
	public void startScanning(){
		startActivityForResult(new Intent(this,CaptureActivity.class), RESULT_FIRST_USER);
	}
	
	protected void onScanCodeResult(String code) {}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		switch (resultCode) {
		case CaptureActivity.ID://扫描返回值
			String code = data.getStringExtra(CaptureActivity.SCAN_CODE);// 取扫描码
			onScanCodeResult(code);
			break;

		default:
			break;
		} 
	}

	protected BaseToastInterface getToast() {
		return App.getCroutonToast();
	}

	public void startIntent(Context packageContext, Class cls, Bundle extras) {
		Intent mStartIntent = new Intent(packageContext, cls);
		if (extras != null)
			mStartIntent.putExtras(extras);
		packageContext.startActivity(mStartIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.qCode:
			// 开启扫描
			startScanning();
			break;

		default:
			break;
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (initDefaultActionBar()) {
			final ActionBar mActionBar = getActionBar();
			mActionBar.setDisplayShowTitleEnabled(false);
			mActionBar.setDisplayShowCustomEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(false);
			DefaultActionBar bar = new DefaultActionBar(this);
			ActionBar.LayoutParams lp = new ActionBar.LayoutParams(Gravity.START);//(ActionBar.LayoutParams) bar.getLayoutParams();
			lp.gravity = lp.gravity & ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK
					| Gravity.START;
			mActionBar.setCustomView(bar, lp);
		}
		return true;
	}

	protected boolean initDefaultActionBar() {
		return true;
	}

	/**
	 * @param layoutId  CustomView的资源id
	 * @return CustomView
	 */
	protected View initCustomActionBar(int layoutId) {
		final ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		View mCustomView = getLayoutInflater().inflate(layoutId, null);
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mCustomView.setLayoutParams(lp);
		mActionBar.setCustomView(mCustomView,lp);
		return mCustomView;
	}


	private DefaultActionBar getBar() {
		final ActionBar mActionBar = getActionBar();
		if (mActionBar.getCustomView() == null)
			return null;
		return (DefaultActionBar) mActionBar.getCustomView();
	}

	protected void setDefaultTitle(String title) {
		if (getBar() != null)
			getBar().setDefaultTitle(title);
	}

	protected void showDefaultProgressBar(boolean b) {
		if (getBar() != null)
			getBar().showDefaultProgressBar(b);
	}

}
