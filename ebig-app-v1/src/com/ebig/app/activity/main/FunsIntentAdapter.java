package com.ebig.app.activity.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ebig.app.activity.auditmgr.AuditActivity;
import com.ebig.app.model.Functions;

public class FunsIntentAdapter {

	public static Intent newIntent(Context packageContext,Functions functions) {
		String moduleId = functions.getModuleId();
		String moduleClass = functions.getModuleClass();
		Intent mIntent = null;
		if("Audit".equals(moduleId)){// Ãÿ ‚
			mIntent = new Intent(packageContext, AuditActivity.class);
		} else if("MobileSrvMsgStockQuery".equals(moduleId)){  //ºÊ»›V0∞Ê±æ
			mIntent = new Intent(packageContext, 
					com.ebig.app.activity.stockquery.StockQueryActivity.class);
		} else {
			Class cls = null;
			try {
				cls = Class.forName(moduleClass);
				if (cls != null) return new Intent(packageContext, cls);
			} catch (Exception e) {
				Log.i("ClassNotFoundException  ==", e.getMessage());
			}
		}
		return mIntent;
	}
 
}
