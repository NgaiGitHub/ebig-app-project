package com.ebig.zxing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ebig.app.R;

public class MainActivity extends Activity implements OnClickListener {
	private Button btn_qr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qcode);
		initView();
	}

	private void initView() {
		btn_qr = (Button) findViewById(R.id.btn_qr);
		btn_qr.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_qr:
			startActivity(new Intent(MainActivity.this, CaptureActivity.class));
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		getMenuInflater().inflate(R.menu.qrcode_menu, menu);
		return true;
	}
}
