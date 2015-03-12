package com.ebig.app.activity.lockpattern;

import java.util.List;

import com.ebig.app.App;
import com.ebig.app.R;

import group.pals.android.lib.ui.lockpattern.prefs.DisplayPrefs;
import group.pals.android.lib.ui.lockpattern.prefs.SecurityPrefs;
import group.pals.android.lib.ui.lockpattern.util.IEncrypter;
import group.pals.android.lib.ui.lockpattern.util.InvalidEncrypterException;
import group.pals.android.lib.ui.lockpattern.util.UI;
import group.pals.android.lib.ui.lockpattern.widget.LockPatternUtils;
import group.pals.android.lib.ui.lockpattern.widget.LockPatternView;
import group.pals.android.lib.ui.lockpattern.widget.LockPatternView.Cell;
import group.pals.android.lib.ui.lockpattern.widget.LockPatternView.DisplayMode;


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
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * ����,���򳬹�4��,�������ɾ���ֳ����벢Ҫ�����µ�¼
 * @author HungLau.Ngai
 *
 */
public class LockScreenFragment extends DialogFragment implements OnClickListener{

	private LockPatternView mLockPatternView;
	
	private String mEncryptionStr ;
	
    /*
     * FIELDS
     */
    private int mMaxRetry;
    private boolean mAutoSave;
    private int mMinWiredDots;
    //private IEncrypter mEncrypter;
    /**
     * Delay time to reload the lock pattern view after a wrong pattern.
     */
    private static final long DELAY_TIME_TO_RELOAD_LOCK_PATTERN_VIEW = DateUtils.SECOND_IN_MILLIS;
    
    /*
     * View
     */
    private TextView mTipTextView;
    private TextView mTipDescTextView;
    
    private int currRetryNum = 5;//�����Գ��Զ��ٴ�
   // private String accLockShaStr;
	
//	public boolean isLock(){
//		if(mEncryptionStr != null) return true;
//		mEncryptionStr = AppDbCtrl.readEncryptionStrByAcc();
//		if(mEncryptionStr != null) return true;
//		return false;
//	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
		
		initLockCfg();
	}
	
	private void initLockCfg() {
		mMinWiredDots = DisplayPrefs.getMinWiredDots(getActivity());
        mMaxRetry = DisplayPrefs.getMaxRetry(getActivity());
        mAutoSave = SecurityPrefs.isAutoSavePattern(getActivity());
        UI.adjustDialogSizeForLargeScreen(getActivity().getWindow());
//        /*
//         * Encrypter.
//         */
//        char[] encrypterClass = SecurityPrefs.getEncrypterClass(getActivity());
//        if (encrypterClass != null) {
//            try {
//                mEncrypter = (IEncrypter) Class.forName(
//                        new String(encrypterClass), false, getActivity().getClassLoader())
//                        .newInstance();
//            } catch (Throwable t) {
//                throw new InvalidEncrypterException();
//            }
//        }
        //
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.lockpattern_screen_check, null);
		mLockPatternView = (LockPatternView) contentView.findViewById(R.id.lockpatterView);
		mLockPatternView.setOnPatternListener(mLockPatternViewListener);
		mTipTextView = (TextView) contentView.findViewById(R.id.lockpatter_tip);
		mTipDescTextView = (TextView) contentView.findViewById(R.id.lockpatter_tip_desc);
		//Button mLoseBtn = (Button) contentView.findViewById(R.id.lockpatter_losePswd);
		((Button) contentView.findViewById(R.id.lockpatter_losePswd)).setOnClickListener(this);
		onKeyListener();
		return contentView;
	}

	private void onKeyListener() {
		/// ȥ�����ؽ�������...
		getDialog().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return true;
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
        public void onPatternStart() {// ֻ����һ��,�ڵ�һ����Ӻ�
            mLockPatternView.removeCallbacks(mLockPatternViewReloader);
            mLockPatternView.setDisplayMode(DisplayMode.Correct);

        }// onPatternStart()

        @Override
        public void onPatternDetected(List<Cell> pattern) {// ����,������Ϻ����
        	Log.i("lockPattern =", pattern.toString());
        	//if(accLockShaStr == null)
        	 
			if (App.getLockPattern().getEncryptionStr() != null
					&& !App.getLockPattern().getEncryptionStr()
							.equals(LockPatternUtils.patternToSha1(pattern))){				
				onPatternDetectedWrong();
			}else {
				onClick(null);
			}
//        	if (pattern.size() < mMinWiredDots) {// ����4���ڵ�
//                onPatternDetectedWrong();
//                return;
//            }else {
//				String lockShaStr = LockPatternUtils.patternToSha1(pattern);
//				mTipTextView.setText(lockShaStr);
//			}
        	/// �������ɹ�����
        }// onPatternDetected()


		@Override
        public void onPatternCleared() {// ������ʱ�����
            mLockPatternView.removeCallbacks(mLockPatternViewReloader);

        	Log.i("lockPattern =", "");
        }// onPatternCleared()

        @Override
        public void onPatternCellAdded(List<Cell> pattern) {// �ڵ�һ���ڵ�ʱ,������һ����
            // TODO Auto-generated method stub
        	Log.i("lockPattern =", pattern.toString());
        }// onPatternCellAdded()
    };// mLockPatternViewListener

    /**
     * ���ƴ�����
     */
    private void onPatternDetectedWrong() {
    	
    	if(currRetryNum == 1) onClick(null);// ����Դ�������,�͵����������봦��
    	
        mLockPatternView.setDisplayMode(DisplayMode.Wrong);
        mLockPatternView.postDelayed(mLockPatternViewReloader,DELAY_TIME_TO_RELOAD_LOCK_PATTERN_VIEW);
        --currRetryNum;
        mTipTextView.setText(Html.fromHtml("<font color=red>�������,���������� "+currRetryNum+" ��</font>"));
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        mTipTextView.startAnimation(shake);
        mTipDescTextView.setVisibility(View.VISIBLE);
        
        mLockPatternView.startAnimation(shake);
        //mLockPatternView.setDisplayMode(DisplayMode.Animate);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}
}
