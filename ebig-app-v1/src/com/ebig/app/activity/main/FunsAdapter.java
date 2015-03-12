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
import com.ebig.app.model.Functions;

public class FunsAdapter extends BaseAdapter {

	private Context mContext;
	private int resourceId = R.layout.item_gridview_funs_fragment;
	private LinkedList<Functions> mFunctions;


	
	public FunsAdapter(Context context) {
		super();
		mContext = context;
		
		if (mFunctions == null)
			mFunctions = new LinkedList<Functions>();
	}

	public LinkedList<Functions> getData(){
		return mFunctions;
	}

	public void setData(LinkedList<Functions> functions) {

		if(functions != null){			
			if(!mFunctions.containsAll(functions)){		
				mFunctions.clear();
				if (functions != null)
					mFunctions.addAll(functions);
			}
		}else {
			mFunctions.clear();
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if (mFunctions == null)
			return 0;
		return mFunctions.size();
	}

	@Override
	public Functions getItem(int position) {
		return (Functions) mFunctions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {// µ±Îª¿ÕÊ±
			holder = new ViewHolder();
			LayoutInflater infl = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infl.inflate(resourceId, null);
			holder.funName = (TextView) convertView.findViewById(R.id.funsTextView);
			holder.funImg = (ImageView) convertView.findViewById(R.id.funsImageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Functions mFuns = (Functions) getItem(position);
		if (mFuns == null) {
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.default_white));
			holder.funName.setText("");
			holder.funImg.setImageResource(R.drawable.space);
		} else {
			convertView.setBackgroundResource(R.drawable.item_funs_bg_sel);
			holder.funName.setText(mFuns.getModuleName());

//		String sc = moduleView.getIsShortcuts();
//		if (sc != null && "true".equals(sc)) {
//			holder.funShortcuts.setVisibility(View.VISIBLE);
//		} else {
//			holder.funShortcuts.setVisibility(View.INVISIBLE);
//		}

			String picUrl = mFuns.getModulePic();
			if (picUrl != null && !"".equals(picUrl)) {
				//imageLoader.displayImage(picUrl, holder.funImg, options, null);
				App.getImageLoader().displayImage(picUrl, holder.funImg ,App.getDisplayImageOptions());// , App.getDisplayImageOptions()
			} else {
				holder.funImg.setImageResource(R.drawable.space);
			}
		
//			int resId = MenuBtnImgSet.getFunResId(moduleView.getModuleId());
//			if (resId != -1) {
//				holder.funImg.setBackgroundResource(resId);
//				holder.funImg.setTag(resId);
//			}

		}
		return convertView;
	}

	public final class ViewHolder {
		private TextView funName;
		private ImageView funImg;
		//private TextView funShortcuts;
	}

}
