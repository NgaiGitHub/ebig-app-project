package com.ebig.app.service;

import java.util.Timer;
import java.util.TimerTask;

import com.ebig.app.activity.main.AppActivity;

import android.app.IntentService;
import android.content.Intent;

/**
 * 处理后台,耗时操作。
 * @author HungLau.Ngai
 *
 */
public class LocalService extends IntentService {

	public LocalService() {
		super(LocalService.class.getName()); 
	}
	
	public LocalService(String name) {
		super(name); 
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Timer mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(AppActivity.APP_MSG_ACTION);
				sendBroadcast(mIntent);
			}
		}, 5 * 1000);
	}

}
