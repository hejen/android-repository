package com.kqhelper;

import android.content.Context;
import android.os.AsyncTask;

import com.kqhelper.db.WorkListManager;

public abstract class QQHelperWorker extends AsyncTask<String, String, Void> {

	protected String sid;
	
	protected Context context;
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public WorkListManager getWm() {
		return wm;
	}

	public void setWm(WorkListManager wm) {
		this.wm = wm;
	}

	protected WorkListManager wm;
	
	public QQHelperWorker(String sid, Context context){
		this.sid = sid;
		this.context = context;
		this.wm = new WorkListManager(context);
	}
	
	public QQHelperWorker(){
	
	}
	
	public void init(String sid, Context context){
		this.sid = sid;
		this.context = context;
		this.wm = new WorkListManager(context);
	}

}
