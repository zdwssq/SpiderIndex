package com.bestsonic.search.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.bestsonic.domain.Keyword;
import com.bestsonic.domain.WebPage;
import com.bestsonic.mapper.KeywordMapper;
import com.bestsonic.mapper.WebPageMapper;
import com.bestsonic.search.utils.BitSetUtils;
import com.bestsonic.spider.Job;
import com.bestsonic.spider.utils.DBUtils;

public class Split implements Job {

	private final static Job job = new Split();
	private final static Logger LOG = Logger.getLogger(Split.class);

	private Split() {
	}

	public static Job getInstance() {
		return job;
	}

	@Override
	public void run() {
		SqlSession session = DBUtils.getSession();
		WebPageMapper webPageMapper = session.getMapper(WebPageMapper.class);
		KeywordMapper keywordMapper = session.getMapper(KeywordMapper.class);
		LOG.debug("进入分词程序!");
		List<WebPage> list = null;
		HashMap<String, Keyword> keywords = new HashMap<String, Keyword>();
		try {
			list = webPageMapper.selectAllNotNull();
			LOG.debug("查询" + list.size() + "条记录，开始分词!");
			for (int i = 0; i < list.size(); i++) {
				WebPage webpage = list.get(i);
				LOG.debug("分词网页Url:" + webpage.getBaseUrl());
				String text = webpage.getText();

				List<Term> terms = ToAnalysis.parse(text);
				LOG.debug("分词结果:" + terms.size());
				for (Term term : terms) {
					Keyword keyword = null;
					BitSet set = null;
					if (keywords.containsKey(term.getName())) {
						keyword = keywords.get(term.getName());
						String relationship = keyword.getRelationship();
						if (relationship == null || relationship.equals("")) {
							set = new BitSet(list.size());
						} else {
							set = BitSetUtils.toBitSet(relationship, list.size());
						}
						set.set(i);
						keyword.setRelationship(set.toString());
					} else {
						keyword = new Keyword();
						keyword.setKeyword(term.getName());
						set = new BitSet(list.size());
						set.set(i);
						keyword.setRelationship(set.toString());
						keywords.put(term.getName(), keyword);
					}
				}
			}
			LOG.debug("分词结束!");
			// 关键词Map保存完毕。
			List<Keyword> keywordList = new ArrayList<Keyword>(keywords.values());

			LOG.debug("分词降序排列!");
			// 降序排列
			Collections.sort(keywordList);
			LOG.debug("将分词插入数据库中!");
			
			// 写入数据库中
			keywordMapper.insertByList(keywordList);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
