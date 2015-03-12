package com.ebig.app.base.intf;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public interface BaseToastInterface {

	// ÐÅÏ¢Êä³ö
	void makeText(Activity activity,String text,OnClickListener clickListener);
	void makeTextError(Activity activity,String text,OnClickListener clickListener);
	void makeTextByViewId(Activity activity,String text,int id,OnClickListener clickListener);
	void makeTextByView(Activity activity,String text,ViewGroup viewGroup,OnClickListener clickListener);
	void makeTextErrorByViewId(Activity activity,String text,int id,OnClickListener clickListener);
	void makeTextErrorByView(Activity activity,String text,ViewGroup viewGroup,OnClickListener clickListener);
	void makeTextProgressBar(Activity activity,String text);
	void makeTextProgressBarHide();
	
}
