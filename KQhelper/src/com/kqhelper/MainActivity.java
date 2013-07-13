package com.kqhelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kqhepler.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btnStartWork = (Button)findViewById(R.id.startWork);
		btnStartWork.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QQCardHelperWorker qchw5 = new QQCardHelperWorker("ARgcN0nyTguSGHnYO2ZcJ0hc", MainActivity.this, findViewById(R.id.editText1));
				qchw5.execute("refreshCardInfo");
				QQCardHelperWorker qchw = new QQCardHelperWorker("AYASVZlbvJNzrPaLoUB6bKpb", MainActivity.this, findViewById(R.id.editText1));
				qchw.execute("dailyWork");
//				QQCardHelperWorker qchw1 = new QQCardHelperWorker("AdIgWDifiRcX8tASMSKYVb1Z", findViewById(R.id.editText1));
//				qchw1.execute("dailyWork");
//				QQCardHelperWorker qchw2 = new QQCardHelperWorker("ARgcN0nyTguSGHnYO2ZcJ0hc", findViewById(R.id.editText1));
//				qchw2.execute("dailyWork");
//				QQCardHelperWorker qchw3 = new QQCardHelperWorker("ARXRV85sJGOw5eCjMthxuIta", findViewById(R.id.editText1));
//				qchw3.execute("dailyWork");
//				QQCardHelperWorker qchw4 = new QQCardHelperWorker("Af_WGcIROJ71bPEuK0x3XA1a", findViewById(R.id.editText1));
//				qchw4.execute("dailyWork");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
