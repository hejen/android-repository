package com.kqhelper;

import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.kqhelper.db.WorkListManager;

public class QQHelperWorkerService extends Service {


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		QQCardHelperWorker qchw5 = new QQCardHelperWorker("ARgcN0nyTguSGHnYO2ZcJ0hc", QQHelperWorkerService.this);
//		qchw5.execute("refreshCardInfo");
		if ("qqcard.dailyWork".equals(intent.getStringExtra("action"))){
			WorkListManager wlm = new WorkListManager(this);
			List<Map> workList = (List<Map>)wlm.getAllValidWorkList();
			for (Map workLine: workList){
				if ("com.kqhelper.QQCardHelperWorker".equalsIgnoreCase(workLine.get("cWorkClassName").toString())){
					QQCardHelperWorker qchw = new QQCardHelperWorker(workLine.get("csid").toString(), QQHelperWorkerService.this);
					qchw.equals("dailyWork");
				}else if ("com.kqhelper.QQFarmHelperWorker".equalsIgnoreCase(workLine.get("cWorkClassName").toString())){
					QQFarmHelperWorker qfhw = new QQFarmHelperWorker(workLine.get("csid").toString(), QQHelperWorkerService.this);
					qfhw.execute("dailyWork");
				}
			}
		}else if ("qqcard.refreshCard".equals(intent.getStringExtra("action"))){
			String csid = intent.getStringExtra("sid");
			QQCardHelperWorker qchw = new QQCardHelperWorker(csid, QQHelperWorkerService.this);
			qchw.execute("refreshCardInfo");
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
