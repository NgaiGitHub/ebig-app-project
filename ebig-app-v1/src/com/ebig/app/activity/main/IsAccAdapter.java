package com.ebig.app.activity.main;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.model.Account;
import com.ebig.app.model.Category;
import com.ebig.app.model.Contacts;
import com.ebig.app.model.RowItem;

public class IsAccAdapter extends BaseAdapter {

	private String mSections = "ABC";
	private String[] mSectionsArr ;
	private Context mContext;
	//private int resourceId = R.layout.item_row;
	private LinkedList<Object> mCustonItemsSeq;// 排序处理
	private LinkedList<Object> mCustonItems;
 
	public IsAccAdapter(Context context, LinkedList<Object> custonItems) {
		super();
		mContext = context;
		mCustonItems = custonItems;
//		if(mCustonItemsSeq == null);
//			mCustonItemsSeq = new LinkedList<Object>();
		//groupSections();
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
		//groupSections();
	}
	
	@Override
	public int getViewTypeCount() {
		return 3;
	} 

	@Override
	public int getItemViewType(int position) {
		if(getItem(position) instanceof Category)
			return 1;
		if(getItem(position) instanceof RowItem)
			return 2;
		if(getItem(position) instanceof Account)
			return 0;
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

        if (item instanceof Category) {
            if (v == null) {
                v = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
            }
            ((TextView) v).setText(((Category) item).getmTitle());
        } else  if(item instanceof RowItem){
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
			RowItem tRowItem = (RowItem) item;
			holder.mTitle.setText(tRowItem.getmTitle());
			holder.mImageView.setImageDrawable(tRowItem.getmDefaultImgDraw());
			holder.mImageViewRight.setImageDrawable(tRowItem.getmDefaultRightImgDraw());
//			if (!"".equals(tContacts.getmImgurl())) {
//				App.getImageLoader().displayImage(tContacts.getmImgurl(), holder.mImageView, App.getDisplayImageOptions());
//			} else {
//				holder.mImageView.setImageDrawable(tContacts.getmDefaultImgDraw());
//			}
        } else if(item instanceof Account){
//           // 个人账号信息
        	if (v == null) {
                v = LayoutInflater.from(mContext).inflate(R.layout.item_row_isacc, parent, false);
            }	
        	ImageView accImageView = (ImageView)v.findViewById(R.id.itemrow_acc_img);
        	TextView accTextView = (TextView)v.findViewById(R.id.itemrow_acc_name);
        	TextView accTextViewDtl = (TextView)v.findViewById(R.id.itemrow_acc_namedtl);
        	Account tAccount = (Account) item;
        	accTextView.setText(tAccount.getAccountName());
        	accTextViewDtl.setText("以讯号:"+tAccount.getAccountId());
        	if (!"".equals(tAccount.getAccountPic())) {
        		accImageView.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.v3));
				//App.getImageLoader().displayImage(tAccount.getAccountPic(), accImageView, App.getDisplayImageOptions());
			} else {
				accImageView.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.v3));
		    }
        }
 
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
