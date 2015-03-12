package com.ebig.app.activity.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ebig.app.App;
import com.ebig.app.base.BaseFirmSingleActivity;

public class LoginActivity extends BaseFirmSingleActivity {

	@Override
	protected Fragment createFragment() { 
		Bundle mBundle = new Bundle();
		mBundle.putString(LoginFragment.ACCOUNT_KEY, App.getAccount().getAccountId());
		mBundle.putString(LoginFragment.PSWD_KEY, App.getAccount().getAccountPassword());
		return LoginFragment.newIntance(mBundle);
	}

	@Override
	protected boolean initDefaultActionBar() {
		return true;
	}

}
