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
     * 删除按钮的引用 
     */  
    private Drawable mClearDrawable;  
    /**
     * 扫描按钮的引用
     */
    private Drawable mScanDrawable;
    /** 
     * 控件是否有焦点 
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
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义  
        this(context, attrs, android.R.attr.editTextStyle);   
        this.context = context;
    }   
      
    public ScanClearEditText(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        this.context = context;
        init();  
    }  
      
      
    private void init() {   
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片  
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
        //默认设置隐藏图标  
        setClearIconVisible(false);   
        //设置焦点改变的监听  
        setOnFocusChangeListener(this);   
        //设置输入框里面内容发生改变的监听  
        addTextChangedListener(this);   
    }   
   
   
    /** 
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和 
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑 
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
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏 
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
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去 
     * @param visible 
     */  
    protected void setClearIconVisible(boolean visible) {   
        Drawable right = visible ? mClearDrawable : mScanDrawable;   
        setCompoundDrawables(getCompoundDrawables()[0],   
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);   
    }   
       
      
    /** 
     * 当输入框里面内容发生变化的时候回调的方法 
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
     * 设置晃动动画 
     */  
    public void setShakeAnimation(){  
        this.setAnimation(shakeAnimation(5));  
    }  
      
      
    /** 
     * 晃动动画 
     * @param counts 1秒钟晃动多少下 
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