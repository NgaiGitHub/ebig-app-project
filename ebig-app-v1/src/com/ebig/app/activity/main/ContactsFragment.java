package com.ebig.app.activity.main;

import java.util.LinkedList;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.model.Contacts;
import com.woozzu.android.widget.IndexableListView;

/**
 * 通讯录,
 * @author HungLau.Ngai
 *
 */
public class ContactsFragment extends BaseFirmFragment {
	
	private IndexableListView mContactsListView;
	private View mContactsProgress;
	private ContactsAdapter mContactsAdapter ;
	private LinkedList<Object> mContacts;
	
	private LinkedList<Object> mDefaultRow;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contacts, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//if(mContactsListView != null) return ;
		View mRootView = getView();
		mContactsListView = (IndexableListView) mRootView.findViewById(R.id.contacts_lv);
		mContactsProgress = mRootView.findViewById(R.id.contacts_progress);
		
		if (mContacts == null) {
			mContacts = new LinkedList<Object>();
		}
		if(mDefaultRow == null){
			mDefaultRow = new LinkedList<Object>();
			Resources tResources = getActivity().getResources();
			mDefaultRow.add(new Contacts("", tResources.getDrawable(R.drawable.a8j), "添加朋友","@"));
			mDefaultRow.add(new Contacts("", tResources.getDrawable(R.drawable.a8r), "群聊","@"));
			mDefaultRow.add(new Contacts("", tResources.getDrawable(R.drawable.a8v), "标签","@"));
			mDefaultRow.add(new Contacts("", tResources.getDrawable(R.drawable.a8p), "公众号","@"));
		}
		if (mContactsAdapter == null)
			mContactsAdapter = new ContactsAdapter(getActivity(), mContacts);
		if (mContacts.size() > 0)
			mContactsProgress.setVisibility(View.GONE);
		mContactsListView.setAdapter(mContactsAdapter);
		mContactsListView.setFastScrollEnabled(true);

		if (mContacts.size()<=0) {
			newAsyncTaskExecute(100, 1);
		}
	}

	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		mContactsProgress.setVisibility(View.GONE);
		mContactsAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPostExecuteTaskError(int asyncid, Exception result) {
		super.onPostExecuteTaskError(asyncid, result);
	}

	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		
		if(mContacts == null)
			mContacts = new LinkedList<Object>();
		mContacts.clear();
		
		mContacts.addAll(mDefaultRow);
		String content = "qwerrtyuiopasddfgfgsdfxcvilzxcdfhghjcvbnm";
		for (int i = 0; i < content.length(); i++) {
			Drawable tDrawable = null;
			switch (i % 3) {
			case 0:
				tDrawable = App.getContext().getResources()
						.getDrawable(R.drawable.v0);
				break;
			case 1:
				tDrawable = App.getContext().getResources()
						.getDrawable(R.drawable.v1);
				break;
			case 2:
				tDrawable = App.getContext().getResources()
						.getDrawable(R.drawable.v2);
				break;
			case 3:
				tDrawable = App.getContext().getResources()
						.getDrawable(R.drawable.v3);
				break;

			default:
				tDrawable = App.getContext().getResources()
						.getDrawable(R.drawable.v0);
				break;
			}
			mContacts.add(new Contacts("", tDrawable, String.valueOf(content
					.charAt(i))));
		} 
		
		return mContacts;
	}

 
	
	
}
