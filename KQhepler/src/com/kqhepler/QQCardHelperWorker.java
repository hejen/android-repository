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
		List<String> fetchUrls= LinkMatcher.getLinkFromUrl(urlstr, null, "抽卡");
		String fetchUrl = null;
		if (fetchUrls!=null){
			fetchUrl = fetchUrls.get(0);
		}
		String fetchResult = LinkMatcher.getLinkText(fetchUrl, urlstr);
		do{
			if (fetchResult.matches("换卡区满了")){
				return;
			}
			
		}while(true);
	}

	private void pickCard(String urlstr){
		List<String> pickUrls = LinkMatcher.getLinkFromUrl(urlstr,null, "取卡");
		do{
			while(pickUrls!=null && pickUrls.size()!=0){
				pickUrls = LinkMatcher.getLinkFromUrl(pickUrls.get(0), urlstr, "取卡");
			}
			List<String> putSuitCard = LinkMatcher.getLinkFromUrl(urlstr,null, "买齐素材卡并放入集卡册");
			while(putSuitCard!=null && putSuitCard.size()!=0){
				putSuitCard = LinkMatcher.getLinkFromUrl(putSuitCard.get(0),null, "买齐素材卡并放入集卡册");
			}
			pickUrls = LinkMatcher.getLinkFromUrl(urlstr,null, "取卡");
			if (pickUrls!=null && pickUrls.size()!=0 && LinkMatcher.isLinkHasText(putSuitCard.get(0), null, "您的卡片箱满了")){
				break;
			}
		}while (pickUrls!=null && pickUrls.size()!=0);
	}

}
