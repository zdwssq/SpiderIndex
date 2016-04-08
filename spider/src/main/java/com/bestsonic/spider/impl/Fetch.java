package com.bestsonic.spider.impl;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.bestsonic.domain.FetchTask;
import com.bestsonic.domain.ThreadPool;
import com.bestsonic.domain.WebPage;
import com.bestsonic.mapper.WebPageMapper;
import com.bestsonic.spider.Job;
import com.bestsonic.spider.utils.DBUtils;
import com.bestsonic.spider.utils.StreamUtils;

/**
 * 从数据库中查询出需要Fetch的记录, 生成FetchTask, 交给线程池来执行
 * 
 * @author zheng
 * @date 2016年4月8日
 */
public class Fetch implements Job {
	private final static Logger LOG = Logger.getLogger(Fetch.class);

	private final static Job job = new Fetch();

	private Fetch() {
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
			// 从数据库中查询出需要Fetch的记录, 生成FetchTask, 交给线程池来执行
			List<WebPage> list = mapper.selectForFetch();
			session.commit();
			CountDownLatch latch = new CountDownLatch(list.size());
			System.err.println(latch.getCount());
			for(WebPage webpage : list) {
				LOG.debug("Fetch过程 - 提交任务：" + webpage);
				ThreadPool.submitJob(new FetchTask(webpage, latch));
			}
			latch.await();
			System.out.println("主线程继续执行.....");
			//session.commit();
		} catch (InterruptedException e) {
			e.printStackTrace();
			if (session != null)
				session.rollback();
		} finally {
			StreamUtils.close(session);
		}
	}

}
