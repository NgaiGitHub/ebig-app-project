package com.ebig.app.activity.register;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ebig.app.base.BaseFirmSingleActivity;

public class RegisterActivity extends BaseFirmSingleActivity {

	@Override
	protected Fragment createFragment() {
		
		return RegisterFragment.newIntance(new Bundle());
	}

}
