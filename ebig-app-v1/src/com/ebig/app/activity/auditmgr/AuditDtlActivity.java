package com.ebig.app.activity.auditmgr;

import android.support.v4.app.Fragment;
import android.view.Menu;

import com.ebig.app.base.BaseFirmSingleActivity;

public class AuditDtlActivity extends BaseFirmSingleActivity {

	@Override
	protected Fragment createFragment() {
		return RecordDtlFragment.newIntance(getIntent().getExtras());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setDefaultTitle("ÉóÅúÏêÏ¸½çÃæ");
		return super.onCreateOptionsMenu(menu);
	}
}
