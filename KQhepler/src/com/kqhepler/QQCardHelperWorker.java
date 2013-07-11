package com.kqhepler;

import java.util.List;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

public class QQCardHelperWorker extends AsyncTask<String, String, Void> {

	private View[] views;
	
	public QQCardHelperWorker(View... views){
		this.views = views;
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		TextView tv = (TextView)views[0];
		tv.setText(values[0]);
	}

	@Override
	protected Void doInBackground(String... params) {
		String urlstr = params[0];
		pickCard(urlstr);
		return null;
	}
	
	private void pickCard(String urlstr){
		List<String> pickUrls = LinkMatcher.getLinkFromUrl(urlstr,null, "È¡¿¨");
		while(pickUrls!=null && pickUrls.size()!=0){
			pickUrls = LinkMatcher.getLinkFromUrl(pickUrls.get(0), urlstr, "È¡¿¨");
		}
	}

}
