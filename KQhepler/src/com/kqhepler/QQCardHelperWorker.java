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
		fetchCard(urlstr);
		return null;
	}
	
	private void fetchCard(String urlstr) {
		List<String> fetchUrls= LinkMatcher.getLinkFromUrl(urlstr, null, "�鿨");
		String fetchUrl = null;
		if (fetchUrls!=null){
			fetchUrl = fetchUrls.get(0);
		}
		String fetchResult = LinkMatcher.getLinkText(fetchUrl, urlstr);
		do{
			if (fetchResult.matches("����������")){
				return;
			}
			
		}while(true);
	}

	private void pickCard(String urlstr){
		List<String> pickUrls = LinkMatcher.getLinkFromUrl(urlstr,null, "ȡ��");
		do{
			while(pickUrls!=null && pickUrls.size()!=0){
				pickUrls = LinkMatcher.getLinkFromUrl(pickUrls.get(0), urlstr, "ȡ��");
			}
			List<String> putSuitCard = LinkMatcher.getLinkFromUrl(urlstr,null, "�����زĿ������뼯����");
			while(putSuitCard!=null && putSuitCard.size()!=0){
				putSuitCard = LinkMatcher.getLinkFromUrl(putSuitCard.get(0),null, "�����زĿ������뼯����");
			}
			pickUrls = LinkMatcher.getLinkFromUrl(urlstr,null, "ȡ��");
			if (pickUrls!=null && pickUrls.size()!=0 && LinkMatcher.isLinkHasText(putSuitCard.get(0), null, "���Ŀ�Ƭ������")){
				break;
			}
		}while (pickUrls!=null && pickUrls.size()!=0);
	}

}
