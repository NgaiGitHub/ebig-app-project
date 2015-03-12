package com.ebig.app.activity.main;
  
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ebig.app.App;
import com.ebig.app.NetInfo;
import com.ebig.app.R;
import com.ebig.app.base.BaseFirmActivity;
import com.ebig.app.custom.ui.DefaultActionBar;
import com.ebig.app.jpush.JpushConst;
import com.ebig.app.service.LocalService;
import com.ebig.app.service.UpdateService;
import com.readystatesoftware.viewbadger.BadgeView;
import com.tpos.widget.crouton.Crouton;
import com.tpos.widget.pagertab.PagerSlidingTabStrip;
 

public class AppActivity extends BaseFirmActivity implements OnClickListener{

	static public String APP_MSG_ACTION = "com.ebig.app.msg";
	//private MenuItem mMsgMenuItem;
	private ViewPager mViewPage ;
	private PagerSlidingTabStrip mViewPageTab;
	private AppPageAdapter mViewPageAdapter;
	private AppReceiver mNetInfoReceiver ;

	public static boolean isForeground = false;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_app);
//		setActionBarCustomView(R.layout.actionbar_custom_appbar);
//		getActionBar().setTitle(getString(R.string.app_name));
//		getActionBar().setDisplayShowTitleEnabled(true);
		registerReceiver();
		
		mViewPageTab = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		//mViewPageTab.setShouldExpand(true);
		
		mViewPage = (ViewPager) findViewById(R.id.pager);
		mViewPageAdapter = new AppPageAdapter(this, getSupportFragmentManager());
		mViewPage.setAdapter(mViewPageAdapter);
		mViewPageTab.setViewPager(mViewPage); 
		mViewPageTab.setDefaultPage(0);
		
		// 模拟
		Intent intent = new Intent(this, LocalService.class);
		startService(intent);
	} 
	
	@Override
	protected void onStart() { 
		super.onStart();
	}
	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	} 
	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		Crouton.clearCroutonsForActivity(this);
		unregisterReceiver(mNetInfoReceiver);
//		stopService(new Intent(this, LocalService.class));
		super.onDestroy();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		initCustomActionBar(R.layout.ui_appbar_custom);
		findBarViews();
		return true;
	}

	private void findBarViews() {
		View v = getActionBar().getCustomView();
		DefaultActionBar mBar = (DefaultActionBar) v.findViewById(R.id.action_title_bar);
		mBar.hideBack(true);
		mBar.setDefaultTitle(getString(R.string.app_name));
		(v.findViewById(R.id.action_search)).setOnClickListener(this);
		(v.findViewById(R.id.action_msg)).setOnClickListener(this);
	}

//	public void onOptionsItemSelected(View v){
//		int id = v.getId();
//		switch (id) {
//		case R.id.action_search:
//			startSearch("Search", false, null , false);
//			break;
//		case R.id.action_msg:
//			//v.setTag(null);
//			//newAsyncTaskExecute(1, 1);
//			break;
//
//		default:
//			break;
//		}
//	} 
	

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.action_search:
			startSearch("Search", false, null , false);
			break;
		case R.id.action_msg:
			//v.setTag(null);
			//newAsyncTaskExecute(1, 1);
			// startIntent(this,SrvMsgActivity.class, null);
			break;

		default:
			break;
		}
	}

