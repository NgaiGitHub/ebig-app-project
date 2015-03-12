package com.ebig.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ebig.app.base.BaseFirmSingleActivity;

public class MainActivity extends BaseFirmSingleActivity {

//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//		//setContentView(R.layout.activity_main);
//	}
//
//	@Override
//	protected void onStart() { 
//		super.onStart();
//		
////		newAsyncTaskExecute(-1);
//		
//	}
//	
	@Override
	protected Fragment createFragment() {
		return MainFragment.newIntance(new Bundle());
	}
//
//	@Override
//	protected void onPostExecuteTask(int asyncid, Object result) {
//		super.onPostExecuteTask(asyncid, result);
//		startIntent(MainActivity.this, LoginActivity.class, null);
//		finish();
////		Timer mTimer = new Timer();
////		mTimer.schedule(new TimerTask() {
////			
////			@Override
////			public void run() {
////				// TODO Auto-generated method stub
////				//startActivity(new Intent(App.getContext(), AppActivity.class));
////				//getBaseInterface().startIntent(MainActivity.this, AppActivity.class, null);
//////				startIntent(MainActivity.this, LoginActivity.class, null);
//////				finish();
////			}
////		},1*1000);
//	}
//
//	@Override
//	protected void onPostExecuteTaskError(int asyncid, Exception result) {
//		super.onPostExecuteTaskError(asyncid, result);
//	}
//
//	@Override
//	protected Object doInBackgroundTask(int asyncid, Object[] params) {
//		
//		App.getAccount();// 初始化用户账号信息 
//		NetInfo.initLocalConfig(App.getContext()); // 初始化网络信息
//		App.putAppParamMap(AppDbCtrl.genAppNewVersion());
//		App.putProjectList(AppDbCtrl.genAppProjectList());
//		
//		return true ;
//	}

}
