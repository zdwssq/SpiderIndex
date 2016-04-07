package com.bestsonic.spider.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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

public class Fetch implements Job {
	private final static Logger LOG = Logger.getLogger(Fetch.class);

	private final static Job job = new Fetch();
	
	public static volatile CopyOnWriteArrayList<String> allUrls;

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
			allUrls = new CopyOnWriteArrayList<String>(mapper.selectAllUrls());
			List<WebPage> list = mapper.selectForFetch();
			CountDownLatch latch = new CountDownLatch(list.size());
			System.err.println(latch.getCount());
			list.forEach((webpage) -> {
				LOG.debug("Fetch过程 - 提交任务：" + webpage);
				ThreadPool.submitJob(new FetchTask(webpage, latch));
				
			});
			latch.await();
			session.commit();
		} catch (InterruptedException e) {
			e.printStackTrace();
			if(session != null) session.rollback();
		} finally{
			StreamUtils.close(session);
		}
	}

}
