package com.kqhelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		setMainPageUrl();
		String action = params[0];
		if ("dailyWork".equalsIgnoreCase(action)){
			String mainHttpText = LinkMatcher.getLinkText(mainPageUrl, null);
			signin(mainHttpText);
			modifyLuck(mainHttpText);
			getStone(mainHttpText);
			getPrize(mainHttpText);
			inviteCustomer();
			Intent intent = new Intent("com.kqhelper.message");
			intent.putExtra("messageType", "finish");
			intent.putExtra("message", sid);
			context.sendBroadcast(intent);
		}
		return null;
	}

	private void inviteCustomer() {
		String mainHttpText = LinkMatcher.getLinkText(mainPageUrl, null);
		String shopshowUrl = addLinkPrefix(LinkMatcher.getFirstLink(mainHttpText, "我的店铺"));
		String shopsHttpText = LinkMatcher.getLinkText(shopshowUrl, mainPageUrl);
		List<String> shopsUrl = LinkMatcher.getLink(shopsHttpText, "进店");
		for (String shopUrl: shopsUrl){
			shopUrl = addLinkPrefix(shopUrl);
			String shopHttpText = LinkMatcher.getLinkText(shopUrl, shopshowUrl);
			int emptySeatNum = getEmptySeatNum(shopHttpText);
			if (emptySeatNum==0){
				continue;
			}
			String inviteCust = getInviteCustomer(shopHttpText);
			if (inviteCust==null || inviteCust.equals("")){
				continue;
			}
			while (emptySeatNum>0){
				int emptyWaitingSeat = getEmptyWaitingSeat(LinkMatcher.getLinkText(shopUrl, shopshowUrl));
				inviteBasketCustomers(emptyWaitingSeat);
				List<Map> waitingCustomers = getWaitingCustomers(LinkMatcher.getLinkText(shopUrl, shopshowUrl));
				if (waitingCustomers!=null && waitingCustomers.size()>0){
					for (Map waitingCustomer: waitingCustomers){
						if (!inviteCust.equals(waitingCustomer.get("name").toString())){
							LinkMatcher.getLinkText(waitingCustomer.get("kickUrl").toString(), shopUrl);
						}else{
							LinkMatcher.getLinkText(waitingCustomer.get("serviceUrl").toString(), shopUrl);
							emptySeatNum--;
						}
					}
				}
			}
		}
	}

	private void inviteBasketCustomers(int emptyWaitingSeat) {
		// TODO Auto-generated method stub
	}

	private int getEmptyWaitingSeat(String linkText) {
		//TODO
		return 0;
	}

	private List<Map> getWaitingCustomers(String linkText) {
		//TODO
		return null;
	}

	private int getEmptySeatNum(String shopHttpText) {
		//TODO
		return 0;
	}

	private String getInviteCustomer(String mainHttpText) {
		String basketFlowerHttpText = LinkMatcher.getLinkText(addLinkPrefix(LinkMatcher.getFirstLink(mainHttpText, "至尊花篮邀请")), null);
		List<Map> basketCustomers = getBasketCustomer(basketFlowerHttpText);
		for (Map basketCustomer: basketCustomers){
			if ("3".equals(basketCustomer.get("price").toString())){
				return basketCustomer.get("name").toString();
			}
		}
		return null;
	}
	
	private List<Map> getBasketCustomer(String basketFlowerHttpText) {
		Pattern p = Pattern.compile(">([^><]+?)(\\d)M[^>]+href=[\"']([^<>]+)[\"']>邀请");
		Matcher m = p.matcher(basketFlowerHttpText);
		List<Map> result = new ArrayList<Map>();
		while(m.find()){
			Map customer = new HashMap();
			customer.put("name", m.group(1).trim());
			customer.put("price", m.group(2).trim());
			customer.put("url", m.group(3).trim());
			result.add(customer);
		}
		return result;
	}

	private void getPrize(String mainHttpText) {
		String anotherUrl = addLinkPrefix(LinkMatcher.getFirstLink(mainHttpText, "进入校友版再领取一次奖品"));
		String friendText = LinkMatcher.getLinkText(anotherUrl, null);
		String zoneUrl = addLinkPrefix(LinkMatcher.getFirstLink(friendText, "去空间领龙珠"));
		LinkMatcher.getLinkText(zoneUrl, anotherUrl);
		LinkMatcher.getLinkText(LinkMatcher.getFirstLink(mainHttpText, "连续登陆抽奖"), null);
	}

	private void getStone(String mainHttpText) {
		String storePageUrl = addLinkPrefix(LinkMatcher.getFirstLink(mainHttpText, "宝石合成"));
		String storePageText = LinkMatcher.getLinkText(storePageUrl, null);
		String getUrl = addLinkPrefix(LinkMatcher.getFirstLink(storePageText, "一键合成"));
		LinkMatcher.getLinkText(getUrl, storePageUrl);
	}

	private void modifyLuck(String mainHttpText) {
		String luckyUrl = addLinkPrefix(LinkMatcher.getFirstLink(mainHttpText, "改运"));
		LinkMatcher.getLinkText(luckyUrl, null);
	}

	private void signin(String mainHttpText) {
		String signinUrl = addLinkPrefix(LinkMatcher.getFirstLink(mainHttpText, "签到"));
		LinkMatcher.getLinkText(signinUrl, null);
	}
	
	private String addLinkPrefix(String url){
		if (url==null || "".equals(url)){
			return url;
		}
		if (!url.startsWith("http")){
			return "http://m.app600.twsapp.com/"+url;
		}
		return url;
	}

}
