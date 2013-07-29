package com.kqhelper;

import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.kqhelper.db.WorkListManager;

public class QQTowerHelperWorker extends QQHelperWorker {
	
	String mainPageUrl;

	public QQTowerHelperWorker(String sid, Context context){
		this.sid = sid;
		this.context = context;
		this.wm = new WorkListManager(context);
	}
	
	public QQTowerHelperWorker(){
	}
	
	@Override
	public void init(String sid, Context context) {
		super.init(sid, context);
		this.setMainPageUrl();
	}
	
	private String getRedirectPageUrl(){
		return "http://app20.z.qq.com/redirect.jsp?appid=601&type=103&sid="+sid;
	}
	
	private void setMainPageUrl(){
		List<String> mainPageUrls = LinkMatcher.getLinkFromUrl(getRedirectPageUrl(), null, "点这里跳转");
		if (mainPageUrls!=null && mainPageUrls.size()>0){
			this.mainPageUrl = mainPageUrls.get(0);
		}
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
			String mainHttpText = LinkMatcher.getLinkText(mainPageUrl, null);
			signin(mainHttpText);
			modifyLuck(mainHttpText);
			getStone(mainHttpText);
			getPrize(mainHttpText);
			Intent intent = new Intent("com.kqhelper.message");
			intent.putExtra("messageType", "finish");
			intent.putExtra("message", sid);
			context.sendBroadcast(intent);
		}
		return null;
	}

	private void getPrize(String mainHttpText) {
		
	}

	private void getStone(String mainHttpText) {
		
	}

	private void modifyLuck(String mainHttpText) {
		
	}

	private void signin(String mainHttpText) {
		
	}

}
