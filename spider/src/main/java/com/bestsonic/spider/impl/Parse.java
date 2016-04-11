package com.bestsonic.spider.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 解析工具类，根据Document解析网页中相应的信息
 * @author zheng
 */
public class Parse{
	
	/**
	 * 解析网页中的正文
	 * @param root DOM树root节点
	 * @return 字符串
	 */
	public static String getText(Document root){
		Elements elements = root.getAllElements();
		StringBuilder sb = new StringBuilder();
		
		for(Element e : elements){
			if(e.textNodes().size()==1&&e.childNodeSize()==1){
				sb.append(e.text()+" ");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获得网页标题
	 * @param root DOM树root节点
	 * @return 字符串
	 */
	public static String getTitle(Document root){
		Elements titles = root.getElementsByTag("title");
		if(titles.size() > 0){
			return titles.first().text();
		}
		return "";
	}
	
	/**
	 * 获得网页内容(HTML)
	 * @param in 网页输入流
	 * @param charset 网页编码
	 * @return 字符串
	 */
	public static String getContent(InputStream in, String charset){
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in,charset));
			String str = null;
			while((str = reader.readLine()) != null){
				sb.append(str + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
	
	/**
	 * 获取网页所有的符合要求的链接
	 * @param root DOM树root节点
	 * @return 链接数组
	 */
	public static String[] getOutLinks(Document root) {
		Elements elements = root.getAllElements();
		List<String> outlsList = new ArrayList<String>();
		for(Element e : elements){
			if(e.hasAttr("href")){
				outlsList.add(e.attributes().get("href"));
			}
		}
		String[] outls = new String[outlsList.size()];
		for(int i=0;i<outlsList.size();i++){
			outls[i] = outlsList.get(i);
		}
		return outls;
	}
	
}
