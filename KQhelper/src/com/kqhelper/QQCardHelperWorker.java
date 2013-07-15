package com.kqhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.kqhelper.db.DbManager;

public class QQCardHelperWorker extends AsyncTask<String, String, Void> {

	private String sid;
	
	private final int cardLevel = 1;
	
	private Context context;
	
	public QQCardHelperWorker(String sid, Context context){
		this.sid = sid;
		this.context = context;
	}
	
	private String getMainPageUrl(){
		return "http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_mainpage?sid="+sid+"&g_f=19011";
	}
	
	private String getRefreshCardInfoPageUrl(String level, String pageno){
		return "http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_theme_list?sid="+sid+"&level="+level+"&fuin=0&steal=0&refine=0&pageno="+pageno;
	}
	
	private String getCardThemeInfoUrl(String themeid){
		return "http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_theme_info?sid="+sid+"&themeid="+themeid;
	}
	
	private String getCardThemeInfoUrl(String themeid, String price){
		return getCardThemeInfoUrl(themeid)+"&price="+price;
	}
	
	private String getChangeBoxUrl(){
		return "http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_box?sid="+sid;
	}
	
	private String getStrategyUrl(String themeid){
		return "http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_strategy?sid="+sid+"&themeid="+themeid+"&fuin=0&steal=0";
	}
	
