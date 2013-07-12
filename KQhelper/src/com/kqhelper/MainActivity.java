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
				QQCardHelperWorker qchw = new QQCardHelperWorker(findViewById(R.id.editText1));
				qchw.execute("http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_mainpage?sid=AYASVZlbvJNzrPaLoUB6bKpb&g_f=19011");
				QQCardHelperWorker qchw1 = new QQCardHelperWorker(findViewById(R.id.editText1));
				qchw1.execute("http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_mainpage?sid=AdIgWDifiRcX8tASMSKYVb1Z&g_f=19011");
				QQCardHelperWorker qchw2 = new QQCardHelperWorker(findViewById(R.id.editText1));
				qchw2.execute("http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_mainpage?sid=ARgcN0nyTguSGHnYO2ZcJ0hc&g_f=19011");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
