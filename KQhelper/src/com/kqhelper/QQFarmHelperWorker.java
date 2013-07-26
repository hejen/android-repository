package com.kqhelper;

import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.kqhelper.db.WorkListManager;

public class QQFarmHelperWorker extends QQHelperWorker {

	public QQFarmHelperWorker(String sid, Context context){
		this.sid = sid;
		this.context = context;
		this.wm = new WorkListManager(context);
	}
	
	public QQFarmHelperWorker(){
	}
	
	private String getMainPageUrl(){
		return "http://mcapp.z.qq.com/nc/cgi-bin/wap_farm_index?sid="+sid+"&g_ut=1&source=moka";
	}
	
	private String getSigninUrl(){
		return "http://mcapp.z.qq.com/nc/cgi-bin/wap_farm_index?sid="+sid+"&g_ut=1&signin=1";
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
			signin();
			oneKeyDailyWork();
			Intent intent = new Intent("com.kqhelper.message");
			intent.putExtra("messageType", "finish");
			intent.putExtra("message", sid);
			context.sendBroadcast(intent);
		}
		return null;
	}

	private void oneKeyDailyWork() {
		String mainText = LinkMatcher.getLinkText(this.getMainPageUrl(),null);
		this.oneKeyWork(mainText, "收获");
		this.oneKeyWork(mainText, "除草");
		this.oneKeyWork(mainText, "杀虫");
		this.oneKeyWork(mainText, "浇水");
//		this.oneKeyWork(mainText, "施肥");
	}
	
	private void oneKeyWork(String httpText, String linkName) {
		List<String> fetchUrls = LinkMatcher.getLink(httpText, linkName);
		if (fetchUrls!=null && fetchUrls.size()>0){
			LinkMatcher.getLinkText(fetchUrls.get(0), this.getMainPageUrl());
		}
	}

	private void signin() {
		LinkMatcher.getLinkText(this.getSigninUrl(), this.getMainPageUrl());
		String mainText = LinkMatcher.getLinkText(this.getMainPageUrl(),null);
		List<String> fetchUrls = LinkMatcher.getLink(mainText, "领取奖励");
		while (fetchUrls!=null && fetchUrls.size()>0){
			fetchUrls = LinkMatcher.getLinkFromUrl(fetchUrls.get(0), this.getMainPageUrl(), "领取奖励");
		}
	}

}
