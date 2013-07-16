package com.kqhelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kqhepler.R;

public class MainActivity extends Activity {

	private ServiceConnection sc = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			
		}
	};
	
	@Override
	protected void onDestroy() {
		unbindService(sc);
		stopService(new Intent(MainActivity.this, QQHelperWorkerService.class));
		super.onDestroy();
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initListView();
		initReceiver();
		
		Button btnStartWork = (Button)findViewById(R.id.startWork);
		btnStartWork.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, QQHelperWorkerService.class);
				startService(intent);
				bindService(intent, sc, BIND_AUTO_CREATE);
				Toast.makeText(MainActivity.this, "开始工作....", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void initReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(1000);
		intentFilter.addAction("com.kqhelper.message");
		registerReceiver(new ServiceMessageReceiver(), intentFilter);
	}


	private void initListView() {
		ListView workList = (ListView)findViewById(R.id.workList);
		SimpleAdapter sa = new SimpleAdapter(this, getWorkList(), R.layout.vlist, new String[]{"cWorkType","cName","iStatus"}, new int[]{R.id.cWorkType,R.id.cName,R.id.iStatus});
		workList.setAdapter(sa);
	}


	private List<? extends Map<String, ?>> getWorkList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		 
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cWorkType", "G1");
        map.put("cName", "google 1");
        map.put("iStatus", 1);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("cWorkType", "G2");
        map.put("cName", "google 2");
        map.put("iStatus", 1);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("cWorkType", "G3");
        map.put("cName", "google 3");
        map.put("iStatus", 1);
        list.add(map);
        return list;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class ServiceMessageReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
		}
		
	}

}
