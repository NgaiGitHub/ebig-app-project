package com.ebig.app.custom.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ebig.app.R;

/**
 * 针对导航栏的统一而设定的,只影响左边显示,不影响正常的Menu使用。<br />
 * 
 * @author HungLau.Ngai
 * 
 */
public class DefaultActionBar extends FrameLayout {

	private Button mBackBtn;
	private TextView mTitle;
	private ProgressBar mPgBar;
	
	public DefaultActionBar(Context context) {
		super(context);
		findViews();
	}
	
	public DefaultActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		findViews();
	}

	private void findViews() {
		View mView = LayoutInflater.from(getContext()).inflate(R.layout.ui_default_actionbar, this);
		mBackBtn = (Button) mView.findViewById(R.id.default_actionbar_back);
		mTitle = (TextView) mView.findViewById(R.id.default_actionbar_title);
		mPgBar = (ProgressBar) mView.findViewById(R.id.default_actionbar_prgbar);
		
		mBackBtn.setOnClickListener(mBarClickListener);
		mPgBar.setVisibility(View.GONE);
	}
	
	public void setDefaultTitle(String title) {
		if (mTitle != null) {
			mTitle.setText(title);
		}
	}
	
	public void setBackDraw(Drawable drawable) {
		if(mBackBtn != null)
			mBackBtn.setBackground(drawable);
	}
	
	public void hideBack(boolean b) {
		if(mBackBtn != null)
			mBackBtn.setVisibility(b == true ? View.GONE : View.VISIBLE);
	}
	
	public void showDefaultProgressBar(boolean b) {
		if (mPgBar != null)
			mPgBar.setVisibility(b == true ? View.VISIBLE : View.GONE);

	}
	
	private OnClickListener mBarClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) { ((Activity)getContext()).finish();}
	};

//	@Override
//	public android.view.ViewGroup.LayoutParams getLayoutParams() {
//		return new ActionBar.LayoutParams(Gravity.START);
//	}
	
	
}