	private String getAllRefineUrl(String themeid){
		return "http://mfkp.qzapp.z.qq.com/qshow/cgi-bin/wl_card_refine?sid="+sid+"&show=1&pageno=1&fuin=0&steal=0&tid="+themeid;
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected Void doInBackground(String... params) {
		String action = params[0];
		if ("dailyWork".equalsIgnoreCase(action)){
			//pickCard(this.getMainPageUrl());
			//fetchCard(this.getMainPageUrl());
			putCard(this.getMainPageUrl());
			stealCard(this.getMainPageUrl());
		}else if ("refreshCardInfo".equalsIgnoreCase(action)){
			refreshAllCardInfo();
		}
		Intent intent = new Intent("com.kqhelper.message");
		intent.putExtra("message", sid+"完成");
		context.sendBroadcast(intent);
		return null;
	}
	
	private void stealCard(String mainPageUrl) {
		
	}

	private void putCard(String mainPageUrl) {
		String mainPageText = LinkMatcher.getLinkText(mainPageUrl, null);
		if (mainPageText.indexOf("空炉位")==-1){
			return;
		}
		String[] putCardids = getPutCardIds();
		for (int i=0;i<putCardids.length;i++){
			String strategyResultText = LinkMatcher.getLinkText(this.getStrategyUrl(putCardids[i]), mainPageUrl);
			if (strategyResultText.indexOf("没有可以合成的卡片")!=-1){
				continue;
			}
			String allRefineText = LinkMatcher.getLinkText(this.getAllRefineUrl(putCardids[i]), mainPageUrl);
			List<String> canRefineLinks = LinkMatcher.getLink(allRefineText, "合成");
			for (String canRefineLink: canRefineLinks){
				canRefineLinks = LinkMatcher.getLinkFromUrl(canRefineLink, mainPageUrl, "合成");
			}
			allRefineText = LinkMatcher.getLinkText(this.getAllRefineUrl(putCardids[i]), mainPageUrl);
			List<Map> refineInfo = parseRefineInfo(allRefineText);
		}
	}
	
	private List<Map> parseRefineInfo(String allRefineText) {
		Pattern p = Pattern.compile("\\d+\\.[^\"]+].+?买齐素材卡并合成");
		Matcher m = p.matcher(allRefineText);
		List<String> tempText = new ArrayList<String>();
		while(m.find()){
			tempText.add(m.group());
		}
		
		return null;
	}

	private String[] getPutCardIds() {
		String[] themeids = new String[1];
		themeids[0] = "52";
		return themeids;
	}

	private void refreshAllCardInfo() {
		List<Map> cardSuits = new ArrayList<Map>();
		List<Map> cardInfo = new ArrayList<Map>();
		for (int i=0;i<this.cardLevel; i++){
			String cardInfoAll = LinkMatcher.getLinkText(this.getRefreshCardInfoPageUrl(String.valueOf(i+1), "1"), null);
			int pageNum = getAllPage(cardInfoAll, i);
			int curPage = 1;
			do{
				cardInfoAll = LinkMatcher.getLinkText(this.getRefreshCardInfoPageUrl(String.valueOf(i+1), String.valueOf(curPage)), null);
				List<Map> cardSuitList = getCardSuit(cardInfoAll, i);
				for (Map cardSuit: cardSuitList){
					cardInfo.addAll(getCardInfo(cardSuit));
				}
				cardSuits.addAll(cardSuitList);
				curPage++;
			}while (curPage<=pageNum);
		}
		List<String[]> cardSuitsParams = new ArrayList<String[]>();
		for (Map cardSuit: cardSuits){
			String[] param = new String[3];
			param[0] = cardSuit.get("cThemeid").toString();
			param[1] = cardSuit.get("iLevel").toString();
			param[2] = cardSuit.get("cName").toString();
			cardSuitsParams.add(param);
		}
		List<String[]> cardInfoParams = new ArrayList<String[]>();
		for (Map cardInfoParam: cardInfo){
			String[] param = new String[5];
			param[0] = cardInfoParam.get("cThemeid").toString();
			param[1] = cardInfoParam.get("cName").toString();
			param[2] = cardInfoParam.get("iPrice").toString();
			param[3] = cardInfoParam.get("iIsBottom").toString();
			param[4] = cardInfoParam.get("cSubs")==null?null:cardInfoParam.get("cSubs").toString();
			cardInfoParams.add(param);
		}
		DbManager db = new DbManager(context);
		db.update("delete from CO_CardSuit");
		db.update("delete from CO_CardInfo");
		db.batchUpdate("insert into CO_CardSuit(cThemeid,iLevel,cName) values(?,?,?)", cardSuitsParams);
		db.batchUpdate("insert into CO_CardInfo(cThemeid,cName,iPrice,iIsBottom,cSubs) values(?,?,?,?,?)", cardInfoParams);
	}
	
	private List<Map> getCardInfo(Map cardSuit) {
		List<Map> result = getBaseCard(cardSuit.get("cThemeid").toString());
		String cardInfoText = LinkMatcher.getLinkText(this.getCardThemeInfoUrl(cardSuit.get("cThemeid").toString()),null);
		Pattern p = Pattern.compile("合成卡\\[(\\d+)]");
		Matcher m = p.matcher(cardInfoText);
		List<String> prices = new ArrayList<String>();
		while(m.find()){
			prices.add(m.group(1));
		}
		Collections.reverse(prices);
		for (String price: prices){
			result.addAll(getGenCard(cardSuit.get("cThemeid").toString(), price));
		}
		return result;
	}

	private List<Map> getBaseCard(String cThemeid) {
		String cardInfoText = LinkMatcher.getLinkText(this.getCardThemeInfoUrl(cThemeid, "10"),null);
		Pattern p = Pattern.compile("\\d\\.([^\"]+):");
		Matcher m = p.matcher(cardInfoText);
		List<Map> result = new ArrayList<Map>();
		while(m.find()){
			Map map = new HashMap();
			map.put("cThemeid", cThemeid);
			map.put("cName", m.group(1).trim());
			map.put("iPrice", 10);
			map.put("iIsBottom", 1);
			result.add(map);
		}
		return result;
	}
	
	private List<Map> getGenCard(String cThemeid, String price) {
		String cardInfoText = LinkMatcher.getLinkText(this.getCardThemeInfoUrl(cThemeid, price),null);
		Pattern p = Pattern.compile("\\d\\.([^\"=]+):[^\"=]+=(.+?)合成");
		Matcher m = p.matcher(cardInfoText);
		List<Map> result = new ArrayList<Map>();
		while(m.find()){
			Map map = new HashMap();
			map.put("cThemeid", cThemeid);
			map.put("cName", m.group(1).trim());
			map.put("iPrice", price);
			map.put("iIsBottom", 0);
			map.put("cSubs", getSubCardStr(m.group(2)));
			result.add(map);
		}
		return result;
	}

	private String getSubCardStr(String str) {
		return str.replaceAll("<br/>", "");
	}

	private List<Map> getCardSuit(String cardInfoAll, int i){
		Pattern p = Pattern.compile("themeid=(\\d+)[^>]*>(.+?)</a>");
		Matcher m = p.matcher(cardInfoAll);
		List<Map> result = new ArrayList<Map>();
		while(m.find()){
			Map map = new HashMap();
			map.put("cThemeid", m.group(1));
			map.put("cName", m.group(2));
			map.put("iLevel", i+1);
			result.add(map);
		}
		return result;
	}

	private int getAllPage(String cardInfoAll, int level) {
		String matchStr = this.getRefreshCardInfoPageUrl(String.valueOf(level), "\\d+");
		return LinkMatcher.getMatchNum(cardInfoAll, matchStr)+1;
	}

	private void fetchCard(String urlstr) {
		List<String> fetchUrls= LinkMatcher.getLinkFromUrl(urlstr, null, "抽卡");
		String fetchUrl = null;
		if (fetchUrls==null || fetchUrls.size()==0){
			fetchUrl = this.getChangeBoxUrl();
		}else{
			fetchUrl = fetchUrls.get(0);
		}
		String fetchResult = LinkMatcher.getLinkText(fetchUrl, urlstr);
		do{
			List<String> baseCardSellLinks = LinkMatcher.getSaleCardLinkByPrice(fetchResult, "10");
			if (baseCardSellLinks.size()==0){
				return;
			}
			while (baseCardSellLinks!=null && baseCardSellLinks.size()!=0){
				baseCardSellLinks = LinkMatcher.getSaleCardLinkByPrice(LinkMatcher.getLinkText(baseCardSellLinks.get(0), urlstr), "10");
			}
			fetchResult = LinkMatcher.getLinkText(fetchUrl, urlstr);
		}while(LinkMatcher.getSaleCardLinkByPrice(fetchResult, "10").size()!=0);
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
