package com.ebig.app.model;

import com.ebig.app.utils.Cn2Spell;
import com.ebig.app.utils.LogUtil;

import android.graphics.drawable.Drawable;

/**
 * @author HungLau.Ngai
 * 
 */
public class Contacts extends RowItem {

	private String mImgurl;
	// private Drawable mDefaultImgDraw;
	// private String mTitle;
	private String mTitleIndex;// »°∆¥“Ù!!~

	public Contacts(String mImgurl, Drawable mDefaultImgDraw, String mTitle,
			String mTitleIndex) {
		super(mDefaultImgDraw, mTitle);
		this.mImgurl = mImgurl;
		this.mDefaultImgDraw = mDefaultImgDraw;
		this.mTitleIndex = mTitleIndex;
	}

	public Contacts(String mImgurl, Drawable mDefaultImgDraw, String mTitle) {
		super(mDefaultImgDraw, mTitle);
		this.mImgurl = mImgurl;
		// this.mDefaultImgDraw = mDefaultImgDraw;
		// this.mTitle = mTitle;

		try {
			this.mTitleIndex = Cn2Spell.getFirstSpell(mTitle);
		} catch (Exception e) {
			LogUtil.Log(Contacts.class, e.getMessage());
			this.mTitleIndex = "@";
		}
	}

	public String getmImgurl() {
		return mImgurl;
	}

	public void setmImgurl(String mImgurl) {
		this.mImgurl = mImgurl;
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

	public String getmTitleIndex() {
		return mTitleIndex;
	}

	public void setmTitleIndex(String mTitleIndex) {
		this.mTitleIndex = mTitleIndex;
	}

}
