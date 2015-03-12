package com.ebig.app.activity.main;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.model.Category;
import com.ebig.app.model.Contacts;
import com.woozzu.android.util.StringMatcher;

public class ContactsAdapter extends BaseAdapter implements SectionIndexer{

	private String mSections = "↑@ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
	private String[] mSectionsArr ;
	private Context mContext;
	private int resourceId = R.layout.item_contacts_fragment;
	private LinkedList<Object> mCustonItemsSeq;// 排序处理
	private LinkedList<Object> mCustonItems;
 
	public ContactsAdapter(Context context, LinkedList<Object> custonItems) {
		super();
		mContext = context;
		mCustonItems = custonItems;
		if(mCustonItemsSeq == null)
			mCustonItemsSeq = new LinkedList<Object>();
		groupSections();
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
		// 当重新刷新列表时,进行重新排序
		groupSections();
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	} 

	@Override
	public int getItemViewType(int position) {
		return getItem(position) instanceof Category ? 0 : 1;
	}

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof Contacts;
    }
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

//		ViewHolder holder = null;
//
//		if (convertView == null) {// 当为空时
//
//			holder = new ViewHolder();
//			LayoutInflater infl = (LayoutInflater) mContext
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = infl.inflate(resourceId, null);
//
//			holder.mTitle = (TextView) convertView
//					.findViewById(R.id.contacts_name);
//			holder.mImageView = (CircularImageView) convertView.findViewById(R.id.contacts_img);
//
//			convertView.setTag(holder);
//
//		} else {
//
//			holder = (ViewHolder) convertView.getTag();
//		} 
//  
//		if (getItem(position) instanceof Category) {
//			Category tCategory = (Category) getItem(position);
//			holder.mImageView.setImageDrawable(null);
//			holder.mTitle.setText(tCategory.getmTitle());
//		}else {
//			Contacts tContacts = (Contacts) getItem(position);
//			holder.mTitle.setText(tContacts.getmTitle());
//			if (!"".equals(tContacts.getmImgurl())) {
//				// holder.mImageView.setBackgroundResource(mCustonItems.getmImgResId());
//				App.getImageLoader().displayImage(tContacts.getmImgurl(),
//						holder.mImageView, App.getDisplayImageOptions());
//			}
//		}
		
		ViewHolder holder = null;
        View v = convertView;
        Object item = getItem(position);

        if (item instanceof Category) {
            if (v == null) {
                v = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_category, parent, false);
            }

            ((TextView) v).setText(((Category) item).getmTitle());

        } else {
            if (v == null) {
                //v = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_category, parent, false);
                holder = new ViewHolder();
                v = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
                
    			holder.mTitle = (TextView) v.findViewById(R.id.contacts_name);
    			holder.mImageView = (ImageView) v.findViewById(R.id.contacts_img);
    
    			v.setTag(holder);
            }else {
            	holder = (ViewHolder) convertView.getTag();
			}
            Contacts tContacts = (Contacts) item;
			holder.mTitle.setText(tContacts.getmTitle());
			if (!"".equals(tContacts.getmImgurl())) {
				App.getImageLoader().displayImage(tContacts.getmImgurl(),holder.mImageView, App.getDisplayImageOptions());
			}else {
				holder.mImageView.setImageDrawable(tContacts.getmDefaultImgDraw());
			}
        }
 
        return v;
	}

	public final class ViewHolder {
		//private CircularImageView mImageView;
		private ImageView mImageView;
		private TextView mTitle;
	}
	
	@Override
	public int getPositionForSection(int section) {
		// If there is no item for current section, previous section will be selected
		
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						String itemCharAt0 = "#";
						if((getItem(j) instanceof Contacts))
								itemCharAt0 = String.valueOf(((Contacts)getItem(j)).getmTitleIndex().charAt(0));
						if((getItem(j) instanceof Category))
							    itemCharAt0 = String.valueOf(((Category)getItem(j)).getmTitle().charAt(0));
						if (StringMatcher.match(itemCharAt0, String.valueOf(k)))
							return j;
					}
				} else {
					//String itemCharAt0 = String.valueOf(((Contacts)getItem(j)).getmTitleIndex().charAt(0));
					String itemCharAt0 = "#";
					if((getItem(j) instanceof Contacts))
							itemCharAt0 = String.valueOf(((Contacts)getItem(j)).getmTitleIndex().charAt(0));
					if((getItem(j) instanceof Category))
						    itemCharAt0 = String.valueOf(((Category)getItem(j)).getmTitle().charAt(0));
					if (StringMatcher.match(itemCharAt0, String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		if(mSectionsArr == null){
			mSectionsArr = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++){
				mSectionsArr[i] = String.valueOf(mSections.charAt(i));
			}			
		}
		return mSectionsArr;
	}
	
	public Object[] groupSections() {
		
		mCustonItemsSeq.clear();
		mCustonItemsSeq.addAll(mCustonItems);
		mCustonItems.clear();
		
		String[] sections = (String[]) getSections();
		Category tCategory ;
		for (int i = 0; i < sections.length; i++) {			
			// 添加信息并排序 steven update		
			tCategory = new Category(sections[i]);
			if (!"@".equals(tCategory.getmTitle()))
				mCustonItems.add(tCategory);
			if(!groupContacts(sections[i])){
				// 如果分组没有数据,那么删除分组标题
				mCustonItems.remove(tCategory);
			}
		}
		mCustonItemsSeq.clear();
		return sections;
	}

	// 根据分组增加通讯信息
	private boolean groupContacts(String indexStr) {
		boolean rs = false;
		if(mCustonItemsSeq != null && mCustonItemsSeq.size()>0)
		for (int i = 0; i < mCustonItemsSeq.size(); i++) {
			if(mCustonItemsSeq.get(i) instanceof Contacts){				
				Contacts tContacts = (Contacts) mCustonItemsSeq.get(i);
				if((tContacts.getmTitleIndex().toUpperCase()).equals(indexStr)){// 如果这个索引字符串与列表相同,那么归为一类
					mCustonItems.add(tContacts);
					rs = true;
				}
			}
		}
		return rs;
	}

}


//
//TextView tv = (TextView) v;
//tv.setText(((Item) item).mTitle);
//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//    tv.setCompoundDrawablesRelativeWithIntrinsicBounds(((Item) item).mIconRes, 0, 0, 0);
//} else {
//    tv.setCompoundDrawablesWithIntrinsicBounds(((Item) item).mIconRes, 0, 0, 0);
//}
