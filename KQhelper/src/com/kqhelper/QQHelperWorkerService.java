package com.kqhelper;

import java.util.List;
import java.util.Map;

import com.kqhelper.db.WorkListManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class QQHelperWorkerService extends Service {


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		QQCardHelperWorker qchw5 = new QQCardHelperWorker("ARgcN0nyTguSGHnYO2ZcJ0hc", QQHelperWorkerService.this);
//		qchw5.execute("refreshCardInfo");
		if ("qqcard.dailyWork".equals(intent.getStringExtra("action"))){
			WorkListManager wlm = new WorkListManager(this);
			List<Map> workList = (List<Map>)wlm.getAllValidWorkList();
			for (Map workLine: workList){
				QQCardHelperWorker qchw = new QQCardHelperWorker(workLine.get("csid").toString(), QQHelperWorkerService.this);
				qchw.execute("dailyWork");
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
