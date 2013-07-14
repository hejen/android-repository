package com.kqhelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kqhepler.R;

public class MainActivity extends Activity {

	private EditText text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btnStartWork = (Button)findViewById(R.id.startWork);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(1000);
		intentFilter.addAction("com.kqhelper.message");
		registerReceiver(new ServiceMessageReceiver(), intentFilter);
		text = (EditText)findViewById(R.id.editText1);
		btnStartWork.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, QQCardHelperWorkerService.class);
				startService(intent);
				bindService(intent, new ServiceConnection() {
					
					@Override
					public void onServiceDisconnected(ComponentName cName) {
						
					}
					
					@Override
					public void onServiceConnected(ComponentName cName, IBinder binder) {
						
					}
				}, BIND_AUTO_CREATE);
				Toast.makeText(MainActivity.this, "开始工作....", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class ServiceMessageReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			text.setText(intent.getCharSequenceExtra("message"));
		}
		
	}

}
