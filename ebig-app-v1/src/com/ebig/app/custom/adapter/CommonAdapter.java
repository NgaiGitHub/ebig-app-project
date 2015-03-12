package com.ebig.app.custom.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 1、视图操作类必须继承commonViewHolder<br />
 * 2、可以记录当前滑动所刷新的页数据<br />
 * 3、只支持同一种输出类型，多类型请自定义适配器<br />
 * 数据源,操作的对象是Map数据。<br />
 * 
 * @author HungLau.Ngai
 * 
 */
public class CommonAdapter extends BaseAdapter {

	protected List mItems;// view + map
	protected Context mContext;
	protected Class mViewHolder;

	private int page = 1;
	private int pageSize = 5;
	private int pageMax = -1;

	private Object mTag;

	public void nextPage() {
		if (pageMax < 0)
			page++;
	}

	public void prevPage() {
		if (page > 1)
			page--;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int size) {
		this.pageSize = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setTag(Object obj) {
		mTag = obj;
	}

	public Object getTag() {
		return mTag;
	}

	public CommonAdapter(Context context, Class viewHolder, List items) {
		super();
		this.mItems = items;
		this.mContext = context;
		this.mViewHolder = viewHolder;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {

		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	public List getList(){
		return mItems;
	};
	public boolean setList(List items) {
		if (mItems != null) {
			mItems.clear();
			mItems.addAll(items);
			notifyDataSetInvalidated();
			return true;
		}
		return false;
	}
	
	public boolean addList(List items) {
		if (mItems != null) {
			mItems.addAll(items);
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Object item = getItem(position);

		CommonViewHolder obj = null;

		try {
			if (v == null) {
				obj = (CommonViewHolder) mViewHolder.newInstance();
				obj.putContext(mContext);
				obj.setPosition(position);
				v = obj.createView();
				obj.refashViewValue(item);
				v.setTag(obj);
			} else {
				obj = (CommonViewHolder) v.getTag();
				obj.setPosition(position);
				obj.refashViewValue(item);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return v;
	}

}
