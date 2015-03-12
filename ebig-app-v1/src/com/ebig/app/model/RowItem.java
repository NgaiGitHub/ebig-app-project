package com.ebig.app.model;

import com.ebig.app.utils.Cn2Spell;

import android.graphics.drawable.Drawable;

/**
 * @author HungLau.Ngai
 * 
 */
public class RowItem {

	protected Drawable mDefaultImgDraw;
	protected Drawable mDefaultRightImgDraw;
	protected String mTitle;
	protected int mId ;

	public RowItem(Drawable mDefaultImgDraw, Drawable mDefaultRightImgDraw,
			String mTitle, int mId) {
		super();
		this.mDefaultImgDraw = mDefaultImgDraw;
		this.mDefaultRightImgDraw = mDefaultRightImgDraw;
		this.mTitle = mTitle;
		this.mId = mId;
	}

	public RowItem(Drawable mDefaultImgDraw, String mTitle, int mId) {
		super();
		this.mDefaultImgDraw = mDefaultImgDraw;
		this.mTitle = mTitle;
		this.mId = mId;
		this.mDefaultRightImgDraw = null;
	}

	public RowItem(Drawable mDefaultImgDraw, String mTitle) {
		super();
		this.mDefaultImgDraw = mDefaultImgDraw;
		this.mTitle = mTitle;
		this.mId = -1;//Cn2Spell.getFirstSpell(mTitle);
		this.mDefaultRightImgDraw = null;
	}

	public Drawable getmDefaultImgDraw() {
		return mDefaultImgDraw;
	}

	public void setmDefaultImgDraw(Drawable mDefaultImgDraw) {
		this.mDefaultImgDraw = mDefaultImgDraw;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public Drawable getmDefaultRightImgDraw() {
		return mDefaultRightImgDraw;
	}

	public void setmDefaultRightImgDraw(Drawable mDefaultRightImgDraw) {
		this.mDefaultRightImgDraw = mDefaultRightImgDraw;
	}

	
}
