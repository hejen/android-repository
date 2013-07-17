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

import com.kqhelper.db.WorkListManager;
import com.kqhepler.R;

public class MainActivity extends Activity {
	
	private List<? extends Map<String, Object>> workList;

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
				refreshListViewForBegin();
				refreshListViewData();
				Toast.makeText(MainActivity.this, "开始工作....", Toast.LENGTH_LONG).show();
			}

		});
	}

	private void refreshListViewForBegin() {
		for (Map<String, Object> map: workList){
			if ("".equals(map.get("iStatus").toString())){
				map.put("iStatus", "工作中....");
			}
		}
	}
	
	private void initReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(1000);
		intentFilter.addAction("com.kqhelper.message");
		registerReceiver(new ServiceMessageReceiver(), intentFilter);
	}


	private void initListView() {
		ListView workList = (ListView)findViewById(R.id.workList);
		this.workList = getWorkList();
		SimpleAdapter sa = new SimpleAdapter(this, this.workList, R.layout.vlist, new String[]{"cWorkTypeName","cName","iStatus"}, new int[]{R.id.cWorkTypeName,R.id.cName,R.id.iStatus});
		workList.setAdapter(sa);
	}
	
	private void refreshListViewData(){
		ListView workList = (ListView)findViewById(R.id.workList);
		SimpleAdapter sa = (SimpleAdapter)workList.getAdapter();
		sa.notifyDataSetChanged();
	}


	private List<? extends Map<String, Object>> getWorkList() {
		WorkListManager wlm = new WorkListManager(this);
		List<Map> workList = (List<Map>)wlm.getAllValidWorkList();
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		 
		for (Map workLine: workList){
			Map<String, Object> map = new HashMap<String, Object>(workLine);
			if ("1".equals(map.get("iStatus").toString())){
				map.put("iStatus", "");
			}else{
				map.put("iStatus", "禁用");
			}
			list.add(map);
		}
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
			if ("QQCard".equalsIgnoreCase(intent.getStringExtra("messageType"))){
				qqcardReceive(context, intent);
			}
		}

		private void qqcardReceive(Context context, Intent intent) {
			String csid = intent.getStringExtra("message");
			for (Map<String, Object> map: workList){
				if (csid.equalsIgnoreCase(map.get("csid").toString())){
					map.put("iStatus", "完成");
				}
			}
			refreshListViewData();
		}
		
	}

}
