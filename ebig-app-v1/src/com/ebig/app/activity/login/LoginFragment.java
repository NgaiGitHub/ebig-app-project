package com.ebig.app.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.activity.main.AppActivity;
import com.ebig.app.activity.register.RegisterActivity;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.custom.ui.ClearEditText;
import com.ebig.app.dbctrl.AppDbCtrl;
import com.github.siyamed.shapeimageview.CircularImageView;

public class LoginFragment extends BaseFirmFragment implements OnClickListener{
	
	private final int LOGIN_ACTION = 1;
	
	private ClearEditText acctEditText;
	private ClearEditText pswdEditText;
	public static String ACCOUNT_KEY = "account";
	public static String PSWD_KEY = "pswd";
	private CircularImageView mImageView;
	 
	static public LoginFragment newIntance(Bundle bundle) {
		LoginFragment mLoginFragment = new LoginFragment();
		mLoginFragment.setArguments(bundle);
		return mLoginFragment; 
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) { 
		super.onActivityCreated(savedInstanceState);
		findViews();
		Bundle mBundle = getArguments();
		String acc = mBundle.getString(ACCOUNT_KEY);
		String pswd = mBundle.getString(PSWD_KEY);
		if (acc != null)
			acctEditText.setText(acc);
		if (pswd != null)
			pswdEditText.setText(pswd);
	}

	private void findViews() {
		View rootView = getView();
		if(rootView == null) return ;
		if(acctEditText != null) return ;
		
		acctEditText = (ClearEditText) rootView.findViewById(R.id.login_account_edit);
		pswdEditText = (ClearEditText) rootView.findViewById(R.id.login_pswd_edit);
		((Button)rootView.findViewById(R.id.login_trouble)).setOnClickListener(this);
		((Button)rootView.findViewById(R.id.login_btn)).setOnClickListener(this);
		
		mImageView = (CircularImageView) rootView.findViewById(R.id.lockpatter_imageView);
		mImageView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login_trouble:
			App.getCroutonToast().makeTextError(getActivity(), "login_trouble", null);
			break;
		case R.id.login_btn:
			boolean rs = true;
			if("".equals(acctEditText.getText().toString().trim())){
				acctEditText.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
				rs = false;
			}
			if("".equals(pswdEditText.getText().toString().trim())){
				pswdEditText.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
				rs = false;
			}
			if(rs){
				newAsyncTaskExecute(LOGIN_ACTION, -1);
			}
			 
			break;
//		case R.id.default_bar_right_btn:
//			//App.getCroutonToast().makeTextError(getActivity(), "action_reg", null);
//			startActivity(new Intent(getActivity(), RegisterActivity.class));
//			break;
		default:
			break;
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		setDefaultBarTitle("以大科技");
		hideDefaultBarBack(true);
		inflater.inflate(R.menu.login_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login_rg:
			startActivity(new Intent(getActivity(), RegisterActivity.class));
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
//		// setActionBarCustomView(R.layout.actionbar_custom_loginbar);
//		setActionBarCustomView(R.layout.default_actionbar_fragment);
//		((Button)getBarCustomView().findViewById(R.id.default_bar_left_btn)).setVisibility(View.GONE);//
//		((TextView) getBarCustomView().findViewById(R.id.default_bar_left_title)).setText(getResources().getString(R.string.app_name));
//		((Button) getBarCustomView().findViewById(R.id.default_bar_right_btn)).setBackgroundResource(R.drawable.login_rg);
//		((Button) getBarCustomView().findViewById(R.id.default_bar_right_btn)).setOnClickListener(this);
//	}
//
//	@Override
//	protected void showProgressBar() {
//		showActionBarProgress();
//	}
//
//	@Override
//	protected void dismissProgressBar() {
//		hideActionBarProgress();
//	}


	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		if(result.equals(true)){
			startActivity(new Intent(getActivity(), AppActivity.class));
		}
	}

	@Override
	protected void onPostExecuteTaskError(int asyncid, Exception result) {
		App.getCroutonToast().makeTextError(getActivity(), result.getMessage(), null);
	}

	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		switch (asyncid) {
		case LOGIN_ACTION:
			try {
				//return true ;
				return AppDbCtrl.appLogin(acctEditText.getText().toString().trim(), pswdEditText.getText().toString().trim());
			} catch (Exception e) { 
				return e;
			}
		default:
			break;
		}
		return super.doInBackgroundTask(asyncid, params);
	}

	
	
}
