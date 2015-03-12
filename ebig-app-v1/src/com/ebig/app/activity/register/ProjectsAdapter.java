package com.ebig.app.activity.register;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebig.app.R;

public class ProjectsAdapter extends BaseAdapter {

	private String mSections = "ABC";
	private String[] mSectionsArr ;
	private Context mContext;
	//private int resourceId = R.layout.item_row;
	private List mCustonItems;
 
	public ProjectsAdapter(Context context, List custonItems) {
		super();
		mContext = context;
		mCustonItems = custonItems;
	}

	@Override
	public int getCount() {
		if (mCustonItems == null)
			return 0;
		return mCustonItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mCustonItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	} 

	@Override
	public int getItemViewType(int position) {
//		if(getItem(position) instanceof Category)
//			return 1;
//		if(getItem(position) instanceof RowItem)
//			return 2;
//		if(getItem(position) instanceof Account)
//			return 0;
		return 0;
	}

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
        View v = convertView;
        Object item = getItem(position);

		if (v == null) {
			// v = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_category, parent, false);
			holder = new ViewHolder();
			v = LayoutInflater.from(mContext).inflate(R.layout.item_row, parent, false); 
			holder.mTitle = (TextView) v.findViewById(R.id.itemrow_name);
			holder.mImageView = (ImageView) v .findViewById(R.id.itemrow_img); 
			holder.mImageViewRight = (ImageView) v .findViewById(R.id.itemrow_img_right); 
			v.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map tRowItem = (Map) item;
		holder.mTitle.setText((String)tRowItem.get("projectname"));
		holder.mImageView.setImageResource(R.drawable.amg);
		holder.mImageViewRight.setImageDrawable(null);
		
        return v;
	}

	public final class ViewHolder {
	    private ImageView mImageViewRight;
		private ImageView mImageView;
		private TextView mTitle;
	}
	
	public Object[] getSections() {
		if(mSectionsArr == null){
			mSectionsArr = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++){
				mSectionsArr[i] = String.valueOf(mSections.charAt(i));
			}			
		}
		return mSectionsArr;
	}
	
}
