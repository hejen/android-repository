package com.kqhelper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class QQHelperWorkerService extends Service {


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		QQCardHelperWorker qchw5 = new QQCardHelperWorker("ARgcN0nyTguSGHnYO2ZcJ0hc", QQHelperWorkerService.this);
//		qchw5.execute("refreshCardInfo");
		QQCardHelperWorker qchw = new QQCardHelperWorker("AYASVZlbvJNzrPaLoUB6bKpb", QQHelperWorkerService.this);
		qchw.execute("dailyWork");
		QQCardHelperWorker qchw1 = new QQCardHelperWorker("AdIgWDifiRcX8tASMSKYVb1Z", QQHelperWorkerService.this);
		qchw1.execute("dailyWork");
		QQCardHelperWorker qchw2 = new QQCardHelperWorker("ARgcN0nyTguSGHnYO2ZcJ0hc", QQHelperWorkerService.this);
		qchw2.execute("dailyWork");
		QQCardHelperWorker qchw3 = new QQCardHelperWorker("ARXRV85sJGOw5eCjMthxuIta", QQHelperWorkerService.this);
		qchw3.execute("dailyWork");
		QQCardHelperWorker qchw4 = new QQCardHelperWorker("Af_WGcIROJ71bPEuK0x3XA1a", QQHelperWorkerService.this);
		qchw4.execute("dailyWork");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
