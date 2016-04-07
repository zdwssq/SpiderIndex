package com.bestsonic.search.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.bestsonic.domain.Keyword;
import com.bestsonic.domain.WebPage;
import com.bestsonic.search.utils.BitSetUtils;
import com.bestsonic.spider.Job;
import com.bestsonic.spider.utils.DBUtils;

public class Split implements Job {

	private final static Job job = new Split();
	private final static Logger LOG = Logger.getLogger(Split.class);
	
	private Split(){}
	
	public static Job getInstance(){
		return job;
	}

	
	@Override
	public void run() {
		LOG.debug("进入分词程序!");
		String sql = "SELECT id, baseUrl, text FROM webpage ORDER BY id ASC";
		List<WebPage> list = null;
		HashMap<String, Keyword> keywords = new HashMap<String, Keyword>();
		try {
			list = DBUtils.getQuery().query(sql, new BeanListHandler<WebPage>(WebPage.class));
			LOG.debug("查询" + list.size() + "条记录，开始分词!");
			for(int i = 0; i < list.size(); i++){
				WebPage webpage = list.get(i);
				LOG.debug("分词网页Url:" + webpage.getBaseUrl());
				String text = webpage.getText();
				
				List<Term> terms = ToAnalysis.parse(text);
				LOG.debug("分词结果:" + terms.size());
				for(Term term : terms){
					Keyword keyword = null;
					BitSet set = null;
					if(keywords.containsKey(term.getName())){
						keyword = keywords.get(term.getName());
						String relationship = keyword.getRelationship();
						if(relationship == null || relationship.equals("")){
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
			//关键词Map保存完毕。
			List<Keyword> keywordList = new ArrayList<Keyword>(keywords.values());
			
			LOG.debug("分词降序排列!");
			//降序排列
			Collections.sort(keywordList);
			LOG.debug("将分词插入数据库中!");
			//写入数据库中
			sql = "INSERT INTO keyword(keyword, relationship) values(?,?)";
			
			Object[][] params = new Object[keywordList.size()][2];
			for(int i = 0; i < keywordList.size(); i++){
				params[i][0] = keywordList.get(i).getKeyword();
				params[i][1] = keywordList.get(i).getRelationship();
			}
			int[] result = DBUtils.getQuery().batch(sql, params);
			
			LOG.debug("数据库插入结果:" + Arrays.toString(result));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
