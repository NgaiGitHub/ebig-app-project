package com.ebig.zxing;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmActivity;
import com.ebig.zxing.camera.CameraManager;
import com.ebig.zxing.control.AmbientLightManager;
import com.ebig.zxing.control.BeepManager;
import com.ebig.zxing.decode.CaptureActivityHandler;
import com.ebig.zxing.decode.FinishListener;
import com.ebig.zxing.decode.InactivityTimer;
import com.ebig.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
 
public final class CaptureActivity extends BaseFirmActivity implements
		SurfaceHolder.Callback { 
	private boolean isTorchOn = false;
	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private Result savedResultToShow;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Collection<BarcodeFormat> decodeFormats;
	private Map<DecodeHintType, ?> decodeHints;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private AmbientLightManager ambientLightManager;
	
	public static final int ID = R.layout.capture;
	public static final String SCAN_CODE = "scan_code";// 扫描码
	
	private TextView mQCode;
	private TextView mOneCode;
	private TextView mLamp;
	
	private String scanCode;

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}
  
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.capture);
		
//		ActionBar actionBar = getActionBar();
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setTitle("二维码/条码");
//		actionBar.setIcon(null);
//		actionBar.setDisplayHomeAsUpEnabled(true); 
//		//actionBar.hide();

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);
		
		mQCode = (TextView) findViewById(R.id.qCode);
		mOneCode = (TextView) findViewById(R.id.oneCode);
		mLamp = (TextView) findViewById(R.id.lamp);
		
	}
 
	@Override
	protected void onResume() {
		super.onResume();

		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		handler = null;
		resetStatusView();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		beepManager.updatePrefs();
		ambientLightManager.start(cameraManager);

		inactivityTimer.onResume();

		decodeFormats = null;
		characterSet = null;
		
		
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		ambientLightManager.stop();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		viewfinderView.recycleLineDrawable();
		 
		if(cameraManager != null)
		cameraManager.setTorch(false);
		 
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_CAMERA:// 拦截相机键
			return true;
		case KeyEvent.KEYCODE_BACK:// 返回按钮
//			Intent intent = new Intent(CaptureActivity.this, BaseScreen.class);
//			intent.putExtra(SCAN_CODE, "");
//			setResult(CaptureActivity.ID,intent);
//			finish();
			scanCode = "";
			sendScanCode();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler,
						R.id.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/** 结果处理 */
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();
		//beepManager.playBeepSoundAndVibrate();

		scanCode = rawResult.getText();
		if (scanCode == null || "".equals(scanCode)) {
			scanCode = "无法识别";
		}

//		Intent intent = new Intent(CaptureActivity.this, ShowActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("msg", msg);
//		intent.putExtras(bundle);
//		startActivity(intent);

//		finish();
		sendScanCode();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			return;
		}
		if (cameraManager.isOpen()) {
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						decodeHints, characterSet, cameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("警告");
		builder.setMessage("抱歉，相机出现问题，您可能需要重启设备");
		builder.setPositiveButton("确定", new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		viewfinderView.setVisibility(View.VISIBLE);
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,0,"开灯");
		menu.add(0,2,0,"关灯");
		menu.add(0,3,1,"二维码");
		menu.add(0,4,2,"条形码"); 
		
		setDefaultTitle("二维码/条码");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		int id = item.getItemId();
		switch (id) {
		case 1:
			Toast.makeText(this, "打开灯光操作...", Toast.LENGTH_LONG).show();
			cameraManager.setTorch(true);
			break;
		case 2:
			Toast.makeText(this, "关闭灯光操作...", Toast.LENGTH_LONG).show();
			cameraManager.setTorch(false);
			break;
		case 3:
			Toast.makeText(this, "已添加扫描二维码功能...", Toast.LENGTH_LONG).show(); 
			break;
		case 4:
			Toast.makeText(this, "已添加扫描条形码功能...", Toast.LENGTH_LONG).show(); 
			break;
		case android.R.id.home:
//			Intent intent = new Intent(CaptureActivity.this, BaseScreen.class);
//			intent.putExtra(SCAN_CODE, "");
//			setResult(CaptureActivity.ID,intent);
//			finish();
			scanCode = "";
			sendScanCode();
		    break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void QCodeOnClick(View v) {
		switch (v.getId()) {
		case R.id.lamp://灯光
			boolean lamp = Boolean.valueOf(mLamp.getTag().toString());
			if(lamp){
				lamp = false;
				cameraManager.setTorch(lamp);
				mLamp.setTag(lamp);	
				mLamp.setTextColor(Color.WHITE);			
			}else {
				lamp = true;
				cameraManager.setTorch(lamp);
				mLamp.setTag(lamp);
				mLamp.setTextColor(Color.GREEN);
			}
			break;
			
		case R.id.qCode:// 二维码
			// TODO
			boolean isSel = Boolean.valueOf(mQCode.getTag().toString());
			if(!isSel){				
				handler.changeQCode();
				mQCode.setTextColor(Color.GREEN);
				mOneCode.setTextColor(Color.WHITE);
				mQCode.setTag("true");
			}
			break;
			
		case R.id.oneCode:// 一维码/条形码 
			boolean isSel_ = Boolean.valueOf(mQCode.getTag().toString());
			if(isSel_){				
				handler.changeOneCode();
				mQCode.setTextColor(Color.WHITE);
				mOneCode.setTextColor(Color.GREEN);
				mQCode.setTag("false");
			}
			break;

		default:
			break;
		}
	}
	
	public void sendScanCode(){
		if(cameraManager != null)
		cameraManager.setTorch(false);
		Intent intent = new Intent(CaptureActivity.this, BaseFirmActivity.class);
		intent.putExtra(SCAN_CODE, scanCode);
		setResult(CaptureActivity.ID,intent);
		finish();
	}
}
