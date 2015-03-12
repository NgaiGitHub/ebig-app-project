package com.ebig.app.base.intf;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ebig.app.R;
import com.tpos.widget.crouton.Configuration;
import com.tpos.widget.crouton.Crouton;
import com.tpos.widget.crouton.Style;

public class BaseToastInterfaceImpl implements BaseToastInterface {

	protected Crouton mCrouton;
	protected Context mContext;
	protected View progressBarView;
	

//	@Override
//	public void bindContext(Context context) {
//		this.mContext = context;
//		if(mContext instanceof BaseAsyncTaskInterface)
//			mBaseAsyncTaskInterface = (BaseAsyncTaskInterface) mContext;
//	}
	
	private static final Configuration CONFIGURATION_INFINITE = new Configuration.Builder()
			.setDuration(Configuration.DURATION_INFINITE).build();
  
	public Crouton getCrouton(){
		return mCrouton;
	}
	
	protected void makeText(Activity activity, String text,
			OnClickListener clickListener, Style style) {
		if (mCrouton != null)
			mCrouton.hide();
		mCrouton = null;
		mCrouton = Crouton.makeText(activity, text, style);

		if (clickListener != null){
			mCrouton.setOnClickListener(clickListener);
			mCrouton.setConfiguration(CONFIGURATION_INFINITE);
		}
		mCrouton.show();
	}
	protected void makeTextId(Activity activity, String text,
			OnClickListener clickListener, Style style,int viewGroupResId) {
		if (mCrouton != null)
			mCrouton.hide();
		mCrouton = null;
		mCrouton = Crouton.makeText(activity, text, style,viewGroupResId);

		if (clickListener != null){
			mCrouton.setOnClickListener(clickListener);
			mCrouton.setConfiguration(CONFIGURATION_INFINITE);
		}
		mCrouton.show();
	}
	protected void makeTextView(Activity activity, String text,
			OnClickListener clickListener, Style style,ViewGroup viewGroup) {
		if (mCrouton != null)
			mCrouton.hide();
		mCrouton = null;
		mCrouton = Crouton.makeText(activity, text, style,viewGroup);

		if (clickListener != null){
			mCrouton.setOnClickListener(clickListener);
			mCrouton.setConfiguration(CONFIGURATION_INFINITE);
		}
		mCrouton.show();
	}

	@Override
	public void makeTextProgressBar(Activity activity, String text) {
		if (mCrouton != null)
			mCrouton.hide();
		mCrouton = null;
		if(progressBarView == null){	
			// 进度条,可自定义...Steven add		
			progressBarView = activity.getLayoutInflater().inflate(R.layout.crouton_progressbar, null);
		}
		if(text != null && !"".equals(text)){			
			TextView mBarTxt = (TextView) progressBarView.findViewById(R.id.crouton_textView);
			mBarTxt.setText(text);
		}
		Button mBarBtn = (Button) progressBarView.findViewById(R.id.crouton_button);
		mBarBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//cancelCurrentAsyncTask();
				if(mCrouton != null)
					mCrouton.hide();
			}
		});
		mCrouton = Crouton.make(activity, progressBarView);
		mCrouton.setConfiguration(CONFIGURATION_INFINITE);
		mCrouton.show();
	}
 
	@Override
	public void makeTextProgressBarHide() {
		//cancelCurrentAsyncTask();
		if(mCrouton != null)
			mCrouton.hide();
	}
 

	@Override
	public void makeText(Activity activity, String text,
			OnClickListener clickListener) {
		makeText(activity, text, clickListener, Style.CONFIRM);
	}

	@Override
	public void makeTextError(Activity activity, String text,
			OnClickListener clickListener) {
		makeText(activity, text, clickListener, Style.ALERT);
	}

	@Override
	public void makeTextByViewId(Activity activity, String text, int id,
			OnClickListener clickListener) {
		makeTextId(activity, text, clickListener, Style.CONFIRM,id);
	}

	@Override
	public void makeTextByView(Activity activity, String text, ViewGroup view,
			OnClickListener clickListener) {
		makeTextView(activity, text, clickListener, Style.CONFIRM,view);
	}

	@Override
	public void makeTextErrorByViewId(Activity activity, String text, int id,
			OnClickListener clickListener) {
		makeTextId(activity, text, clickListener, Style.ALERT,id);
	}

	@Override
	public void makeTextErrorByView(Activity activity, String text, ViewGroup view,
			OnClickListener clickListener) {
		makeTextView(activity, text, clickListener, Style.ALERT,view); 
	}

//	@Override
//	public void startIntent(Activity activity, Class cls, Bundle extras) {
//		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//		Intent mStartIntent = new Intent(activity, cls);
//		if(extras != null)
//		mStartIntent.putExtras(extras);
//		activity.startActivity(mStartIntent);
//		// 动画效果 TODO 
//		//new ActivityAnimator().fadeAnimation(activity);
//		activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//	}
//	
//	@Override
//	public void finish(Activity activity) { 
//		if (activity instanceof Activity){
//			activity.finish();
//			new ActivityAnimator().fadeAnimation(activity);
//		}
//	}


}
