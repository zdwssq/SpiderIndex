package com.bestsonic.search.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.bestsonic.domain.WebPage;
import com.bestsonic.spider.Job;
import com.bestsonic.spider.utils.DBUtils;

public class PageRank implements Job {
	private final static Job job = new PageRank();
	private final static Logger LOG = Logger.getLogger(PageRank.class);
	
	private PageRank(){}
	
	public static Job getInstance(){
		return job;
	}

	@Override
	public void run() {
		String sql = "SELECT id, inlinks, outlinks FROM webpage ORDER BY id ASC";
		List<WebPage> list = null;
		try {
			list = DBUtils.getQuery().query(sql, new BeanListHandler<WebPage>(WebPage.class));
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
