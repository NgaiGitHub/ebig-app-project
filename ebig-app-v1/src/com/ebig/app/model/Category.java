package com.ebig.app.model;

public class Category {

	private String mTitle;
	private int iconId;

	public Category(String mTitle, int iconId) {
		super();
		this.mTitle = mTitle;
		this.iconId = iconId;
	}

	public Category(String title) {
		mTitle = title;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

}
