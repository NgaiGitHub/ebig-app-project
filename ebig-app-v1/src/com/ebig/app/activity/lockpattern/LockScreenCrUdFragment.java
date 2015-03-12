package com.ebig.app.activity.lockpattern;

import group.pals.android.lib.ui.lockpattern.prefs.DisplayPrefs;
import group.pals.android.lib.ui.lockpattern.prefs.SecurityPrefs;
import group.pals.android.lib.ui.lockpattern.util.IEncrypter;
import group.pals.android.lib.ui.lockpattern.util.InvalidEncrypterException;
import group.pals.android.lib.ui.lockpattern.util.UI;
import group.pals.android.lib.ui.lockpattern.widget.LockPatternUtils;
import group.pals.android.lib.ui.lockpattern.widget.LockPatternView;
import group.pals.android.lib.ui.lockpattern.widget.LockPatternView.Cell;
import group.pals.android.lib.ui.lockpattern.widget.LockPatternView.DisplayMode;

import java.util.List;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.dbctrl.AppDbCtrl;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LockScreenCrUdFragment extends DialogFragment implements OnClickListener{

	private LockPatternView mLockPatternView;
	
	private String mEncryptionStr ;
	
    /*
     * FIELDS
     */
  //  private int mMaxRetry;
   // private boolean mAutoSave;
    private int mMinWiredDots;
   // private IEncrypter mEncrypter;
    /**
     * Delay time to reload the lock pattern view after a wrong pattern.
     */
    private static final long DELAY_TIME_TO_RELOAD_LOCK_PATTERN_VIEW = DateUtils.SECOND_IN_MILLIS;
    
    /*
     * View
     */
    private TextView mTipTextView;
    //private TextView mTipDescTextView;
    
    //private int currRetryNum = 5;//还可以尝试多少次 
	 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
		
		initLockCfg();
	}
	
	private void initLockCfg() {
		mMinWiredDots = DisplayPrefs.getMinWiredDots(getActivity());
        //mMaxRetry = DisplayPrefs.getMaxRetry(getActivity());
       // mAutoSave = SecurityPrefs.isAutoSavePattern(getActivity());  
        UI.adjustDialogSizeForLargeScreen(getActivity().getWindow());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.lockpattern_create_or_update, null);
		mLockPatternView = (LockPatternView) contentView.findViewById(R.id.lockpatterView);
		mLockPatternView.setOnPatternListener(mLockPatternViewListener);
		mTipTextView = (TextView) contentView.findViewById(R.id.lockpatter_tip); 
		//Button mLoseBtn = (Button) contentView.findViewById(R.id.lockpatter_losePswd);
		((Button) contentView.findViewById(R.id.lockpattern_cancle)).setOnClickListener(this);
		onKeyListener();
		return contentView;
	}

	private void onKeyListener() {
		/// 去掉返回建的设置...
		getDialog().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				mEncryptionStr = "";
				return false;
			}
		});
	}
	
    /**
     * This reloads the {@link #mLockPatternView} after a wrong pattern.
     */
    private final Runnable mLockPatternViewReloader = new Runnable() {

        @Override
        public void run() {
            mLockPatternView.clearPattern();
            mLockPatternViewListener.onPatternCleared();
        }// run()
    };// mLockPatternViewReloader
	
	 /*
     * LISTENERS
     */ 
    private final LockPatternView.OnPatternListener mLockPatternViewListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {// 只调用一次,在第一个添加后。
            mLockPatternView.removeCallbacks(mLockPatternViewReloader);
            mLockPatternView.setDisplayMode(DisplayMode.Correct);

        }// onPatternStart()

        @Override
        public void onPatternDetected(List<Cell> pattern) {// 手势,滑动完毕后调用
        	//Log.i("lockPattern =", pattern.toString());
        	mLockPatternView.postDelayed(mLockPatternViewReloader,DELAY_TIME_TO_RELOAD_LOCK_PATTERN_VIEW);
            
        	if (pattern.size() < mMinWiredDots) {// 至少4个节点
                Toast.makeText(getActivity(), "手势密码至少需要4个节点 ! ", Toast.LENGTH_LONG).show();
		        mLockPatternView.startAnimation( AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
				return;
            }else {
            	//匹配手势密码
            	if(mEncryptionStr != null && !"".equals(mEncryptionStr)){
            		 if(mEncryptionStr.equals(LockPatternUtils.patternToSha1(pattern))){
            			 // 两次密码相同,记下改手势密码
            			 App.getLockPattern().setIslock(true);
            			 App.getLockPattern().setEncryptionStr(mEncryptionStr);
            			 AppDbCtrl.saveLockPattern();
            			 onClick(null);
            		 }
            	}else {					
            		mEncryptionStr = LockPatternUtils.patternToSha1(pattern);
            		mTipTextView.setText(getString(R.string.lockpatter_sure));
				}
			}
        	/// 当滑动成功后再
        }// onPatternDetected()


		@Override
        public void onPatternCleared() {// 清屏的时候调用
            mLockPatternView.removeCallbacks(mLockPatternViewReloader);

        	//Log.i("lockPattern =", "");
        }// onPatternCleared()

        @Override
        public void onPatternCellAdded(List<Cell> pattern) {// 第到一个节点时,都调用一翻。
            // TODO Auto-generated method stub
        	//Log.i("lockPattern =", pattern.toString());
        }// onPatternCellAdded()
    };// mLockPatternViewListener

//    /**
//     * 绘制错误处理
//     */
//    private void onPatternDetected() {
//    	
//    	if(currRetryNum == 1) onClick(null);// 最大尝试次数完了,就当作忘记密码处理
//    	
//        mLockPatternView.setDisplayMode(DisplayMode.Wrong);
//        mLockPatternView.postDelayed(mLockPatternViewReloader,DELAY_TIME_TO_RELOAD_LOCK_PATTERN_VIEW);
//        --currRetryNum;
//        mTipTextView.setText(Html.fromHtml("<font color=red>密码错误,还可以再试 "+currRetryNum+" 次</font>"));
//        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
//        mTipTextView.startAnimation(shake);
//        mTipDescTextView.setVisibility(View.VISIBLE);
//        
//        mLockPatternView.startAnimation(shake);
//        //mLockPatternView.setDisplayMode(DisplayMode.Animate);
//	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}
}
