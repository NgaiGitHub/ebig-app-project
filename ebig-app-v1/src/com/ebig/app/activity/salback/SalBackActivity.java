package com.ebig.app.activity.salback;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmActivity;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.base.BaseFirmSingleActivity;

public class SalBackActivity extends BaseFirmSingleActivity {

	@Override
	protected Fragment createFragment() {
		
		return SalBackQueryFragment.newIntance(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onScanCodeResult(String code) {
		super.onScanCodeResult(code);
		View rootview = this.getWindow().getDecorView();
		if (rootview.findFocus() instanceof EditText){
			EditText tEditText = (EditText) rootview.findFocus();
			tEditText.setText(code);
		}
	}

	
}
