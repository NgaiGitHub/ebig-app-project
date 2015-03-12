package com.ebig.app.utils;

import android.content.Context;
import android.view.View.OnClickListener;

import com.ebig.app.R;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

public class DialogUtil {
	private NiftyDialogBuilder dialogBuilder;
	public void openDefaultDialog(Context context,String title,String msg,OnClickListener listener1,OnClickListener listener2) {
		dialogBuilder = NiftyDialogBuilder.getInstance(context);

		dialogBuilder.withTitle(title)
				.withTitleColor(context.getResources().getColor(R.color.default_custom_actionbar_bg))
				.withDividerColor(context.getResources().getColor(R.color.default_custom_actionbar_bg))
				.withMessage(msg)
				.withMessageColor(context.getResources().getColor(R.color.default_custom_actionbar_bg))// 白色
				.withDialogColor(context.getResources().getColor(R.color.default_white))
			  //.withIcon(context.getResources().getDrawable(R.drawable.icon))
				.withDuration(700) // def
				.withEffect(Effectstype.Slideright) // def Effectstype.Slidetop
				.withButtonTextColor(context.getResources().getColor(R.color.default_custom_actionbar_bg))
				.withButton1Text("确  定") // def gone
				.withButton2Text("取  消") // def gone
				.isCancelableOnTouchOutside(true) // def | isCancelable(true)
				//.setCustomView(R.layout.notification_update_app, context) // .setCustomView(View
				.setButton1Click(listener1)
				.setButton2Click(listener2)
				.show();
	}
	
	public void closeDefaultDialog() {
		dialogBuilder.dismiss();
	}
}
