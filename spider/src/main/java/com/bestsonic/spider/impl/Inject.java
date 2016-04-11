package com.bestsonic.spider.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.bestsonic.domain.WebPage;
import com.bestsonic.mapper.WebPageMapper;
import com.bestsonic.spider.Job;
import com.bestsonic.spider.utils.DBUtils;
import com.bestsonic.spider.utils.FileUtils;
import com.bestsonic.spider.utils.StreamUtils;
/**
 * 从配置文件injectURL.txt中获取起点Url, 注入数据库中
 * @author zheng
 */
public class Inject implements Job {

	private final static Job job = new Inject();
	private final static Logger LOG = Logger.getLogger(Inject.class);

	private Inject() {
	}

	public static Job getInstance() {
		return job;
	}

	@Override
	public void run() {
		SqlSession session = null;
		try {
			session = DBUtils.getSession();
			WebPageMapper mapper = session.getMapper(WebPageMapper.class);
			String[] urls = FileUtils.getFileContent("classpath:injectURL.txt").split("\n");
			LOG.debug("Inject过程 - 读取配置文件Url:" + Arrays.toString(urls));
			// 过滤
			Set<String> filteredUrls = Filter.filter(urls);
			LOG.debug("Inject过程 - 过滤后Url:" + filteredUrls);
			// 写数据库
			
			List<String> list = mapper.selectExistUrls(new ArrayList<String>(filteredUrls));
			for (int i = 0; i < list.size(); i++) {
				LOG.debug("重复的Url：" + list.get(i));
				filteredUrls.remove(list.get(i));
			}
			// 插入不存在的baseUrl信息
			List<WebPage> pages = new ArrayList<WebPage>();
			for (Iterator<String> iter = filteredUrls.iterator(); iter.hasNext();) {
				WebPage page = new WebPage();
				page.setBaseUrl(iter.next());
				pages.add(page);
			}
			mapper.insertByList(pages);
			LOG.debug("Inject过程 - 插入数据库!");
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) session.rollback();
		} finally {
			StreamUtils.close(session);
		}
	}
}
