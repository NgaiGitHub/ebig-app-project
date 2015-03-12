package com.ebig.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebig.app.App;
import com.ebig.app.NetInfo;
import com.ebig.app.R;
import com.ebig.app.activity.login.LoginActivity;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.dbctrl.AppDbCtrl;

public class MainFragment extends BaseFirmFragment {
	 
	static public MainFragment newIntance(Bundle bundle) {
		MainFragment mainFragment = new MainFragment();
		mainFragment.setArguments(bundle);
		return mainFragment; 
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		return inflater.inflate(R.layout.activity_main, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) { 
		super.onActivityCreated(savedInstanceState);
		findViews();
		newAsyncTaskExecute(-1, -1);
	}

	private void findViews() {
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		getActionBar().hide();
	}


	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		startActivity(new Intent(getActivity(), LoginActivity.class));
		getActivity().finish();
//		Timer mTimer = new Timer();
//		mTimer.schedule(new TimerTask() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				//startActivity(new Intent(App.getContext(), AppActivity.class));
//				//getBaseInterface().startIntent(MainActivity.this, AppActivity.class, null);
////				startIntent(MainActivity.this, LoginActivity.class, null);
////				finish();
//			}
//		},1*1000);
	}

	@Override
	protected void onPostExecuteTaskError(int asyncid, Exception result) {
		super.onPostExecuteTaskError(asyncid, result);
	}

	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		
		App.getAccount();// 初始化用户账号信息 
		NetInfo.initLocalConfig(App.getContext()); // 初始化网络信息
		App.putAppParamMap(AppDbCtrl.genAppNewVersion());
		App.putProjectList(AppDbCtrl.genAppProjectList());
		
		return true ;
	}
	
	
}
