package com.ebig.app.activity.suggest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.dbctrl.AppDbCtrl;

public class SuggestFragment extends BaseFirmFragment {
	
	private EditText mEditText;
	
	static public SuggestFragment newIntance(Bundle bundle) {
		SuggestFragment mSuggestFragment = new SuggestFragment();
		mSuggestFragment.setArguments(bundle);
		return mSuggestFragment; 
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		return inflater.inflate(R.layout.fragment_suggest, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) { 
		super.onActivityCreated(savedInstanceState);
		mEditText = (EditText) getView().findViewById(R.id.suggest_ed);
	}

	private String getSuggestText(){
		if(mEditText == null)
			return "";
		return mEditText.getText().toString().trim();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		setDefaultBarTitle("意见与反馈");
		// TODO 增加提交按钮
//		setActionBarCustomView(R.layout.default_actionbar_fragment);
//		((TextView)getBarCustomView().findViewById(R.id.default_bar_left_title)).setText();
//		((Button)getBarCustomView().findViewById(R.id.default_bar_right_btn)).setBackgroundResource(R.drawable.aj2);
//		
//		((Button)getBarCustomView().findViewById(R.id.default_bar_right_btn)).setOnClickListener(this);
//		((Button)getBarCustomView().findViewById(R.id.default_bar_left_btn)).setOnClickListener(this);
	}

	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		if (result.equals(true)) {
			getToast().makeText(getActivity(), "反馈成功!谢谢!", null);
			getActivity().finish();
		}
	}

	@Override
	protected void onPostExecuteTaskError(int asyncid, Exception result) {
		super.onPostExecuteTaskError(asyncid, result);
		getToast().makeText(getActivity(), result.getMessage(), null);
	}

	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		if("".equals(getSuggestText()))
			throw new RuntimeException("意见不能为空!!");
		return AppDbCtrl.saveAccSuggest(getSuggestText());
	}

//	@Override
//	public void onClick(View v) {
//		super.onClick(v);
//		int id = v.getId();
//		switch (id) {
//		case R.id.default_bar_left_btn:
//			getActivity().finish();
//			break;
//		case R.id.default_bar_right_btn:
//			// TODO
//			newAsyncTaskExecute(-1);
//			break;
//		default:
//			break;
//		}
//	}
	
	
	
}
