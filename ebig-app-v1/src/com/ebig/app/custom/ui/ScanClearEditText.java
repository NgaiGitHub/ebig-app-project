package com.ebig.app.custom.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmActivity;
  
public class ScanClearEditText extends EditText implements    
        OnFocusChangeListener, TextWatcher {   
    /** 
     * ɾ����ť������ 
     */  
    private Drawable mClearDrawable;  
    /**
     * ɨ�谴ť������
     */
    private Drawable mScanDrawable;
    /** 
     * �ؼ��Ƿ��н��� 
     */  
    private boolean hasFoucs;  
    
    private OnTextChangedListener mListener;
	private Context context;
    
    public interface OnTextChangedListener {
		boolean onTextChange(String text);
	}
   public void setOnTextChangedListener(OnTextChangedListener listener){
	   mListener = listener;
   }
    
    public ScanClearEditText(Context context) {   
        this(context, null);   
        this.context = context;
    }   
   
    public ScanClearEditText(Context context, AttributeSet attrs) {   
        //���ﹹ�췽��Ҳ����Ҫ����������ܶ����Բ�����XML���涨��  
        this(context, attrs, android.R.attr.editTextStyle);   
        this.context = context;
    }   
      
    public ScanClearEditText(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        this.context = context;
        init();  
    }  
      
      
    private void init() {   
        //��ȡEditText��DrawableRight,����û���������Ǿ�ʹ��Ĭ�ϵ�ͼƬ  
        mClearDrawable = getCompoundDrawables()[2];   
        if (mClearDrawable == null) {   
//          throw new NullPointerException("You can add drawableRight attribute in XML");  
            mClearDrawable = getResources().getDrawable(R.drawable.ak5);   
        }   
        
        if(mScanDrawable == null){
        	mScanDrawable = getResources().getDrawable(R.drawable.ic_scan_nomal);
        }
          
        mClearDrawable.setBounds(0, 0, (int) (mClearDrawable.getIntrinsicWidth() * 0.4), (int) (mClearDrawable.getIntrinsicHeight() * 0.4));   
        mScanDrawable.setBounds(0, 0, (int) (mClearDrawable.getIntrinsicWidth() * 0.4), (int) (mClearDrawable.getIntrinsicHeight() * 0.4));   
        //Ĭ����������ͼ��  
        setClearIconVisible(false);   
        //���ý���ı�ļ���  
        setOnFocusChangeListener(this);   
        //����������������ݷ����ı�ļ���  
        addTextChangedListener(this);   
    }   
   
   
    /** 
     * ��Ϊ���ǲ���ֱ�Ӹ�EditText���õ���¼������������ü�ס���ǰ��µ�λ����ģ�����¼� 
     * �����ǰ��µ�λ�� ��  EditText�Ŀ�� - ͼ�굽�ؼ��ұߵļ�� - ͼ��Ŀ��  �� 
     * EditText�Ŀ�� - ͼ�굽�ؼ��ұߵļ��֮�����Ǿ�������ͼ�꣬��ֱ�����û�п��� 
     */  
    @Override   
    public boolean onTouchEvent(MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_UP) {  
            if (getCompoundDrawables()[2] != null) {  
            	boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())  
                        && (event.getX() < ((getWidth() - getPaddingRight())));  
                  
                if (touchable) {  
                	if(this.getText().length() > 0){                		
                		this.setText("");  
                	}else{
                		startScaning();
                	}
                }  
            }  
        }  
  
        return super.onTouchEvent(event);  
    }  
   
    private void startScaning() {
		if(context instanceof BaseFirmActivity){
			((BaseFirmActivity)context).startScanning();
		}
	}

	/** 
     * ��ClearEditText���㷢���仯��ʱ���ж������ַ��������������ͼ�����ʾ������ 
     */  
    @Override   
    public void onFocusChange(View v, boolean hasFocus) {   
        this.hasFoucs = hasFocus;  
        if (hasFocus) {   
            setClearIconVisible(getText().length() > 0);   
        } else {   
            setClearIconVisible(false);   
        }   
    }   
   
   
    /** 
     * �������ͼ�����ʾ�����أ�����setCompoundDrawablesΪEditText������ȥ 
     * @param visible 
     */  
    protected void setClearIconVisible(boolean visible) {   
        Drawable right = visible ? mClearDrawable : mScanDrawable;   
        setCompoundDrawables(getCompoundDrawables()[0],   
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);   
    }   
       
      
    /** 
     * ��������������ݷ����仯��ʱ��ص��ķ��� 
     */  
    @Override   
    public void onTextChanged(CharSequence s, int start, int count,   
            int after) {   
                if(hasFoucs){  
                    setClearIconVisible(s.length() > 0);  
                }  
                if(mListener != null)
                	mListener.onTextChange(s.toString());
    }   
   
    @Override   
    public void beforeTextChanged(CharSequence s, int start, int count,   
            int after) {   
           
    }   
   
    @Override   
    public void afterTextChanged(Editable s) {   
           
    }   
      
     
    /** 
     * ���ûζ����� 
     */  
    public void setShakeAnimation(){  
        this.setAnimation(shakeAnimation(5));  
    }  
      
      
    /** 
     * �ζ����� 
     * @param counts 1���ӻζ������� 
     * @return 
     */  
    public static Animation shakeAnimation(int counts){  
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);  
        translateAnimation.setInterpolator(new CycleInterpolator(counts));  
        translateAnimation.setDuration(1000);  
        return translateAnimation;  
    }  
   
    public void setScanDrawable(Drawable scanDrawable){
    	mScanDrawable = scanDrawable;
    }
}  