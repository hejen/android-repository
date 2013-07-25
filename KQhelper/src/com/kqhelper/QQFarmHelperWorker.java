package com.kqhelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.kqhelper.db.WorkListManager;

public class QQFarmHelperWorker extends AsyncTask<String, String, Void> {

	private String sid;
	
	private Context context;
	
	private WorkListManager wm;
	
	public QQFarmHelperWorker(String sid, Context context){
		this.sid = sid;
		this.context = context;
		this.wm = new WorkListManager(context);
	}
	
	private String getMainPageUrl(){
		return "http://mcapp.z.qq.com/nc/cgi-bin/wap_farm_index?sid="+sid+"&g_ut=1&source=moka";
	}
	
	private String getRefreshCardInfoPageUrl(String level, String pageno){
		return "http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_theme_list?sid="+sid+"&level="+level+"&fuin=0&steal=0&refine=0&pageno="+pageno;
	}
	
	
	private String getPrefer(String cName){
		return wm.getWorkPrefer("2", sid, cName);
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected Void doInBackground(String... params) {
		String action = params[0];
		if ("dailyWork".equalsIgnoreCase(action)){
//			pickCard(this.getMainPageUrl());
//			fetchCard(this.getMainPageUrl());
//			if (!"0".equals(getPrefer("smeltCard"))){
//				putCard(this.getMainPageUrl());
//			}
//			if ("1".equals(getPrefer("isSteal"))){
//				stealCard(this.getMainPageUrl());
//			}
			Intent intent = new Intent("com.kqhelper.message");
			intent.putExtra("messageType", "QQCard");
			intent.putExtra("message", sid);
			context.sendBroadcast(intent);
		}else if ("refreshCardInfo".equalsIgnoreCase(action)){
//			refreshAllCardInfo();
			Intent intent = new Intent("com.kqhelper.message");
			intent.putExtra("messageType", "qqcard.refresh");
			context.sendBroadcast(intent);
		}
		return null;
	}

}
