package com.bestsonic.search.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.bestsonic.domain.Page;
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

	/**
	 * 根据入参关键词，从数据库查询符合条件的WebPage对象List，并返回。
	 * 
	 * @param keywords
	 *            关键词数组
	 * @return 分页对象
	 */
	public static Page<WebPage> rankedList(String[] keywords, int currentNum, int pageSize) {
		SqlSession session = DBUtils.getSession();
		KeywordMapper keywordmapper = session.getMapper(KeywordMapper.class);
		WebPageMapper webpageMapper = session.getMapper(WebPageMapper.class);
		List<WebPage> rankedList = null;
		String res = null;
		for (int i = 0; i < keywords.length; i++) {
			List<String> relations = keywordmapper.getKeywords("%" + keywords[i] + "%");
			if (!relations.isEmpty()) {
				String bytes = relations.get(0);
				for (String str : relations)
					bytes = or(bytes, str);
				if (res == null)
					res = bytes;
				res = and(res, bytes);
			}
		}
		if (res == null)
			return new Page<WebPage>();
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
			return new Page<WebPage>();

		rankedList = webpageMapper.selectByIds(ids);
		if (rankedList != null) {
			for (WebPage webpage : rankedList) {
				int before_length = webpage.getText().length();
				for (String key : keywords)
					webpage.setText(webpage.getText().replaceAll(key, ""));

				double ratue = 1 - (webpage.getText().length() / before_length);
				double rank = webpage.getScore() * ratue;
				webpage.setRank(rank);
			}
			Collections.sort(rankedList);

			Page<WebPage> page = new Page<WebPage>();
			page.setLength(pageSize);
			page.setTotalRecords(rankedList.size());
			page.setPageDatas(rankedList.subList((currentNum - 1) * pageSize,
					Math.min(currentNum * pageSize + pageSize, rankedList.size())));

			return page;
		}

		return new Page<WebPage>();
	}

	/**
	 * 两字符串与操作，左对齐操作
	 * 
	 * @param str1
	 * @param str2
	 * @return 新字符串
	 */
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

	/**
	 * 两字符串或操作，左对齐操作
	 * 
	 * @param str1
	 * @param str2
	 * @return 新字符串
	 */
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

}
