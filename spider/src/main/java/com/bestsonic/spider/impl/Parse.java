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


public class Parse{
	
	public static String getText(Document root){
		Elements elements = root.getAllElements();
		StringBuilder sb = new StringBuilder();
		
		for(Element e : elements){
			if(e.textNodes().size()==1&&e.childNodeSize()==1){
				//System.out.println(e.text());
				sb.append(e.text()+" ");
			}
		}
		return sb.toString();
	}
	
	public static String getTitle(Document root){
		Elements titles = root.getElementsByTag("title");
		if(titles.size() > 0){
			return titles.first().text();
		}
		return "";
	}
	
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

	public static String[] getOutLinks(Document root) {
		Elements elements = root.getAllElements();
		List<String> ourlsList = new ArrayList<String>();
		for(Element e : elements){
			if(e.hasAttr("href")){
				//System.out.println(e.attributes().get("href"));
				ourlsList.add(e.attributes().get("href"));
			}
		}
		String[] ourls = new String[ourlsList.size()];
		for(int i=0;i<ourlsList.size();i++){
			ourls[i] = ourlsList.get(i);
		}
		return ourls;
	}
	
}
