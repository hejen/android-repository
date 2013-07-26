package com.kqhelper;

import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.kqhelper.db.WorkListManager;

public class QQHelperWorkerService extends Service {


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if ("qqcard.dailyWork".equals(intent.getStringExtra("action"))){
			WorkListManager wlm = new WorkListManager(this);
			List<Map> workList = (List<Map>)wlm.getAllValidWorkList();
			for (Map workLine: workList){
				try {
					QQHelperWorker qhw = (QQHelperWorker) Class.forName(workLine.get("cWorkClassName").toString()).newInstance();
					qhw.init(workLine.get("csid").toString(), QQHelperWorkerService.this);
					qhw.execute("dailyWork");
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
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
