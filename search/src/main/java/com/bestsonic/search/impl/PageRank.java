package com.bestsonic.search.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.bestsonic.domain.WebPage;
import com.bestsonic.mapper.WebPageMapper;
import com.bestsonic.spider.Job;
import com.bestsonic.spider.utils.DBUtils;
import com.bestsonic.spider.utils.StreamUtils;

/**
 * 
 * @author zheng
 */
public class PageRank implements Job {
	private final static Job job = new PageRank();
	private final static Logger LOG = Logger.getLogger(PageRank.class);
	/* 阀值 */
	public static double MAX = 0.00000000001;

	/* 阻尼系数 */
	public static double alpha = 0.85;

	public static HashMap<Integer, Double> init = new HashMap<Integer, Double>();

	public static HashMap<Integer, Double> pr = new HashMap<Integer, Double>();

	private PageRank() {
	}

	public static Job getInstance() {
		return job;
	}

	/**
	 * 计算pagerank
	 * 
	 * @param init
	 * @param alpho
	 * @return
	 */
	private static HashMap<Integer, Double> doPageRank(HashMap<Integer, List<String>> outLinks) {
		HashMap<Integer, Double> map = new HashMap<>(outLinks.size());
		for (Integer key : init.keySet()) {
			double temp = 0;
			for (Integer num : init.keySet()) {
				List<String> ols = outLinks.get(num);
				// 计算对本页面链接相关总值
				if (key != num && ols.size() != 0 && ols.contains(
						key + "")/* he0.getInLinks().contains(he.getPath()) */) {
					temp = temp + init.get(num) / ols.size();
				}
			}
			// 经典的pr公式
			map.put(key, alpha + (1 - alpha) * temp);
		}
		return map;
	}

	/**
	 * 判断前后两次的pr数组之间的差别是否大于我们定义的阀值 假如大于，那么返回false，继续迭代计算pr
	 * 
	 * @param pr
	 * @param init
	 * @param max
	 * @return
	 */
	private static boolean checkMax() {
		boolean flag = true;
		for (Integer key : pr.keySet()) {
			if (Math.abs(pr.get(key) - init.get(key)) > MAX) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	@Override
	public void run() {
		SqlSession session = null;
		try {
			session = DBUtils.getSession();
			WebPageMapper mapper = session.getMapper(WebPageMapper.class);
			List<WebPage> list = mapper.selectLinks();
			LOG.debug("计算PageRank!");
			LOG.debug("查询结果：" + list);
			// 1.拿到所有的outlinks
			HashMap<Integer, List<String>> outLinks = new HashMap<>();
			for (WebPage page : list) {
				outLinks.put(page.getId(), Arrays.asList(page.getOutlinks().split(",")));
			}

			// 2.初始化init[i] = 0.0;
			for (Integer key : outLinks.keySet()) {
				init.put(key, 0.0);
			}
			// 3.计算pageRank
			pr = doPageRank(outLinks);
			LOG.debug("计算pr：" + pr.toString());
			while (!(checkMax())) {
				init = new HashMap<>(pr);
				pr = doPageRank(outLinks);
				LOG.debug("计算pr：" + pr.toString());
			}
			// 4.存入pageRank，下标为id，值为rank
			for (Integer key : pr.keySet()) {
				WebPage page = new WebPage();
				page.setId(key);
				page.setScore(pr.get(key));
				mapper.update(page);
			}
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null)
				session.rollback();
		} finally {
			StreamUtils.close(session);
		}
	}
}