//	@Override
//	protected void onPostExecuteTask(int asyncid, Object result) {
//		// TODO Auto-generated method stub
//		super.onPostExecuteTask(asyncid, result);
//		switch (asyncid) {
//		case -1:
//			App.appLogin();
//			break;
//		case 1:
//			
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	@Override
//	protected void onPostExecuteTaskError(int asyncid, Exception result) {
//		// TODO Auto-generated method stub
//		super.onPostExecuteTaskError(asyncid, result);
//	}
//
//	@Override
//	protected Object doInBackgroundTask(int asyncid, Object[] params) {
//		// TODO Auto-generated method stub
//		return true;
//		//return new RuntimeException("Auto-generated method stub");
//	}
	 
	protected void showProgressBar() {
		View v = getActionBar().getCustomView();
		if(v != null){			
			DefaultActionBar mBar = (DefaultActionBar) v.findViewById(R.id.action_title_bar);
			if (mBar != null)
				mBar.showDefaultProgressBar(true);
		}
	}

	protected void dismissProgressBar() {
		View v = getActionBar().getCustomView();
		if(v != null){
			DefaultActionBar mBar = (DefaultActionBar) v.findViewById(R.id.action_title_bar);			
			if (mBar != null)
				mBar.showDefaultProgressBar(false);
		}
	}

	// ********************************如果离线，修改App导航栏颜色**********************
	private View addMsgBadge(){
		View v = (View)getActionBar().getCustomView().findViewById(R.id.action_msg);
		BadgeView mBadgeView = (BadgeView) v.getTag();
		if(mBadgeView == null){
			mBadgeView = new BadgeView(this, v);
			mBadgeView.setText("0");
			mBadgeView.setTextSize(10);
			mBadgeView.setBadgeBackgroundColor(Color.RED);
			v.setTag(mBadgeView);
		}  
		if(mBadgeView.isShown()){			
			mBadgeView.increment(1); 
		}else {			
			mBadgeView.show();
		}
		return v;
	};
	private void registerReceiver(){
		// 注册个广播接收器
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mIntentFilter.addAction(APP_MSG_ACTION);
		// jpush 过滤Action
		mIntentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		mIntentFilter.addAction(JpushConst.MESSAGE_RECEIVED_ACTION);
		mNetInfoReceiver = new AppReceiver();
		registerReceiver(mNetInfoReceiver, mIntentFilter);
	}
	public void openPhoneSet(View v){
		Intent intent = new Intent(Settings.ACTION_SETTINGS);
		startActivity(intent);
	}
	
	private class AppReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
				if(NetInfo.isConnected()){
					// 如果账号权限功能，不为空且大于0，则代表已经登录过，需要重连操作。
					if (!NetInfo.isEnable()) {// 应该启动后台去进行重连。
						   showProgressBar();
						   App.appLogin();  
						   dismissProgressBar();
						 // newAsyncTaskExecute(-1);
					}
					// 网络通
					View v = findViewById(R.id.app_curr_netinfo);
					if (v != null)
						v.setVisibility(View.GONE);
					NetInfo.setEnable(true);
				}else { 
					// 网络不通
					//findViewById(R.id.app_curr_netinfo).setVisibility(View.VISIBLE);
					View v = findViewById(R.id.app_curr_netinfo);
					if (v != null)
						v.setVisibility(View.VISIBLE);
					
					NetInfo.setEnable(false);
					Toast.makeText(App.getContext(), "当前网络连接不可用 ! ",Toast.LENGTH_SHORT).show();
				} 
			}
			// 消息服务接收
			if(APP_MSG_ACTION.equals(intent.getAction())){
//				if(mMsgMenuItem != null)
//				mMsgMenuItem.setIcon(R.drawable.actionbar_menu_msg_dot);
			    addMsgBadge();
			}
			
			// Jpush消息
			if(JpushConst.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())){
				addMsgBadge();
			}
			
			// 如果App更新下载中..则断点续传
			if(NetInfo.isEnable())
			if(UpdateService.isLoading()){
				Intent updateIntent = new Intent(AppActivity.this,UpdateService.class);
				updateIntent.putExtra(UpdateService.UPDATE_URL_KEY, "http://183.63.146.102:2015/appsrv/imgupload/apk/ebig-app-v1.apk");
				startService(updateIntent);
			}
				
		}
	}


}
	/******************************/
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		 
//		getMenuInflater().inflate(R.menu.app_menu, menu);
//		mMsgMenuItem = menu.findItem(R.id.action_msg); 
//////		menuItem.setActionView(LayoutInflater.from(this).inflate(R.layout.linearlayout, null));
//////		menuItem.setIcon(R.drawable.actionbar_menu_about);
////		
////		BadgeView badge = new BadgeView(this, menuItem.getActionView());
////    	badge.setText("New!");
////    	badge.setTextColor(Color.BLUE);
////    	badge.setBadgeBackgroundColor(Color.YELLOW);
////    	badge.setTextSize(12); 
//		
//		//ActionItemBadge.update(this, menu.findItem(R.id.action_msg), 12);
//		
//		return true;
//	} 
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		switch (item.getItemId()) {
//
//		case R.id.action_msg:
//			//getBaseInterface().makeText(this, "action_msg", null);
//			mMsgMenuItem.setIcon(R.drawable.actionbar_menu_msg);
//			newAsyncTaskExecute(1, 1);
//			return true;
////		case R.id.action_about:
////			//getBaseInterface().makeText(this, "action_about", null);
////			App.getLockPatternFragment().show(getSupportFragmentManager(), "LockPattern");
////			return true;
////			
////		case R.id.action_login:
////			getToast().makeText(this, "action_login", null);
////			return true;
//		case R.id.action_search:
//			startSearch("123", false, null , false);
//			return true;
//
//		}
//
//		return super.onOptionsItemSelected(item);
//	}
	
//	@Override
//	public boolean onMenuOpened(int featureId, Menu menu) {
//		setOverflowIconVisible(featureId, menu);
//		return super.onMenuOpened(featureId, menu);
//	}
//	/**
//	* 利用反射让隐藏在Overflow中的MenuItem显示Icon图标
//	* @param featureId
//	* @param menu
//	* onMenuOpened方法中调用
//	*/
//	public static void setOverflowIconVisible(int featureId, Menu menu) {
//		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
//			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
//				try {
//					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//					m.setAccessible(true);
//					m.invoke(menu, true);
//				} catch (Exception e) {
//				}
//			}
//		}
//	}
