package com.ebig.app.activity.suggest;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ebig.app.base.BaseFirmSingleActivity;

public class SuggestActivity extends BaseFirmSingleActivity {

	@Override
	protected Fragment createFragment() { 
		Bundle mBundle = new Bundle();
		return SuggestFragment.newIntance(mBundle);
	}

}
