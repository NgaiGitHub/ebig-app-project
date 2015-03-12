package com.ebig.app.activity.srvmsg;

import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.ebig.app.base.BaseFirmActivity;

public class SrvMsgActivity extends BaseFirmActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Bundle bundle = getIntent().getBundleExtra("msg");
		getToast().makeText(this, "SrvMsgActivity...."+bundle.getString(JPushInterface.EXTRA_EXTRA), null);
	}

	
}
