package com.kqhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkMatcher {

	public static List<String> getLink(String httpText, String linkName){
		Pattern p = Pattern.compile("href=\"?([^\\s\">]*)\"?[^>]*>"+linkName);
		Matcher m = p.matcher(httpText);
		List<String> result = new ArrayList<String>();
		while(m.find()){
			result.add(m.group(1).replaceAll("&amp;", "&"));
		}
		return result;
	}
	
	public static List<String> getSaleCardLinkByPrice(String httpText, String price){
		Pattern p = Pattern.compile("\\["+price+"].+?href=\"?([^\\s\">]*)\"?[^>]*>出售");
		Matcher m = p.matcher(httpText);
		List<String> result = new ArrayList<String>();
		while(m.find()){
			result.add(m.group(1).replaceAll("&amp;", "&"));
		}
		return result;
	}
	
	public static boolean isHasText(String httpText, String matchText){
		return httpText.matches(matchText);
	}
	
	public static boolean isLinkHasText(String urlstr, String referUrl, String matchText){
		return isHasText(getLinkText(urlstr, referUrl), matchText);
	}
	
	public static String getLinkText(String urlstr, String referUrl){
		URL url;
		try {
			url = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5* 1000);//设置连接超时
			conn.setUseCaches(false);
			conn.setRequestProperty("User-agent", "JUC (Linux; U; 2.3.7; zh-cn; MB200; 320*480) UCWEB7.9.3.103/139/999");
			conn.setRequestMethod("GET");//以get方式发起请求
			if (referUrl!=null){
				conn.setRequestProperty("Referer", referUrl);
			}
			if (conn.getResponseCode() != 200) throw new RuntimeException("请求url失败");
			InputStream is = conn.getInputStream();//得到网络返回的输入流
			String httpText = readData(is);
			conn.disconnect();
			return httpText;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> getLinkFromUrl(String urlstr, String referUrl, String linkName){
		return getLink(getLinkText(urlstr, referUrl), linkName);
	}
	
	private static String readData(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer result = new StringBuffer();
		String str;
		try {
			str = br.readLine();
			while (str!=null){
				result.append(str);
				str = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
