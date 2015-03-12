package com.ebig.app.custom.adapter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

/**
 * �������˹��ܵ�<br />
 * 1��filterString����������Դ����Ҫƥ����ֶ�ֵ<br />
 * 2������adapterFilter������������ֶ�
 * @author HungLau.Ngai
 * 
 */
public class CommonFilterAdapter extends CommonAdapter {

	private List mFilterItems;// view + map
	private CommonFilter mCommonFilter;

	public interface CommonFilter {
		/**
		 * ��Ҫ���˵��ֶ�!
		 * @param map
		 * @return
		 */
		public String filterString(Map map);
	}

	public CommonFilterAdapter(Context context, Class viewHolder, List items) {
		super(context, viewHolder, items);
		refashFilterItems();
	}

	public void setCommonFilter(CommonFilter filter) {
		mCommonFilter = filter;
	}

	public void notifyDataSetChanged(boolean refashFilter) {
		super.notifyDataSetChanged();
		if (refashFilter)
			refashFilterItems();
	}

	public void notifyDataSetInvalidated(boolean refashFilter) {
		super.notifyDataSetInvalidated();
		if (refashFilter)
			refashFilterItems();
	}

	protected void refashFilterItems() {
		if (mFilterItems == null)
			mFilterItems = new LinkedList();
		mFilterItems.clear();
		mFilterItems.addAll(mItems);
	}

	public void adapterFilter(String filter) {
		if (mFilterItems == null)
			return;
		if (mCommonFilter == null)
			return;

		mItems.clear();
		Pattern p = Pattern.compile(filter.toUpperCase());
		if (mFilterItems != null)
			for (int i = 0; i < mFilterItems.size(); i++) {
				Map tempItem = (Map) mFilterItems.get(i);
				String html = mCommonFilter.filterString(tempItem);// (String)
																	// tempItem.get("html");
				Matcher matcher = p.matcher(html);
				if (matcher.find()) {
					mItems.add(tempItem);
				}
			}
		notifyDataSetChanged();
	}

}
