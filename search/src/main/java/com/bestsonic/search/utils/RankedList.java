package com.bestsonic.search.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.bestsonic.domain.WebPage;
import com.bestsonic.mapper.KeywordMapper;
import com.bestsonic.mapper.WebPageMapper;
import com.bestsonic.spider.utils.DBUtils;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author zheng
 * @time 2016年4月9日 下午1:24:30
 */

public class RankedList {

	public static List<WebPage> rankedList(String[] keywords) {
		SqlSession session = DBUtils.getSession();
		KeywordMapper keywordmapper = session.getMapper(KeywordMapper.class);
		WebPageMapper webpageMapper = session.getMapper(WebPageMapper.class);
		List<WebPage> rankedList = null;
		String res = null;
		for (int i = 0; i < keywords.length; i++) {
			List<String> relations = keywordmapper.getKeywords("%" + keywords[i] + "%");
			String bytes = relations.get(0);
			for (String str : relations) {
				bytes = or(bytes, str);
			}
			if(res == null) res = bytes;
			res = and(res, bytes);
		}

		// 查找数据库
		List<Integer> ids = new ArrayList<Integer>();
		byte[] bs = res.getBytes();
		for (int i = 0; i < bs.length; i++) {
			byte c = bs[i];
			if (c == '1') {
				ids.add(webpageMapper.selectId(i));
			}
		}

		if (ids.isEmpty())
			return new ArrayList<>();
		
		rankedList = webpageMapper.selectByIds(ids);
		return rankedList;
	}

	private static String and(String str1, String str2) {
		StringBuilder res = new StringBuilder();
		int min = Math.min(str1.length(), str2.length());
		int i = 0;
		for (i = 0; i < min; i++) {
			int c = str1.charAt(i) & str2.charAt(i);
			res.append(c - 48);
		}

		return res.toString();
	}

	private static String or(String str1, String str2) {
		StringBuilder res = new StringBuilder();
		int min = Math.min(str1.length(), str2.length());
		int i = 0;
		for (i = 0; i < min; i++) {
			int c = str1.charAt(i) | str2.charAt(i);
			res.append(c - 48);
		}

		if (i < str1.length())
			res.append(str1.substring(i));

		if (i < str2.length())
			res.append(str2.substring(i));

		return res.toString();
	}

	@Test
	public void test() {

		List<WebPage> webPages = rankedList(new String[] { "宁波", "网" });
		System.err.println(webPages.get(0) + "dhuhdu");
	}
}
