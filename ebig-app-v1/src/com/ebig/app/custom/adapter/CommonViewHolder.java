package com.ebig.app.custom.adapter;

import android.content.Context;
import android.view.View;

public abstract class CommonViewHolder {

	protected Context mContext;
	protected int position;

	public CommonViewHolder() {
		super();
	}

	public void putContext(Context context) {
		mContext = context;
	};
	
	public void setPosition(int p){
		position = p;
	}

	abstract public View createView();

	abstract public void refashViewValue(Object obj);
 

}
